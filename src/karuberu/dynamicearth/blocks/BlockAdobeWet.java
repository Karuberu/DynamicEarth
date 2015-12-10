package karuberu.dynamicearth.blocks;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class BlockAdobeWet extends BlockDynamicEarth {
	public static CreativeTabs
		creativeTab = CreativeTabs.tabBlock;
    
	public BlockAdobeWet(int id) {
		super(id, Material.ground);
		this.setHardness(0.5F);
		this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(creativeTab);
		this.setUnlocalizedName("adobeWet");
        this.setTickRandomly(true);
        this.setHydrateRadius(2, 1, 2);
        this.setTextureName(BlockTexture.ADOBEWET.getIconPath());
	}
	
	@Override
	protected ItemStack getDryBlock(int metadata) {
		return new ItemStack(DynamicEarth.adobe.blockID, 1, 0);
	}
}
