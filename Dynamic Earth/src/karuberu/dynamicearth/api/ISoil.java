package karuberu.dynamicearth.api;

import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public interface ISoil {
	/**
	 * A more powerful version of Forge's canSustainPlant, this function overrides
	 * all other checks that the plant may do when checking if it can stay at a
	 * given location (e.g. light level checks). Returning true will force the
	 * plant to stay, while returning false will allow the plant to decide.
	 * 
	 * Currently only implemented by mushrooms.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param direction
	 * @param plant
	 * @return
	 */
	public boolean willForcePlantToStay(World world, int x, int y, int z, IPlantable plant);
}
