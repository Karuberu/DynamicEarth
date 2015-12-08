package karuberu.dynamicearth.world;

import java.util.Arrays;
import java.util.Random;

import karuberu.core.KaruberuLogger;
import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockMud;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.blocks.BlockPeatMoss;
import karuberu.dynamicearth.blocks.MaterialPeatMoss;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.feature.WorldGenLakes;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenPeat implements IWorldGenerator {
	
	public static int
		maximumGenHeight = 75,
		minimumGenHeight = 55;
	private int
		diameter,
		depth,
		irregularity;
	private boolean[][] validCoordinates;
	
	public WorldGenPeat() {
		this.diameter = 30;
		this.depth = 1;
		this.irregularity = 4;
		this.clearValidCoordinates();
	}
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		assert WorldGenMudMod.doGeneratePeat;
		assert !world.isRemote;
		
		int irregularity = 1 + (this.irregularity / 2) + (random.nextInt(this.irregularity));
		int y = maximumGenHeight + 5;
		for (int i = 0; i < irregularity; i++) {
			int x = chunkX * 16 + random.nextInt(16);
			int z = chunkZ * 16 + random.nextInt(16);
			if (world.getBiomeGenForCoords(x, z) == BiomeGenBase.swampland) {       
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
				int max = diameter / 2;
				double radius = (-diameter / irregularity) + (diameter / irregularity * random.nextDouble());
				for (int xi = min; xi <= max; xi++) {
		            for (int zi = min; zi <= max; zi++) {
		            	double centerX = -(radius / 2.0D) + (radius * random.nextDouble());
		            	double centerZ = -(radius / 2.0D) + (radius * random.nextDouble());
		            	boolean blockIsWithinBounds = Math.pow(xi - centerX, 2) + Math.pow(zi - centerZ, 2) <= Math.pow(radius, 2);
		                if (blockIsWithinBounds) {
		                	this.setValidCoordinate(xi - (int)centerX, zi - (int)centerZ);
		                }
		            }
		        }
		        if (!this.locationIsValid(world, x, y, z)) {
	            	return;
	            }
	            for (int xi = min; xi <= max; xi++) {
	                for (int zi = min; zi <= max; zi++) {
	                	if (this.isValidCoordinate(xi, zi)) {
	                		this.generatePeat(this.depth, world, x + xi, y, z + zi, random);
	                	}
	                }
	            }
	            this.cleanBogEdges(world, x, y, z, random);
	            this.clearValidCoordinates();
			}
	    }
	}
	
	private boolean locationIsValid(World world, int x, int y, int z) {
		int min = -this.diameter / 2;
		int max = this.diameter / 2;
		for (int xi = min; xi <= max; xi++) {
            for (int zi = min; zi <= max; zi++) {
            	if (!this.isValidCoordinate(xi, zi)) {
            		if (xi < max && this.isValidCoordinate(xi + 1, zi)
                    || xi > min && this.isValidCoordinate(xi - 1, zi)
                    || zi < max && this.isValidCoordinate(xi, zi + 1)
                    || zi > min && this.isValidCoordinate(xi, zi - 1)) {
            			int adjustedX = x + xi;
            			int adjustedZ = z + zi;
                        if (world.getBlockMaterial(adjustedX, y, adjustedZ).isLiquid()
                        && world.getBlockId(adjustedX, y - 1, adjustedZ) != DynamicEarth.peat.blockID) {
                        	return false;
                        }
                        if (world.isAirBlock(adjustedX, y, adjustedZ)) {
                        	return false;
                        }
            		}
            	}
            }
        }
        for (int xi = min; xi <= max; xi++) {
        	for (int zi = min; zi <= max; zi++) {
    			int adjustedX = x + xi;
    			int adjustedZ = z + zi;
        		if (this.isValidCoordinate(xi, zi)) {
        			if (world.getBlockMaterial(adjustedX, y + 1, adjustedZ).isSolid()) {
        				return false;
        			}
        		}
        	}
        }
        return true;
	}
	
	private void setValidCoordinate(int x, int z) {
		x += this.diameter / 2;
		z += this.diameter / 2;
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
	
	private void generatePeat(int depth, World world, int x, int y, int z, Random random) {
		if (!world.getBlockMaterial(x, y + 1, z).isSolid()) {
			if (world.isAirBlock(x, y + 1, z)
			|| !BlockPeatMoss.canSpreadToBlock(world, x, y + 1, z)) {
				world.setBlock(x, y, z, world.getBlockId(x, y + 1, z), world.getBlockMetadata(x, y + 1, z), MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
			} else {
				world.setBlock(x, y, z, DynamicEarth.peatMoss.blockID, BlockPeatMoss.getMetadataForSpread(world, x, y + 1, z), MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
			}
			world.setBlock(x, y + 1, z, 0, 0, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
			y--;
		}
		Material material = world.getBlockMaterial(x, y + 1, z);
		if (random.nextInt(BlockPeatMoss.hydrationRadius) == 0) {
			if (material.isReplaceable()
			&& material != MaterialPeatMoss.material) {
				world.setBlock(x, y + 1, z, 0, 0, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
				world.setBlock(x, y, z, Block.waterMoving.blockID, 0, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
			} else {
				world.setBlock(x, y, z, DynamicEarth.peat.blockID, 0, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
			}
		} else {
			if (material.isReplaceable()
			&& material != MaterialPeatMoss.material) {
				world.setBlock(x, y + 1, z, DynamicEarth.peatMoss.blockID, BlockPeatMoss.GROWTHSTAGE_FULLGROWN, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
			}
			world.setBlock(x, y, z, DynamicEarth.peat.blockID, BlockPeat.WET, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
		}
		if (depth > 1) {
			depth -= random.nextInt(depth / 2);
		}
		int i;
    	for	(i = 1; i < depth; i++) {
        	world.setBlock(x, y - i, z, DynamicEarth.peat.blockID, BlockPeat.WET, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
    	}
    	int meta = BlockPeat.ONE_EIGHTH;
    	switch(random.nextInt(4)) {
    	case 1: meta = BlockPeat.TWO_EIGHTHS; break;
    	case 2: meta = BlockPeat.THREE_EIGHTHS; break;
    	case 3: meta = BlockPeat.FOUR_EIGHTHS; break;
    	}
    	world.setBlock(x, y - i, z, DynamicEarth.peat.blockID, meta, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
	}
	
	private void cleanBogEdges(World world, int x, int y, int z, Random random) {
		int min = -diameter / 2;
		int max = diameter / 2;
		for (int xi = min; xi <= max; xi++) {
            for (int zi = min; zi <= max; zi++) {
            	if (!this.isValidCoordinate(xi, zi)) {
            		if (xi < max - 1 && this.isValidCoordinate(xi + 1, zi)
                    || xi > min + 1 && this.isValidCoordinate(xi - 1, zi)
                    || zi < max - 1 && this.isValidCoordinate(xi, zi + 1)
                    || zi > min + 1 && this.isValidCoordinate(xi, zi - 1)) {
            			int adjustedX = x + xi;
            			int adjustedZ = z + zi;
                        if (world.getBlockMaterial(adjustedX, y, adjustedZ).isSolid()) {
                        	this.generatePeat(1, world, adjustedX, y, adjustedZ, random);
                        }
            		}
            	}
            }
        }
	}
}
