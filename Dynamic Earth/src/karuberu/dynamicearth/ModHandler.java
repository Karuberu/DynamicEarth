package karuberu.dynamicearth;

import java.util.logging.Level;

import karuberu.dynamicearth.blocks.BlockAdobeSlab;
import karuberu.dynamicearth.blocks.BlockDirtSlab;
import karuberu.dynamicearth.blocks.BlockFertileSoil;
import karuberu.dynamicearth.blocks.BlockGrassSlab;
import karuberu.dynamicearth.blocks.BlockMud;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.blocks.BlockSandySoil;
import karuberu.dynamicearth.fluids.FluidHandler;
import karuberu.dynamicearth.items.ItemVase;
import karuberu.dynamicearth.items.crafting.RecipeManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModHandler {
	public static boolean
		enableForestryIntegration,
		useForestryPeat;
	private static ItemStack peat;
		
	public static void integrateMods() {
		ModHandler.integrateEnrichedGravel();
	    if (ModHandler.enableForestryIntegration) {
	    	ModHandler.integrateForestry();
	    }
	    ModHandler.integrateIndustrialCraft();
	    ModHandler.integrateRailcraft();
	    ModHandler.integrateThaumcraft();
	    ModHandler.integrateThermalExpansion();
	}
	
	private static void integrateEnrichedGravel() {
		try {
			mods.KBIgravelore.api.GravelDropAPI.addItemToList(0, new ItemStack(DynamicEarth.mudBlob));
			mods.KBIgravelore.api.GravelDropAPI.addItemToList(0, new ItemStack(DynamicEarth.adobeDust));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(DynamicEarth.mud.blockID, BlockMud.NORMAL, new ItemStack(DynamicEarth.mudBlob, 4));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(DynamicEarth.mud.blockID, BlockMud.WET, new ItemStack(DynamicEarth.mudBlob, 4));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(DynamicEarth.mud.blockID, BlockMud.FERTILE, new ItemStack(DynamicEarth.mudBlob, 4));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(DynamicEarth.mud.blockID, BlockMud.FERTILE_WET, new ItemStack(DynamicEarth.mudBlob, 4));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(Block.dirt.blockID, 0, new ItemStack(DynamicEarth.dirtClod, 4));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(DynamicEarth.fertileSoil.blockID, BlockFertileSoil.SOIL, new ItemStack(DynamicEarth.dirtClod, 3));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(DynamicEarth.fertileSoil.blockID, BlockFertileSoil.SOIL, new ItemStack(DynamicEarth.peatClump, 1));
		} catch (NoClassDefFoundError e) {
			DELogger.log(Level.INFO, "Enriched Gravel not found. Skipping API integration.");
			return;
		} catch (IndexOutOfBoundsException e) {
			DELogger.log(Level.WARNING, "Enriched Gravel was detected, but has failed. Skipping integration.");
			DELogger.fine("Error was: " + e.toString());
		}
	}
	
	private static void integrateForestry() {
		try {
	    	if (forestry.api.storage.BackpackManager.backpackItems != null) {
		    	final int DIGGER = 1, FORESTER = 2;
		    	if (forestry.api.storage.BackpackManager.backpackItems.length >= 4) {
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.mud));
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.mudBlob));
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.dirtClod));
		    		if (DynamicEarth.includeDirtSlabs) {
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.dirtSlab));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.GRASS));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.MYCELIUM));
		    		}
			    	if (DynamicEarth.includePermafrost) {
		    			forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.permafrost));
		    		}
		    		if (DynamicEarth.includePeat) {
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(DynamicEarth.peat, 1, BlockPeat.WET));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(DynamicEarth.peat, 1, BlockPeat.DRY));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(DynamicEarth.peatClump));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(DynamicEarth.peatBrick));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(DynamicEarth.peatMossSpecimen));
		    		}
		    		if (DynamicEarth.includeFertileSoil) {
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.fertileSoil, 1, BlockFertileSoil.SOIL));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.fertileSoil, 1, BlockFertileSoil.GRASS));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.fertileSoil, 1, BlockFertileSoil.MYCELIUM));
		    		}
		    		if (DynamicEarth.includeSandySoil) {
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.sandySoil, 1, BlockSandySoil.DIRT));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.sandySoil, 1, BlockSandySoil.GRASS));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.sandySoil, 1, BlockSandySoil.MYCELIUM));
		    		}
		    	}
	    	}
	    	if (ModHandler.useForestryPeat) {
        		OreDictionary.registerOre(OreDictionary.getOreID("brickPeat"), DynamicEarth.peatBrick);
	    		peat = forestry.api.core.ItemInterface.getItem("brickPeat");
	    	} else {
	    		peat = new ItemStack(DynamicEarth.peatBrick);
	    	}
	    	RecipeManager.addForestryRecipes();
	    	DEFuelHandler.registerForestryFuels();
		} catch (NoClassDefFoundError e) {
			DELogger.log(Level.INFO, "Forestry not found. Skipping API integration.");
			return;
		}
	}
	
	public static ItemStack getPeatBrick() {
		return peat == null ? new ItemStack(DynamicEarth.peatBrick) : peat;
	}
	
	private static void integrateIndustrialCraft() {
		try {
			RecipeManager.addIndustrialCraftRecipes();
		} catch (NoClassDefFoundError e) {
			DELogger.log(Level.INFO, "IC2 not found. Skipping API integration.");
			return;
		}
	}
	
	private static void integrateRailcraft() {
		try {
			RecipeManager.addRailcraftRecipes();
		} catch (NoClassDefFoundError e) {
			DELogger.log(Level.INFO, "Railcraft not found. Skipping API integration.");
			return;
		}
	}
	
	private static void integrateThaumcraft() {
		try {
	        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mud.blockID, BlockMud.NORMAL, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.WATER, 1));
	        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mud.blockID, BlockMud.WET, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.WATER, 2));
	        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mudBlob.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.WATER, 1));
		    if (DynamicEarth.includeMudBrick) {
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mudBrick.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.FIRE, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.blockMudBrick.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 6).add(thaumcraft.api.EnumTag.FIRE, 3));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mudBrickWall.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 6).add(thaumcraft.api.EnumTag.FIRE, 3));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeSingleSlab.blockID, BlockAdobeSlab.MUDBRICK, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.FIRE, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mudBrickStairs.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.FIRE, 1));
		    }
		    if (DynamicEarth.includeAdobe) {
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobe.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.EARTH, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeWet.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.WATER, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeSingleSlab.blockID, BlockAdobeSlab.ADOBE, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.EARTH, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeStairs.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.EARTH, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeDust.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.DESTRUCTION, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeBlob.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.WATER, 1));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vaseRaw.itemID, -1, (new thaumcraft.api.ObjectTags()));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, 0, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.VOID, 1));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, ItemVase.getDamage(FluidRegistry.getFluidStack(FluidHandler.WATER, 1000)), (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.WATER, 4));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, ItemVase.getDamage(FluidRegistry.getFluidStack(FluidHandler.MILK, 1000)), (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.HEAL, 2).add(thaumcraft.api.EnumTag.LIFE, 2));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, ItemVase.getDamage(FluidRegistry.getFluidStack(FluidHandler.SOUP, 1000)), (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.LIFE, 2));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.earthbowlRaw.itemID, -1, (new thaumcraft.api.ObjectTags().add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.WATER, 1)));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.earthbowl.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.VOID, 1));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(DynamicEarth.earthbowlSoup.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.LIFE, 2));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.bomb.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.DESTRUCTION, 4).add(thaumcraft.api.EnumTag.FIRE, 4));
		    }
		    if (DynamicEarth.includePermafrost) {
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.permafrost.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.COLD, 2));
		    }
		    if (DynamicEarth.includeDirtSlabs) {
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.dirtSlab.blockID, BlockDirtSlab.DIRT, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.grassSlab.blockID, BlockGrassSlab.GRASS, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.PLANT, 1).add(thaumcraft.api.EnumTag.EARTH, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.grassSlab.blockID, BlockGrassSlab.MYCELIUM, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.FUNGUS, 1).add(thaumcraft.api.EnumTag.EARTH, 1));
		    }
		    if (DynamicEarth.includePeat) {
		    	thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.peat.blockID, BlockPeat.DRY, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 3).add(thaumcraft.api.EnumTag.POWER, 6).add(thaumcraft.api.EnumTag.FIRE, 6));
		    	thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.peatClump.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.POWER, 2).add(thaumcraft.api.EnumTag.WATER, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.peatBrick.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.POWER, 2).add(thaumcraft.api.EnumTag.FIRE, 2));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.peatMossSpecimen.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.POWER, 1).add(thaumcraft.api.EnumTag.PLANT, 2).add(thaumcraft.api.EnumTag.EXCHANGE, 4));
		    }
		    if (DynamicEarth.includeFertileSoil) {
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.fertileSoil.blockID, BlockFertileSoil.SOIL, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.CROP, 1).add(thaumcraft.api.EnumTag.EXCHANGE, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.fertileSoil.blockID, BlockFertileSoil.GRASS, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.PLANT, 1).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.CROP, 1).add(thaumcraft.api.EnumTag.EXCHANGE, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.fertileSoil.blockID, BlockFertileSoil.MYCELIUM, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.FUNGUS, 1).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.CROP, 1).add(thaumcraft.api.EnumTag.EXCHANGE, 1));
		    }
		} catch (NoClassDefFoundError e) {
			DELogger.log(Level.INFO, "Thaumcraft not found. Skipping API integration.");
			return;
		}
	}
	
	private static void integrateThermalExpansion() {
		try {
	    	RecipeManager.addThermalExpansionRecipes();
		} catch (NoClassDefFoundError e) {
			DELogger.log(Level.INFO, "Thermal Expansion not found. Skipping API integration.");
			return;
		}
	}
}
