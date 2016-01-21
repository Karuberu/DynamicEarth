package karuberu.dynamicearth.blocks;

import karuberu.core.util.block.BlockExtended;
import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.block.material.Material;

public class BlockDynamicEarth extends BlockExtended {

	public BlockDynamicEarth(String unlocalizedName, Material material) {
		super(DynamicEarth.config, unlocalizedName, material);
		this.setTickRandomly(true);
	}
}
