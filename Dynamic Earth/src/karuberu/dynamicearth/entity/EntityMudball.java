package karuberu.dynamicearth.entity;

import java.util.Random;

import karuberu.dynamicearth.DynamicEarth;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityMudball extends EntityThrowable {
    public EntityMudball(World world) {
        super(world);
    }

    public EntityMudball(World world, EntityLiving entityLiving) {
        super(world, entityLiving);
    }

    public EntityMudball(World world, double x, double y, double z) {
        super(world, x, y, z);
    }
    
    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        if (movingObjectPosition.entityHit != null) {
            movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
        }

        Random rand = new Random();
        for (int i = 0; i < 8; ++i) {
        	double x = rand.nextInt(4) / 10D;
        	double y = rand.nextInt(4) / 10D;
        	double z = rand.nextInt(4) / 10D;
            this.worldObj.spawnParticle("iconcrack_" + new ItemStack(DynamicEarth.mudBlob).getItem().itemID, this.posX, this.posY, this.posZ, -0.2D + x, -0.2D + y, -0.2D + z);
        }

        if (!this.worldObj.isRemote){
            this.setDead();
        }
    }
}
