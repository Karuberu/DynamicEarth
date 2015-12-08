package karuberu.mods.mudmod.world;

import java.util.Random;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.blocks.BlockMud;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenMud implements IWorldGenerator {
    private static int depth = 4;
    
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
				            while (yi > yi - WorldGenMud.depth && yi > 0) {
				            	yi--;
				                int id = world.getBlockId(xi, yi, zi);
				                if (id == Block.dirt.blockID
				                || id == Block.grass.blockID
				                || id == Block.mycelium.blockID) {
				                    world.setBlock(xi, yi, zi, MudMod.mud.blockID, BlockMud.WET, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
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
