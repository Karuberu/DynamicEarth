package karuberu.mods.mudmod;

import java.util.logging.Level;
import cpw.mods.fml.common.Loader;

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
	private static ItemStack
		peat;
	public static final int
		GENERIC_ERROR = -1,
		MOD_NOT_FOUND = 1,
		ERROR_NOT_FOUND = 2;
	private static final String
		pleaseNotify = "Please contact Karuberu in the Dynamic Earth forum thread and notify him of this error (include the contents of this log file using spoiler tags).";
		
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
		String modName = "Enriched Gravel";
		String modID = "KBIgravelore";
		if (Loader.isModLoaded(modID)) {
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
				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
				return;
			} catch (NoSuchMethodError e) {
				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
				return;
			} catch (Exception e) {
				ModHandler.printNotification(modName, GENERIC_ERROR, e);
				return;
			}
		} else {
			ModHandler.printNotification(modName, MOD_NOT_FOUND, null);			
		}
	}
	
	private static void integrateForestry() {
		String modName = "Forestry";
		String modID = "Forestry";
		if (Loader.isModLoaded(modID)) {
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
				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
				return;
			} catch (NoSuchMethodError e) {
				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
				return;
			} catch (Exception e) {
				ModHandler.printNotification(modName, GENERIC_ERROR, e);
				return;
			}
		} else {
			ModHandler.printNotification(modName, MOD_NOT_FOUND, null);						
		}
	}
	
	public static ItemStack getPeatBrick() {
		return peat == null ? new ItemStack(MudMod.peatBrick) : peat;
	}
	
	private static void integrateIndustrialCraft() {
		String modName = "IC2";
		String modID = "IC2";
		if (Loader.isModLoaded(modID)) {
			try {
				RecipeManager.addIndustrialCraftRecipes();
			} catch (NoClassDefFoundError e) {
				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
				return;
			} catch (NoSuchMethodError e) {
				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
				return;
			} catch (Exception e) {
				ModHandler.printNotification(modName, GENERIC_ERROR, e);
				return;
			}
		} else {
			ModHandler.printNotification(modName, MOD_NOT_FOUND, null);			
		}
	}
	
	private static void integrateRailcraft() {
		String modName = "Railcraft";
		String modID = "Railcraft";
		if (Loader.isModLoaded(modID)) {
			try {
				RecipeManager.addRailcraftRecipes();
			} catch (NoClassDefFoundError e) {
				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
				return;
			} catch (Exception e) {
				ModHandler.printNotification(modName, GENERIC_ERROR, e);
				return;
			}
		} else {
			ModHandler.printNotification(modName, MOD_NOT_FOUND, null);
		}
	}
	
	private static void integrateThaumcraft() {
		String modName = "Thaumcraft";
		String modID = "Thaumcraft";
		if (Loader.isModLoaded(modID)) {
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
				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
				return;
			} catch (NoSuchMethodError e) {
				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
				return;
			} catch (Exception e) {
				ModHandler.printNotification(modName, GENERIC_ERROR, e);
				return;
			}
		} else {
			ModHandler.printNotification(modName, MOD_NOT_FOUND, null);
		}
	}
	
	private static void integrateThermalExpansion() {
		String modName = "Thermal Expansion";
		String modID = "ThermalExpansion";
		if (Loader.isModLoaded(modID)) {
			try {
		    	RecipeManager.addThermalExpansionRecipes();
			} catch (NoClassDefFoundError e) {
				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
				return;
			} catch (NoSuchMethodError e) {
				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
				return;
			} catch (Exception e) {
				ModHandler.printNotification(modName, GENERIC_ERROR, e);
				return;
			}
		} else {
			ModHandler.printNotification(modName, MOD_NOT_FOUND, null);
		}
	}
	
	public static void printNotification(String modName, int errorCode, Exception e) {
		switch (errorCode) {
		case MOD_NOT_FOUND:
			MMLogger.log(Level.INFO, modName + " not found. Skipping API integration.");
			break;
		case ERROR_NOT_FOUND:
			MMLogger.log(Level.WARNING, modName + " was detected, but the API was not found. Skipping integration. " + pleaseNotify);
			break;
		case GENERIC_ERROR:
			MMLogger.log(Level.WARNING, modName + " was detected, but integration has failed. The API may have changed. " + pleaseNotify);
			MMLogger.fine("Error was: " + e.toString());
			e.printStackTrace();
			break;
		}
	}
}
