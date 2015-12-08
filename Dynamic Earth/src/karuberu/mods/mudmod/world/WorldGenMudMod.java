package karuberu.mods.mudmod.world;

import java.util.Random;

import karuberu.mods.mudmod.MudMod;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WorldGenMudMod implements IWorldGenerator {
	public static boolean
		doGenerateMud,
		doGeneratePermafrost,
		doGeneratePeat;
	public static final IWorldGenerator
		permafrost = new WorldGenPermafrost(),
		mud = new WorldGenMud(),
		peat = new WorldGenPeat();

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (!MudMod.restoreDirtOnChunkLoad
		&& !(chunkProvider instanceof ChunkProviderHell)
		&& !(chunkProvider instanceof ChunkProviderEnd)) {
	        if (doGeneratePeat && MudMod.includePeat) {
	        	peat.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	        }
			if (doGeneratePermafrost && MudMod.includePermafrost) {
	        	permafrost.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	        }
	        if (doGenerateMud) {
	        	mud.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	        }
		}
	}
}
