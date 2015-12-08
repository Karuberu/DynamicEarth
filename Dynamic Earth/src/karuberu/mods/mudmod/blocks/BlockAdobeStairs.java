package karuberu.mods.mudmod.blocks;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

public class BlockAdobeStairs extends BlockStairs {

	public BlockAdobeStairs(int id, Block block, int par3) {
		super(id, block, par3);
        this.useNeighborBrightness[id] = true;
        this.setTextureFile(MudMod.terrainFile);
	}
}
