package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityThrowable;
import net.minecraft.src.IMob;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class EntityMudball extends EntityThrowable implements IMob
{
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
            movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_85052_h()), 0);
        }

        Random rand = new Random();
        for (int i = 0; i < 8; ++i) {
        	double x = rand.nextInt(4) / 10D;
        	double y = rand.nextInt(4) / 10D;
        	double z = rand.nextInt(4) / 10D;
            this.worldObj.spawnParticle("iconcrack_" + new ItemStack(MudMod.mudBlob).getItem().shiftedIndex, this.posX, this.posY, this.posZ, -0.2D + x, -0.2D + y, -0.2D + z);
        }

        if (!this.worldObj.isRemote){
            this.setDead();
        }
    }
}
