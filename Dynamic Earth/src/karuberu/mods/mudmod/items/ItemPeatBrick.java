package karuberu.mods.mudmod.items;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemPeatBrick extends ItemMudMod implements IFuelHandler {

	public ItemPeatBrick(int id) {
		super(id);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		return 1200;
	}
}
