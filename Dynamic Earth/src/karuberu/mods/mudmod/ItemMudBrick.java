package karuberu.mods.mudmod;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemMudBrick extends Item {

	protected ItemMudBrick(int i) {
		super(i);
		this.setTabToDisplayOn(CreativeTabs.tabMaterials);
	}
	
    @Override
    public String getTextureFile() {
    	return MudMod.itemsFile;
    }
}
