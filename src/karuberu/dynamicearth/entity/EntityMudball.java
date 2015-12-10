package karuberu.dynamicearth.entity;

import java.util.Random;

import karuberu.core.util.Helper;
import karuberu.dynamicearth.DynamicEarth;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityMudball extends EntityThrowable {
    public EntityMudball(World world) {
        super(world);
    }

    public EntityMudball(World world, EntityLivingBase entityLiving) {
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
        if (!this.worldObj.isRemote
        && DynamicEarth.includeMudLayers) {
        	int x = MathHelper.floor_double(this.posX);
        	int y = MathHelper.floor_double(this.posY);
        	int z = MathHelper.floor_double(this.posZ);
            if (DynamicEarth.mudLayer.canBlockStay(this.worldObj, x, y, z)) {
            	Material material = this.worldObj.getBlockMaterial(x, y, z);
            	if (material.isReplaceable() && !material.isLiquid()) {
            		this.worldObj.setBlock(x, y, z, DynamicEarth.mudLayer.blockID, 0, Helper.NOTIFY_AND_UPDATE_REMOTE);
            	} else if (this.worldObj.getBlockId(x, y, z) == DynamicEarth.mudLayer.blockID) {
            		int metadata = this.worldObj.getBlockMetadata(x, y, z);
            		this.worldObj.setBlock(x, y, z, DynamicEarth.mudLayer.blockID, metadata + 1, Helper.NOTIFY_AND_UPDATE_REMOTE);
            	}
            }
            this.setDead();
        }
    }
}
