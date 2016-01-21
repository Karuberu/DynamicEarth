package karuberu.dynamicearth.items;

import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.item.ItemStack;

public class ItemBlockSandySoil extends ItemBlockDynamicEarth {
	
	public ItemBlockSandySoil(String unlocalizedName) {
		super(unlocalizedName);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		if (damage == DynamicEarth.sandySoil.DIRT) {
			return super.getUnlocalizedName() + ".dirt";
		} else if (damage == DynamicEarth.sandySoil.GRASS) {
			return super.getUnlocalizedName() + ".grass";
		} else if (damage == DynamicEarth.sandySoil.MYCELIUM) {
			return super.getUnlocalizedName() + ".mycelium";
		} else {
			return super.getUnlocalizedName();
		}
	}
}