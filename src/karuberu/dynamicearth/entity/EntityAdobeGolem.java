package karuberu.dynamicearth.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityAdobeGolem extends EntityIronGolem {
	
	private int attackTimer;

	public EntityAdobeGolem(World world) {
		super(world);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getAttackTimer() {
		return this.attackTimer;
	}
	
	@Override
    public boolean attackEntityAsMob(Entity entity) {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte)4);
        boolean entityDamaged = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);

        if (entityDamaged) {
            entity.motionY += 0.2D;
        }
        
        this.worldObj.playSoundAtEntity(this, "mob.irongolem.throw", 1.0F, 1.0F);
        return entityDamaged;
    }
    
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20.0D);
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.attackTimer > 0) {
			--this.attackTimer;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte status) {
		if (status == 4) {
			this.attackTimer = 10;
			this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
		} else {
			super.handleHealthUpdate(status);
		}
	}
    
	@Override
    protected void dropFewItems(boolean par1, int par2) {
        int numDust = 3 + this.rand.nextInt(6);
        for (int i = 0; i < numDust; ++i) {
            this.dropItem(DynamicEarth.adobeDust.itemID, 1);
        }
    }

}
