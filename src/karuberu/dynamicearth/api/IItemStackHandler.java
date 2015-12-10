package karuberu.dynamicearth.api;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IItemStackHandler {

	/**
	 * Fired whenever an Item Stack containing this item expires. Returning any
	 * number above 0 will keep the item from expiring for the given number of
	 * game ticks (5000 being the default).
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param itemStack
	 * @return
	 */
	public int onExpire(World world, int x, int y, int z, ItemStack itemStack);
	
	public boolean onPickup(World world, int x, int y, int z, ItemStack itemStack);
}
