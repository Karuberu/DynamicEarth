package karuberu.dynamicearth.items;

import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.item.ItemStack;

public class ItemBlockFertileSoil extends ItemBlockDynamicEarth {

	public ItemBlockFertileSoil(String unlocalizedName) {
		super(unlocalizedName);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		if (damage == DynamicEarth.fertileSoil.DIRT) {
			return super.getUnlocalizedName() + ".dirt";
		} else if (damage == DynamicEarth.fertileSoil.GRASS) {
			return super.getUnlocalizedName() + ".grass";
		} else if (damage == DynamicEarth.fertileSoil.MYCELIUM) {
			return super.getUnlocalizedName() + ".mycelium";
		} else {
			return super.getUnlocalizedName();
		}
	}
}