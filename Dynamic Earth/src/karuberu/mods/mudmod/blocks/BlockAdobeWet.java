package karuberu.mods.mudmod.blocks;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class BlockAdobeWet extends BlockMudMod {
	
	public BlockAdobeWet(int id) {
		super(id, Material.ground);
		this.setHardness(0.5F);
		this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName("adobeWet");
        this.setTickRandomly(true);
        this.setHydrateRadius(2, 1, 2);
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = TextureManager.instance().getBlockTexture(Texture.ADOBEWET);
	}
	
	@Override
	protected int getDryBlock(int metadata) {
		return MudMod.adobe.blockID;
	}
}
