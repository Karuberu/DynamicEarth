package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public abstract class BlockMudMod extends Block {
	
	protected int
		hydrateRadiusX,
		hydrateRadiusY,
		hydrateRadiusZ;
	
    public BlockMudMod(int i, int j, Material material) {
		super(i, j, material);
	}
    
    protected void setHydrateRadius(int x, int y, int z) {
		hydrateRadiusX = x;
		hydrateRadiusY = y;
		hydrateRadiusZ = z;
    }

    /**
     * returns true if the world is raining and the block is exposed to the elements.
     */
    protected boolean isGettingRainedOn(World world, int x, int y, int z) {
    	return world.isRaining() && world.canBlockSeeTheSky(x, y+1, z);
    }
    /**
     * returns true if there's water within hydration range.
     */
    protected boolean isWaterNearby(World world, int i, int j, int k) {
        for (int x = i - hydrateRadiusX; x <= i + hydrateRadiusX; ++x) {
            for (int y = j - 1; y <= j + hydrateRadiusY; ++y) {
                for (int z = k - hydrateRadiusZ; z <= k + hydrateRadiusZ; ++z) {
                    if (world.getBlockMaterial(x, y, z) == Material.water) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
