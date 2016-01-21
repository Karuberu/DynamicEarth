package karuberu.dynamicearth.items;

import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.item.ItemStack;

public class ItemBlockGlowingMud extends ItemBlockDynamicEarth {
	
	public ItemBlockGlowingMud(String unlocalizedName) {
		super(unlocalizedName);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		if (damage == DynamicEarth.glowingMud.NORMAL) {
			return super.getUnlocalizedName() + ".mud";
		} else if (damage == DynamicEarth.glowingMud.GRASS) {
			return super.getUnlocalizedName() + ".grass";
		} else if (damage == DynamicEarth.glowingMud.MYCELIUM) {
			return super.getUnlocalizedName() + ".mycelium";
		} else if (damage == DynamicEarth.glowingMud.WET) {
			return super.getUnlocalizedName() + ".wet";
		} else if (damage == DynamicEarth.glowingMud.WET_GRASS) {
			return super.getUnlocalizedName() + ".grassWet";
		} else if (damage == DynamicEarth.glowingMud.WET_MYCELIUM) {
			return super.getUnlocalizedName() + ".myceliumWet";
		} else {
			return super.getUnlocalizedName();
		}
	}
}