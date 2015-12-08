package karuberu.core.event;

import karuberu.core.MCHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class EventFactory {
		
	public static boolean onBlockUpdate(World world, int x, int y, int z, boolean randomTick) {
		BlockUpdateEvent event = new BlockUpdateEvent(world, x, y, z, randomTick);
		MinecraftForge.EVENT_BUS.post(event);
		return event.isCanceled();
	}
	
	public static void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		int side;
		int[][] neighborBlocks = MCHelper.getSurroundingBlocks(x, y, z);
		for (int[] coordinates : neighborBlocks) {
			int xi = coordinates[0];
			int yi = coordinates[1];
			int zi = coordinates[2];
			if (y > yi) {
				side = MCHelper.SIDE_TOP;
			} else if (y < yi) {
				side = MCHelper.SIDE_BOTTOM;
			} else if (z > zi) {
				side = MCHelper.SIDE_SOUTH;
			} else if (z < zi) {
				side = MCHelper.SIDE_NORTH;
			} else if (x > xi) {
				side = MCHelper.SIDE_EAST;
			} else if (x < xi) {
				side = MCHelper.SIDE_WEST;
			} else {
				break;
			}
			NeighborBlockChangeEvent event = new NeighborBlockChangeEvent(world, xi, yi, zi, neighborBlockID, side);
			MinecraftForge.EVENT_BUS.post(event);
		}
	}
}
