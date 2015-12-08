package karuberu.dynamicearth.blocks;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;

public class BlockSandySoil extends BlockSoil {
	
	public BlockSandySoil(int id) {
		super(id);
		this.setUnlocalizedName("sandySoil");
	}
	
	@Override
    public void registerIcons(IconRegister iconRegister) {
		this.textureDirt = iconRegister.registerIcon(BlockTexture.SANDYSOIL.getIconPath());
		this.textureGrassSide = iconRegister.registerIcon(BlockTexture.SANDYGRASSSIDE.getIconPath());
		this.textureMyceliumSide = iconRegister.registerIcon(BlockTexture.SANDYMYCELIUMSIDE.getIconPath());
		this.textureSnowSide = iconRegister.registerIcon(BlockTexture.SANDYSNOWSIDE.getIconPath());
		this.textureGrassSideOverlay = iconRegister.registerIcon("grass_side_overlay");
		this.textureGrassTop = iconRegister.registerIcon("grass_top");
		this.textureMyceliumTop = iconRegister.registerIcon("mycelium_top");
    }
	
	@Override
	protected void initializeItemStacks() {
		super.initializeItemStacks();
		this.farmlandStack = new ItemStack(DynamicEarth.farmland.blockID, 1, BlockDynamicFarmland.SANDY_DRY);
	}
	
    @Override
	public ItemStack getWetBlock(int metadata) {
    	return null;
    }
    
    @Override
    protected ItemStack getDryBlock(int metadata) {
    	return null;
    }
}
