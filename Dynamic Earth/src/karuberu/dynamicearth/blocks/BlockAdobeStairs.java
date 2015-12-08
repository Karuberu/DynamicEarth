package karuberu.dynamicearth.blocks;

import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IconRegister;

public class BlockAdobeStairs extends BlockStairs {

	public BlockAdobeStairs(int id, Block par2Block, int par3) {
		super(id, par2Block, par3);
        this.useNeighborBrightness[id] = true;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {}
}
