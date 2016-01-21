package karuberu.dynamicearth.items;

import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.item.ItemStack;

public class ItemBlockGlowingSoil extends ItemBlockDynamicEarth {

	public ItemBlockGlowingSoil(String unlocalizedName) {
		super(unlocalizedName);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		if (damage == DynamicEarth.glowingSoil.DIRT) {
			return super.getUnlocalizedName() + ".dirt";
		} else if (damage == DynamicEarth.glowingSoil.GRASS) {
			return super.getUnlocalizedName() + ".grass";
		} else if (damage == DynamicEarth.glowingSoil.MYCELIUM) {
			return super.getUnlocalizedName() + ".mycelium";
		} else {
			return super.getUnlocalizedName();
		}
	}
}