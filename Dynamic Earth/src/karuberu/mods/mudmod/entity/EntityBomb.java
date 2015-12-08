package karuberu.mods.mudmod.entity;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import karuberu.core.KaruberuLogger;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.items.ItemBomb;
import karuberu.mods.mudmod.items.ItemBombLit;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBomb extends EntityThrowable {
	private int
		fuse = 0,
		explosiveness = 0;
	private boolean
		fireCharged = false,
		impact = true;
	
	public EntityBomb(World world) {
		super(world);
	}
	
	public EntityBomb(World world, EntityLiving entityLiving, int fuse, int explosiveness) {
		super(world, entityLiving);
		this.fuse = fuse;
		this.explosiveness = explosiveness;
	}
	
	public EntityBomb(World world, double x, double y, double z, int fuse, int explosiveness) {
		super(world, x, y, z);
		this.fuse = fuse;
		this.explosiveness = explosiveness;
 	}
	
	public EntityBomb(World world, EntityLiving entityLiving, ItemStack bomb) {
		this(world, entityLiving, ItemBombLit.getTrueFuseLength(bomb), ((ItemBombLit)bomb.getItem()).getExplosiveness());
		this.fireCharged = ((ItemBombLit)bomb.getItem()).getFireCharged();
        this.dataWatcher.updateObject(8, bomb);
	}
	
	public EntityBomb(World world, double x, double y, double z, ItemStack bomb) {
		this(world, x, y, z, ItemBombLit.getTrueFuseLength(bomb), ((ItemBombLit)bomb.getItem()).getExplosiveness());
		this.fireCharged = ((ItemBombLit)bomb.getItem()).getFireCharged();
        this.dataWatcher.updateObject(8, bomb);
	}
	
	public EntityBomb setImpact(boolean impact) {
		this.impact = impact;
		return this;
	}
	
	public EntityBomb setFireCharged() {
		this.fireCharged = true;
		return this;
	}
	
	public EntityBomb setFuse(int fuse) {
		this.fuse = fuse;
		return this;
	}
	
	@Override
    protected void entityInit() {
        this.dataWatcher.addObjectByDataType(8, 5);
    }
	
	@Override
	protected void onImpact(MovingObjectPosition movingObjectPosition) {
		if (movingObjectPosition.entityHit != null) {
			movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 2 + (explosiveness / 2));
			if (impact) {
				movingObjectPosition.entityHit.addVelocity(this.motionX / 2, 0.25D + (0.05D * explosiveness), this.motionZ / 2);
			}
			if (fireCharged && !movingObjectPosition.entityHit.isImmuneToFire()) {
		 		movingObjectPosition.entityHit.setFire(5);
			}
		}
		this.explode();
	}
	
	@Override
	public void onUpdate() {
		if (!this.worldObj.isRemote) {
			if (this.fuse <= 0) {
				this.explode();
			} else {
				this.fuse--;
			}
		}
		super.onUpdate();
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public void handleHealthUpdate(byte par1) {
        if (par1 == 17 && this.worldObj.isRemote) {
        	ItemStack item = this.dataWatcher.getWatchableObjectItemStack(8);
        	NBTTagCompound compound = null;
            if (item != null && item.hasTagCompound()) {
                compound = item.getTagCompound();
            }
            this.worldObj.func_92088_a(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, compound);
        }
        super.handleHealthUpdate(par1);
    }
	
	protected void explode() {
        this.worldObj.setEntityState(this, (byte)17);
        if (this.worldObj.isRemote || FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
    		for (int i = 0; i < 8; ++i) {
    			double px = (this.rand.nextInt(5) - 2) / 8.0D;
    			double py = (this.rand.nextInt(5) - 2) / 8.0D;
    			double pz = (this.rand.nextInt(5) - 2) / 8.0D;
    			this.worldObj.spawnParticle("iconcrack_" + MudMod.bomb.itemID, this.posX, this.posY, this.posZ, px, py, pz);
    		}
        }
        if (fireCharged || explosiveness > 0) {
        	if (!this.worldObj.isRemote) {
				float explosionSize;
				if (explosiveness <= 2) {
					explosionSize = 0.8F + (this.explosiveness * 0.5F);
				} else {
					explosionSize = 0.8F * this.explosiveness;
				}
				this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, explosionSize, fireCharged, explosiveness > 0);
        	}
        }
        this.setDead();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setByte("Fuse", (byte)this.fuse);
		compound.setByte("Explosiveness", (byte)this.explosiveness);
		compound.setBoolean("Fire-charged", this.fireCharged);
		ItemStack item = this.dataWatcher.getWatchableObjectItemStack(8);
        if (item != null) {
        	NBTTagCompound itemCompound = new NBTTagCompound();
            item.writeToNBT(itemCompound);
            compound.setCompoundTag("FireworksItem", itemCompound);
        }
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
        if (compound != null) {
			this.fuse = compound.getByte("Fuse");
			this.explosiveness = compound.getByte("Explosiveness");
			this.fireCharged = compound.getBoolean("Fire-charged");
	        if (compound.hasKey("FireworksItem")) {
	            NBTTagCompound itemCompound = compound.getCompoundTag("FireworksItem");
	            this.dataWatcher.updateObject(8, ItemStack.loadItemStackFromNBT(itemCompound));
	        }
        }
	}
}
