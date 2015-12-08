package karuberu.mods.mudmod.blocks;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IconRegister;

@SuppressWarnings("unused")
public class BlockAdobeStairs extends BlockStairs {

	public BlockAdobeStairs(int id, Block par2Block, int par3) {
		super(id, par2Block, par3);
        Block.useNeighborBrightness[id] = true;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {}
}
