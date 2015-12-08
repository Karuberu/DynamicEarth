package karuberu.dynamicearth.world;

import java.util.Random;

import karuberu.dynamicearth.DynamicEarth;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class WorldGenDynamicEarth implements IWorldGenerator {
	public static WorldGenDynamicEarth
		instance = new WorldGenDynamicEarth();
	public static boolean
		doGenerateMud,
		doGeneratePermafrost,
		doGeneratePeat,
		doGenerateFertileGrass,
		doGenerateSandyGrass,
		enableDiverseGeneration;
	public static final IWorldGenerator
		permafrost = new WorldGenPermafrost(),
		mud = new WorldGenMud(),
		peat = new WorldGenPeat(),
		fertileGrass = new WorldGenFertileGrass(),
		sandyGrass = new WorldGenSandyGrass();

	public static void register() {
		GameRegistry.registerWorldGenerator(WorldGenDynamicEarth.instance);
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (!DynamicEarth.restoreDirtOnChunkLoad
		&& !(chunkProvider instanceof ChunkProviderHell)
		&& !(chunkProvider instanceof ChunkProviderEnd)) {
			if (WorldGenDynamicEarth.doGeneratePermafrost && DynamicEarth.includePermafrost) {
				WorldGenDynamicEarth.permafrost.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	        }
	        if (WorldGenDynamicEarth.doGenerateMud && DynamicEarth.includeMud) {
	        	WorldGenDynamicEarth.mud.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	        }
	        if (WorldGenDynamicEarth.doGenerateFertileGrass && DynamicEarth.includeFertileSoil) {
	        	WorldGenDynamicEarth.fertileGrass.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	        }
	        if (WorldGenDynamicEarth.doGenerateSandyGrass && DynamicEarth.includeSandySoil) {
	        	WorldGenDynamicEarth.sandyGrass.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
	        }
		}
	}

	public static void populateChunk(World world, IChunkProvider chunkProvider, int chunkX, int chunkZ, boolean hasVillageGenerated, Random random) {
		if (!DynamicEarth.restoreDirtOnChunkLoad
		&& !(chunkProvider instanceof ChunkProviderHell)
		&& !(chunkProvider instanceof ChunkProviderEnd)) {
	        if (WorldGenDynamicEarth.doGeneratePeat && DynamicEarth.includePeat) {
	        	WorldGenDynamicEarth.peat.generate(random, chunkX, chunkZ, world, null, chunkProvider);
	        }
		}
	}
}
