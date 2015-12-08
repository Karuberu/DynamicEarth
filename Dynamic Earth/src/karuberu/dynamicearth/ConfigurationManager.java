package karuberu.dynamicearth;

import java.util.Arrays;
import karuberu.dynamicearth.fluids.FluidHelper;
import karuberu.dynamicearth.fluids.FluidHelper.FluidReference;
import karuberu.dynamicearth.items.ItemBombLit;
import karuberu.dynamicearth.items.ItemVase;
import karuberu.dynamicearth.items.crafting.RecipeBombs;
import karuberu.dynamicearth.items.crafting.RecipeManager;
import karuberu.dynamicearth.world.WorldGenMudMod;
import net.minecraftforge.common.Configuration;

public class ConfigurationManager {
	
	private static Configuration
		config;
	
	public static void setConfigurationFile(Configuration configuration) {
		if (config != null && config.hasChanged()) {
			config.save();
		}
		config = configuration;
		config.load();
	}
	
	public static void closeConfig() {
		if (config.hasChanged()) {
			config.save();
		}
	}
		
	public static void configCrafting() {
		RecipeManager.canCraftBombs = ConfigurationManager.get(
			"Crafting",
			"canCraftBombs",
			true
		);
		RecipeManager.canCraftMossyStone = ConfigurationManager.get(
			"Crafting",
			"canCraftMossyStone",
			true,
			"Enables the crafting of moss stone and mossy stone bricks with peat moss."
		);
	}
	
	public static void configFeatures() {
		DynamicEarth.enableDeepMud = ConfigurationManager.get(
			"Features",
			"enableDeepMud",
			true,
			"When this setting is enabled, walking in hydrated mud will slow entities."
		);
		DynamicEarth.enableDeepPeat = ConfigurationManager.get(
			"Features",
			"enableDeepPeat",
			true,
			"When this setting is enabled, walking in hydrated peat will slow entities."
		);
		DynamicEarth.enableBottomSlabGrassKilling = ConfigurationManager.get(
			"Features",
			"enableBottomSlabGrassKilling",
			true,
			"When this setting is enabled, grass blocks will become dirt blocks if they" +
			"are covered by a bottom-oriented half-slab as if it were a full block."
		);
		DynamicEarth.enableGrassBurning = ConfigurationManager.get(
			"Features",
			"enableGrassBurning",
			true,
			"When this setting is enabled, grass blocks will become dirt blocks if set " +
			"on fire."
		);
		DynamicEarth.enableMoreDestructiveMudslides = ConfigurationManager.get(
			"Features",
			"enableMoreDestructiveMudslides",
			false,
			"When this setting is enabled, mudslides that occur during rainstorms may " +
			"turn a random selection of surrounding dirt blocks into mud, greatly increasing " +
			"the destructive power of a single mudslide."
		);
		DynamicEarth.enableMyceliumTilling = ConfigurationManager.get(
			"Features", 
			"enableMyceliumTilling", 
			false, 
			"Allows mycelium blocks to be tilled (a feature not present in vanilla " +
			"Minecraft)."
		);
		DynamicEarth.includeAdobe = ConfigurationManager.get(
			"Features", 
			"includeAdobe", 
			true
		);
		DynamicEarth.includeBombs = ConfigurationManager.get(
			"Features", 
			"includeBombs", 
			true
		);
		DynamicEarth.includeAdobeGolems = ConfigurationManager.get(
			"Features", 
			"includeClayGolems", 
			true
		);
		DynamicEarth.includeDirtSlabs = ConfigurationManager.get(
			"Features", 
			"includeDirtSlabs", 
			true
		);
		DynamicEarth.includeFertileSoil = ConfigurationManager.get(
			"Features", 
			"includeFertileSoil", 
			true
		);
		DynamicEarth.includeMudBrick = ConfigurationManager.get(
			"Features", 
			"includeMudBrick", 
			true
		);
		DynamicEarth.includePeat = ConfigurationManager.get(
			"Features", 
			"includePeat", 
			true
		);
		DynamicEarth.includePermafrost = ConfigurationManager.get(
			"Features", 
			"includePermafrost", 
			true
		);
		DynamicEarth.includeSandySoil = ConfigurationManager.get(
			"Features", 
			"includeSandySoil", 
			true
		);
		DynamicEarth.showSnowyBottomSlabs = ConfigurationManager.get(
			"Features", 
			"showSnowyBottomSlabs", 
			true, 
			"When this setting is enabled, bottom grass slabs will show a snowy texture " +
			"if snow or a snow block is directly adjacent."
		);
		DynamicEarth.useCustomCreativeTab = ConfigurationManager.get(
			"Features", 
			"useCustomCreativeTab", 
			true, 
			"Use Dynamic Earth's creative tab. If this setting is disabled, the items " +
			"will be sorted into various vanilla creative tabs."
		);
	}
	
	public static void configMaintenance() {
		DynamicEarth.restoreDirtOnChunkLoad = ConfigurationManager.get(
			"Maintenance", 
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
		WorldGenMudMod.doGenerateMud = ConfigurationManager.get(
			"Terrain Generation", 
			"doGenerateMud", 
			true
		);
		WorldGenMudMod.doGeneratePermafrost = ConfigurationManager.get(
			"Terrain Generation", 
			"doGeneratePermafrost", 
			true
		);
		WorldGenMudMod.doGeneratePeat = ConfigurationManager.get(
			"Terrain Generation", 
			"doGeneratePeat", 
			true
		);
	}
	
	public static void configAdjustments() {
		FuelHandler.peatBurnTime = ConfigurationManager.get(
			"Adjustments", 
			"peatBurnTime", 
			FuelHandler.peatBurnTime, 
			"The amount of time peat will burn for in a furnace. By default, it is " +
			"enough to cook 6 items as opposed to coal's 8."
		);
		RecipeBombs.maxGunpowder = ConfigurationManager.get(
			"Adjustments", 
			"maxGunpowderForBombs", 
			RecipeBombs.maxGunpowder
		);
		ItemBombLit.maxFuseLength = ConfigurationManager.get(
			"Adjustments", 
			"maxFuseLengthForBombs", 
			ItemBombLit.maxFuseLength
		);
		ItemVase.showMeasurement = ConfigurationManager.get(
			"Adjustments", 
			"showVaseMeasurements", 
			true
		);
		ItemVase.blacklist = Arrays.asList(ConfigurationManager.get(
			"Adjustments", 
			"vaseBlacklist", 
			new String[] {}, 
			"Used to ban liquids from being contained in the vase. Entries should be " +
			"in all lowercase letters."
		));
		ItemVase.whitelist = Arrays.asList(ConfigurationManager.get(
			"Adjustments",
			"vaseWhitelist",
			new String[] {}, 
			"Used to prevent liquids from being automatically banned from the vase. " +
			"Entries should be in all lowercase letters."
		));
		FluidHelper.setColorList(ConfigurationManager.get(
			"Adjustments",
			"fluidColors",
			FluidReference.getConfig(),
			"Sets the icon color for fluids contained in vases. If a fluid doesn't " +
			"appear here and the fluid isn't registered with a color, it will default" +
			"to using the \"sealed vase\" icon. It should be noted that gaseous fluids " +
			"will always used the sealed vase icon, even if they appear here."
		));
		DynamicEarth.useSimpleHydration = ConfigurationManager.get(
			"Adjustments", 
			"useSimpleHydration", 
			false, 
			"Ignore biome humidity and other factors when calculating if a block " +
			"should become/stay hydrated. Enabling this may provide a slight " +
			"performance increase."
		);
	}
	
	public static void configModHandling() {
		ModHandler.useForestryPeat = ConfigurationManager.get(
			"Forestry", 
			"useForestryPeat", 
			false, 
			"If this setting is enabled and Forestry is present, Dynamic Earth will " +
			"use Forestry's peat item instead of creating a new one."
		);
		FuelHandler.peatForestryBurnTime = ConfigurationManager.get(
			"Forestry", 
			"peatBurnTime", 
			FuelHandler.peatForestryBurnTime, 
			"The amount of time Dynamic Earth's peat will burn in a Peat-fired Engine " +
			"from Forestry. By default, this is lower than Forestry's peat."
		);
	}
	
	public static int getBlockID(String key, int defaultValue) {
		return config.getBlock(key, defaultValue).getInt();
	}
	
	public static int getItemID(String key, int defaultValue) {
		return config.getItem(key, defaultValue).getInt();		
	}
	
	private static boolean get(String category, String key, boolean defaultValue) {
		return config.get(category, key, defaultValue).getBoolean(defaultValue);
	}
	
	private static boolean get(String category, String key, boolean defaultValue, String comment) {
		return config.get(category, key, defaultValue, comment).getBoolean(defaultValue);
	}
	
	private static int get(String category, String key, int defaultValue) {
		return config.get(category, key, defaultValue).getInt();
	}
	
	private static int get(String category, String key, int defaultValue, String comment) {
		return config.get(category, key, defaultValue, comment).getInt();
	}
	
	private static String[] get(String category, String key, String[] defaultValue, String comment) {
		return config.get(category, key, defaultValue, comment).getStringList();
	}
}
