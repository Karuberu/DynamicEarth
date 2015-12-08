package karuberu.mods.mudmod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public abstract class BlockMudMod extends Block {
	
	public int
		hydrateRadiusX,
		hydrateRadiusYUp,
		hydrateRadiusYDown,
		hydrateRadiusZ;
	protected int
		dryBlock = -1,
		wetBlock = -1;
	protected Material[] checkList = {Material.water, Material.fire, Material.ice, Material.snow};

    public BlockMudMod(int i, int j, Material material) {
		super(i, j, material);
	}
    
    public BlockMudMod(int i, int j, Material material, int dryBlock, int wetBlock) {
    	this(i, j, material);
		this.setTickRandomly(true);
    	this.dryBlock = dryBlock;
    	this.wetBlock = wetBlock;
    }
       
    protected BlockMudMod setHydrateRadius(int x, int up, int down, int z) {
		hydrateRadiusX = x;
		hydrateRadiusYUp = up;
		hydrateRadiusYDown = down;
		hydrateRadiusZ = z;
		return this;
    }
    protected BlockMudMod setHydrateRadius(int x, int y, int z) {
		return setHydrateRadius(x, y, y, z);
    }
    protected BlockMudMod setHydrateRadius(int radius) {
		return setHydrateRadius(radius, radius, radius, radius);
    }
    
    protected boolean canDry(int metadata) {
    	return dryBlock > -1;
    }
    final protected boolean canDry() {
    	return canDry(0);
    }
      
    protected boolean canHydrate(int metadata) {
    	return wetBlock > -1;
    }
    final protected boolean canHydrate() {
    	return this.canHydrate(0);
    }
     
    protected boolean canSpread(int metadata) {
    	return false;
    }
    final protected boolean canSpread() {
    	return this.canSpread(0);
    }
    
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
        if (this.willDry(world, x, y, z)) {
			this.becomeDry(world, x, y, z);
        } else {
        	if (this.willHydrate(world, x, y, z)) {
    			this.becomeWet(world, x, y, z);
        	}
            this.tryToSpread(world, x, y, z);
        }
        super.updateTick(world, x, y, z, random);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
    	Material material = world.getBlockMaterial(x, y + 1, z);
    	if (material == Material.fire || material == Material.lava) {
	    	this.becomeDry(world, x, y, z);
	    }
    }
    
    /**
     * returns true if the world is raining and the block is exposed to the elements.
     */
    protected boolean isGettingRainedOn(World world, int x, int y, int z) {
    	return world.isRaining() && world.canBlockSeeTheSky(x, y+1, z);
    }
    
    /**
     * Checks all blocks within the given radius and returns a list of their block materials.
     */
    protected List<Material> getNearbyMaterials(World world, int x, int y, int z, int radiusX, int radiusUp, int radiusDown, int radiusZ) {
    	ArrayList<Material> materials = new ArrayList<Material>();
    	for (int xi = x - radiusX; xi <= x + radiusX; ++xi) {
            for (int yi = y - radiusUp; yi <= y + radiusDown; ++yi) {
                for (int zi = z - radiusZ; zi <= z + radiusZ; ++zi) {
	                materials.add(world.getBlockMaterial(xi, yi, zi));
                }
            }
        }
        return materials;
    }
    protected List<Material> getNearbyMaterials(World world, int x, int y, int z, int radiusX, int radiusY, int radiusZ) {
    	return getNearbyMaterials(world, x, y, z, radiusX, radiusY, radiusY, radiusZ);
    }
    
    /**
     * Returns the distance the block is from a particular material, within the given radius.
     */
    protected boolean isMaterialNearby(World world, int x, int y, int z, Material material, int radiusX, int radiusUp, int radiusDown, int radiusZ) {
       	for (int xi = x - radiusX; xi <= x + radiusX; ++xi) {
            for (int yi = y - radiusUp; yi <= y + radiusDown; ++yi) {
                for (int zi = z - radiusZ; zi <= z + radiusZ; ++zi) {
                	if (world.getBlockMaterial(xi, yi, zi) == material) {
                		return true;
                	}
                }
            }
        }
        return false;
    }
    protected boolean isMaterialNearby(World world, int x, int y, int z, Material material, int radiusX, int radiusY, int radiusZ) {
    	return isMaterialNearby(world, x, y, z, material, radiusX, radiusY, radiusY, radiusZ);
    }
    
    /**
     * Returns a positive value if water is within the hydration range of the block
     * or 0 if the block is exposed to rain. Returns -1 if the block is not hydrated.
     */
    protected int getHydrationDistance(World world, int x, int y, int z) {
    	int minimumDistance = -1;
    	if (this.isGettingRainedOn(world, x, y, z)) {
    		return 0;
    	}
        for (int xi = -hydrateRadiusX; xi <= hydrateRadiusX; ++xi) {
            for (int yi = -hydrateRadiusYUp; yi <= hydrateRadiusYDown; ++yi) {
                for (int zi = -hydrateRadiusZ; zi <= hydrateRadiusZ; ++zi) {
                    if (world.getBlockMaterial(x + xi, y + yi, z + zi) == Material.water) {
                    	int distance = Math.max(Math.max(Math.abs(xi), Math.abs(yi)), Math.abs(zi));
                    	if (distance == 1) {
                    		return distance;
                    	} else if (distance < minimumDistance || minimumDistance == -1) {
                    		minimumDistance = distance;
                    	}
                    }
                }
            }
        }
        return minimumDistance;
    }
    /**
     * Returns true if there's water within hydration range or if the block is exposed to rain.
     */
    protected boolean isHydrated(World world, int x, int y, int z) {
    	return this.getHydrationDistance(world, x, y, z) >= 0;
    }
    
    /**
     * Returns true if the block is near fire or lava.
     */
    protected boolean isBeingHeated(World world, int x, int y, int z) {
    	int lavaRadius = 2,
    		fireRadius = 1;
        for (int xi = -lavaRadius; xi <= lavaRadius; ++xi) {
            for (int yi = -lavaRadius; yi <= lavaRadius; ++yi) {
                for (int zi = -lavaRadius; zi <= lavaRadius; ++zi) {
                	Material material = world.getBlockMaterial(x + xi, y + yi, z + zi);
                    if (material == Material.lava) {
                        return true;
                    }
                    if (material == Material.fire &&
                    	Math.abs(xi) <= fireRadius &&
                    	Math.abs(yi) <= fireRadius &&
                    	Math.abs(zi) <= fireRadius) {
                    	return true;
                    }
                }
            }
        }
        return false;
    }
    
    protected int getHydrationRate(World world, int x, int y, int z) {
    	final int
    		quick = 1,
    		medium = 2,
    		slow = 4,
    		none = -1;
    	float rainfall = world.getBiomeGenForCoords(x, z).getFloatRainfall();
    	if (rainfall >= 0.9) {
    		return quick;
    	} else if (rainfall >= 0.5) {
    		return medium;
    	} else if (rainfall >= 0.1) {
    		return slow;
    	} else {
    		return none;
    	}
    }
    
    protected int getDryRate(World world, int x, int y, int z) {
    	final int
    		quick = 1,
    		medium = 2,
    		slow = 4,
    		none = -1;
    	float rainfall = world.getBiomeGenForCoords(x, z).getFloatRainfall();
    	if (rainfall >= 1.0) {
    		return none;
    	} else if (rainfall >= 0.9) {
    		return slow;
    	} else if (rainfall >= 0.5) {
    		return medium;
    	} else {
    		return quick;
    	}
    }
    
    /**
     * Returns true if the block is set to hydrate, is not being heated,
     * is properly hydrated, and passes a random chance to hydrate based
     * on the biome the block is in.
     */
    protected boolean willHydrate(World world, int x, int y, int z) {
    	if (this.canHydrate()) {
	    	if (this.isBeingHeated(world, x, y, z)) {
	    		return false;
	    	} else if (this.isHydrated(world, x, y, z)){
	    		int rate = this.getHydrationRate(world, x, y, z);
	    		return rate <= 0 ? false : world.rand.nextInt(rate) == 0;
	    	}
    	}
    	return false;
    }
    
    /**
     * Returns true when the block is set to dry and is either heated, or is not
     * hydrated and passes a random chance to dry out based on the biome the
     * block is in.
     */
    protected boolean willDry(World world, int x, int y, int z) {
    	if (this.canDry()) {
	    	if (this.isBeingHeated(world, x, y, z)) {
	    		return true;
	    	}
	    	if (!this.isHydrated(world, x, y, z)) {
	        	int rate = this.getDryRate(world, x, y, z);
	    		return rate <= 0 ? false : world.rand.nextInt(rate) == 0;
	    	}
    	}
    	return false;
    }
    
    protected void becomeDry(World world, int x, int y, int z) {
    	if (dryBlock >= 0) {
        	world.setBlockWithNotify(x, y, z, dryBlock);
    	}
    }
    
    protected void becomeWet(World world, int x, int y, int z) {
    	if (wetBlock >= 0) {
    		world.setBlockWithNotify(x, y, z, wetBlock);
    	}
    }
    
    protected void tryToSpread (World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	if (this.canSpread(metadata) && isHydrated(world, x, y, z)) {
    		int rate = this.getHydrationRate(world, x, y, z);
    		if (rate > 0) {
    			this.spread(world, x, y, z, 5 - rate);
    		}
    	}
    }
    
    /**
     * Randomly picks blocks around the source block in an attempt to find
     * a dry block to hydrate. If a valid block is found, that block is converted
     * and the process ends. By default, the block will only spread downwards
     * and laterally.
     * @param n = the number of attempts to find a valid block.
     */
    protected void spread(World world, int x, int y, int z, int n) {
    	for (int i = 0; i < n; ++i) {
        	int[][] surroundingBlocks = new int[][] {
        		{x-1, y, z},
        		{x+1, y, z},
        		{x, y-1, z},
        		{x, y, z-1},
        		{x, y, z+1}
        	};
        	int randomIndex = world.rand.nextInt(5),
        		xi = surroundingBlocks[randomIndex][0],
        		yi = surroundingBlocks[randomIndex][1],
        		zi = surroundingBlocks[randomIndex][2];
	    	if (world.getBlockId(xi, yi, zi) == dryBlock && this.isHydrated(world, xi, yi, zi)) {
	    		world.setBlockWithNotify(xi, yi, zi, this.blockID);
	    		break;
	    	}
    	}
    }
}
