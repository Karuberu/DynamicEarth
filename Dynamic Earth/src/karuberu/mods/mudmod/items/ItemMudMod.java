package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemMudMod extends Item {

	public ItemMudMod(int id, int icon) {
		super(id);
        this.setTextureFile(MudMod.itemsFile);
        this.setIconIndex(icon);
	}
}
