package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.blocks.BlockMud;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMud extends ItemBlock {

	public ItemBlockMud(int id) {
		super(id);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		switch (itemStack.getItemDamage()) {
		case BlockMud.NORMAL:
			return super.getUnlocalizedName() + ".mud";
		case BlockMud.WET:
			return super.getUnlocalizedName() + ".wet";
		case BlockMud.FERTILE:
			return super.getUnlocalizedName() + ".fertile";
		case BlockMud.FERTILE_WET:
			return super.getUnlocalizedName() + ".fertileWet";
		default:
			return super.getUnlocalizedName();
		}
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}