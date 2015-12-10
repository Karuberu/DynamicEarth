package karuberu.dynamicearth;

import karuberu.dynamicearth.blocks.BlockMud;
import karuberu.dynamicearth.blocks.BlockSoil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryHelper {

	public static void registerOre(String tag, Block block, int metadata) {
    	OreDictionary.registerOre(tag, new ItemStack(block.blockID, 1, metadata));		
	}
	
	public static void registerSoilBlock(BlockSoil block) {
		OreDictionary.registerOre("blockDirt", new ItemStack(block.blockID, 1, block.DIRT));
		OreDictionary.registerOre("blockGrass", new ItemStack(block.blockID, 1, block.GRASS));
		OreDictionary.registerOre("blockMycelium", new ItemStack(block.blockID, 1, block.MYCELIUM));
	}
	
	public static void registerMudBlock(BlockMud block) {
		OreDictionary.registerOre("blockMud", new ItemStack(block.blockID, 1, block.NORMAL));
		OreDictionary.registerOre("blockMud", new ItemStack(block.blockID, 1, block.WET));
		OreDictionary.registerOre("blockGrass", new ItemStack(block.blockID, 1, block.GRASS));
		OreDictionary.registerOre("blockGrass", new ItemStack(block.blockID, 1, block.WET_GRASS));
		OreDictionary.registerOre("blockMycelium", new ItemStack(block.blockID, 1, block.MYCELIUM));
		OreDictionary.registerOre("blockMycelium", new ItemStack(block.blockID, 1, block.WET_MYCELIUM));		
	}
}
