package karuberu.dynamicearth.api;

import net.minecraft.item.ItemStack;
import net.minecraft.world.chunk.Chunk;

public interface IVanillaReplaceable {

	/**
	 * Returns an item stack containing the block id and metadata for this block's
	 * replacement block, preferrably one from un-modified Minecraft.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public ItemStack getVanillaBlockReplacement(Chunk chunk, int x, int y, int z);

}
