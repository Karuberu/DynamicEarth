package karuberu.dynamicearth.api.grass;

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
    public enum GrassType {
    	DIRT,
    	GRASS,
    	MYCELIUM
    };
    /**
     * Returns true if the block can spread in its current state.
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
    public void tryToGrow(World world, int x, int y, int z, GrassType type);
    /**
     * Returns the grass type for this block at the given coordinates.
     * @param world: the world the blocks are in.
     * @param x: the block's x coordinate.
     * @param y: the block's y coordinate.
     * @param z: the block's z coordinate.
     * @return
     */
    public GrassType getType(World world, int x, int y, int z);
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
    public ItemStack getBlockForType(World world, int x, int y, int z, GrassType type);
    /**
     * Return true if the grass will burn into dirt when lit on fire.
     * @param world: the world the blocks are in.
     * @param x: the block's x coordinate.
     * @param y: the block's y coordinate.
     * @param z: the block's z coordinate.
     */
    public boolean willBurn(World world, int x, int y, int z);
}
