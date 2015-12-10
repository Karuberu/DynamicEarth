package karuberu.dynamicearth.world;

import java.util.Random;

import karuberu.core.util.Coordinates;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.api.grass.IGrassyBlock;
import karuberu.dynamicearth.api.grass.IGrassyBlock.GrassType;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.BiomeDictionary.Type;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenFertileGrass implements IWorldGenerator {
	
	public static int
		abundance = 10;
	private static int
		searchDiameter = 6;	
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		assert WorldGenDynamicEarth.doGenerateFertileGrass;
		assert !world.isRemote;
		
		for (int j = 0; j < abundance; j++) {
			int x = chunkX * 16 + random.nextInt(16);
			int z = chunkZ * 16 + random.nextInt(16);
			int y = 256;
			BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
			if (BiomeDictionary.isBiomeOfType(biome, Type.PLAINS)) {
				while (world.isAirBlock(x, y, z)
				|| world.getBlockMaterial(x, y, z).isReplaceable()) {
					y--;
				}
				if (y > 255 || y < 0) {
					return;
				}
				if (world.getBlockMaterial(x, y, z).isLiquid()) {
					return;
				}
				int min = -(searchDiameter - 1) / 2;
				int max = searchDiameter / 2;
				int numGrass = 0;
				Coordinates[] coords = new Coordinates[searchDiameter * searchDiameter];
				for (int xi = min + x; xi <= max + x; xi++) {
		            for (int zi = min + z; zi <= max + z; zi++) {
		            	// Make sure it's grass.
		            	Block block = Block.blocksList[world.getBlockId(xi, y, zi)];
		            	if (block == null) {
		            		continue;
		            	}
		            	if (block.blockID == Block.grass.blockID
		            	|| (block instanceof IGrassyBlock && ((IGrassyBlock)block).getType(world, xi, y, zi) == GrassType.GRASS)) {
		    				// Make sure it has tall grass or similar growing on it.
		            		block = Block.blocksList[world.getBlockId(xi, y + 1, zi)];
			                if (block instanceof IPlantable
			                && ((IPlantable)block).getPlantType(world, xi, y + 1, zi) == EnumPlantType.Plains) {
			                	coords[numGrass] = new Coordinates(xi, y, zi);
			                	numGrass++;
			                }
		            	}
		            }
		        }
		        if (numGrass < searchDiameter) {
		        	return;
		        }
	            for (int i = 0; i < numGrass; i++) {
	                coords[i].setBlock(world, DynamicEarth.fertileSoil.blockID, DynamicEarth.fertileSoil.GRASS);
	            }
		    }
		}
	}
}
