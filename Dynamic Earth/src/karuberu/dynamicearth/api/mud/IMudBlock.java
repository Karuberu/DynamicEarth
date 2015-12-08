package karuberu.dynamicearth.api.mud;

import net.minecraft.world.World;

public interface IMudBlock {

	public void tryToMudslide(World world, int x, int y, int z, int intensity);
	
	public boolean canFormMud(World world, int x, int y, int z);
	
	public void tryToFormMud(World world, int x, int y, int z);
}
