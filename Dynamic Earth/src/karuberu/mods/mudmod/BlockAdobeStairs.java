package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.BlockStairs;
import net.minecraft.src.CreativeTabs;

public class BlockAdobeStairs extends BlockStairs {

	protected BlockAdobeStairs(int par1, Block par2Block, int par3) {
		super(par1, par2Block, par3);
		this.setLightOpacity(0);
	}

    @Override
    public String getTextureFile() {
    	return MudMod.terrainFile;
    }
}
