package karuberu.dynamicearth.plugins;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockAdobeSlab;
import karuberu.dynamicearth.blocks.BlockDirtSlab;
import karuberu.dynamicearth.blocks.BlockGrassSlab;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.fluids.FluidHelper.FluidReference;
import karuberu.dynamicearth.items.ItemVase;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class PluginThaumcraft implements IDynamicEarthPlugin {

	@Override
	public String getName() {
		return "Thaumcraft Plugin";
	}

	@Override
	public String getErrorReportRequestMessage() {
		return PluginHandler.pleaseNotify;
	}

	@Override
	public boolean requiredModsAreLoaded() {
		return Loader.isModLoaded("Thaumcraft");
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {}

	@Override
	public void initialize() throws NoClassDefFoundError, NoSuchMethodError, Exception {
		this.registerAspects();
	}

	private void registerAspects() {
        ThaumcraftApi.registerObjectTag(DynamicEarth.dirtClod.itemID, -1, (new AspectList()).add(Aspect.EARTH, 1));
		if (DynamicEarth.includeMud) {
			ThaumcraftApi.registerObjectTag(DynamicEarth.mud.blockID, DynamicEarth.mud.NORMAL, (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.WATER, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.mud.blockID, DynamicEarth.mud.WET, (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.WATER, 2));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.mudBlob.itemID, -1, (new AspectList()).add(Aspect.EARTH, 1).add(Aspect.WATER, 1));
		}
	    if (DynamicEarth.includeMudBrick) {
	        ThaumcraftApi.registerObjectTag(DynamicEarth.mudBrick.itemID, -1, (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.FIRE, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.blockMudBrick.blockID, -1, (new AspectList()).add(Aspect.EARTH, 6).add(Aspect.FIRE, 3));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.mudBrickWall.blockID, -1, (new AspectList()).add(Aspect.EARTH, 6).add(Aspect.FIRE, 3));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.adobeSingleSlab.blockID, BlockAdobeSlab.MUDBRICK, (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.FIRE, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.mudBrickStairs.blockID, -1, (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.FIRE, 1));
	    }
	    if (DynamicEarth.includeAdobe) {
	        ThaumcraftApi.registerObjectTag(DynamicEarth.adobe.blockID, -1, (new AspectList()).add(Aspect.STONE, 1).add(Aspect.EARTH, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.adobeWet.blockID, -1, (new AspectList()).add(Aspect.STONE, 1).add(Aspect.EARTH, 1).add(Aspect.WATER, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.adobeSingleSlab.blockID, BlockAdobeSlab.ADOBE, (new AspectList()).add(Aspect.STONE, 1).add(Aspect.EARTH, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.adobeStairs.blockID, -1, (new AspectList()).add(Aspect.STONE, 1).add(Aspect.EARTH, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.adobeDust.itemID, -1, (new AspectList()).add(Aspect.EARTH, 1).add(Aspect.STONE, 1).add(Aspect.ENTROPY, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.adobeBlob.itemID, -1, (new AspectList()).add(Aspect.EARTH, 1).add(Aspect.STONE, 1).add(Aspect.WATER, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.vaseRaw.itemID, -1, (new AspectList()).add(Aspect.EARTH, 3).add(Aspect.STONE, 3).add(Aspect.WATER, 3));
	        ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, 0, (new AspectList()).add(Aspect.EARTH, 3).add(Aspect.STONE, 3).add(Aspect.VOID, 1));
	        ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, ItemVase.getDamage(FluidReference.WATER.getBucketVolumeStack()), (new AspectList()).add(Aspect.EARTH, 3).add(Aspect.STONE, 3).add(Aspect.WATER, 4).add(Aspect.VOID, 1));
	        ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, ItemVase.getDamage(FluidReference.MILK.getBucketVolumeStack()), (new AspectList()).add(Aspect.EARTH, 3).add(Aspect.STONE, 3).add(Aspect.HUNGER, 2).add(Aspect.HEAL, 2).add(Aspect.HEAL, 2).add(Aspect.WATER, 2).add(Aspect.VOID, 1));
	        ThaumcraftApi.registerComplexObjectTag(DynamicEarth.vase.itemID, ItemVase.getDamage(FluidReference.SOUP.getBucketVolumeStack()), (new AspectList()).add(Aspect.EARTH, 3).add(Aspect.STONE, 3).add(Aspect.HUNGER, 6).add(Aspect.PLANT, 6).add(Aspect.DARKNESS, 1).add(Aspect.VOID, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.earthbowlRaw.itemID, -1, (new AspectList().add(Aspect.EARTH, 1).add(Aspect.WATER, 1)));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.earthbowl.itemID, -1, (new AspectList()).add(Aspect.VOID, 1));
	        ThaumcraftApi.registerComplexObjectTag(DynamicEarth.earthbowlSoup.itemID, -1, (new AspectList()).add(Aspect.HUNGER, 4).add(Aspect.PLANT, 4).add(Aspect.DARKNESS, 2).add(Aspect.VOID, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.bomb.itemID, -1, (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.ENTROPY, 4).add(Aspect.FIRE, 4));
	    }
	    if (DynamicEarth.includePermafrost) {
	        ThaumcraftApi.registerObjectTag(DynamicEarth.permafrost.blockID, -1, (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.ICE, 2));
	    }
	    if (DynamicEarth.includeDirtSlabs) {
	        ThaumcraftApi.registerObjectTag(DynamicEarth.dirtSlab.blockID, BlockDirtSlab.DIRT, (new AspectList()).add(Aspect.EARTH, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.grassSlab.blockID, BlockGrassSlab.GRASS, (new AspectList()).add(Aspect.PLANT, 1).add(Aspect.EARTH, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.grassSlab.blockID, BlockGrassSlab.MYCELIUM, (new AspectList()).add(Aspect.PLANT, 1).add(Aspect.EARTH, 1));
	    }
	    if (DynamicEarth.includePeat) {
	    	ThaumcraftApi.registerObjectTag(DynamicEarth.peat.blockID, BlockPeat.DRY, (new AspectList()).add(Aspect.EARTH, 3).add(Aspect.ENERGY, 6).add(Aspect.FIRE, 6));
	    	ThaumcraftApi.registerObjectTag(DynamicEarth.peatClump.itemID, -1, (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.ENERGY, 2).add(Aspect.WATER, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.peatBrick.itemID, -1, (new AspectList()).add(Aspect.EARTH, 1).add(Aspect.ENERGY, 2).add(Aspect.FIRE, 2));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.peatMossSpecimen.itemID, -1, (new AspectList()).add(Aspect.ENERGY, 1).add(Aspect.PLANT, 2).add(Aspect.EXCHANGE, 4));
	    }
	    if (DynamicEarth.includeFertileSoil) {
	        ThaumcraftApi.registerObjectTag(DynamicEarth.fertileSoil.blockID, DynamicEarth.fertileSoil.DIRT, (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.LIFE, 1).add(Aspect.SENSES, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.fertileSoil.blockID, DynamicEarth.fertileSoil.GRASS, (new AspectList()).add(Aspect.PLANT, 1).add(Aspect.EARTH, 1).add(Aspect.LIFE, 1).add(Aspect.SENSES, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.fertileSoil.blockID, DynamicEarth.fertileSoil.MYCELIUM, (new AspectList()).add(Aspect.PLANT, 1).add(Aspect.EARTH, 1).add(Aspect.LIFE, 1).add(Aspect.SENSES, 1));
	        if (DynamicEarth.includeMud) {
		        ThaumcraftApi.registerObjectTag(DynamicEarth.fertileMud.blockID, DynamicEarth.fertileMud.NORMAL, (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.WATER, 1).add(Aspect.LIFE, 1).add(Aspect.SENSES, 1));
		        ThaumcraftApi.registerObjectTag(DynamicEarth.fertileMud.blockID, DynamicEarth.fertileMud.WET, (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.WATER, 2).add(Aspect.LIFE, 1).add(Aspect.SENSES, 1));
	        }
	    }
	    if (DynamicEarth.includeSandySoil) {
	        ThaumcraftApi.registerObjectTag(DynamicEarth.sandySoil.blockID, DynamicEarth.sandySoil.DIRT, (new AspectList()).add(Aspect.EARTH, 2));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.sandySoil.blockID, DynamicEarth.sandySoil.GRASS, (new AspectList()).add(Aspect.PLANT, 1).add(Aspect.EARTH, 1));
	        ThaumcraftApi.registerObjectTag(DynamicEarth.sandySoil.blockID, DynamicEarth.sandySoil.MYCELIUM, (new AspectList()).add(Aspect.PLANT, 1).add(Aspect.EARTH, 1));
	    }
	}

}
