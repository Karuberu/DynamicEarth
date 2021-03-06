package karuberu.dynamicearth.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.creativetab.CreativeTabDynamicEarth;

public class BlockLiquid extends BlockDynamicEarth {
	private BlockTexture
		textureStill,
		textureMoving;
	private Icon
		iconStill,
		iconMoving;
	
	public BlockLiquid(String unlocalizedName, Material material, BlockTexture icon) {
		super(unlocalizedName, material);
		this.textureStill = this.textureMoving = icon;
		this.setCreativeTab(CreativeTabDynamicEarth.tabDynamicEarth);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.iconStill = iconRegister.registerIcon(this.textureStill.getIconPath());
		this.iconMoving = iconRegister.registerIcon(this.textureMoving.getIconPath());
	}
	
    @Override
    public Icon getIcon(int side, int metadata) {
    	if (metadata == 0) {
    		return this.iconStill;
    	} else {
    		return this.iconMoving;
    	}
    }
}
