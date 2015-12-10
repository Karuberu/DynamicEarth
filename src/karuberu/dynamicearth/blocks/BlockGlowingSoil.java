package karuberu.dynamicearth.blocks;

import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class BlockGlowingSoil extends BlockSoil {
	public static final int
		LIGHT_LEVEL = 13;
	
	public BlockGlowingSoil(int id) {
		super(id);
		this.setUnlocalizedName("glowingSoil");
	}

	@Override
    public void registerIcons(IconRegister iconRegister) {
		this.textureDirt = iconRegister.registerIcon("dirt");
		this.textureGrassSide = iconRegister.registerIcon("grass_side");
		this.textureMyceliumSide = iconRegister.registerIcon("mycelium_side");
		this.textureSnowSide = iconRegister.registerIcon("grass_snow_side");
		this.textureGrassSideOverlay = iconRegister.registerIcon("grass_side_overlay");
		this.textureGrassTop = iconRegister.registerIcon("grass_top");
		this.textureMyceliumTop = iconRegister.registerIcon("mycelium_top");
    }
	
	@Override
	protected void initializeItemStacks() {
		super.initializeItemStacks();
		this.mudStack = new ItemStack(DynamicEarth.glowingMud.blockID, 1, DynamicEarth.glowingMud.NORMAL);
		this.farmlandStack = new ItemStack(DynamicEarth.farmland.blockID, 1, BlockDynamicFarmland.GLOWING_DRY);
	}
	
	@Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        if (block != null && block != this) {
            return block.getLightValue(world, x, y, z);
        }
        return BlockGlowingSoil.LIGHT_LEVEL;
    }
}
