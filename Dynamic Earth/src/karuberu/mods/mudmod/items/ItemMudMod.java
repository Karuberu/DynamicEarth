package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemMudMod extends Item {

	public ItemMudMod(int i) {
		super(i);
        this.setTextureFile(MudMod.itemsFile);
	}
}
