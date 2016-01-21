package karuberu.dynamicearth;

import java.util.Arrays;
import karuberu.core.util.KaruberuLogger;
import karuberu.dynamicearth.fluids.FluidColorRegistry;
import karuberu.core.util.FluidHelper.FluidReference;
import karuberu.dynamicearth.items.ItemBombLit;
import karuberu.dynamicearth.items.ItemVase;
import karuberu.dynamicearth.items.crafting.RecipeBombs;
import karuberu.dynamicearth.items.crafting.RecipeManager;
import karuberu.dynamicearth.world.WorldGenDynamicEarth;
import karuberu.dynamicearth.world.WorldGenPeat;

public class ConfigurationManager extends karuberu.core.util.ConfigurationManager {
				
	public ConfigurationManager(KaruberuLogger logger) {
		super(logger);
	}

	public void configCrafting() {
		final String category = "Crafting";
		RecipeManager.canCraftBombs = this.get(category,
			"canCraftBombs",
			true
		);
		RecipeManager.canCraftMossyStone = this.get(category,
			"canCraftMossyStone",
			true,
			"Enables the crafting of moss stone and mossy stone bricks with peat moss."
		);
	}
	
	public void configFeatures() {
		final String category = "Features";
		DynamicEarth.enableBottomSlabGrassKilling = this.get(category,
			"enableBottomSlabGrassKilling",
			true,
			"When this setting is enabled, grass blocks will become dirt blocks if they" +
			"are covered by a bottom-oriented half-slab as if it were a full block."
		);
		DynamicEarth.enableDeepMud = this.get(category,
			"enableDeepMud",
			true,
			"When this setting is enabled, walking in hydrated mud will slow entities."
		);
		DynamicEarth.enableDeepPeat = this.get(category,
			"enableDeepPeat",
			true,
			"When this setting is enabled, walking in hydrated peat will slow entities."
		);
		DynamicEarth.enableEndermanBlockDrops = this.get(category,
			"enableEndermanBlockDrops",
			true,
			"When this setting is enabled, endermen will drop the blocks they are " +
			"carrying when they are killed."
		);
		DynamicEarth.enableFancyMudslides = this.get(category,
			"enableFancyMudslides",
			true,
			"When this setting is enabled, mud will not only fall directly down, but " +
			"will fall to the sides as well, causing any steep mountains covered in " +
			"dirt to slowly erode."
		);
		DynamicEarth.enableGrassBurning = this.get(category,
			"enableGrassBurning",
			true,
			"When this setting is enabled, grass blocks will become dirt blocks if set " +
			"on fire."
		);
		DynamicEarth.enableMoreDestructiveMudslides = this.get(category,
			"enableMoreDestructiveMudslides",
			true,
			"When this setting is enabled, mudslides have a chance to bring other" +
			"blocks, such as stone and dirt, down with them when they fall."
		);
		DynamicEarth.enableMoreDestructiveRain = this.get(category,
			"enableMoreDestructiveRain",
			false,
			"When this setting is enabled, rain will soak through more layers of dirt," +
			"thus causing more mudslides to occur."
		);
		DynamicEarth.enableMudslideBlockPreservation = this.get(category, 
			"enableMudslideBlockPreservation", 
			true,
			"When this setting is enabled, mud blocks that are broken during a mud slide" +
			"will produce mud blobs that turn into mud layers or mud blocks when they" +
			"expire instead of simply disappearing like normal items."
		);
		DynamicEarth.enableMyceliumTilling = this.get(category,
			"enableMyceliumTilling", 
			false, 
			"Allows mycelium blocks to be tilled (a feature not present in vanilla " +
			"Minecraft)."
		);
		DynamicEarth.enableThrownMudLayers = this.get(category, 
			"enableThrownMudLayers", 
			false,
			"When this setting is enabled, throwing mud balls will create mud layers" +
			"when they hit the ground."
		);
		DynamicEarth.enableUnderwaterMudslides = this.get(category,
			"enableUnderwaterMudslides",
			false,
			"When this setting is enabled, mudslides can occur underwater. This happens" +
			"frequently when a chunk is loaded and can be very rough on a slow computer" +
			"or on a server. Because of this, it is disabled by default."
		);
		DynamicEarth.includeAdobe = this.get(category,
			"includeAdobe", 
			true
		);
		DynamicEarth.includeBombs = this.get(category,
			"includeBombs", 
			true
		);
		DynamicEarth.includeAdobeGolems = this.get(category,
			"includeClayGolems", 
			true
		);
		DynamicEarth.includeBurningSoil = this.get(category,
			"includeBurningSoil", 
			true
		);
		DynamicEarth.includeDirtSlabs = this.get(category,
			"includeDirtSlabs", 
			true
		);
		DynamicEarth.includeFertileSoil = this.get(category,
			"includeFertileSoil", 
			true
		);
		DynamicEarth.includeGlowingSoil = this.get(category,
			"includeGlowingSoil", 
			true
		);
		DynamicEarth.includeMud = this.get(category,
			"includeMud", 
			true
		);
		DynamicEarth.includeMudBrick = this.get(category,
			"includeMudBrick", 
			true
		);
		DynamicEarth.includeMudLayers = this.get(category,
			"includeMudLayers",
			true
		);
		DynamicEarth.includeNetherGrass = this.get(category,
			"includeNetherGrass", 
			true
		);
		DynamicEarth.includePeat = this.get(category,
			"includePeat", 
			true
		);
		DynamicEarth.includePermafrost = this.get(category,
			"includePermafrost", 
			true
		);
		DynamicEarth.includeSandySoil = this.get(category,
			"includeSandySoil", 
			true
		);
		DynamicEarth.showSnowyBottomSlabs = this.get(category,
			"showSnowyBottomSlabs", 
			true, 
			"When this setting is enabled, bottom grass slabs will show a snowy texture " +
			"if snow or a snow block is directly adjacent."
		);
		DynamicEarth.useCustomCreativeTab = this.get(category,
			"useCustomCreativeTab", 
			true, 
			"Use Dynamic Earth's creative tab. If this setting is disabled, the items " +
			"will be sorted into various vanilla creative tabs."
		);
	}
	
	public void configMaintenance() {
		final String category = "Maintenance";
		DynamicEarth.restoreDirtOnChunkLoad = this.get(category,
			"restoreDirtOnChunkLoad", 
			false, 
			"If this setting is enabled, all earth blocks from Dynamic Earth will be " +
			"replaced with their respective vanilla counterparts (mud -> dirt, fertile " +
			"soil -> grass, etc) and prevents the blocks from reforming. The swap is " +
			"made when a chunk is loaded, so you may need to run around your world to " +
			"make sure all areas are covered. This is to assist in uninstalling the mod."
		);
	}
	
	public void configWorldGen() {
		final String category = "Terrain_Generation";
		WorldGenDynamicEarth.doGenerateMud = this.get(category,
			"doGenerateMud", 
			true
		);
		WorldGenDynamicEarth.doGeneratePermafrost = this.get(category,
			"doGeneratePermafrost", 
			true
		);
		WorldGenDynamicEarth.doGeneratePeat = this.get(category,
			"doGeneratePeat", 
			true
		);
		WorldGenDynamicEarth.doGenerateFertileGrass = this.get(category,
			"doGenerateFertileGrass", 
			true
		);
		WorldGenDynamicEarth.doGenerateSandyGrass = this.get(category,
			"doGenerateSandyGrass", 
			true
		);
		WorldGenDynamicEarth.enableDiverseGeneration = this.get(category,
			"enableDiverseGeneration",
			false,
			"Enables peat to generate in non-vanilla swampland biomes."
		);
		WorldGenPeat.maximumGenHeight = this.get(category,
			"maximumPeatGenerationHeight",
			WorldGenPeat.maximumGenHeight,
			"Sets the maximum y value at which peat bogs will generate."
		);
		WorldGenPeat.minimumGenHeight = this.get(category,
			"minimumPeatGenerationHeight",
			WorldGenPeat.minimumGenHeight,
			"Sets the minimum y value at which peat bogs will generate."
		);
		WorldGenPeat.rarity = this.get(category,
			"peatBogRarity",
			WorldGenPeat.rarity,
			"Controls how rare or how common peat bogs are in a newly-generated world."
		);
	}
	
	public void configAdjustments() {
		final String category = "Adjustments";
		FuelHandler.peatBurnTime = this.get(category, 
			"peatBurnTime", 
			FuelHandler.peatBurnTime, 
			"The amount of time peat will burn for in a furnace. By default, it is " +
			"enough to cook 6 items as opposed to coal's 8."
		);
		RecipeBombs.maxGunpowder = this.get(category, 
			"maxGunpowderForBombs", 
			RecipeBombs.maxGunpowder
		);
		ItemBombLit.maxFuseLength = this.get(category, 
			"maxFuseLengthForBombs", 
			ItemBombLit.maxFuseLength
		);
		ItemVase.showMeasurement = this.get(category, 
			"showVaseMeasurements", 
			true
		);
		ItemVase.blacklist = Arrays.asList(this.get(category, 
			"vaseBlacklist", 
			new String[] {}, 
			"Used to ban liquids from being contained in the vase. Entries should be " +
			"in all lowercase letters."
		));
		ItemVase.whitelist = Arrays.asList(this.get(category,
			"vaseWhitelist",
			new String[] {}, 
			"Used to prevent liquids from being automatically banned from the vase. " +
			"Entries should be in all lowercase letters."
		));
		FluidColorRegistry.setColorList(this.get(category,
			"fluidColors",
			FluidReference.getConfig(),
			"Sets the icon color for fluids contained in vases. If a fluid doesn't " +
			"appear here and the fluid isn't registered with a color, it will default" +
			"to using the \"sealed vase\" icon. It should be noted that gaseous fluids " +
			"will always used the sealed vase icon, even if they appear here."
		));
		DynamicEarth.useAdjustedBottleVolume = this.get(category,
			"useAdjustedBottleVolume",
			true,
			"Sets the bottle volume to 250 mB instead of the Forge default of 1000 mB. " +
			"This more accurately represents how much water a bottle contains in " +
			"vanilla Minecraft (a cauldron fills three bottles and is filled by one " +
			"bucket; 250 is rounded down for ease of use). However, it may cause " +
			"issues in mods that don't account for a modification such as this."		
		);
		DynamicEarth.useSimpleHydration = this.get(category, 
			"useSimpleHydration", 
			false,
			"Ignore biome humidity and other factors when calculating if a block " +
			"should become/stay hydrated. Enabling this may provide a slight " +
			"performance increase."
		);
	}
}
