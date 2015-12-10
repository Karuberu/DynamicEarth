package karuberu.dynamicearth.world;

import java.util.Random;
import cpw.mods.fml.common.IWorldGenerator;

import karuberu.core.util.Coordinates;
import karuberu.core.util.Helper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.api.grass.GrassyBlockRegistry;
import karuberu.dynamicearth.api.grass.IGrassyBlock.GrassType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldGenSandyGrass implements IWorldGenerator {
	
	public static int
		abundance = 15,
		radius = 2;
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		assert WorldGenDynamicEarth.doGenerateSandyGrass;
		assert !world.isRemote;

		for (int j = 0; j < abundance; j++) {
			int x = chunkX * 16 + random.nextInt(16);
			int z = chunkZ * 16 + random.nextInt(16);
			int y = world.getTopSolidOrLiquidBlock(x, z);
			while (world.isAirBlock(x, y, z)
			|| world.getBlockMaterial(x, y, z).isReplaceable()) {
				y--;
			}
			if (y > 255 || y < 0) {
				return;
			}
        	Block block = Block.blocksList[world.getBlockId(x, y, z)];
        	if (block != null
        	&& block.blockID == Block.sand.blockID
        	&& !world.getBlockMaterial(x, y + 1, z).isLiquid()) {
        		Coordinates[] coords = Coordinates.getSurroundingBlockCoords(x, y, z, radius);
        		for (Coordinates coordinates : coords) {
        			GrassType type = GrassyBlockRegistry.getGrassyBlockType(world, coordinates.x, coordinates.y, coordinates.z);
        			if (type != null) {
            			ItemStack sandySoil = DynamicEarth.sandySoil.getBlockForType(world, coordinates.x, coordinates.y, coordinates.z, type);
            			coordinates.setBlock(world, sandySoil.itemID, sandySoil.getItemDamage(), Helper.DO_NOT_NOTIFY_OR_UPDATE);
        			}
        		}
        	}
		}
	}
}
