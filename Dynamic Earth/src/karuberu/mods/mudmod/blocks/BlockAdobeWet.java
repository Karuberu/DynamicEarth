package karuberu.mods.mudmod.blocks;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockAdobeWet extends BlockMudMod {
	
	public BlockAdobeWet(int id, int textureIndex) {
		super(id, textureIndex, Material.ground);
		this.setHardness(0.5F);
		this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("adobeWet");
        this.setTickRandomly(true);
        this.setTextureFile(MudMod.terrainFile);
        this.setHydrateRadius(2, 1, 2);
	}
	
	@Override
	protected int getDryBlock(int metadata) {
		return MudMod.adobe.blockID;
	}
}
