package karuberu.mods.mudmod;

import java.util.logging.Level;

import karuberu.mods.mudmod.blocks.BlockAdobeSlab;
import karuberu.mods.mudmod.blocks.BlockDirtSlab;
import karuberu.mods.mudmod.blocks.BlockGrassSlab;
import karuberu.mods.mudmod.blocks.BlockMud;
import karuberu.mods.mudmod.items.crafting.RecipeManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
			mods.KBIgravelore.api.GravelDropAPI.addItemToList(0, new ItemStack(MudMod.mudBlob));
			mods.KBIgravelore.api.GravelDropAPI.addItemToList(0, new ItemStack(MudMod.adobeDust));
		} catch (NoClassDefFoundError e) {
			MMLogger.log(Level.INFO, "Enriched Gravel not found. Skipping API integration.");
			return;
		} catch (IndexOutOfBoundsException e) {
			MMLogger.log(Level.WARNING, "Enriched Gravel was detected, but has failed. Skipping integration.");
			MMLogger.fine("Error was: " + e.toString());
		}
	}
	
	private static void integrateForestry() {
		try {
	    	if (forestry.api.storage.BackpackManager.backpackItems != null) {
		    	final int DIGGER = 1, FORESTER = 2;
		    	if (forestry.api.storage.BackpackManager.backpackItems.length >= 4) {
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.mud));
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.mudBlob));
		    		if (MudMod.includeDirtSlabs) {
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.dirtSlab));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.grassSlab, 1, BlockGrassSlab.GRASS));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.grassSlab, 1, BlockGrassSlab.MYCELIUM));
		    		}
			    	if (MudMod.includePermafrost) {
		    			forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.permafrost));
		    		}
		    		if (MudMod.includePeat) {
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(MudMod.peat));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(MudMod.peatDry));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(MudMod.peatClump));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(MudMod.peatBrick));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(MudMod.peatMossSpecimen));
		    		}
		    	}
	    	}
	    	if (ModHandler.useForestryPeat) {
	    		peat = forestry.api.core.ItemInterface.getItem("brickPeat");
	    	} else {
	    		peat = new ItemStack(MudMod.peatBrick);
	    	}
	    	RecipeManager.addForestryRecipes();
	    	FuelHandler.registerForestryFuels();
		} catch (NoClassDefFoundError e) {
			MMLogger.log(Level.INFO, "Forestry not found. Skipping API integration.");
			return;
		}
	}
	
	public static ItemStack getPeat() {
		return peat == null ? new ItemStack(MudMod.peatBrick) : peat;
	}
	
	private static void integrateIndustrialCraft() {
		// TODO: Add industrialcraft API.
	}
	
	private static void integrateRailcraft() {
		try {
			RecipeManager.addRailcraftRecipes();
		} catch (NoClassDefFoundError e) {
			MMLogger.log(Level.INFO, "Railcraft not found. Skipping API integration.");
			return;
		}
	}
	
	private static void integrateThaumcraft() {
		try {
	        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.mud.blockID, BlockMud.NORMAL, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.WATER, 1));
	        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.mud.blockID, BlockMud.WET, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.WATER, 2));
	        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.mudBlob.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.WATER, 1));
		    if (MudMod.includeMudBrick) {
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.mudBrick.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.FIRE, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.blockMudBrick.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 6).add(thaumcraft.api.EnumTag.FIRE, 3));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.mudBrickWall.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 6).add(thaumcraft.api.EnumTag.FIRE, 3));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.adobeSingleSlab.blockID, BlockAdobeSlab.MUDBRICK, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.FIRE, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.mudBrickStairs.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.FIRE, 1));
		    }
		    if (MudMod.includeAdobe) {
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.adobe.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.EARTH, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.adobeWet.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.WATER, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.adobeSingleSlab.blockID, BlockAdobeSlab.ADOBE, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.EARTH, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.adobeStairs.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.EARTH, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.adobeDust.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.DESTRUCTION, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.adobeBlob.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.ROCK, 1).add(thaumcraft.api.EnumTag.WATER, 1));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(MudMod.vaseRaw.itemID, -1, (new thaumcraft.api.ObjectTags()));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(MudMod.vase.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.VOID, 1));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(MudMod.vaseWater.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.WATER, 4));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(MudMod.vaseMilk.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.HEAL, 2).add(thaumcraft.api.EnumTag.LIFE, 2));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.earthbowlRaw.itemID, -1, (new thaumcraft.api.ObjectTags().add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.WATER, 1)));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.earthbowl.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.VOID, 1));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(MudMod.earthbowlSoup.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.LIFE, 2));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.bomb.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.DESTRUCTION, 4).add(thaumcraft.api.EnumTag.FIRE, 4));
		    }
		    if (MudMod.includePermafrost) {
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.permafrost.blockID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.COLD, 2));
		    }
		    if (MudMod.includeDirtSlabs) {
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.dirtSlab.blockID, BlockDirtSlab.DIRT, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.grassSlab.blockID, BlockGrassSlab.GRASS, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.PLANT, 1).add(thaumcraft.api.EnumTag.EARTH, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.grassSlab.blockID, BlockGrassSlab.MYCELIUM, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.FUNGUS, 1).add(thaumcraft.api.EnumTag.EARTH, 1));
		    }
		    if (MudMod.includePeat) {
		    	thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.peatDry.blockID, 0, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 3).add(thaumcraft.api.EnumTag.POWER, 6).add(thaumcraft.api.EnumTag.FIRE, 6));
		    	thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.peatClump.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.POWER, 2).add(thaumcraft.api.EnumTag.WATER, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.peatBrick.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.POWER, 2).add(thaumcraft.api.EnumTag.FIRE, 2));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.peatMossSpecimen.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.POWER, 1).add(thaumcraft.api.EnumTag.PLANT, 2).add(thaumcraft.api.EnumTag.EXCHANGE, 4));
		    }
		} catch (NoClassDefFoundError e) {
			MMLogger.log(Level.INFO, "Thaumcraft not found. Skipping API integration.");
			return;
		}
	}
	
	private static void integrateThermalExpansion() {
		try {
	    	RecipeManager.addThermalExpansionRecipes();
		} catch (NoClassDefFoundError e) {
			MMLogger.log(Level.INFO, "Thermal Expansion not found. Skipping API integration.");
			return;
		}
	}
}
