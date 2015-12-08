package karuberu.mods.mudmod.blocks;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockMudBrick extends Block {

	public BlockMudBrick(int i, int j) {
		super(i, j, Material.rock);
		this.setHardness(1.0F);
		this.setResistance(3.0F);
		this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("blockMudBrick");
        this.setTextureFile(MudMod.terrainFile);
	}
}
