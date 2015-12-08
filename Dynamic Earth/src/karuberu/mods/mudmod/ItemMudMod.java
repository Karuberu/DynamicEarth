package karuberu.mods.mudmod;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemMudMod extends Item {

	protected ItemMudMod(int i) {
		super(i);
		this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setTextureFile(MudMod.itemsFile);
	}
}
