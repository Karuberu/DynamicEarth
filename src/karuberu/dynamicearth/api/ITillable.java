package karuberu.dynamicearth.api;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface ITillable {
	public boolean onTilled(World world, int x, int y, int z);
}
