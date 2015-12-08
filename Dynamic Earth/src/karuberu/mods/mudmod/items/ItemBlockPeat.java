package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.blocks.BlockPeat;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPeat extends ItemBlock {

	public ItemBlockPeat(int id) {
		super(id);
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
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}