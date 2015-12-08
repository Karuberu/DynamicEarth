package karuberu.mods.mudmod.world;

import java.util.Random;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.blocks.BlockMud;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenMud implements IWorldGenerator {
    private int depth;
    
    public WorldGenMud(int depth) {
        this.depth = depth;
    }
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		assert WorldGenMudMod.doGenerateMud;
		assert !world.isRemote;

		int x = chunkX * 16,
			z = chunkZ * 16;
		for (int xi = x; xi < x + 16; xi++) {
			for (int zi = z; zi < z + 16; zi++) {
				if (world.getBiomeGenForCoords(x, z).rainfall >= 0.1F) {
					for (int yi = 127; yi > 0; yi--) {
				        if (world.getBlockMaterial(xi, yi, zi) == Material.water) {
				            while (yi > yi - this.depth && yi > 0) {
				            	yi--;
				                int id = world.getBlockId(xi, yi, zi);
				                if (id == Block.dirt.blockID || id == Block.grass.blockID) {
				                    world.setBlock(xi, yi, zi, MudMod.mud.blockID);
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
