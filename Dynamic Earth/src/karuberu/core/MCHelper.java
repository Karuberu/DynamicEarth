package karuberu.core;

import karuberu.mods.mudmod.blocks.BlockDirtSlab;

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
	
	public static int getSlabMetadata(int metadata) {
		return metadata & 7;
	}
	
	public static int getSlabType(int metadata) {
		return metadata & 8;
	}
	
	public static boolean isTopSlab(int metadata) {
		return getSlabType(metadata) == TOP_SLAB;
	}
	
	public static boolean isBottomSlab(int metadata) {
		return getSlabType(metadata) == BOTTOM_SLAB;
	}
	
	public static int convertSlabMetadata(int sourceMeta, int targetMeta) {
		return MCHelper.getSlabType(sourceMeta) - MCHelper.getSlabMetadata(sourceMeta) + targetMeta;
	}
	
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
}
