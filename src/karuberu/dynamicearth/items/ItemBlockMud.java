package karuberu.dynamicearth.items;

import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.item.ItemStack;

public class ItemBlockMud extends ItemBlockDynamicEarth {
	
	public ItemBlockMud(String unlocalizedName) {
		super(unlocalizedName);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		if (damage == DynamicEarth.mud.NORMAL) {
			return super.getUnlocalizedName() + ".mud";
		} else if (damage == DynamicEarth.mud.GRASS) {
			return super.getUnlocalizedName() + ".grass";
		} else if (damage == DynamicEarth.mud.MYCELIUM) {
			return super.getUnlocalizedName() + ".mycelium";
		} else if (damage == DynamicEarth.mud.WET) {
			return super.getUnlocalizedName() + ".wet";
		} else if (damage == DynamicEarth.mud.WET_GRASS) {
			return super.getUnlocalizedName() + ".grassWet";
		} else if (damage == DynamicEarth.mud.WET_MYCELIUM) {
			return super.getUnlocalizedName() + ".myceliumWet";
		} else {
			return super.getUnlocalizedName();
		}
	}
}