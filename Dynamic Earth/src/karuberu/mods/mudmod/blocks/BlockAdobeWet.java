package karuberu.mods.mudmod.blocks;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockAdobeWet extends BlockMudMod {
	
	public BlockAdobeWet(int id, int texture) {
		super(id, texture, Material.ground);
		this.setHardness(0.5F);
		this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("adobeWet");
        this.setTickRandomly(true);
        this.setHydrateRadius(2, 1, 2);
        this.setTextureFile(MudMod.terrainFile);
	}
	
	@Override
	protected int getDryBlock(int metadata) {
		return MudMod.adobe.blockID;
	}
}
