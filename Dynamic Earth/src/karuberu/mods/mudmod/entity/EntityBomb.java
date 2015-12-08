package karuberu.mods.mudmod.entity;

import java.util.Random;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.items.ItemBomb;
import karuberu.mods.mudmod.items.ItemBombLit;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBomb extends EntityThrowable {
	private int fuse = 0;
	
    public EntityBomb(World world) {
        super(world);
    }

    public EntityBomb(World world, EntityLiving entityLiving) {
        super(world, entityLiving);
        this.fuse = ItemBombLit.maxFuseLength;
    }
    
    public EntityBomb(World world, EntityLiving entityLiving, int fuse) {
        super(world, entityLiving);
        this.fuse = fuse;
        if (fuse == 0) {
        	this.explode();
        }
    }
    
    public EntityBomb(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.fuse = ItemBombLit.maxFuseLength;
    }
    
    public EntityBomb(World world, double x, double y, double z, int fuse) {
        super(world, x, y, z);
        this.fuse = fuse;
    }
    
    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
    	if (movingObjectPosition.entityHit != null) {
            movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 2);
            movingObjectPosition.entityHit.addVelocity(this.motionX / 2, 0.3D, this.motionZ / 2);
    	}
        Random rand = new Random();
        for (int i = 0; i < 8; ++i) {
        	double px = rand.nextInt(4) / 10D;
        	double py = rand.nextInt(4) / 10D;
        	double pz = rand.nextInt(4) / 10D;
            this.worldObj.spawnParticle("iconcrack_" + new ItemStack(MudMod.bomb).getItem().itemID, this.posX, this.posY, this.posZ, -0.2D + px, -0.2D + py, -0.2D + pz);
        }
        this.explode();
    }
    
    @Override
    public void onUpdate() {
    	super.onUpdate();
    	if (!this.worldObj.isRemote) {
        	if (this.fuse <= 0) {
        		this.explode();
	    	} else {
	    		this.fuse--;
	    		this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.3D, this.posZ, 0.0D, 0.0D, 0.0D);
	    	}
    	}
    }
    
    protected void explode() {
        this.setDead();
        if (!this.worldObj.isRemote){
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 1.2F, true);
        }
    }
    
    @Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.setByte("Fuse", (byte)this.fuse);
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        this.fuse = par1NBTTagCompound.getByte("Fuse");
    }
}
