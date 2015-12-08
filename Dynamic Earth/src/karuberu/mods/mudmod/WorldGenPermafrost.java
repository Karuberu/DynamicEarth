package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenPermafrost implements IWorldGenerator {
	
	private int depth, permafrostID;

	public WorldGenPermafrost(int permafrostID) {
		this.permafrostID = permafrostID;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		int x = chunkX*16,
			z = chunkZ*16;
		for (int xi = x; xi < x + 16; xi++) {
			for (int zi = z; zi < z + 16; zi++) {
				float temperature = world.getBiomeGenForCoords(xi, zi).getFloatTemperature();
				if (temperature <= 0.15F) {
					for (int yi = 125; yi > 0; yi--) {
				        if (!world.canBlockSeeTheSky(xi, yi, zi)) {
				        	if (world.getBlockId(xi, yi, zi) == Block.dirt.blockID) {
					            while (world.getBlockId(xi, yi, zi) == Block.dirt.blockID) {
					                if (world.getBlockMaterial(xi, yi+1, zi) != Material.wood) {
					                    world.setBlock(xi, yi, zi, this.permafrostID);
					                }
					            	yi--;
					            }
				        		break;
				        	}
				        }
					}
				}
			}
		}
	}
}
