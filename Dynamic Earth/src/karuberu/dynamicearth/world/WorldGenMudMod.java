package karuberu.dynamicearth.world;

import java.util.Random;

import karuberu.dynamicearth.DynamicEarth;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class WorldGenMudMod implements IWorldGenerator {
	public static WorldGenMudMod
		instance = new WorldGenMudMod();
	public static boolean
		doGenerateMud,
		doGeneratePermafrost,
		doGeneratePeat;
	public static final IWorldGenerator
		permafrost = new WorldGenPermafrost(),
		mud = new WorldGenMud(),
		peat = new WorldGenPeat();

	public static void register() {
		GameRegistry.registerWorldGenerator(instance);
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (!DynamicEarth.restoreDirtOnChunkLoad
		&& !(chunkProvider instanceof ChunkProviderHell)
		&& !(chunkProvider instanceof ChunkProviderEnd)) {
	        if (doGeneratePeat && DynamicEarth.includePeat) {
	        	peat.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	        }
			if (doGeneratePermafrost && DynamicEarth.includePermafrost) {
	        	permafrost.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	        }
	        if (doGenerateMud) {
	        	mud.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	        }
		}
	}
}
