package karuberu.dynamicearth.world;

import java.util.Random;

import karuberu.core.util.Coordinates;
import karuberu.core.util.Helper;
import karuberu.core.util.block.BlockSide;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.blocks.BlockPeatMoss;
import karuberu.dynamicearth.blocks.MaterialPeatMoss;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenPeat implements IWorldGenerator {
	
	public static int
		rarity = 10,
		maximumGenHeight = 75,
		minimumGenHeight = 55;
	private int
		diameter,
		depth,
		irregularity;
	private boolean[][] validCoordinates;
	
	public WorldGenPeat() {
		this.diameter = 15;
		this.depth = 1;
		this.irregularity = 4;
		this.clearValidCoordinates();
	}
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		assert WorldGenDynamicEarth.doGeneratePeat;
		assert !world.isRemote;
		
		if (random.nextInt(rarity) != 0) {
			return;
		}
		int irregularity = 1 + (this.irregularity / 2) + (random.nextInt(this.irregularity));
		int y = maximumGenHeight + 5;
		for (int i = 0; i < irregularity; i++) {
			int x = chunkX * 16 + random.nextInt(16);
			int z = chunkZ * 16 + random.nextInt(16);
			BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
			if (biome == BiomeGenBase.swampland
			|| (WorldGenDynamicEarth.enableDiverseGeneration && BiomeDictionary.isBiomeOfType(biome, Type.SWAMP))) {       
				while (world.isAirBlock(x, y, z)) {
					y--;
				}
				if (y > maximumGenHeight || y < minimumGenHeight) {
					return;
				}
				if (world.getBlockMaterial(x, y, z).isLiquid()) {
					return;
				}
				int min = -diameter / 2;
				int max = diameter / 2 + diameter % 2;
				double radius = (double)diameter / 2.0D;
				for (int xi = min; xi <= max; xi++) {
		            for (int zi = min; zi <= max; zi++) {
		            	double centerX = -(radius / 2.0D) + (radius * random.nextDouble());
		            	double centerZ = -(radius / 2.0D) + (radius * random.nextDouble());
		            	boolean blockIsWithinBounds = Math.pow(xi - centerX, 2) + Math.pow(zi - centerZ, 2) <= Math.pow(radius, 2);
		                if (blockIsWithinBounds) {
		                	this.setValidCoordinate(xi - MathHelper.floor_double(centerX), zi - MathHelper.floor_double(centerZ));
		                }
		            }
		        }
		        if (!this.locationIsValid(world, x, y, z, min, max)) {
	            	return;
	            }
		        DynamicEarth.logger.debug("Generated peat at", x, y, z);
	            for (int xi = min; xi <= max; xi++) {
	                for (int zi = min; zi <= max; zi++) {
	                	if (this.isValidCoordinate(xi, zi)) {
	                		this.generatePeat(this.depth, world, x + xi, y - 1, z + zi, random, true);
	                	}
	                }
	            }
	            this.cleanBogEdges(world, x, y, z, random, min, max);
	            this.clearValidCoordinates();
			}
	    }
	}
	
	private boolean locationIsValid(World world, int x, int y, int z, int min, int max) {
		for (int xi = min; xi <= max; xi++) {
            for (int zi = min; zi <= max; zi++) {
    			int adjustedX = x + xi;
    			int adjustedZ = z + zi;
            	if (!this.isValidCoordinate(xi, zi)) {
            		if (xi < max && this.isValidCoordinate(xi + 1, zi)
                    || xi > min && this.isValidCoordinate(xi - 1, zi)
                    || zi < max && this.isValidCoordinate(xi, zi + 1)
                    || zi > min && this.isValidCoordinate(xi, zi - 1)) {
            			// prevent generation when edges are next to a liquid pool.
                        if (world.getBlockMaterial(adjustedX, y, adjustedZ).isLiquid()
                        && world.getBlockId(adjustedX, y - 1, adjustedZ) != DynamicEarth.peat.blockID) {
                        	//DELogger.debug("Liquid encountered. Could not generate bog.");
                        	return false;
                        }
                        // prevent generation when there's a sharp decline at the edge.
                        if (world.isAirBlock(adjustedX, y, adjustedZ)
                        && world.isAirBlock(adjustedX, y - 1, adjustedZ)) {
                        	//DELogger.debug("Sharp decline encountered. Could not generate bog.");
                        	return false;
                        }
            		}
            	} else {
        			if (world.getBlockMaterial(adjustedX, y + 1, adjustedZ).isSolid()
        			&& world.getBlockMaterial(adjustedX, y + 2, adjustedZ).isSolid()) {
                    	//DELogger.debug("Wall encountered. Could not generate bog.");
        				return false;
        			} else if (world.getBlockMaterial(adjustedX, y, adjustedZ) == Material.sand
        			|| world.getBlockMaterial(adjustedX, y - 1, adjustedZ) == Material.sand) {
        				//DELogger.debug("Sand encountered. Could not generate bog.");
        				return false;
        			} else if (!world.isBlockNormalCube(adjustedX, y - (this.depth + 2), adjustedZ)) {
        				//DELogger.debug("Hole encountered. Could not generate bog.");
        				return false;
        			}
            	}
            }
        }
        return true;
	}
	
	private void setValidCoordinate(int x, int z) {
		x += this.diameter / 2 + this.diameter % 2;
		z += this.diameter / 2 + this.diameter % 2;
		if (x > 0
		&& z > 0
		&& x < this.diameter
		&& z < this.diameter) {
			this.validCoordinates[x][z] = true;
		}
	}
	
	private void clearValidCoordinates() {
		this.validCoordinates = new boolean[diameter+1][diameter+1];
	}
	
	private boolean isValidCoordinate(int x, int z) {
		x += this.diameter / 2;
		z += this.diameter / 2;		
		return this.validCoordinates[x][z];
	}
	
	private void generatePeat(int depth, World world, int x, int y, int z, Random random, boolean generateWater) {
		// kill blocks at the top of the bog.
		for (int i = 1; i < 3; i++) {
			world.setBlockToAir(x, y + i, z);
		}
		Coordinates
			block = new Coordinates(world, x, y, z),
			topBlock = block.onSide(BlockSide.TOP);
		if (topBlock.getBlockMaterial().isSolid()) {
			if (topBlock.onSide(BlockSide.TOP).getBlockMaterial().isSolid()) {
				return;
			} else {
				topBlock.setBlock(0, 0, Helper.DO_NOT_NOTIFY_OR_UPDATE);
			}
		} else {
			// check the block above and if it is air or something peat moss cannot spread
			// onto, then move it down a block.
			// if not, replace it with peat moss
			if (topBlock.isAirBlock()
			|| !BlockPeatMoss.canSpreadToBlock(world, topBlock.x, topBlock.y, topBlock.z)) {
				block.setBlock(topBlock.getBlockID(), topBlock.getBlockMetadata(), Helper.DO_NOT_NOTIFY_OR_UPDATE);
			} else {
				block.setBlock(DynamicEarth.peatMoss.blockID, BlockPeatMoss.getMetadataForSpread(world, x, y + 1, z), Helper.DO_NOT_NOTIFY_OR_UPDATE);
			}
			topBlock.setBlock(0, 0, Helper.DO_NOT_NOTIFY_OR_UPDATE);
			block.y--;
			topBlock.y--;
		}
		Material topBlockMaterial = topBlock.getBlockMaterial();
		if (generateWater && random.nextInt(BlockPeatMoss.hydrationRadius) == 0) {
			// randomly place water pools throughout the bog to keep all the moss hydrated.
			if (topBlockMaterial.isReplaceable()
			&& topBlockMaterial != MaterialPeatMoss.material) {
				topBlock.setBlock(0, 0, Helper.DO_NOT_NOTIFY_OR_UPDATE);
				block.setBlock(Block.waterMoving.blockID, 0, Helper.DO_NOT_NOTIFY_OR_UPDATE);
			} else {
				block.setBlock(DynamicEarth.peat.blockID, BlockPeat.WET, Helper.DO_NOT_NOTIFY_OR_UPDATE);
			}
		} else {
			if (topBlockMaterial.isReplaceable()
			&& topBlockMaterial != MaterialPeatMoss.material) {
				topBlock.setBlock(DynamicEarth.peatMoss.blockID, BlockPeatMoss.GROWTHSTAGE_FULLGROWN, Helper.DO_NOT_NOTIFY_OR_UPDATE);
			}
			block.setBlock(DynamicEarth.peat.blockID, BlockPeat.WET, Helper.DO_NOT_NOTIFY_OR_UPDATE);
		}
		depth -= depth > 1 ? random.nextInt(depth / 2) : 0;
    	for	(int i = 1; i < depth; i++) {
    		block.y--;
    		block.setBlock(DynamicEarth.peat.blockID, BlockPeat.WET, Helper.DO_NOT_NOTIFY_OR_UPDATE);
    	}
    	while (!block.getBlockMaterial().isSolid()) {
    		block.y--;
    		block.setBlock(DynamicEarth.peat.blockID, BlockPeat.WET, Helper.DO_NOT_NOTIFY_OR_UPDATE);
    	}
    	block.y--;
    	block.setBlock(DynamicEarth.peat.blockID, BlockPeat.getMetaForFractionFormed(random.nextInt(4) + 1), Helper.DO_NOT_NOTIFY_OR_UPDATE);
	}
	
	private void cleanBogEdges(World world, int x, int y, int z, Random random, int min, int max) {
		for (int xi = min; xi <= max; xi++) {
            for (int zi = min; zi <= max; zi++) {
            	if (!this.isValidCoordinate(xi, zi)) {
            		if (xi < max - 1 && this.isValidCoordinate(xi + 1, zi)
                    || xi > min + 1 && this.isValidCoordinate(xi - 1, zi)
                    || zi < max - 1 && this.isValidCoordinate(xi, zi + 1)
                    || zi > min + 1 && this.isValidCoordinate(xi, zi - 1)) {
            			int adjustedX = x + xi;
            			int adjustedZ = z + zi;
            			Material material = world.getBlockMaterial(adjustedX, y, adjustedZ);
                        if (material.isSolid()) {
                        	this.generatePeat(1, world, adjustedX, y, adjustedZ, random, false);
                        } else if (!world.isAirBlock(adjustedX, y, adjustedZ)
                        && material.isReplaceable()
                        && material != MaterialPeatMoss.material) {
                        	world.setBlock(adjustedX, y, adjustedZ, world.getBiomeGenForCoords(adjustedX, adjustedZ).topBlock, 0, Helper.DO_NOT_NOTIFY_OR_UPDATE);
                        } else if (world.getBlockMaterial(adjustedX, y, adjustedZ) == Material.sand) {
                        	world.setBlock(adjustedX, y, adjustedZ, world.getBiomeGenForCoords(adjustedX, adjustedZ).topBlock, 0, Helper.DO_NOT_NOTIFY_OR_UPDATE);
                        }
            		}
            	}
            }
        }
	}
}
