package karuberu.dynamicearth.plugins.ic2;

import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockAdobeSlab;
import karuberu.dynamicearth.blocks.BlockDirtSlab;
import karuberu.dynamicearth.blocks.BlockGrassSlab;
import karuberu.dynamicearth.blocks.BlockPermafrost;
import karuberu.dynamicearth.items.crafting.RecipeManager;

public class PluginIC2 {
	
	public static void initialize() throws NoClassDefFoundError, NoSuchMethodError, Exception {
		addMaceratorRecipes();
		addCompressorRecipes();
		addScrapboxDrops();
	}
	
	private static void addMaceratorRecipes() {
		if (Recipes.macerator != null) {
			Recipes.macerator.addRecipe(
				new RecipeInputItemStack(new ItemStack(Block.dirt, 1, 0)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			Recipes.macerator.addRecipe(
				new RecipeInputItemStack(new ItemStack(Block.grass, 1, 0)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			Recipes.macerator.addRecipe(
				new RecipeInputItemStack(new ItemStack(Block.mycelium, 1, 0)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			Recipes.macerator.addRecipe(
				new RecipeInputItemStack(new ItemStack(DynamicEarth.permafrost, 1, BlockPermafrost.META_PERMAFROST)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			Recipes.macerator.addRecipe(
				new RecipeInputItemStack(new ItemStack(DynamicEarth.dirtSlab, 1, BlockDirtSlab.DIRT)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 2)
			);
			Recipes.macerator.addRecipe(
				new RecipeInputItemStack(new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.GRASS)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 2)
			);
			Recipes.macerator.addRecipe(
				new RecipeInputItemStack(new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.MYCELIUM)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 2)
			);
			Recipes.macerator.addRecipe(
				new RecipeInputItemStack(new ItemStack(DynamicEarth.mudBrick)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 1)
			);
			Recipes.macerator.addRecipe(
				new RecipeInputItemStack(new ItemStack(DynamicEarth.blockMudBrick)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			Recipes.macerator.addRecipe(
				new RecipeInputItemStack(new ItemStack(DynamicEarth.adobeSingleSlab, 1, BlockAdobeSlab.MUDBRICK)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 2)
			);
			Recipes.macerator.addRecipe(
				new RecipeInputItemStack(new ItemStack(DynamicEarth.mudBrickStairs)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			if (DynamicEarth.includeAdobe) {
				Recipes.macerator.addRecipe(
					new RecipeInputItemStack(new ItemStack(DynamicEarth.adobe)),
					null,
					new ItemStack(DynamicEarth.adobeDust, 4)
				);
				Recipes.macerator.addRecipe(
					new RecipeInputItemStack(new ItemStack(DynamicEarth.adobeSingleSlab, 1, BlockAdobeSlab.ADOBE)),
					null,
					new ItemStack(DynamicEarth.adobeDust, 2)
				);
				Recipes.macerator.addRecipe(
					new RecipeInputItemStack(new ItemStack(DynamicEarth.adobeStairs)),
					null,
					new ItemStack(DynamicEarth.adobeDust, 4)
				);
				Recipes.macerator.addRecipe(
					new RecipeInputItemStack(new ItemStack(DynamicEarth.earthbowl)),
					null,
					new ItemStack(DynamicEarth.adobeDust, 3)
				);
				Recipes.macerator.addRecipe(
					new RecipeInputItemStack(new ItemStack(DynamicEarth.vase, 1, 0)),
					null,
					new ItemStack(DynamicEarth.adobeDust, 5)
				);
			}
		}
	}
	
	private static void addCompressorRecipes() {
		if (Recipes.compressor != null) {
			Recipes.compressor.addRecipe(
				new RecipeInputItemStack(new ItemStack(DynamicEarth.dirtClod, 4)),
				null,
				new ItemStack(Block.dirt)
			);
			if (DynamicEarth.includeMud) {
				Recipes.compressor.addRecipe(
					new RecipeInputItemStack(new ItemStack(DynamicEarth.mudBlob, 4)),
					null,
					new ItemStack(DynamicEarth.mud.blockID, 1, DynamicEarth.mud.NORMAL)
				);
			}
			if (DynamicEarth.includeAdobe) {
				Recipes.compressor.addRecipe(
					new RecipeInputItemStack(new ItemStack(DynamicEarth.adobeBlob, 4)),
					null,
					new ItemStack(DynamicEarth.adobe)
				);
				Recipes.compressor.addRecipe(
					new RecipeInputItemStack(new ItemStack(DynamicEarth.earthbowlRaw)),
					null,
					new ItemStack(DynamicEarth.adobeBlob, 3)
				);
				Recipes.compressor.addRecipe(
					new RecipeInputItemStack(new ItemStack(DynamicEarth.vaseRaw)),
					null,
					new ItemStack(DynamicEarth.adobeBlob, 5)
				);
			}
			if (DynamicEarth.includePeat) {
				Recipes.compressor.addRecipe(
					new RecipeInputItemStack(new ItemStack(DynamicEarth.peatClump)),
					null,
					RecipeManager.getPeatBrick()
				);
			}
		}
	}
	
	private static void addScrapboxDrops() {
		if (Recipes.scrapboxDrops != null) {
			if (DynamicEarth.includePeat) {
				Recipes.scrapboxDrops.addDrop(
					new ItemStack(DynamicEarth.peatMossSpecimen), 0.2F
				);
			}
		}
	}
}
