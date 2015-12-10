package karuberu.dynamicearth.blocks;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.items.ItemMudBlob;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class BlockGlowingMud extends BlockMud {

	public BlockGlowingMud(int id) {
		super(id);
		this.setUnlocalizedName("glowingMud");
	}
	
	@Override
	protected void initializeItemStacks() {
		super.initializeItemStacks();
		this.dryStack = new ItemStack(DynamicEarth.glowingSoil.blockID, 1, DynamicEarth.glowingSoil.DIRT);
		this.dryGrassStack = new ItemStack(DynamicEarth.glowingSoil.blockID, 1, DynamicEarth.glowingSoil.GRASS);
		this.dryMyceliumStack = new ItemStack(DynamicEarth.glowingSoil.blockID, 1, DynamicEarth.fertileSoil.MYCELIUM);
		this.farmlandStack = new ItemStack(DynamicEarth.farmland.blockID, 1, BlockDynamicFarmland.GLOWING_WET);
	}

	@Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        if (block != null && block != this) {
            return block.getLightValue(world, x, y, z);
        }
        return BlockGlowingSoil.LIGHT_LEVEL;
    }
	
	@Override
	public int damageDropped(int metadata) {
		return ItemMudBlob.GLOWING;
	}
}
