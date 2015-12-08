package karuberu.core;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class MCHelper {
	public static final int
		SIDE_BOTTOM = 0,
		SIDE_TOP = 1,
		SIDE_EAST = 2,
		SIDE_WEST = 3,
		SIDE_NORTH = 4,
		SIDE_SOUTH = 5;
	public static final int
		BOTTOM_SLAB = 0,
		TOP_SLAB = 8;
	public static final int
		DO_NOT_NOTIFY_OR_UPDATE = 0,
		UPDATE_WITHOUT_NOTIFY_REMOTE = 2,
		UPDATE_WITHOUT_NOTIFY = 6,
		NOTIFY_WITHOUT_UPDATE = 1,
		NOTIFY_AND_UPDATE_REMOTE = 3,
		NOTIFY_AND_UPDATE = 7;
	
	/**
	 * Returns the pure metadata of the slab, regardless of whether it is a
	 * top slab or a bottom slab (e.g. an oak wood slab would return 0, while
	 * a spruce wood slab would return 1).
	 * @param metadata : the raw metadata of the slab
	 */
	public static int getSlabMetadata(int metadata) {
		return metadata & 7;
	}
	
	public static boolean isTopSlab(int metadata) {
		return getSlabType(metadata) == TOP_SLAB;
	}
	
	public static boolean isBottomSlab(int metadata) {
		return getSlabType(metadata) == BOTTOM_SLAB;
	}
	
	private static int getSlabType(int metadata) {
		return metadata & 8;
	}
	
	/**
	 * Applies the appropriate changes to the source metadata in order to
	 * convert it to the target metadata (e.g. top slabs remain top slabs,
	 * bottom slabs remain bottom slabs, but an oak slab could be turned into
	 * a spruce slab).
	 * @param sourceMeta : the raw metadata of the block
	 * @param targetMeta : the pure metadata of the block it needs to become
	 */
	public static int convertSlabMetadata(int sourceMeta, int targetMeta) {
		return MCHelper.getSlabType(sourceMeta) - MCHelper.getSlabMetadata(sourceMeta) + targetMeta;
	}
	
	/**
	 * Returns a two-dimensional array of coordinates that represent the blocks
	 * on each the six sides of the given coordinate.
	 * @param x : the x coordinate of the block
	 * @param y : the y coordinate of the block
	 * @param z : the z coordinate of the block
	 */
	public static int[][] getSurroundingBlocks(int x, int y, int z) {
		return new int[][] {
			{x-1, y, z},
			{x+1, y, z},
			{x, y-1, z},
			{x, y+1, z},
			{x, y, z-1},
			{x, y, z+1}
 		};
	}
	
	/**
	 * Searches through the given inventory for the given item stack.
	 * @param inventory
	 * @param itemStack
	 * @return
	 * Returns the slot the item was found at or -1 if the item wasn't found.
	 */
	public static int getItemIndexInInventory(IInventory inventory, ItemStack itemStack) {
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (itemStack.equals(inventory.getStackInSlot(i))) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Workaround for a vanilla bug. Without this, items with NBT values
	 * that were created at different times may not stack together (even when
	 * they are the same values). Fireworks are an example of this in vanilla
	 * Minecraft.
	 * @param itemStack
	 * @return
	 */
	public static ItemStack getFixedNBTItemStack(ItemStack itemStack) {
		NBTTagCompound compound = new NBTTagCompound();
    	itemStack.writeToNBT(compound);
    	itemStack.readFromNBT(compound);
    	return itemStack;
	}
}
