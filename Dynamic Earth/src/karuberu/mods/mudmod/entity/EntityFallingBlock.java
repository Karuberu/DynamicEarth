package karuberu.mods.mudmod.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.blocks.IFallingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockSand;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class EntityFallingBlock extends Entity implements IEntityAdditionalSpawnData
{
    public int blockID;
    public int metadata;
    public int fallTime;
    public boolean shouldDropItem;
    private boolean blockDestroyed;
    private boolean dealsFallDamage;
    private int fallDamageMax;
    private float fallDamage;
    private static final String
    	BLOCKID = "BlockID",
    	METADATA = "Metadata",
    	FALLTIME = "FallTime",
    	SHOULDDROPITEM = "ShouldDropItem",
    	DEALSFALLDAMAGE = "DealsFallDamage",
    	FALLDAMAGEMAX = "FallDamageMax",
    	FALLDAMAGE = "FallDamage";

    public EntityFallingBlock(World world) {
        super(world);
        this.fallTime = 0;
        this.shouldDropItem = true;
        this.blockDestroyed = false;
        this.dealsFallDamage = false;
        this.fallDamageMax = 40;
        this.fallDamage = 2.0F;
    }

    public EntityFallingBlock(World world, double x, double y, double z, int id) {
        this(world, x, y, z, id, 0);
    }

    public EntityFallingBlock(World world, double x, double y, double z, int id, int meta) {
        super(world);
        this.fallTime = 0;
        this.shouldDropItem = true;
        this.blockDestroyed = false;
        this.blockID = id;
        this.metadata = meta;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(x, y, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        if (Block.blocksList[id] instanceof IFallingBlock) {
        	IFallingBlock fallingBlock = (IFallingBlock)Block.blocksList[id];
	        this.dealsFallDamage = fallingBlock.getMaxFallDamage() > 0;
	        this.fallDamageMax = fallingBlock.getMaxFallDamage();
	        this.fallDamage = fallingBlock.getFallDamage();
        } else {
	        this.dealsFallDamage = false;
	        this.fallDamageMax = 40;
	        this.fallDamage = 2.0F;
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
    	Block block = Block.blocksList[this.blockID];
        if (block instanceof IFallingBlock) {
        	((IFallingBlock)block).onStartFalling(this);
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public void onUpdate() {
        if (this.blockID == 0) {
            this.setDead();
        } else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.fallTime++;
            this.motionY -= 0.03999999910593033D;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;

            if (!this.worldObj.isRemote) {
                int x = MathHelper.floor_double(this.posX);
                int y = MathHelper.floor_double(this.posY);
                int z = MathHelper.floor_double(this.posZ);

                if (this.fallTime == 1) {
                    if (this.fallTime != 1 || this.worldObj.getBlockId(x, y, z) != this.blockID) {
                        this.setDead();
                        return;
                    }
                    this.worldObj.setBlockWithNotify(x, y, z, 0);
                }

                if (this.onGround) {
                    this.motionX *= 0.699999988079071D;
                    this.motionZ *= 0.699999988079071D;
                    this.motionY *= -0.5D;

                    if (this.worldObj.getBlockId(x, y, z) != Block.pistonMoving.blockID) {
                        this.setDead();

                        if (!this.blockDestroyed
                        && this.worldObj.canPlaceEntityOnSide(this.blockID, x, y, z, true, 1, (Entity)null)
                        && !BlockSand.canFallBelow(this.worldObj, x, y - 1, z)
                        && this.worldObj.setBlockAndMetadataWithNotify(x, y, z, this.blockID, this.metadata)) {
                        	if (Block.blocksList[this.blockID] instanceof IFallingBlock) {
                                ((IFallingBlock)Block.blocksList[this.blockID]).onFinishFalling(this.worldObj, x, y, z, this.metadata);
                            }
                        } else if (this.shouldDropItem && !this.blockDestroyed) {
                            this.entityDropItem(new ItemStack(this.blockID, 1, Block.blocksList[this.blockID].damageDropped(this.metadata)), 0.0F);
                        }
                    }
                } else if (this.fallTime > 600
                || this.fallTime > 100
                && !this.worldObj.isRemote
                && (y < 1 || y > 256)) {
                    if (this.shouldDropItem) {
                        this.entityDropItem(new ItemStack(this.blockID, 1, Block.blocksList[this.blockID].damageDropped(this.metadata)), 0.0F);
                    }
                    this.setDead();
                }
            }
        }
    }

    @Override
    protected void fall(float floatFallTime) {
        if (this.dealsFallDamage) {
            int fallTime = MathHelper.ceiling_float_int(floatFallTime - 1.0F);

            if (fallTime > 0) {
                Block block = Block.blocksList[this.blockID];
                ArrayList entitiesList = new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox));
                DamageSource damageSource = block instanceof IFallingBlock ? ((IFallingBlock)block).getDamageSource() : DamageSource.fallingBlock;
                Iterator iterator = entitiesList.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity)iterator.next();
                    entity.attackEntityFrom(damageSource, Math.min(MathHelper.floor_float((float)fallTime * this.fallDamage), this.fallDamageMax));
                }

                if (block instanceof IFallingBlock
                && ((IFallingBlock)block).isDamagedByFall()) {
                	int meta = ((IFallingBlock)block).getMetaForFall(fallTime, this.metadata, this.rand);
                	if (meta <= -1) {
                		this.blockDestroyed = true;
                	} else {
                		this.metadata = meta;
                	}
                }
            }
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger(BLOCKID, this.blockID);
        tagCompound.setByte(METADATA, (byte)this.metadata);
        tagCompound.setByte(FALLTIME, (byte)this.fallTime);
        tagCompound.setBoolean(SHOULDDROPITEM, this.shouldDropItem);
        tagCompound.setBoolean(DEALSFALLDAMAGE, this.dealsFallDamage);
        tagCompound.setFloat(FALLDAMAGE, this.fallDamage);
        tagCompound.setInteger(FALLDAMAGEMAX, this.fallDamageMax);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompound) {
        this.blockID = tagCompound.getInteger(BLOCKID);
        this.metadata = tagCompound.getByte(METADATA) & 255;
        this.fallTime = tagCompound.getByte(FALLTIME) & 255;
        if (tagCompound.hasKey(DEALSFALLDAMAGE)) {
            this.dealsFallDamage = tagCompound.getBoolean(DEALSFALLDAMAGE);
            this.fallDamage = tagCompound.getFloat(FALLDAMAGE);
            this.fallDamageMax = tagCompound.getInteger(FALLDAMAGEMAX);
        }
        if (tagCompound.hasKey(SHOULDDROPITEM)) {
            this.shouldDropItem = tagCompound.getBoolean(SHOULDDROPITEM);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public World getWorld() {
        return this.worldObj;
    }

    public void setDealsFallDamage(boolean bool) {
        this.dealsFallDamage = bool;
    }
    
    public void setBlockDestroyed(boolean bool) {
    	this.blockDestroyed = bool;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    public void func_85029_a(CrashReportCategory par1CrashReportCategory) {
        super.func_85029_a(par1CrashReportCategory);
        par1CrashReportCategory.addCrashSection("Immitating block ID", Integer.valueOf(this.blockID));
        par1CrashReportCategory.addCrashSection("Immitating block metadata", Integer.valueOf(this.metadata));
    }

	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		data.writeInt(this.blockID);
		data.writeInt(this.metadata);
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		this.blockID = data.readInt();
		this.metadata = data.readInt();
	}
}
