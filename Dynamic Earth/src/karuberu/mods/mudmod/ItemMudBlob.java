package karuberu.mods.mudmod;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemMudBlob extends Item {

	protected ItemMudBlob(int i) {
		super(i);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
    @Override
    public String getTextureFile() {
    	return MudMod.itemsFile;
    }
}
