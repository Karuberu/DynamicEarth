package karuberu.mods.mudmod.blocks;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class BlockMudBrick extends Block {

	public BlockMudBrick(int id) {
		super(id, Material.rock);
		this.setHardness(1.0F);
		this.setResistance(3.0F);
		this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName("blockMudBrick");
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = TextureManager.instance().getBlockTexture(Texture.MUDBRICKBLOCK);
	}
}
