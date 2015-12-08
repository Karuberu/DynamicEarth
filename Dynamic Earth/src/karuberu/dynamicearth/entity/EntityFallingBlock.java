package karuberu.dynamicearth.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import karuberu.core.util.Helper;
import karuberu.core.util.block.BlockSide;
import karuberu.dynamicearth.TickHandler;
import karuberu.dynamicearth.api.fallingblock.BlockFalling;
import karuberu.dynamicearth.api.fallingblock.IFallingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFallingBlock extends karuberu.dynamicearth.api.fallingblock.EntityFallingBlock implements IEntityAdditionalSpawnData {
	public int blockID;
	public int metadata;
	public int fallTime;
	public boolean shouldDropItem;
	private boolean blockDestroyed;
	private boolean dealsFallDamage;
	private int fallDamageMax;
	private float fallDamage;
	private NBTTagCompound additionalTags;
	private NBTTagCompound tileEntityData;
	private static final String
		BLOCKID = "BlockID",
		METADATA = "Metadata",
		FALLTIME = "FallTime",
		SHOULDDROPITEM = "ShouldDropItem",
		DEALSFALLDAMAGE = "DealsFallDamage",
		FALLDAMAGEMAX = "FallDamageMax",
		FALLDAMAGE = "FallDamage",
		ADDITIONALTAGS = "AdditionalTags",
		TILEENTITYDATA = "TileEntityData";

	public EntityFallingBlock(World world) {
		super(world);
		this.fallTime = 0;
		this.shouldDropItem = true;
		this.blockDestroyed = false;
		this.dealsFallDamage = false;
		this.fallDamageMax = 40;
		this.fallDamage = 2.0F;
		this.additionalTags = null;
		this.tileEntityData = null;
	}

	public EntityFallingBlock(World world, int x, int y, int z, int id) {
		this(world, x, y, z, id, 0);
	}
	
	public EntityFallingBlock(World world, int x, int y, int z, int id, int meta) {
		this(world, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, id, meta);
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
		IFallingBlock fallingBlock = this.getFallingBlock();
		if (fallingBlock != null) {
			this.setFallDamage(
				fallingBlock.getMaxFallDamage(),
				fallingBlock.getFallDamage()
			);
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
	protected void entityInit() {}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	public void onUpdate() {
		if (this.blockID == 0) {
			this.setDead();
			return;
		}
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.fallTime++;
		this.motionY -= 0.04D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.98D;
		this.motionY *= 0.98D;
		this.motionZ *= 0.98D;
		if (this.worldObj.isRemote) {
			return;
		}
		
		int x = MathHelper.floor_double(this.posX);
		int y = MathHelper.floor_double(this.posY);
		int z = MathHelper.floor_double(this.posZ);
		IFallingBlock fallingBlock = this.getFallingBlock();
		if (this.fallTime == 1) {
			if (this.fallTime != 1
			|| !this.canSpawnFromBlock(this.worldObj, x, y, z)) {
				this.setDead();
				return;
			}
			this.collectTileEntityData(this.worldObj, x, y, z);
			this.worldObj.setBlockToAir(x, y, z);
			if (fallingBlock != null) {
				fallingBlock.onStartFalling(this, this.worldObj, x, y, z, this.metadata);
			}
			if (this.isDead) {
				return;
			}
		}
		if (this.onGround) {
			this.motionX *= 0.7D;
			this.motionZ *= 0.7D;
			this.motionY *= -0.5D;
			if (this.worldObj.getBlockId(x, y, z) != Block.pistonMoving.blockID) {
				if (fallingBlock != null) {
					fallingBlock.onFinishFalling(this, this.worldObj, x, y, z, this.metadata);
				}
				if (!this.isDead) {
					this.setDead();
					if (!this.blockDestroyed) {
						boolean canPlace = this.worldObj.canPlaceEntityOnSide(this.blockID, x, y, z, true, BlockSide.TOP.code, this, (ItemStack)null);
						boolean canFall = fallingBlock != null ? fallingBlock.canFallBelow(this.worldObj, x, y, z) : BlockFalling.blockCanFallBelow(this.worldObj, x, y, z);
						boolean blockWasSet = false;
						if (canPlace && !canFall) {
							blockWasSet = this.worldObj.setBlock(x, y, z, this.blockID, this.metadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
						}
						if (blockWasSet) {
							this.setTileEntity(this.worldObj, x, y, z);
						} else {
							this.dropItems();
						}
					}
				}
			}
			return;
		}
		if (this.fallTime > 600 || (this.fallTime > 100 && (y < 1 || y > 256))) {
			this.dropItems();
			this.setDead();
			return;
		}
		if (fallingBlock != null) {
			fallingBlock.onFallTick(this, this.worldObj, x, y, z, this.metadata, this.fallTime);
		}
	}
	
	/**
	 * Drop items based on the block that is falling.
	 */
	private void dropItems() {
		if (this.shouldDropItem
		&& this.worldObj.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			int x = MathHelper.floor_double(this.posX);
			int y = MathHelper.floor_double(this.posY);
			int z = MathHelper.floor_double(this.posZ);
			IFallingBlock fallingBlock = this.getFallingBlock();
			if (fallingBlock != null) {
				ArrayList<ItemStack> drops = fallingBlock.getItemsDropped(this.worldObj, x, y, z, fallTime, this.metadata, this.rand);
				if (drops != null) {
					for (ItemStack itemStack : drops) {
						this.entityDropItem(itemStack, 0.0F);
					}
				}
			} else {
				this.entityDropItem(new ItemStack(this.blockID, 1, Block.blocksList[this.blockID].damageDropped(this.metadata)), 0.0F);
			}
		}
	}
	
	private void collectTileEntityData(World world, int x, int y, int z) {
		if (Block.blocksList[this.blockID] instanceof ITileEntityProvider) {
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if (tileEntity != null) {
				NBTTagCompound tagCompound = new NBTTagCompound();
				tileEntity.writeToNBT(tagCompound);
				this.tileEntityData = tagCompound;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setTileEntity(World world, int x, int y, int z) {
		if (this.tileEntityData != null
		&& Block.blocksList[this.blockID] instanceof ITileEntityProvider) {
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if (tileEntity != null) {
				NBTTagCompound tagCompound = new NBTTagCompound();
				tileEntity.writeToNBT(tagCompound);
				Iterator<NBTBase> iterator = this.tileEntityData.getTags().iterator();
				while (iterator.hasNext()) {
					NBTBase tagBase = iterator.next();
					String tagName = tagBase.getName();
					if (!"x".equals(tagName)
					&& !"y".equals(tagName)
					&& !"z".equals(tagName)) {
						tagCompound.setTag(tagName, tagBase.copy());
					}
				}
				tileEntity.readFromNBT(tagCompound);
				tileEntity.onInventoryChanged();
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void fall(float floatFallTime) {
		if (this.dealsFallDamage) {
			int fallTime = MathHelper.ceiling_float_int(floatFallTime - 1.0F);
			if (fallTime > 0) {
				IFallingBlock fallingBlock = this.getFallingBlock();
				ArrayList<Entity> entitiesList = new ArrayList<Entity>(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox));
				DamageSource damageSource = fallingBlock != null ? fallingBlock.getDamageSource() : DamageSource.fallingBlock;
				Iterator<Entity> iterator = entitiesList.iterator();
				while (iterator.hasNext()) {
					Entity entity = iterator.next();
					entity.attackEntityFrom(damageSource, Math.min(MathHelper.floor_float((float)fallTime * this.fallDamage), this.fallDamageMax));
				}
				if (fallingBlock != null) {
					int meta = fallingBlock.getMetaForFall(fallTime, this.metadata, this.rand);
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
		tagCompound.setShort(BLOCKID, (short)this.blockID);
		tagCompound.setByte(METADATA, (byte)this.metadata);
		tagCompound.setByte(FALLTIME, (byte)this.fallTime);
		tagCompound.setBoolean(SHOULDDROPITEM, this.shouldDropItem);
		tagCompound.setBoolean(DEALSFALLDAMAGE, this.dealsFallDamage);
		tagCompound.setFloat(FALLDAMAGE, this.fallDamage);
		tagCompound.setShort(FALLDAMAGEMAX, (short)this.fallDamageMax);
		if (this.additionalTags != null) {
			tagCompound.setCompoundTag(ADDITIONALTAGS, this.additionalTags);
		}
        if (this.tileEntityData != null) {
        	tagCompound.setCompoundTag(TILEENTITYDATA, this.tileEntityData);
        }
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompound) {
		this.blockID = tagCompound.getShort(BLOCKID);
		this.metadata = tagCompound.getByte(METADATA) & 255;
		this.fallTime = tagCompound.getByte(FALLTIME) & 255;
		if (tagCompound.hasKey(DEALSFALLDAMAGE)) {
			this.dealsFallDamage = tagCompound.getBoolean(DEALSFALLDAMAGE);
			this.fallDamage = tagCompound.getFloat(FALLDAMAGE);
			this.fallDamageMax = tagCompound.getShort(FALLDAMAGEMAX);
		}
		if (tagCompound.hasKey(SHOULDDROPITEM)) {
			this.shouldDropItem = tagCompound.getBoolean(SHOULDDROPITEM);
		}
		if (tagCompound.hasKey(ADDITIONALTAGS)) {
			this.additionalTags = tagCompound.getCompoundTag(ADDITIONALTAGS);
		}
		if (tagCompound.hasKey(TILEENTITYDATA)) {
			this.tileEntityData = tagCompound.getCompoundTag(TILEENTITYDATA);
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

	/**
	 * Sets the amount of fall damage dealt and turns fall damage on if the
	 * number is greater than zero or off if it is zero or below.
	 * @param maxFallDamage : The maximum damage this block can deal when falling
	 * at max speed.
	 * @param fallDamage : The damage this block will deal per tick of fall time.
	 */
	public void setFallDamage(int maxFallDamage, float fallDamage) {
		if (maxFallDamage > 0 && fallDamage > 0.0F) {
			this.dealsFallDamage = true;
			this.fallDamageMax = maxFallDamage;
			this.fallDamage = fallDamage;
		} else {
			this.dealsFallDamage = false;
			this.fallDamageMax = 0;
			this.fallDamage = 0.0F;
		}
	}
	
	/**
	 * Switches fall damage on or off without changing the amount of damage dealt.
	 * @param bool
	 */
	public void setDealsFallDamage(boolean bool) {
		if (this.fallDamageMax > 0 && this.fallDamage > 0.0F) {
			this.dealsFallDamage = bool;
		}
	}
	
	public void setBlockDestroyed(boolean bool) {
		this.blockDestroyed = bool;
	}
	
	public void setAdditionalTags(NBTTagCompound tagCompound) {
		this.additionalTags = tagCompound;
	}
	
	public NBTTagCompound getAdditionalTags() {
		return this.additionalTags;
	}
	
	public IFallingBlock getFallingBlock() {
		Block block = Block.blocksList[this.blockID];
		return block instanceof IFallingBlock ? (IFallingBlock)block : null;
	}
	
	public boolean canSpawnFromBlock(World world, int x, int y, int z) {
		IFallingBlock fallingBlock = this.getFallingBlock();
		if (fallingBlock != null) {
			return fallingBlock.canSpawnFromBlock(world, x, y, z);
		} else {
			return world.getBlockId(x, y, z) == this.blockID;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire() {
		return false;
	}

	public void addEntityCrashInfo(CrashReportCategory crashReportCategory) {
		super.addEntityCrashInfo(crashReportCategory);
		crashReportCategory.addCrashSection("Immitating block ID", Integer.valueOf(this.blockID));
		crashReportCategory.addCrashSection("Immitating block metadata", Integer.valueOf(this.metadata));
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
	
	public static void spawnFallingBlock(World world, int x, int y, int z, int blockID, int metadata, NBTTagCompound additionalTags, int delay) {
		EntityFallingBlock entityFallingBlock = new EntityFallingBlock(world, x, y, z, blockID, metadata);
		entityFallingBlock.setAdditionalTags(additionalTags);
		EntityFallingBlock.spawnFallingBlock(entityFallingBlock, delay);
	}
	
	public static void spawnFallingBlock(EntityFallingBlock entityFallingBlock, int delay) {
		World world = entityFallingBlock.worldObj;
		int x = MathHelper.floor_double(entityFallingBlock.posX),
			y = MathHelper.floor_double(entityFallingBlock.posY),
			z = MathHelper.floor_double(entityFallingBlock.posZ);
		byte radius = 32;
        if (BlockFalling.doSpawnEntity()
        && world.checkChunksExist(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius)) {
            if (!world.isRemote) {
				if (delay <= 0) {
					world.spawnEntityInWorld(entityFallingBlock);
				} else {
					TickHandler.instance.scheduleEntitySpawn(entityFallingBlock, delay);
				}
            }
        } else {
    		IFallingBlock fallingBlock = entityFallingBlock.getFallingBlock();
            world.setBlockToAir(x, y, z);
            while (fallingBlock != null ? fallingBlock.canFallBelow(world, x, y, z) : BlockFalling.blockCanFallBelow(world, x, y, z)) {
                --y;
            } if (y > 0) {
                world.setBlock(x, y, z, entityFallingBlock.blockID, entityFallingBlock.metadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
            }
        }
	}
}
