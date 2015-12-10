package karuberu.dynamicearth.api.grass;

import karuberu.dynamicearth.api.grass.IGrassyBlock.GrassType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GrassyBlockRegistry {
	private static GrassyBlock[][]
		registeredBlocks = new GrassyBlock[Block.blocksList.length][16];
	
	/**
	 * Creates a new grassy block using the given ids and metadata values and adds it to the registry.
	 * For more control over how the block behaves, implement the IGrassyBlock interface instead.
	 * @param dirtStack : An item stack containing the block id and metadata for the dirt form of this block.
	 * @param grassStack : An item stack containing the block id and metadata for the grass form of this block.
	 * @param myceliumStack : An item stack containing the block id and metadata for the mycelium form of this block.
	 */
	public static void registerGrassyBlock(ItemStack dirtStack, ItemStack grassStack, ItemStack myceliumStack) {
		GrassyBlock grassyBlock = new GrassyBlock(dirtStack, grassStack, myceliumStack);
		GrassyBlockRegistry.registeredBlocks[dirtStack.itemID][dirtStack.getItemDamage()] = grassyBlock;
		GrassyBlockRegistry.registeredBlocks[grassStack.itemID][grassStack.getItemDamage()] = grassyBlock;
		GrassyBlockRegistry.registeredBlocks[myceliumStack.itemID][myceliumStack.getItemDamage()] = grassyBlock;
	}
		
	public static boolean isGrassyBlock(World world, int x, int y, int z) {
		return GrassyBlockRegistry.getGrassyBlock(world, x, y, z) != null;
	}
	
	public static IGrassyBlock getGrassyBlock(World world, int x, int y, int z) {
		return GrassyBlockRegistry.getGrassyBlock(world.getBlockId(x, y, z), world.getBlockMetadata(x, y, z));
	}
	
	public static IGrassyBlock getGrassyBlock(int blockID, int metadata) {
		Block block = Block.blocksList[blockID];
		return GrassyBlockRegistry.getGrassyBlock(block, metadata);		
	}
	
	public static IGrassyBlock getGrassyBlock(Block block, int metadata) {
		if (block == null) {
			return null;
		}
		if (block instanceof IGrassyBlock) {
			return (IGrassyBlock)block;
		}
		return GrassyBlockRegistry.registeredBlocks[block.blockID][metadata];
	}
	
	public static GrassType getGrassyBlockType(World world, int x, int y, int z) {
		IGrassyBlock grassyBlock = GrassyBlockRegistry.getGrassyBlock(world, x, y, z);
		if (grassyBlock == null) {
			return null;
		}
		return grassyBlock.getType(world, x, y, z);
	}
	
	public static boolean isModdedBlock(World world, int x, int y, int z) {
		int blockId = world.getBlockId(x, y, z);
		return blockId != Block.grass.blockID && blockId != Block.mycelium.blockID;
	}
}
