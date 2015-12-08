package karuberu.core.event;

import net.minecraft.world.World;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event;

/**
 * An event that triggers whenever a block is updated. Provides
 * more data than the vanilla updateTick method.
 */
@Cancelable
public class BlockUpdateEvent extends Event {
	
	public World world;
	public int x;
	public int y;
	public int z;
	public boolean isRandomTick;
	
	/**
	 * @param world : The world the block is in.
	 * @param x : The block's x coordinate.
	 * @param y : The block's y coordinate.
	 * @param z : The block's z coordinate.
	 * @param isRandomTick : Whether or not this was a randomly scheduled tick.
	 */
	public BlockUpdateEvent(World world, int x, int y, int z, boolean isRandomTick) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.isRandomTick = isRandomTick;
	}
}
