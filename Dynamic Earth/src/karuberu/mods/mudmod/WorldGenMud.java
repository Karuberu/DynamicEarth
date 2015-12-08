package karuberu.mods.mudmod;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;

import net.minecraft.src.Block;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

public class WorldGenMud implements IWorldGenerator {
    private int mudID, depth;
    
    public WorldGenMud(int depth, int mudID) {
        this.mudID = mudID;
        this.depth = depth;
    }
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (!world.provider.isHellWorld) {
			int x = chunkX*16,
				z = chunkZ*16;
			for (int xi = x; xi < x + 16; xi++) {
				for (int zi = z; zi <= z + 16; zi++) {
					for (int yi = 125; yi > 0; yi--) {
				        if (world.getBlockMaterial(xi, yi, zi) == Material.water) {
				            while (yi > yi - this.depth && yi > 0) {
				            	yi--;
				                int id = world.getBlockId(xi, yi, zi);
				                if (id == Block.dirt.blockID || id == Block.grass.blockID) {
				                    world.setBlock(xi, yi, zi, this.mudID);
				                } else if (world.getBlockMaterial(xi, yi, zi) != Material.water) {
				                	break;
				                }
				            }
				        }
					}
				}
			}
		}
	}

}
