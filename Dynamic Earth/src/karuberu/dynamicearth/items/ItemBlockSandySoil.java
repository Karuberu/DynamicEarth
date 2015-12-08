package karuberu.dynamicearth.items;

import karuberu.dynamicearth.blocks.BlockSandySoil;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockSandySoil extends ItemBlock {

	public ItemBlockSandySoil(int id) {
		super(id);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		switch (itemStack.getItemDamage()) {
		case BlockSandySoil.DIRT:
			return super.getUnlocalizedName() + ".sandySoil";
		case BlockSandySoil.GRASS:
			return super.getUnlocalizedName() + ".sandyGrass";
		case BlockSandySoil.MYCELIUM:
			return super.getUnlocalizedName() + ".sandyMycelium";
		default:
			return super.getUnlocalizedName();
		}
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}