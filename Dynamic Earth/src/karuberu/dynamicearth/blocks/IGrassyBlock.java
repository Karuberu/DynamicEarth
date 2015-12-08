package karuberu.dynamicearth.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Interface used to emulate vanilla dirt, grass, and mycelium blocks. Includes
 * methods to facilitate the growing and spreading of grass and mycelium. Simply
 * implementing this interface will cause the block to grow grass/mycelium
 * and spread that grass/mycelium to other blocks. It will also allow the grass
 * to be eaten by sheep.
 * @author Karuberu
 */
public interface IGrassyBlock {
    public enum EnumGrassType {
    	DIRT,
    	GRASS,
    	MYCELIUM
    };
    /**
     * Attempt to grow this block's version of the given type of grass.
     * @param world: the world the blocks are in.
     * @param x: the block's x coordinate.
     * @param y: the block's y coordinate.
     * @param z: the block's z coordinate.
     */
    public boolean canSpread(World world, int x, int y, int z);
    /**
     * Attempt to grow this block's version of the given type of grass.
     * @param world: the world the blocks are in.
     * @param x: the block's x coordinate.
     * @param y: the block's y coordinate.
     * @param z: the block's z coordinate.
     * @param type: the type of grass that wants to grow.
     */
    public void tryToGrow(World world, int x, int y, int z, EnumGrassType type);
    /**
     * Returns the grass type for this block at the given coordinates.
     * @param world: the world the blocks are in.
     * @param x: the block's x coordinate.
     * @param y: the block's y coordinate.
     * @param z: the block's z coordinate.
     * @return
     */
    public EnumGrassType getType(World world, int x, int y, int z);
    /**
     * Returns an item stack containing this block's version of the given grass
     * type.
     * @param world: the world the blocks are in.
     * @param x: the block's x coordinate.
     * @param y: the block's y coordinate.
     * @param z: the block's z coordinate.
     * @param type: the type of grass being requested.
     * @return
     */
    public ItemStack getBlockForType(World world, int x, int y, int z, EnumGrassType type);

    /**
     * Implementation of IGrassyBlock for vanilla dirt, grass, and mycelium.
     */
    public static IGrassyBlock dirt = new IGrassyBlock() {
    	@Override
    	public boolean canSpread(World world, int x, int y, int z) {
    		EnumGrassType type = this.getType(world, x, y, z);
    		if ((type == EnumGrassType.GRASS || type == EnumGrassType.MYCELIUM)
    		&& world.getBlockLightValue(x, y + 1, z) >= 9) {
    			return true;
    		}
    		return false;
    	}
    	
    	@Override
    	public void tryToGrow(World world, int x, int y, int z, EnumGrassType type) {
    		if (this.getType(world, x, y, z) == EnumGrassType.DIRT
    		&& world.getBlockLightValue(x, y + 1, z) >= 4
            && world.getBlockLightOpacity(x, y + 1, z) <= 2) {
    			switch (type) {
    			case GRASS:
    				world.setBlock(x, y, z, Block.grass.blockID);
    				break;
    			case MYCELIUM:
    				world.setBlock(x, y, z, Block.mycelium.blockID);
    				break;
    			default:
    				return;
    			}
    		}
    	}

    	@Override
    	public EnumGrassType getType(World world, int x, int y, int z) {
    		int blockId = world.getBlockId(x, y, z);
            if (blockId == Block.grass.blockID) {
        		return EnumGrassType.GRASS;
            } else if (blockId == Block.mycelium.blockID) {
        		return EnumGrassType.MYCELIUM;
            } else {
        		return EnumGrassType.DIRT;
            }
    	}
    	
    	@Override
    	public ItemStack getBlockForType(World world, int x, int y, int z, EnumGrassType type) {
    		switch (type) {
    		case DIRT:
    			return new ItemStack(Block.dirt);
    		case GRASS:
    			return new ItemStack(Block.grass);
    		case MYCELIUM:
    			return new ItemStack(Block.mycelium);
    		}
    		return null;
    	}
    };
}
