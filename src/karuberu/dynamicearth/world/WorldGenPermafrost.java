package karuberu.dynamicearth.world;

import java.util.Random;

import karuberu.core.util.Helper;
import karuberu.dynamicearth.DynamicEarth;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenPermafrost implements IWorldGenerator {
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		assert WorldGenDynamicEarth.doGeneratePermafrost;
		assert !world.isRemote;
		
		int x = chunkX*16,
			z = chunkZ*16;
		for (int xi = x; xi < x + 16; xi++) {
			for (int zi = z; zi < z + 16; zi++) {
				BiomeGenBase biome = world.getBiomeGenForCoords(xi, zi);
				if (biome == null) {
					return;
				}
				float temperature = biome.getFloatTemperature();
				if (temperature <= 0.15F) {
					for (int yi = 127; yi > 60; yi--) {
				        if (!world.canBlockSeeTheSky(xi, yi, zi)) {
				            while (blockShouldBeConverted(world.getBlockId(xi, yi, zi))) {
				                if (world.getBlockMaterial(xi, yi+1, zi) != Material.wood) {
				                    world.setBlock(xi, yi, zi, DynamicEarth.permafrost.blockID, 0, Helper.DO_NOT_NOTIFY_OR_UPDATE);
				                }
				            	yi--;
				            }
				        }
					}
				}
			}
		}
	}
	
	private boolean blockShouldBeConverted(int blockID) {
		return blockID == Block.dirt.blockID;
	}
}
