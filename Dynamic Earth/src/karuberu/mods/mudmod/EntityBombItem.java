package karuberu.mods.mudmod;

import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityBombItem extends EntityItem {
	public EntityBombItem(World world) {
		super(world);
	}
	
	public EntityBombItem(World world, double x, double y, double z, ItemStack itemStack) {
		super(world, x, y, z, itemStack);
	}

	@Override
    public void onUpdate() {
		super.onUpdate();
		if (this.item.getItemDamage() >= this.item.getMaxDamage()) {
			this.worldObj.spawnEntityInWorld(new EntityBomb(this.worldObj, this.posX, this.posY, this.posZ, 0));
			this.setDead();
		} else {
			this.item.setItemDamage(this.item.getItemDamage()+1);
		}
	}
}
