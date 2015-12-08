package karuberu.mods.mudmod.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bouncycastle.util.Strings;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;

import cpw.mods.fml.common.Mod.PostInit;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class BlockMudMod extends Block {
	
	public int
		hydrateRadiusX,
		hydrateRadiusYUp,
		hydrateRadiusYDown,
		hydrateRadiusZ;
	public static enum Rate {
		QUICK,
		MEDIUM,
		SLOW,
		NONE
	}

	public BlockMudMod(int id, Material material) {
		super(id, material);
		this.setTickRandomly(true);
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		super.updateTick(world, x, y, z, random);
		if (this.willDry(world, x, y, z)) {
			this.becomeDry(world, x, y, z);
		} else {
			if (this.willHydrate(world, x, y, z)) {
				this.becomeWet(world, x, y, z);
			}
			this.tryToSpread(world, x, y, z);
		}
	}
	   
	protected final BlockMudMod setHydrateRadius(int x, int up, int down, int z) {
		hydrateRadiusX = x;
		hydrateRadiusYUp = up;
		hydrateRadiusYDown = down;
		hydrateRadiusZ = z;
		return this;
	}
	
	protected final BlockMudMod setHydrateRadius(int x, int y, int z) {
		return setHydrateRadius(x, y, y, z);
	}
	
	protected final BlockMudMod setHydrateRadius(int radius) {
		return setHydrateRadius(radius, radius, radius, radius);
	}
	
	protected int getDryBlock(int metadata) {
		return -1;
	}
	
	protected int getWetBlock(int metadata) {
		return -1;
	}
	
	protected boolean canDry(int metadata) {
		return this.getDryBlock(metadata) > -1;
	}
	
	protected boolean canHydrate(int metadata) {
		return this.getWetBlock(metadata) > -1;
	}
	 
	protected boolean canSpread(int metadata) {
		return false;
	}
	
	protected int getHydrationDistance(World world, int x, int y, int z) {
		return getHydrationDistanceWithinRadius(world, x, y, z, this.hydrateRadiusX, this.hydrateRadiusYUp, this.hydrateRadiusYDown, this.hydrateRadiusZ);
	}
	
	protected boolean isHydrated(World world, int x, int y, int z) {
		return this.getHydrationDistance(world, x, y, z) >= 0;
	}
	
	protected boolean isBeingHeated(World world, int x, int y, int z) {
		return isBlockBeingHeated(world, x, y, z);
	}
	
	protected int getHydrationRate(World world, int x, int y, int z) {
		Rate biomeRate = this.getHydrationRateForBiome(world.getBiomeGenForCoords(x, z));
		Rate adjacentBlockRate = this.getHydrationRateForAdjacentBlocks(world, x, y, z);
		return getStandardHydrationRate(world, x, y, z, biomeRate, adjacentBlockRate);
	}
	
	protected Rate getHydrationRateForBiome(BiomeGenBase biome) {
		return getStandardHydrationRateForBiome(biome);
	}
	
	protected Rate getHydrationRateForAdjacentBlocks(World world, int x, int y, int z) {
		return getStandardHydrationRateForAdjacentBlocks(world, x, y, z);
	}
	
	protected int getDryRate(World world, int x, int y, int z) {
		Rate biomeRate = this.getDryRateForBiome(world.getBiomeGenForCoords(x, z));
		Rate adjacentBlockRate = this.getDryRateForAdjacentBlocks(world, x, y, z);
		return getStandardDryRate(world, x, y, z, biomeRate, adjacentBlockRate);
	}
	
	protected Rate getDryRateForBiome(BiomeGenBase biome) {
		return getStandardDryRateForBiome(biome);
	}
	
	protected Rate getDryRateForAdjacentBlocks(World world, int x, int y, int z) {
		return getStandardDryRateForAdjacentBlocks(world, x, y, z);
	}
	
	/**
	 * Returns true if the block is set to hydrate, is not being heated,
	 * is properly hydrated, and passes a random chance to hydrate based
	 * on the biome the block is in.
	 */
	protected boolean willHydrate(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (this.canHydrate(metadata)) {
			if (this.isBeingHeated(world, x, y, z)) {
				return false;
			} else if (this.isHydrated(world, x, y, z)){
				int rate = this.getHydrationRate(world, x, y, z);
				return rate == 0 ? true : rate < 0 ? false : world.rand.nextInt(rate) == 0;
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
		int metadata = world.getBlockMetadata(x, y, z);
		if (this.canDry(metadata)) {
			if (this.isBeingHeated(world, x, y, z)) {
				return true;
			}
			if (!this.isHydrated(world, x, y, z)) {
				int rate = this.getDryRate(world, x, y, z);
				return rate == 0 ? true : rate < 0 ? false : world.rand.nextInt(rate) == 0;
			}
		}
		return false;
	}
	
	protected void becomeDry(World world, int x, int y, int z) {
		int dryBlock = this.getDryBlock(world.getBlockMetadata(x, y, z));
		if (dryBlock >= 0) {
			world.setBlockAndMetadataWithNotify(x, y, z, dryBlock, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
		}
	}
	
	protected void becomeWet(World world, int x, int y, int z) {
		int wetBlock = this.getWetBlock(world.getBlockMetadata(x, y, z));
		if (wetBlock >= 0) {
			world.setBlockAndMetadataWithNotify(x, y, z, wetBlock, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
		}
	}
	
	protected void tryToSpread (World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (this.canSpread(metadata) && this.isHydrated(world, x, y, z)) {
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
	 * @param world : The world the block is in.
	 * @param x : The block's x coordinate.
	 * @param y : The block's y coordinate.
	 * @param z : The block's z coordinate.
	 * @param attempts : The number of attempts the block will make to find a valid block.
	 */
	protected void spread(World world, int x, int y, int z, int attempts) {
		int metadata = world.getBlockMetadata(x, y, z);
		for (int i = 0; i < attempts; ++i) {
			int[][] surroundingBlocks = new int[][] {
				{x-1, y, z},
				{x+1, y, z},
				{x, y-1, z},
				{x, y, z-1},
				{x, y, z+1}
			};
			int randomIndex = world.rand.nextInt(surroundingBlocks.length),
				xi = surroundingBlocks[randomIndex][0],
				yi = surroundingBlocks[randomIndex][1],
				zi = surroundingBlocks[randomIndex][2];
			if (world.getBlockId(xi, yi, zi) == this.getDryBlock(metadata)
			&& this.isHydrated(world, xi, yi, zi)
			&& this.canBlockStay(world, xi, yi, zi)) {
				world.setBlockAndMetadataWithNotify(xi, yi, zi, this.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
				break;
			}
		}
	}
	
	/**
	 * Returns true if the world is raining and the block is exposed to
	 * the elements.
	 * @param world : The world the block is in.
	 * @param x : The block's x coordinate.
	 * @param y : The block's y coordinate.
	 * @param z : The block's z coordinate.
	 */
	public static boolean isBlockGettingRainedOn(World world, int x, int y, int z) {
		return world.isRaining()
		&& world.canBlockSeeTheSky(x, y + 1, z)
		&& world.getPrecipitationHeight(x, z) <= y + 1
		&& !world.getBiomeGenForCoords(x, z).getEnableSnow();
	}
	
	/**
	 * Returns a positive value if water is within the hydration range of the block
	 * or 0 if the block is exposed to rain. Returns -1 if the block is not hydrated.
	 */
	public static int getHydrationDistanceWithinRadius(World world, int x, int y, int z, int radiusX, int radiusYUp, int radiusYDown, int radiusZ) {
		int minimumDistance = -1;
		if (isBlockGettingRainedOn(world, x, y, z)) {
			return 0;
		}
		for (int xi = -radiusX; xi <= radiusX; ++xi) {
			for (int yi = -radiusYUp; yi <= radiusYDown; ++yi) {
				for (int zi = -radiusZ; zi <= radiusZ; ++zi) {
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
	public static boolean isBlockHydrated(World world, int x, int y, int z, int radiusX, int radiusYUp, int radiusYDown, int radiusZ) {
		return getHydrationDistanceWithinRadius(world, x, y, z, radiusX, radiusYUp, radiusYDown, radiusZ) >= 0;
	}
	
	/**
	 * Returns true if the block is near fire or lava.
	 */
	public static boolean isBlockBeingHeated(World world, int x, int y, int z) {
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
	
	public static int getAdjacentWetBlocks(World world, int x, int y, int z) {
		int count = 0;
		int[][] surroundingBlocks = MCHelper.getSurroundingBlocks(x, y, z);
		for (int i = 0; i < surroundingBlocks.length; i++) {
			int xi = surroundingBlocks[i][0],
				yi = surroundingBlocks[i][1],
				zi = surroundingBlocks[i][2];
			int id = world.getBlockId(xi, yi, zi);
			int metadata = world.getBlockMetadata(xi, yi, zi);
			if (Block.blocksList[id] instanceof BlockMudMod
			&& ((BlockMudMod)Block.blocksList[id]).canDry(metadata)) {
				count++;
			}
		}
		return count;
	}
	
	public static boolean materialIsAdjacent(Material material, World world, int x, int y, int z) {
		int[][] surroundingBlocks = MCHelper.getSurroundingBlocks(x, y, z);
		for (int i = 0; i < surroundingBlocks.length; i++) {
			int xi = surroundingBlocks[i][0],
				yi = surroundingBlocks[i][1],
				zi = surroundingBlocks[i][2];
			Material adjacentMaterial = world.getBlockMaterial(xi, yi, zi);
			if (adjacentMaterial == material) {
				return true;
			}
		}
		return false;
	}
	
	public final static int getStandardHydrationRate(World world, int x, int y, int z) {
		Rate biomeRate = getStandardHydrationRateForBiome(world.getBiomeGenForCoords(x, z));
		Rate adjacentBlockRate = getStandardHydrationRateForAdjacentBlocks(world, x, y, z);
		return getStandardHydrationRate(world, x, y, z, biomeRate, adjacentBlockRate);
	}
	
	public static int getStandardHydrationRate(World world, int x, int y, int z, Rate biomeRate, Rate adjacentBlockRate) {
		int rate = -1;
		switch(biomeRate) {
		case SLOW: rate = 4; break;
		case MEDIUM: rate = 2; break;
		case QUICK: rate = 1; break;
		case NONE: return -1;
		}
		switch(adjacentBlockRate) {
		case SLOW: return rate++;
		case MEDIUM: return rate;
		case QUICK: return rate > 0 ? rate-- : rate;
		case NONE: return -1;
		}
		return rate;
	}
	
	public static Rate getStandardHydrationRateForBiome(BiomeGenBase biome) {
		float biomeHumidity = biome.rainfall;
		if (biomeHumidity >= 0.9F) {
			return Rate.QUICK;
		} else if (biomeHumidity  >= 0.5F) {
			return Rate.MEDIUM;
		} else if (biomeHumidity  >= 0.1F) {
			return Rate.SLOW;
		} else {
			return Rate.NONE;
		}
	}
	
	public static Rate getStandardHydrationRateForAdjacentBlocks(World world, int x, int y, int z) {
		int rate = getAdjacentWetBlocks(world, x, y, z);
		if (rate >= 3) {
			return Rate.QUICK;
		} else {
			return Rate.MEDIUM;
		}
	}
	
	public final static int getStandardDryRate(World world, int x, int y, int z) {
		Rate biomeRate = getStandardDryRateForBiome(world.getBiomeGenForCoords(x, z));
		Rate adjacentBlockRate = getStandardDryRateForAdjacentBlocks(world, x, y, z);
		return getStandardDryRate(world, x, y, z, biomeRate, adjacentBlockRate);
	}
	
	public static int getStandardDryRate(World world, int x, int y, int z, Rate biomeRate, Rate adjacentBlockRate) {
		int rate = -1;
		switch(biomeRate) {
		case SLOW: rate = 4; break;
		case MEDIUM: rate = 2; break;
		case QUICK: rate = 1; break;
		case NONE: return -1;
		}
		switch(adjacentBlockRate) {
		case SLOW: return rate + 2;
		case MEDIUM: return rate;
		case QUICK: return rate > 0 ? rate-- : rate;
		case NONE: return -1;
		}
		return rate;
	}
	
	public static Rate getStandardDryRateForBiome(BiomeGenBase biome) {
		float biomeHumidity = biome.rainfall;
		if (biomeHumidity >= 1.0F) {
			return Rate.NONE;
		} else if (biomeHumidity >= 0.9F) {
			return Rate.SLOW;
		} else if (biomeHumidity >= 0.5F) {
			return Rate.MEDIUM;
		} else {
			return Rate.QUICK;
		}
	}
	
	public static Rate getStandardDryRateForAdjacentBlocks(World world, int x, int y, int z) {
		int rate = getAdjacentWetBlocks(world, x, y, z);
		if (rate >= 6) {
			return Rate.NONE;
		} else if (rate >= 3) {
			return Rate.SLOW;
		} else if (rate >= 1) {
			return Rate.MEDIUM;
		} else {
			return Rate.QUICK;
		}
	}
	
	/**
	 * Returns true if the block is set to hydrate, is not being heated,
	 * is properly hydrated, and passes a random chance to hydrate based
	 * on the biome the block is in.
	 */
	public static boolean willBlockHydrate(World world, int x, int y, int z, int radiusX, int radiusYUp, int radiusYDown, int radiusZ) {
		if (isBlockBeingHeated(world, x, y, z)) {
			return false;
		} else if (isBlockHydrated(world, x, y, z, radiusX, radiusYUp, radiusYDown, radiusZ)){
			int rate = getStandardHydrationRate(world, x, y, z);
			return rate == 0 ? true : rate < 0 ? false : world.rand.nextInt(rate) == 0;
		}
		return false;
	}
	
	/**
	 * Returns true when the block is set to dry and is either heated, or is not
	 * hydrated and passes a random chance to dry out based on the biome the
	 * block is in.
	 */
	protected boolean willBlockDry(World world, int x, int y, int z, int radiusX, int radiusYUp, int radiusYDown, int radiusZ) {
		if (isBlockBeingHeated(world, x, y, z)) {
			return true;
		}
		if (isBlockHydrated(world, x, y, z, radiusX, radiusYUp, radiusYDown, radiusZ)) {
			int rate = getStandardDryRate(world, x, y, z);
			return rate == 0 ? true : rate < 0 ? false : world.rand.nextInt(rate) == 0;
		}
		return false;
	}
}
