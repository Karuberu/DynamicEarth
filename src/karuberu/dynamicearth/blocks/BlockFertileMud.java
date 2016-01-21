package karuberu.dynamicearth.blocks;

import java.util.Random;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.items.ItemMudBlob;
import net.minecraft.block.Block;
import net.minecraft.block.BlockReed;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class BlockFertileMud extends BlockMud {

	public BlockFertileMud(String unlocalizedName) {
		super(unlocalizedName);
	}
	
	@Override
	protected void initializeItemStacks() {
		super.initializeItemStacks();
		this.dryStack = new ItemStack(DynamicEarth.fertileSoil.blockID, 1, DynamicEarth.fertileSoil.DIRT);
		this.dryGrassStack = new ItemStack(DynamicEarth.fertileSoil.blockID, 1, DynamicEarth.fertileSoil.GRASS);
		this.dryMyceliumStack = new ItemStack(DynamicEarth.fertileSoil.blockID, 1, DynamicEarth.fertileSoil.MYCELIUM);
		this.farmlandStack = new ItemStack(DynamicEarth.farmland.blockID, 1, BlockDynamicFarmland.FERTILE_WET);
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (this.getRawMeta(metadata) == this.NORMAL) {
			Block topBlock = Block.blocksList[world.getBlockId(x, y + 1, z)];
			if (topBlock instanceof IPlantable) {
				// extra growth for plants
				topBlock.updateTick(world, x, y + 1, z, random);
				if (topBlock instanceof BlockReed) {
					// even more extra growth for reeds
					topBlock.updateTick(world, x, y + 1, z, random);
				}
			}
		}
		super.updateTick(world, x, y, z, random);
	}
	
	@Override
	public int damageDropped(int metadata) {
		return ItemMudBlob.FERTILE;
	}
}
