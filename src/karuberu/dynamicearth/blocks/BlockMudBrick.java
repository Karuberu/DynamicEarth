package karuberu.dynamicearth.blocks;

import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class BlockMudBrick extends Block {
    public static CreativeTabs
		creativeTab = CreativeTabs.tabBlock;
    
	public BlockMudBrick(int id) {
		super(id, Material.rock);
		this.setHardness(1.0F);
		this.setResistance(3.0F);
		this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(creativeTab);
		this.setUnlocalizedName("blockMudBrick");
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(BlockTexture.MUDBRICKBLOCK.getIconPath());
	}
}
