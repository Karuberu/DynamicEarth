package karuberu.dynamicearth.plugins;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import karuberu.dynamicearth.DynamicEarth;
import mods.railcraft.api.crafting.IRockCrusherRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class PluginRailcraft implements IDynamicEarthPlugin {

	@Override
	public String getName() {
		return "Railcraft Plugin";
	}

	@Override
	public String getErrorReportRequestMessage() {
		return PluginHandler.pleaseNotify;
	}

	@Override
	public boolean requiredModsAreLoaded() {
		return Loader.isModLoaded("Railcraft");
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {}

	@Override
	public void initialize() throws NoClassDefFoundError, NoSuchMethodError, Exception {
		this.addRockCrusherRecipes();
	}
	
	private void addRockCrusherRecipes() {
		if (mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher != null) {
			if (DynamicEarth.includePeat) {
				IRockCrusherRecipe mossyCobblestoneRecipe = RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(Block.cobblestoneMossy), false, false);
				mossyCobblestoneRecipe.addOutput(new ItemStack(Block.gravel), 1.0F);
				mossyCobblestoneRecipe.addOutput(new ItemStack(DynamicEarth.peatMossSpecimen), 0.1F);
			}
			if (DynamicEarth.includeAdobe) {
				RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(DynamicEarth.adobe), false, false
				).addOutput(new ItemStack(DynamicEarth.adobeDust, 4), 1.0F);
				RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(DynamicEarth.adobeSingleSlab), false, false
				).addOutput(new ItemStack(DynamicEarth.adobeDust, 2), 1.0F);
				RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(DynamicEarth.adobeStairs), false, false
				).addOutput(new ItemStack(DynamicEarth.adobeDust, 4), 1.0F);
				RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(DynamicEarth.earthbowl), false, false
				).addOutput(new ItemStack(DynamicEarth.adobeDust, 3), 1.0F);
				RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(DynamicEarth.vase), false, false
				).addOutput(new ItemStack(DynamicEarth.adobeDust, 5), 1.0F);
			}
		}
	}

}
