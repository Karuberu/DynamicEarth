package karuberu.dynamicearth.items;

import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.item.ItemStack;

public class ItemBlockBurningSoil extends ItemBlockDynamicEarth {
	
	public ItemBlockBurningSoil(String unlocalizedName) {
		super(unlocalizedName);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		if (damage == DynamicEarth.burningSoil.DIRT) {
			return super.getUnlocalizedName() + ".burningSoil";
		} else if (damage == DynamicEarth.burningSoil.GRASS) {
			return super.getUnlocalizedName() + ".burningGrass";
		} else {
			return super.getUnlocalizedName();
		}
	}
}