package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.blocks.BlockFertileSoil;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockFertileSoil extends ItemBlock {

	public ItemBlockFertileSoil(int id) {
		super(id);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		switch (itemStack.getItemDamage()) {
		case BlockFertileSoil.SOIL:
			return super.getUnlocalizedName() + ".fertileSoil";
		case BlockFertileSoil.GRASS:
			return super.getUnlocalizedName() + ".fertileGrass";
		case BlockFertileSoil.MYCELIUM:
			return super.getUnlocalizedName() + ".fertileMycelium";
		default:
			return super.getUnlocalizedName();
		}
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}