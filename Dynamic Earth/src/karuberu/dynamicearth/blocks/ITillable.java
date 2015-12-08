package karuberu.dynamicearth.blocks;

import net.minecraft.world.World;

public interface ITillable {
	public boolean onTilled(World world, int x, int y, int z);
}
