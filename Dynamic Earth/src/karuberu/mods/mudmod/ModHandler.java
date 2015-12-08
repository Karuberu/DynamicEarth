package karuberu.mods.mudmod;

import java.util.logging.Level;

import karuberu.mods.mudmod.blocks.BlockAdobeSlab;
import karuberu.mods.mudmod.blocks.BlockDirtSlab;
import karuberu.mods.mudmod.blocks.BlockFertileSoil;
import karuberu.mods.mudmod.blocks.BlockGrassSlab;
import karuberu.mods.mudmod.blocks.BlockMud;
import karuberu.mods.mudmod.blocks.BlockPeat;
import karuberu.mods.mudmod.blocks.BlockSandySoil;
import karuberu.mods.mudmod.items.ItemVase;
import karuberu.mods.mudmod.items.crafting.RecipeManager;
import karuberu.mods.mudmod.liquids.LiquidHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidDictionary;
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
			mods.KBIgravelore.api.GravelDropAPI.addItemToList(0, new ItemStack(MudMod.mudBlob));
			mods.KBIgravelore.api.GravelDropAPI.addItemToList(0, new ItemStack(MudMod.adobeDust));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(MudMod.mud.blockID, BlockMud.NORMAL, new ItemStack(MudMod.mudBlob, 4));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(MudMod.mud.blockID, BlockMud.WET, new ItemStack(MudMod.mudBlob, 4));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(MudMod.mud.blockID, BlockMud.FERTILE, new ItemStack(MudMod.mudBlob, 4));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(MudMod.mud.blockID, BlockMud.FERTILE_WET, new ItemStack(MudMod.mudBlob, 4));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(Block.dirt.blockID, 0, new ItemStack(MudMod.dirtClod, 4));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(MudMod.fertileSoil.blockID, BlockFertileSoil.SOIL, new ItemStack(MudMod.dirtClod, 3));
			mods.KBIgravelore.api.CustomSifterDropAPI.setConstant(MudMod.fertileSoil.blockID, BlockFertileSoil.SOIL, new ItemStack(MudMod.peatClump, 1));
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
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.dirtClod));
		    		if (MudMod.includeDirtSlabs) {
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.dirtSlab));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.grassSlab, 1, BlockGrassSlab.GRASS));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.grassSlab, 1, BlockGrassSlab.MYCELIUM));
		    		}
			    	if (MudMod.includePermafrost) {
		    			forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.permafrost));
		    		}
		    		if (MudMod.includePeat) {
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(MudMod.peat, 1, BlockPeat.WET));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(MudMod.peat, 1, BlockPeat.DRY));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(MudMod.peatClump));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(MudMod.peatBrick));
			    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(MudMod.peatMossSpecimen));
		    		}
		    		if (MudMod.includeFertileSoil) {
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.fertileSoil, 1, BlockFertileSoil.SOIL));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.fertileSoil, 1, BlockFertileSoil.GRASS));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.fertileSoil, 1, BlockFertileSoil.MYCELIUM));
		    		}
		    		if (MudMod.includeSandySoil) {
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.sandySoil, 1, BlockSandySoil.DIRT));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.sandySoil, 1, BlockSandySoil.GRASS));
			    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(MudMod.sandySoil, 1, BlockSandySoil.MYCELIUM));
		    		}
		    	}
	    	}
	    	if (ModHandler.useForestryPeat) {
        		OreDictionary.registerOre(OreDictionary.getOreID("brickPeat"), MudMod.peatBrick);
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
	
	public static ItemStack getPeatBrick() {
		return peat == null ? new ItemStack(MudMod.peatBrick) : peat;
	}
	
	private static void integrateIndustrialCraft() {
		try {
			RecipeManager.addIndustrialCraftRecipes();
		} catch (NoClassDefFoundError e) {
			MMLogger.log(Level.INFO, "IC2 not found. Skipping API integration.");
			return;
		}
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
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(MudMod.vase.itemID, 0, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.VOID, 1));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(MudMod.vase.itemID, ItemVase.getDamage(LiquidDictionary.getCanonicalLiquid(LiquidHandler.WATER)), (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.WATER, 4));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(MudMod.vase.itemID, ItemVase.getDamage(LiquidDictionary.getCanonicalLiquid(LiquidHandler.MILK)), (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.HEAL, 2).add(thaumcraft.api.EnumTag.LIFE, 2));
		        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(MudMod.vase.itemID, ItemVase.getDamage(LiquidDictionary.getCanonicalLiquid(LiquidHandler.SOUP)), (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.LIFE, 2));
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
		    	thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.peat.blockID, BlockPeat.DRY, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 3).add(thaumcraft.api.EnumTag.POWER, 6).add(thaumcraft.api.EnumTag.FIRE, 6));
		    	thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.peatClump.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.POWER, 2).add(thaumcraft.api.EnumTag.WATER, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.peatBrick.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 1).add(thaumcraft.api.EnumTag.POWER, 2).add(thaumcraft.api.EnumTag.FIRE, 2));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.peatMossSpecimen.itemID, -1, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.POWER, 1).add(thaumcraft.api.EnumTag.PLANT, 2).add(thaumcraft.api.EnumTag.EXCHANGE, 4));
		    }
		    if (MudMod.includeFertileSoil) {
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.fertileSoil.blockID, BlockFertileSoil.SOIL, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.CROP, 1).add(thaumcraft.api.EnumTag.EXCHANGE, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.fertileSoil.blockID, BlockFertileSoil.GRASS, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.PLANT, 1).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.CROP, 1).add(thaumcraft.api.EnumTag.EXCHANGE, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(MudMod.fertileSoil.blockID, BlockFertileSoil.MYCELIUM, (new thaumcraft.api.ObjectTags()).add(thaumcraft.api.EnumTag.FUNGUS, 1).add(thaumcraft.api.EnumTag.EARTH, 2).add(thaumcraft.api.EnumTag.CROP, 1).add(thaumcraft.api.EnumTag.EXCHANGE, 1));
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
