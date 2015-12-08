package karuberu.dynamicearth;

import java.util.logging.Level;
import cpw.mods.fml.common.Loader;

import karuberu.dynamicearth.blocks.BlockAdobeSlab;
import karuberu.dynamicearth.blocks.BlockDirtSlab;
import karuberu.dynamicearth.blocks.BlockFertileSoil;
import karuberu.dynamicearth.blocks.BlockGrassSlab;
import karuberu.dynamicearth.blocks.BlockMud;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.blocks.BlockSandySoil;
import karuberu.dynamicearth.fluids.FluidHelper.FluidReference;
import karuberu.dynamicearth.items.ItemVase;
import karuberu.dynamicearth.items.crafting.RecipeManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModHandler {
	public static boolean
		useForestryPeat;
	private static ItemStack
		peat;
	public static final int
		GENERIC_ERROR = -1,
		MOD_NOT_FOUND = 1,
		ERROR_NOT_FOUND = 2;
	private static final String
		pleaseNotify = "Please contact Karuberu in the Dynamic Earth " +
			"forum thread and notify him of this error (include the " +
			"contents of this log file using spoiler tags).";
		
	public static void integrateMods() {
	    ModHandler.integrateBiomesOPlenty();
		ModHandler.integrateEnrichedGravel();
    	ModHandler.integrateForestry();
	    ModHandler.integrateIndustrialCraft();
	    ModHandler.integrateRailcraft();
	    ModHandler.integrateThaumcraft();
	    ModHandler.integrateThermalExpansion();
	}

	private static void integrateBiomesOPlenty() {
		String modName = "Biomes O' Plenty";
		String modID = "BiomesOPlenty";
		if (Loader.isModLoaded(modID)) {
			try {
				Item mudball = biomesoplenty.api.Items.mudball.get();
				if (mudball != null) {
					OreDictionary.registerOre("mudBlob", mudball);
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
	
	private static void integrateEnrichedGravel() {
		String modName = "Enriched Gravel";
		String modID = "KBIgravelore";
		if (Loader.isModLoaded(modID)) {
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
		return peat == null ? new ItemStack(DynamicEarth.peatBrick) : peat;
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
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.dirtClod.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mud.blockID, BlockMud.NORMAL, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2).add(thaumcraft.api.aspects.Aspect.WATER, 1));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mud.blockID, BlockMud.WET, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2).add(thaumcraft.api.aspects.Aspect.WATER, 2));
		        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mudBlob.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 1).add(thaumcraft.api.aspects.Aspect.WATER, 1));
			    if (DynamicEarth.includeMudBrick) {
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mudBrick.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2).add(thaumcraft.api.aspects.Aspect.FIRE, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.blockMudBrick.blockID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 6).add(thaumcraft.api.aspects.Aspect.FIRE, 3));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mudBrickWall.blockID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 6).add(thaumcraft.api.aspects.Aspect.FIRE, 3));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeSingleSlab.blockID, BlockAdobeSlab.MUDBRICK, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2).add(thaumcraft.api.aspects.Aspect.FIRE, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mudBrickStairs.blockID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2).add(thaumcraft.api.aspects.Aspect.FIRE, 1));
			    }
			    if (DynamicEarth.includeAdobe) {
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobe.blockID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.STONE, 1).add(thaumcraft.api.aspects.Aspect.EARTH, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeWet.blockID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.STONE, 1).add(thaumcraft.api.aspects.Aspect.EARTH, 1).add(thaumcraft.api.aspects.Aspect.WATER, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeSingleSlab.blockID, BlockAdobeSlab.ADOBE, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.STONE, 1).add(thaumcraft.api.aspects.Aspect.EARTH, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeStairs.blockID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.STONE, 1).add(thaumcraft.api.aspects.Aspect.EARTH, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeDust.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 1).add(thaumcraft.api.aspects.Aspect.STONE, 1).add(thaumcraft.api.aspects.Aspect.ENTROPY, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.adobeBlob.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 1).add(thaumcraft.api.aspects.Aspect.STONE, 1).add(thaumcraft.api.aspects.Aspect.WATER, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.vaseRaw.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 3).add(thaumcraft.api.aspects.Aspect.STONE, 3).add(thaumcraft.api.aspects.Aspect.WATER, 3));
			        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, 0, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 3).add(thaumcraft.api.aspects.Aspect.STONE, 3).add(thaumcraft.api.aspects.Aspect.VOID, 1));
			        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, ItemVase.getDamage(FluidReference.WATER.getBucketVolumeStack()), (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 3).add(thaumcraft.api.aspects.Aspect.STONE, 3).add(thaumcraft.api.aspects.Aspect.WATER, 4).add(thaumcraft.api.aspects.Aspect.VOID, 1));
			        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, ItemVase.getDamage(FluidReference.MILK.getBucketVolumeStack()), (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 3).add(thaumcraft.api.aspects.Aspect.STONE, 3).add(thaumcraft.api.aspects.Aspect.HUNGER, 2).add(thaumcraft.api.aspects.Aspect.HEAL, 2).add(thaumcraft.api.aspects.Aspect.HEAL, 2).add(thaumcraft.api.aspects.Aspect.WATER, 2).add(thaumcraft.api.aspects.Aspect.VOID, 1));
			        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, ItemVase.getDamage(FluidReference.SOUP.getBucketVolumeStack()), (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 3).add(thaumcraft.api.aspects.Aspect.STONE, 3).add(thaumcraft.api.aspects.Aspect.HUNGER, 6).add(thaumcraft.api.aspects.Aspect.PLANT, 6).add(thaumcraft.api.aspects.Aspect.DARKNESS, 1).add(thaumcraft.api.aspects.Aspect.VOID, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.earthbowlRaw.itemID, -1, (new thaumcraft.api.aspects.AspectList().add(thaumcraft.api.aspects.Aspect.EARTH, 1).add(thaumcraft.api.aspects.Aspect.WATER, 1)));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.earthbowl.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.VOID, 1));
			        thaumcraft.api.ThaumcraftApi.registerComplexObjectTag(DynamicEarth.earthbowlSoup.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.HUNGER, 4).add(thaumcraft.api.aspects.Aspect.PLANT, 4).add(thaumcraft.api.aspects.Aspect.DARKNESS, 2).add(thaumcraft.api.aspects.Aspect.VOID, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.bomb.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2).add(thaumcraft.api.aspects.Aspect.ENTROPY, 4).add(thaumcraft.api.aspects.Aspect.FIRE, 4));
			    }
			    if (DynamicEarth.includePermafrost) {
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.permafrost.blockID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2).add(thaumcraft.api.aspects.Aspect.ICE, 2));
			    }
			    if (DynamicEarth.includeDirtSlabs) {
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.dirtSlab.blockID, BlockDirtSlab.DIRT, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.grassSlab.blockID, BlockGrassSlab.GRASS, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.PLANT, 1).add(thaumcraft.api.aspects.Aspect.EARTH, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.grassSlab.blockID, BlockGrassSlab.MYCELIUM, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.PLANT, 1).add(thaumcraft.api.aspects.Aspect.EARTH, 1));
			    }
			    if (DynamicEarth.includePeat) {
			    	thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.peat.blockID, BlockPeat.DRY, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 3).add(thaumcraft.api.aspects.Aspect.ENERGY, 6).add(thaumcraft.api.aspects.Aspect.FIRE, 6));
			    	thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.peatClump.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2).add(thaumcraft.api.aspects.Aspect.ENERGY, 2).add(thaumcraft.api.aspects.Aspect.WATER, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.peatBrick.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 1).add(thaumcraft.api.aspects.Aspect.ENERGY, 2).add(thaumcraft.api.aspects.Aspect.FIRE, 2));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.peatMossSpecimen.itemID, -1, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.ENERGY, 1).add(thaumcraft.api.aspects.Aspect.PLANT, 2).add(thaumcraft.api.aspects.Aspect.EXCHANGE, 4));
			    }
			    if (DynamicEarth.includeFertileSoil) {
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.fertileSoil.blockID, BlockFertileSoil.SOIL, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2).add(thaumcraft.api.aspects.Aspect.LIFE, 1).add(thaumcraft.api.aspects.Aspect.SENSES, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.fertileSoil.blockID, BlockFertileSoil.GRASS, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.PLANT, 1).add(thaumcraft.api.aspects.Aspect.EARTH, 1).add(thaumcraft.api.aspects.Aspect.LIFE, 1).add(thaumcraft.api.aspects.Aspect.SENSES, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.fertileSoil.blockID, BlockFertileSoil.MYCELIUM, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.PLANT, 1).add(thaumcraft.api.aspects.Aspect.EARTH, 1).add(thaumcraft.api.aspects.Aspect.LIFE, 1).add(thaumcraft.api.aspects.Aspect.SENSES, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mud.blockID, BlockMud.FERTILE, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2).add(thaumcraft.api.aspects.Aspect.WATER, 1).add(thaumcraft.api.aspects.Aspect.LIFE, 1).add(thaumcraft.api.aspects.Aspect.SENSES, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.mud.blockID, BlockMud.FERTILE_WET, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2).add(thaumcraft.api.aspects.Aspect.WATER, 2).add(thaumcraft.api.aspects.Aspect.LIFE, 1).add(thaumcraft.api.aspects.Aspect.SENSES, 1));
			    }
			    if (DynamicEarth.includeSandySoil) {
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.sandySoil.blockID, BlockSandySoil.DIRT, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.EARTH, 2));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.sandySoil.blockID, BlockSandySoil.GRASS, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.PLANT, 1).add(thaumcraft.api.aspects.Aspect.EARTH, 1));
			        thaumcraft.api.ThaumcraftApi.registerObjectTag(DynamicEarth.sandySoil.blockID, BlockSandySoil.MYCELIUM, (new thaumcraft.api.aspects.AspectList()).add(thaumcraft.api.aspects.Aspect.PLANT, 1).add(thaumcraft.api.aspects.Aspect.EARTH, 1));
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
	
	public static void integrateThermalExpansion() {
		String modName = "Thermal Expansion";
		String modID = "ThermalExpansion";
		if (Loader.isModLoaded(modID)) {
	    	RecipeManager.addThermalExpansionRecipes();
//			try {
//		    	RecipeManager.addThermalExpansionRecipes();
//			} catch (NoClassDefFoundError e) {
//				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
//				return;
//			} catch (NoSuchMethodError e) {
//				ModHandler.printNotification(modName, ERROR_NOT_FOUND, null);
//				return;
//			} catch (Exception e) {
//				ModHandler.printNotification(modName, GENERIC_ERROR, e);
//				return;
//			}
		} else {
			ModHandler.printNotification(modName, MOD_NOT_FOUND, null);
		}
	}
	
	public static void printNotification(String modName, int errorCode, Exception e) {
		switch (errorCode) {
		case MOD_NOT_FOUND:
			DELogger.log(Level.INFO, modName + " not found. Skipping API integration.");
			break;
		case ERROR_NOT_FOUND:
			DELogger.log(Level.WARNING, modName + " was detected, but the API was not found. Skipping integration. " + pleaseNotify);
			break;
		case GENERIC_ERROR:
			DELogger.log(Level.WARNING, modName + " was detected, but integration has failed. The API may have changed. " + pleaseNotify);
			DELogger.fine("Error was: " + e.toString());
			e.printStackTrace();
			break;
		}
	}
}
