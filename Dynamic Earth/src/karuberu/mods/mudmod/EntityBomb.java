package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityThrowable;
import net.minecraft.src.Explosion;
import net.minecraft.src.IMob;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityBomb extends EntityThrowable {
	private int fuse = 0;
	
    public EntityBomb(World world) {
        super(world);
    }

    public EntityBomb(World world, EntityLiving entityLiving, int fuse) {
        super(world, entityLiving);
        this.fuse = fuse;
        if (fuse == 0) {
        	this.explode();
        }
    }
    
    public EntityBomb(World world, double x, double y, double z, int fuse) {
        super(world, x, y, z);
        this.fuse = fuse;
    }
    
    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
    	if (movingObjectPosition.entityHit != null) {
            movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_85052_h()), 2);
            movingObjectPosition.entityHit.addVelocity(0, 0.5D, 0);
    	}
        Random rand = new Random();
        for (int i = 0; i < 8; ++i) {
        	double px = rand.nextInt(4) / 10D;
        	double py = rand.nextInt(4) / 10D;
        	double pz = rand.nextInt(4) / 10D;
            this.worldObj.spawnParticle("iconcrack_" + new ItemStack(MudMod.bomb).getItem().shiftedIndex, this.posX, this.posY, this.posZ, -0.2D + px, -0.2D + py, -0.2D + pz);
        }
        this.explode();
    }
    
    @Override
    public void onUpdate() {
    	super.onUpdate();
    	if (this.fuse <= 0) {
    		this.explode();
    	} else {
    		this.fuse--;
    		this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.3D, this.posZ, 0.0D, 0.0D, 0.0D);
    	}
    }
    
    protected void explode() {
        this.setDead();
        if (!this.worldObj.isRemote){
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 1.5F, true);
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
