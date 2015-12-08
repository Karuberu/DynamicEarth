package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityIronGolem;
import net.minecraft.src.Item;
import net.minecraft.src.World;

public class EntityClayGolem extends EntityIronGolem {
	
	private int attackTimer;

	public EntityClayGolem(World world) {
		super(world);
        this.getNavigator().setAvoidsWater(false);
        this.texture = MudMod.clayGolemFile;
	}
	
	@Override
    public int getMaxHealth() {
        return 20;
    }
	
	@Override
    public boolean attackEntityAsMob(Entity entity) {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte)4);
        boolean entityDamaged = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5);

        if (entityDamaged) {
            entity.motionY += 0.2000000059604645D;
        }

        this.worldObj.playSoundAtEntity(this, "mob.irongolem.throw", 1.0F, 1.0F);
        return entityDamaged;
    }
    
	@Override
    protected void dropFewItems(boolean par1, int par2) {
        int numDust = 3 + this.rand.nextInt(6);
        for (int i = 0; i < numDust; ++i) {
            this.dropItem(MudMod.adobeDust.shiftedIndex, 1);
        }
    }

}
