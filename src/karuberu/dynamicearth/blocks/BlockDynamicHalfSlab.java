package karuberu.dynamicearth.blocks;

import karuberu.core.util.block.BlockHalfSlabExtended;
import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public abstract class BlockDynamicHalfSlab extends BlockHalfSlabExtended {
    public static CreativeTabs
		creativeTab = CreativeTabs.tabBlock;
	
	public BlockDynamicHalfSlab(String unlocalizedName, boolean isDoubleSlab, Material material) {
		super(DynamicEarth.config, unlocalizedName, isDoubleSlab, material);
		this.setTickRandomly(true);
		this.setCreativeTab(BlockDynamicHalfSlab.creativeTab);
		this.setUnlocalizedName(unlocalizedName);
		this.setUseNeighborBrightness(true);
	}
}
