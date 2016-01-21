package karuberu.dynamicearth.items;

import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.item.ItemStack;

public class ItemBlockFertileMud extends ItemBlockDynamicEarth {
	
	public ItemBlockFertileMud(String unlocalizedName) {
		super(unlocalizedName);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		if (damage == DynamicEarth.fertileMud.NORMAL) {
			return super.getUnlocalizedName() + ".mud";
		} else if (damage == DynamicEarth.fertileMud.GRASS) {
			return super.getUnlocalizedName() + ".grass";
		} else if (damage == DynamicEarth.fertileMud.MYCELIUM) {
			return super.getUnlocalizedName() + ".mycelium";
		} else if (damage == DynamicEarth.fertileMud.WET) {
			return super.getUnlocalizedName() + ".wet";
		} else if (damage == DynamicEarth.fertileMud.WET_GRASS) {
			return super.getUnlocalizedName() + ".grassWet";
		} else if (damage == DynamicEarth.fertileMud.WET_MYCELIUM) {
			return super.getUnlocalizedName() + ".myceliumWet";
		} else {
			return super.getUnlocalizedName();
		}
	}
}