package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;

public class BlockMudBrick extends Block {

	public BlockMudBrick(int i, int j) {
		super(i, j, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
    @Override
    public String getTextureFile() {
    	return MudMod.terrainFile;
    }
}
