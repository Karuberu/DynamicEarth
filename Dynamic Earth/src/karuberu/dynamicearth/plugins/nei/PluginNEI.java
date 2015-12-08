package karuberu.dynamicearth.plugins.nei;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import karuberu.dynamicearth.DynamicEarth;
import codechicken.nei.api.API;

public class PluginNEI {

	public static void initialize() throws NoClassDefFoundError, NoSuchMethodError, Exception {
		if (DynamicEarth.includeAdobe
		|| DynamicEarth.includeMudBrick) {
			API.hideItem(DynamicEarth.adobeDoubleSlab.blockID);
		}
		if (DynamicEarth.includeDirtSlabs) {
			API.hideItem(DynamicEarth.dirtDoubleSlab.blockID);
			API.hideItem(DynamicEarth.grassDoubleSlab.blockID);
		}
		if (DynamicEarth.includePeat) {
			API.hideItem(DynamicEarth.peatMoss.blockID);
		}
		if (DynamicEarth.includeAdobe) {
			API.registerRecipeHandler(new RecipeHandlerVase());
			API.registerUsageHandler(new UsageHandlerVase());
			if (DynamicEarth.includeBombs) {
				API.hideItem(DynamicEarth.bombLit.itemID);
				API.registerRecipeHandler(new RecipeHandlerBomb());
			}
			RecipeHandlerWet.addWetRecipe(
				new ItemStack(DynamicEarth.adobeDust, 1, 0),
				new ItemStack(DynamicEarth.adobeBlob, 1, 0)
			);
			RecipeHandlerWet.addWetRecipe(
				new ItemStack(DynamicEarth.vaseRaw, 1, 0),
				new ItemStack(DynamicEarth.adobeBlob, 5, 0)
			);
			RecipeHandlerWet.addWetRecipe(
				new ItemStack(DynamicEarth.earthbowlRaw, 1, 0),
				new ItemStack(DynamicEarth.adobeBlob, 3, 0)
			);
		}
		if (DynamicEarth.includeMud) {
			RecipeHandlerWet.addWetRecipe(
				new ItemStack(Block.dirt, 1, 0),
				new ItemStack(DynamicEarth.mud, 1, DynamicEarth.mud.NORMAL)
			);
			RecipeHandlerWet.addWetRecipe(
				new ItemStack(DynamicEarth.dirtClod, 1, 0),
				new ItemStack(DynamicEarth.mudBlob, 1, 0)
			);
			if (DynamicEarth.includeFertileSoil) {
				RecipeHandlerWet.addWetRecipe(
					new ItemStack(DynamicEarth.fertileSoil, 1, 0),
					new ItemStack(DynamicEarth.fertileMud, 1, DynamicEarth.fertileMud.NORMAL)
				);
			}
		}
		API.registerRecipeHandler(new RecipeHandlerWet());
	}

}
