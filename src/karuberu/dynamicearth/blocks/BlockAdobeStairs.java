package karuberu.dynamicearth.blocks;

import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class BlockAdobeStairs extends BlockStairs {

    public static CreativeTabs
		creativeTab = CreativeTabs.tabBlock;
    
	public BlockAdobeStairs(String unlocalizedName, Block par2Block, int par3) {
		super(DynamicEarth.config.getBlockID(unlocalizedName), par2Block, par3);
		this.setCreativeTab(creativeTab);
        Block.useNeighborBrightness[this.blockID] = true;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {}
}
