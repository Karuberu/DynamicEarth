package karuberu.dynamicearth;

import java.io.File;
import java.util.Arrays;
import karuberu.dynamicearth.fluids.FluidHelper;
import karuberu.dynamicearth.fluids.FluidHelper.FluidReference;
import karuberu.dynamicearth.items.ItemBombLit;
import karuberu.dynamicearth.items.ItemVase;
import karuberu.dynamicearth.items.crafting.RecipeBombs;
import karuberu.dynamicearth.items.crafting.RecipeManager;
import karuberu.dynamicearth.world.WorldGenDynamicEarth;
import karuberu.dynamicearth.world.WorldGenPeat;
import net.minecraftforge.common.Configuration;

public class ConfigurationManager {
	
	private static Configuration
		config;
	
	public static void setConfigurationFile(File configurationFile) {
		if (config != null && config.hasChanged()) {
			config.save();
		}
		config = new Configuration(configurationFile);
		config.load();
	}
	
	public static void closeConfig() {
		if (config.hasChanged()) {
			config.save();
		}
	}
		
	public static void configCrafting() {
		final String category = "Crafting";
		RecipeManager.canCraftBombs = ConfigurationManager.get(category,
			"canCraftBombs",
			true
		);
		RecipeManager.canCraftMossyStone = ConfigurationManager.get(category,
			"canCraftMossyStone",
			true,
			"Enables the crafting of moss stone and mossy stone bricks with peat moss."
		);
	}
	
	public static void configFeatures() {
		final String category = "Features";
		DynamicEarth.enableBottomSlabGrassKilling = ConfigurationManager.get(category,
			"enableBottomSlabGrassKilling",
			true,
			"When this setting is enabled, grass blocks will become dirt blocks if they" +
			"are covered by a bottom-oriented half-slab as if it were a full block."
		);
		DynamicEarth.enableDeepMud = ConfigurationManager.get(category,
			"enableDeepMud",
			true,
			"When this setting is enabled, walking in hydrated mud will slow entities."
		);
		DynamicEarth.enableDeepPeat = ConfigurationManager.get(category,
			"enableDeepPeat",
			true,
			"When this setting is enabled, walking in hydrated peat will slow entities."
		);
		DynamicEarth.enableEndermanBlockDrops = ConfigurationManager.get(category,
			"enableEndermanBlockDrops",
			true,
			"When this setting is enabled, endermen will drop the blocks they are " +
			"carrying when they are killed."
		);
		DynamicEarth.enableFancyMudslides = ConfigurationManager.get(category,
			"enableFancyMudslides",
			true,
			"When this setting is enabled, mud will not only fall directly down, but " +
			"will fall to the sides as well, causing any steep mountains covered in " +
			"dirt to slowly erode."
		);
		DynamicEarth.enableGrassBurning = ConfigurationManager.get(category,
			"enableGrassBurning",
			true,
			"When this setting is enabled, grass blocks will become dirt blocks if set " +
			"on fire."
		);
		DynamicEarth.enableMoreDestructiveMudslides = ConfigurationManager.get(category,
			"enableMoreDestructiveMudslides",
			true,
			"When this setting is enabled, mudslides have a chance to bring other" +
			"blocks, such as stone and dirt, down with them when they fall."
		);
		DynamicEarth.enableMoreDestructiveRain = ConfigurationManager.get(category,
			"enableMoreDestructiveRain",
			false,
			"When this setting is enabled, rain will soak through more layers of dirt," +
			"thus causing more mudslides to occur."
		);
		DynamicEarth.enableMudslideBlockPreservation = ConfigurationManager.get(category, 
			"enableMudslideBlockPreservation", 
			true,
			"When this setting is enabled, mud blocks that are broken during a mud slide" +
			"will produce mud blobs that turn into mud layers or mud blocks when they" +
			"expire instead of simply disappearing like normal items."
		);
		DynamicEarth.enableMyceliumTilling = ConfigurationManager.get(category,
			"enableMyceliumTilling", 
			false, 
			"Allows mycelium blocks to be tilled (a feature not present in vanilla " +
			"Minecraft)."
		);
		DynamicEarth.enableThrownMudLayers = ConfigurationManager.get(category, 
			"enableThrownMudLayers", 
			false,
			"When this setting is enabled, throwing mud balls will create mud layers" +
			"when they hit the ground."
		);
		DynamicEarth.enableUnderwaterMudslides = ConfigurationManager.get(category,
			"enableUnderwaterMudslides",
			false,
			"When this setting is enabled, mudslides can occur underwater. This happens" +
			"frequently when a chunk is loaded and can be very rough on a slow computer" +
			"or on a server. Because of this, it is disabled by default."
		);
		DynamicEarth.includeAdobe = ConfigurationManager.get(category,
			"includeAdobe", 
			true
		);
		DynamicEarth.includeBombs = ConfigurationManager.get(category,
			"includeBombs", 
			true
		);
		DynamicEarth.includeAdobeGolems = ConfigurationManager.get(category,
			"includeClayGolems", 
			true
		);
		DynamicEarth.includeBurningSoil = ConfigurationManager.get(category,
			"includeBurningSoil", 
			true
		);
		DynamicEarth.includeDirtSlabs = ConfigurationManager.get(category,
			"includeDirtSlabs", 
			true
		);
		DynamicEarth.includeFertileSoil = ConfigurationManager.get(category,
			"includeFertileSoil", 
			true
		);
		DynamicEarth.includeGlowingSoil = ConfigurationManager.get(category,
			"includeGlowingSoil", 
			true
		);
		DynamicEarth.includeMud = ConfigurationManager.get(category,
			"includeMud", 
			true
		);
		DynamicEarth.includeMudBrick = ConfigurationManager.get(category,
			"includeMudBrick", 
			true
		);
		DynamicEarth.includeMudLayers = ConfigurationManager.get(category,
			"includeMudLayers",
			true
		);
		DynamicEarth.includeNetherGrass = ConfigurationManager.get(category,
			"includeNetherGrass", 
			true
		);
		DynamicEarth.includePeat = ConfigurationManager.get(category,
			"includePeat", 
			true
		);
		DynamicEarth.includePermafrost = ConfigurationManager.get(category,
			"includePermafrost", 
			true
		);
		DynamicEarth.includeSandySoil = ConfigurationManager.get(category,
			"includeSandySoil", 
			true
		);
		DynamicEarth.showSnowyBottomSlabs = ConfigurationManager.get(category,
			"showSnowyBottomSlabs", 
			true, 
			"When this setting is enabled, bottom grass slabs will show a snowy texture " +
			"if snow or a snow block is directly adjacent."
		);
		DynamicEarth.useCustomCreativeTab = ConfigurationManager.get(category,
			"useCustomCreativeTab", 
			true, 
			"Use Dynamic Earth's creative tab. If this setting is disabled, the items " +
			"will be sorted into various vanilla creative tabs."
		);
	}
	
	public static void configMaintenance() {
		final String category = "Maintenance";
		DynamicEarth.restoreDirtOnChunkLoad = ConfigurationManager.get(category,
			"restoreDirtOnChunkLoad", 
			false, 
			"If this setting is enabled, all earth blocks from Dynamic Earth will be " +
			"replaced with their respective vanilla counterparts (mud -> dirt, fertile " +
			"soil -> grass, etc) and prevents the blocks from reforming. The swap is " +
			"made when a chunk is loaded, so you may need to run around your world to " +
			"make sure all areas are covered. This is to assist in uninstalling the mod."
		);
	}
	
	public static void configWorldGen() {
		final String category = "Terrain_Generation";
		WorldGenDynamicEarth.doGenerateMud = ConfigurationManager.get(category,
			"doGenerateMud", 
			true
		);
		WorldGenDynamicEarth.doGeneratePermafrost = ConfigurationManager.get(category,
			"doGeneratePermafrost", 
			true
		);
		WorldGenDynamicEarth.doGeneratePeat = ConfigurationManager.get(category,
			"doGeneratePeat", 
			true
		);
		WorldGenDynamicEarth.doGenerateFertileGrass = ConfigurationManager.get(category,
			"doGenerateFertileGrass", 
			true
		);
		WorldGenDynamicEarth.doGenerateSandyGrass = ConfigurationManager.get(category,
			"doGenerateSandyGrass", 
			true
		);
		WorldGenDynamicEarth.enableDiverseGeneration = ConfigurationManager.get(category,
			"enableDiverseGeneration",
			false,
			"Enables peat to generate in non-vanilla swampland biomes."
		);
		WorldGenPeat.maximumGenHeight = ConfigurationManager.get(category,
			"maximumPeatGenerationHeight",
			WorldGenPeat.maximumGenHeight,
			"Sets the maximum y value at which peat bogs will generate."
		);
		WorldGenPeat.minimumGenHeight = ConfigurationManager.get(category,
			"minimumPeatGenerationHeight",
			WorldGenPeat.minimumGenHeight,
			"Sets the minimum y value at which peat bogs will generate."
		);
		WorldGenPeat.rarity = ConfigurationManager.get(category,
			"peatBogRarity",
			WorldGenPeat.rarity,
			"Controls how rare or how common peat bogs are in a newly-generated world."
		);
	}
	
	public static void configAdjustments() {
		final String category = "Adjustments";
		FuelHandler.peatBurnTime = ConfigurationManager.get(category, 
			"peatBurnTime", 
			FuelHandler.peatBurnTime, 
			"The amount of time peat will burn for in a furnace. By default, it is " +
			"enough to cook 6 items as opposed to coal's 8."
		);
		RecipeBombs.maxGunpowder = ConfigurationManager.get(category, 
			"maxGunpowderForBombs", 
			RecipeBombs.maxGunpowder
		);
		ItemBombLit.maxFuseLength = ConfigurationManager.get(category, 
			"maxFuseLengthForBombs", 
			ItemBombLit.maxFuseLength
		);
		ItemVase.showMeasurement = ConfigurationManager.get(category, 
			"showVaseMeasurements", 
			true
		);
		ItemVase.blacklist = Arrays.asList(ConfigurationManager.get(category, 
			"vaseBlacklist", 
			new String[] {}, 
			"Used to ban liquids from being contained in the vase. Entries should be " +
			"in all lowercase letters."
		));
		ItemVase.whitelist = Arrays.asList(ConfigurationManager.get(category,
			"vaseWhitelist",
			new String[] {}, 
			"Used to prevent liquids from being automatically banned from the vase. " +
			"Entries should be in all lowercase letters."
		));
		FluidHelper.setColorList(ConfigurationManager.get(category,
			"fluidColors",
			FluidReference.getConfig(),
			"Sets the icon color for fluids contained in vases. If a fluid doesn't " +
			"appear here and the fluid isn't registered with a color, it will default" +
			"to using the \"sealed vase\" icon. It should be noted that gaseous fluids " +
			"will always used the sealed vase icon, even if they appear here."
		));
		DynamicEarth.useAdjustedBottleVolume = ConfigurationManager.get(category,
			"useAdjustedBottleVolume",
			true,
			"Sets the bottle volume to 250 mB instead of the Forge default of 1000 mB. " +
			"This more accurately represents how much water a bottle contains in " +
			"vanilla Minecraft (a cauldron fills three bottles and is filled by one " +
			"bucket; 250 is rounded down for ease of use). However, it may cause " +
			"issues in mods that don't account for a modification such as this."		
		);
		DynamicEarth.useSimpleHydration = ConfigurationManager.get(category, 
			"useSimpleHydration", 
			false,
			"Ignore biome humidity and other factors when calculating if a block " +
			"should become/stay hydrated. Enabling this may provide a slight " +
			"performance increase."
		);
	}
	
	public static int getBlockID(String key, int defaultValue) {
		return config.getBlock(key, defaultValue).getInt();
	}
	
	public static int getItemID(String key, int defaultValue) {
		return config.getItem(key, defaultValue).getInt();		
	}
	
	public static boolean get(String category, String key, boolean defaultValue) {
		return config.get(category, key, defaultValue).getBoolean(defaultValue);
	}
	
	public static boolean get(String category, String key, boolean defaultValue, String comment) {
		return config.get(category, key, defaultValue, comment).getBoolean(defaultValue);
	}
	
	public static int get(String category, String key, int defaultValue) {
		return config.get(category, key, defaultValue).getInt();
	}
	
	public static int get(String category, String key, int defaultValue, String comment) {
		return config.get(category, key, defaultValue, comment).getInt();
	}
	
	public static String[] get(String category, String key, String[] defaultValue, String comment) {
		return config.get(category, key, defaultValue, comment).getStringList();
	}
}
