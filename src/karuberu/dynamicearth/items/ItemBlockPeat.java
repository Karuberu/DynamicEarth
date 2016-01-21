package karuberu.dynamicearth.items;

import karuberu.dynamicearth.blocks.BlockPeat;
import net.minecraft.item.ItemStack;

public class ItemBlockPeat extends ItemBlockDynamicEarth {
	
	public ItemBlockPeat(String unlocalizedName) {
		super(unlocalizedName);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		switch (itemStack.getItemDamage()) {
		case BlockPeat.WET:
			return super.getUnlocalizedName() + ".wet";
		case BlockPeat.DRY:
			return super.getUnlocalizedName() + ".dry";
		default:
			return super.getUnlocalizedName();
		}
	}
}