package karuberu.core.event;

import net.minecraft.world.World;
import net.minecraftforge.event.Event;

/**
 * An event that triggers whenever a setBlock method is used. Provides
 * more data than the vanilla onNeighborBlockChange method.
 * @author Karuberu
 */
public class NeighborBlockChangeEvent extends Event {
	
	public World world;
	public int x, y, z, neighborBlockID, side;

	
	/**
	 * @param world : The world the blocks are in.
	 * @param x : The x coordinate for the block that is being notified.
	 * @param y : The y coordinate for the block that is being notified.
	 * @param z : The z coordinate for the block that is being notified.
	 * @param neighborBlockID : The blockID for the block that changed.
	 * @param side : The side the block that changed is on.
	 */
	public NeighborBlockChangeEvent(World world, int x, int y, int z, int neighborBlockID, int side) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.neighborBlockID = neighborBlockID;
		this.side = side;
	}

}
