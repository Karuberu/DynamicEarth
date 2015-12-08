package karuberu.dynamicearth;

import karuberu.dynamicearth.blocks.BlockAdobe;
import karuberu.dynamicearth.blocks.BlockAdobeSlab;
import karuberu.dynamicearth.blocks.BlockAdobeStairs;
import karuberu.dynamicearth.blocks.BlockAdobeWet;
import karuberu.dynamicearth.blocks.BlockDirtSlab;
import karuberu.dynamicearth.blocks.BlockFertileSoil;
import karuberu.dynamicearth.blocks.BlockGrassSlab;
import karuberu.dynamicearth.blocks.BlockMud;
import karuberu.dynamicearth.blocks.BlockMudBrick;
import karuberu.dynamicearth.blocks.BlockMudBrickWall;
import karuberu.dynamicearth.blocks.BlockDynamicFarmland;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.blocks.BlockPeatMoss;
import karuberu.dynamicearth.blocks.BlockPermafrost;
import karuberu.dynamicearth.blocks.BlockSandySoil;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import karuberu.dynamicearth.entity.EntityAdobeGolem;
import karuberu.dynamicearth.entity.EntityBomb;
import karuberu.dynamicearth.entity.EntityFallingBlock;
import karuberu.dynamicearth.entity.EntityMudball;
import karuberu.dynamicearth.fluids.FluidHandler;
import karuberu.dynamicearth.items.BehaviorBombDispense;
import karuberu.dynamicearth.items.BehaviorMudballDispense;
import karuberu.dynamicearth.items.BehaviorVaseDispense;
import karuberu.dynamicearth.items.ItemAdobeSlab;
import karuberu.dynamicearth.items.ItemBlockFertileSoil;
import karuberu.dynamicearth.items.ItemBlockMud;
import karuberu.dynamicearth.items.ItemBlockPeat;
import karuberu.dynamicearth.items.ItemBlockSandySoil;
import karuberu.dynamicearth.items.ItemBomb;
import karuberu.dynamicearth.items.ItemBombLit;
import karuberu.dynamicearth.items.ItemClump;
import karuberu.dynamicearth.items.ItemDirtSlab;
import karuberu.dynamicearth.items.ItemEarthbowlSoup;
import karuberu.dynamicearth.items.ItemGrassSlab;
import karuberu.dynamicearth.items.ItemLiquid;
import karuberu.dynamicearth.items.ItemMudMod;
import karuberu.dynamicearth.items.ItemPeatMossSpecimen;
import karuberu.dynamicearth.items.ItemVase;
import karuberu.dynamicearth.items.crafting.CraftingHandler;
import karuberu.dynamicearth.items.crafting.RecipeBombs;
import karuberu.dynamicearth.items.crafting.RecipeManager;
import karuberu.dynamicearth.world.WorldGenMudMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
	useMetadata=true,
	modid = "DynamicEarth",
	name = "Dynamic Earth",
	version = "1.7.0",
	dependencies = "required-after:Forge; after:Thaumcraft; after:Forestry; after:ThermalExpansion")
//	dependencies = "required-after:Forge; required-after:KaruberuCore; after:Thaumcraft; after:Forestry; after:ThermalExpansion")
@NetworkMod(
	clientSideRequired = true,
	serverSideRequired = false
)
public class DynamicEarth {
	@Mod.Instance("DynamicEarth")
	public static DynamicEarth instance;
	public static Block
    	mud,
    	permafrost,
    	adobeWet,
     	adobe,
    	blockMudBrick,
    	adobeStairs,
    	mudBrickStairs,
    	mudBrickWall,
    	peatMoss,
    	peat,
    	farmland,
    	fertileSoil,
    	sandySoil;
	public static BlockHalfSlab
		adobeSingleSlab,
		adobeDoubleSlab,
		dirtSlab,
		dirtDoubleSlab,
		grassSlab,
		grassDoubleSlab;
	public static Item
    	mudBrick,
    	adobeSingleSlabItem,
    	adobeDoubleSlabItem,
    	vaseRaw,
    	earthbowl,
    	earthbowlRaw,
    	earthbowlSoup,
    	bomb,
    	bombLit,
    	peatBrick,
    	peatMossSpecimen,
    	liquidMilk,
    	liquidSoup;
	public static ItemClump
		dirtClod,
		mudBlob,
		adobeDust,
		adobeBlob,
		peatClump;
	public static ItemVase
		vase;
	public static int
		overlayBlockRenderID,
		peatMossRenderID;
	private static int
		BLOCKID_MUD					= 4000,
		BLOCKID_PERMAFROST			= BLOCKID_MUD+1,
		BLOCKID_ADOBEWET			= BLOCKID_PERMAFROST+1,
		BLOCKID_ADOBE				= BLOCKID_ADOBEWET+1,
		BLOCKID_MUDBRICKBLOCK		= BLOCKID_ADOBE+1,
		BLOCKID_ADOBEDOUBLESLAB		= BLOCKID_MUDBRICKBLOCK+1,
		BLOCKID_ADOBESINGLESLAB		= BLOCKID_ADOBEDOUBLESLAB+1,
		BLOCKID_ADOBESTAIRS			= BLOCKID_ADOBESINGLESLAB+1,
		BLOCKID_MUDBRICKSTAIRS		= BLOCKID_ADOBESTAIRS+1,
		BLOCKID_MUDBRICKWALL		= BLOCKID_MUDBRICKSTAIRS+1,
		BLOCKID_DIRTSLAB			= BLOCKID_MUDBRICKWALL+1,
		BLOCKID_DIRTDOUBLESLAB		= BLOCKID_DIRTSLAB+1,
		BLOCKID_GRASSSLAB			= BLOCKID_DIRTDOUBLESLAB+1,
		BLOCKID_GRASSDOUBLESLAB		= BLOCKID_GRASSSLAB+1,
		BLOCKID_PEATMOSS			= BLOCKID_GRASSDOUBLESLAB+1,
		BLOCKID_PEAT				= BLOCKID_PEATMOSS+1,
		BLOCKID_FARMLAND			= BLOCKID_PEAT+1,
		BLOCKID_FERTILESOIL			= BLOCKID_FARMLAND+1,
		BLOCKID_SANDYSOIL			= BLOCKID_FERTILESOIL+1,
		ITEMID_DIRTCLOD				= 10000,
		ITEMID_MUDBLOB				= ITEMID_DIRTCLOD+1,
		ITEMID_MUDBRICK				= ITEMID_MUDBLOB+1,
		ITEMID_ADOBEDUST			= ITEMID_MUDBRICK+1,
		ITEMID_ADOBEBLOB			= ITEMID_ADOBEDUST+1,
		ITEMID_VASERAW				= ITEMID_ADOBEBLOB+1,
		ITEMID_VASE					= ITEMID_VASERAW+1,
		ITEMID_EARTHBOWLRAW			= ITEMID_VASE+1,
		ITEMID_EARTHBOWL			= ITEMID_EARTHBOWLRAW+1,
		ITEMID_EARTHBOWLSOUP		= ITEMID_EARTHBOWL+1,
		ITEMID_BOMB					= ITEMID_EARTHBOWLSOUP+1,
		ITEMID_BOMBLIT				= ITEMID_BOMB+1,
		ITEMID_PEATMOSSSPECIMEN		= ITEMID_BOMBLIT+1,
		ITEMID_PEATCLUMP			= ITEMID_PEATMOSSSPECIMEN+1,
		ITEMID_PEATBRICK			= ITEMID_PEATCLUMP+1,
		ITEMID_LIQUIDMILK			= ITEMID_PEATBRICK+1,
		ITEMID_LIQUIDSOUP			= ITEMID_LIQUIDMILK+1;
	
	public static boolean
		showSnowyBottomSlabs,
		enableDeepMud,
		enableDeepPeat,
		enableGrassBurning,
		enableMyceliumTilling,
		useSimpleHydration,
		includeAdobe,
		includeBombs,
		includeClayGolems,
		includeDirtSlabs,
		includeFertileSoil,
		includeMudBrick,
		includePeat,
		includePermafrost,
		includeSandySoil,
		restoreDirtOnChunkLoad;
	
	@EventHandler
	public void loadConfiguration(FMLPreInitializationEvent event) {       
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		RecipeManager.canCraftBombs = config.get("Crafting", "canCraftBombs", true).getBoolean(true);
		RecipeManager.canCraftMossyStone = config.get("Crafting", "canCraftMossyStone", true).getBoolean(true);
		DynamicEarth.enableDeepMud = config.get("Features", "enableDeepMud", true).getBoolean(true);
		DynamicEarth.enableDeepPeat = config.get("Features", "enableDeepPeat", true).getBoolean(true);
		DynamicEarth.enableGrassBurning = config.get("Features", "enableGrassBurning", true).getBoolean(true);
		DynamicEarth.enableMyceliumTilling = config.get("Features", "enableMyceliumTilling", false).getBoolean(false);
		DynamicEarth.includeAdobe = config.get("Features", "includeAdobe", true).getBoolean(true);
		DynamicEarth.includeBombs = config.get("Features", "includeBombs", true).getBoolean(true);
		DynamicEarth.includeClayGolems = config.get("Features", "includeClayGolems", true).getBoolean(true);
		DynamicEarth.includeDirtSlabs = config.get("Features", "includeDirtSlabs", true).getBoolean(true);
		DynamicEarth.includeFertileSoil = config.get("Features", "includeFertileSoil", true).getBoolean(true);
		DynamicEarth.includeMudBrick = config.get("Features", "includeMudBrick", true).getBoolean(true);
		DynamicEarth.includePeat = config.get("Features", "includePeat", true).getBoolean(true);
		DynamicEarth.includePermafrost = config.get("Features", "includePermafrost", true).getBoolean(true);
		DynamicEarth.includeSandySoil = config.get("Features", "includeSandySoil", true).getBoolean(true);
		DynamicEarth.showSnowyBottomSlabs = config.get("Features", "showSnowyBottomSlabs", true).getBoolean(true);
		DynamicEarth.restoreDirtOnChunkLoad = config.get("Maintenance", "restoreDirtOnChunkLoad", false).getBoolean(false);
		WorldGenMudMod.doGenerateMud = config.get("Terrain Generation", "doGenerateMud", true).getBoolean(true);
		WorldGenMudMod.doGeneratePermafrost = config.get("Terrain Generation", "doGeneratePermafrost", true).getBoolean(true);
		WorldGenMudMod.doGeneratePeat = config.get("Terrain Generation", "doGeneratePeat", true).getBoolean(true);
		DEFuelHandler.peatBurnTime = config.get("Adjustments", "peatBurnTime", DEFuelHandler.peatBurnTime).getInt();
		RecipeBombs.maxGunpowder = config.get("Adjustments", "maxGunpowderForBombs", RecipeBombs.maxGunpowder).getInt();
		ItemBombLit.maxFuseLength = config.get("Adjustments", "maxFuseLengthForBombs", ItemBombLit.maxFuseLength).getInt();
		ItemVase.showMeasurement = config.get("Adjustments", "showVaseMeasurements", true).getBoolean(true);
		DynamicEarth.useSimpleHydration = config.get("Adjustments", "useSimpleHydration", false).getBoolean(false);
		ModHandler.enableForestryIntegration = config.get("Forestry", "enableIntegration", true).getBoolean(true);
		ModHandler.useForestryPeat = config.get("Forestry", "useForestryPeat", false).getBoolean(false);
		DEFuelHandler.peatForestryBurnTime = config.get("Forestry", "peatBurnTime", DEFuelHandler.peatForestryBurnTime).getInt();
		BLOCKID_MUD				= config.getBlock("Mud", BLOCKID_MUD).getInt();
		BLOCKID_PERMAFROST		= config.getBlock("Permafrost", BLOCKID_PERMAFROST).getInt();
		BLOCKID_ADOBEWET		= config.getBlock("AdobeMoist", BLOCKID_ADOBEWET).getInt();
		BLOCKID_ADOBE			= config.getBlock("Adobe", BLOCKID_ADOBE).getInt();
		BLOCKID_MUDBRICKBLOCK	= config.getBlock("MudBrick", BLOCKID_MUDBRICKBLOCK).getInt();
		BLOCKID_ADOBEDOUBLESLAB	= config.getBlock("AdobeDoubleSlab", BLOCKID_ADOBEDOUBLESLAB).getInt();
		BLOCKID_ADOBESINGLESLAB	= config.getBlock("AdobeSingleSlab", BLOCKID_ADOBESINGLESLAB).getInt();
		BLOCKID_ADOBESTAIRS		= config.getBlock("AdobeStairs", BLOCKID_ADOBESTAIRS).getInt();
		BLOCKID_MUDBRICKSTAIRS	= config.getBlock("MudBrickStairs", BLOCKID_MUDBRICKSTAIRS).getInt();
		BLOCKID_MUDBRICKWALL	= config.getBlock("MudBrickWall", BLOCKID_MUDBRICKWALL).getInt();
		BLOCKID_DIRTSLAB		= config.getBlock("DirtSlab", BLOCKID_DIRTSLAB).getInt();
		BLOCKID_DIRTDOUBLESLAB	= config.getBlock("DirtDoubleSlab", BLOCKID_DIRTDOUBLESLAB).getInt();
		BLOCKID_GRASSSLAB		= config.getBlock("GrassSlab", BLOCKID_GRASSSLAB).getInt();
		BLOCKID_GRASSDOUBLESLAB = config.getBlock("GrassDoubleSlab", BLOCKID_GRASSDOUBLESLAB).getInt();
		BLOCKID_PEATMOSS		= config.getBlock("PeatMoss", BLOCKID_PEATMOSS).getInt();
		BLOCKID_PEAT			= config.getBlock("Peat", BLOCKID_PEAT).getInt();
		BLOCKID_FARMLAND		= config.getBlock("Farmland", BLOCKID_FARMLAND).getInt();
		BLOCKID_FERTILESOIL		= config.getBlock("FertileSoil", BLOCKID_FERTILESOIL).getInt();
		BLOCKID_SANDYSOIL		= config.getBlock("SandySoil", BLOCKID_SANDYSOIL).getInt();
		ITEMID_DIRTCLOD			= config.getItem("DirtClod", ITEMID_DIRTCLOD).getInt();
		ITEMID_MUDBLOB			= config.getItem("MudBlob", ITEMID_MUDBLOB).getInt();
		ITEMID_MUDBRICK			= config.getItem("MudBrick", ITEMID_MUDBRICK).getInt();
		ITEMID_ADOBEDUST		= config.getItem("AdobeDust", ITEMID_ADOBEDUST).getInt();
		ITEMID_ADOBEBLOB		= config.getItem("AdobeBlob", ITEMID_ADOBEBLOB).getInt();
		ITEMID_VASERAW			= config.getItem("VaseRaw", ITEMID_VASERAW).getInt();
		ITEMID_VASE				= config.getItem("Vase", ITEMID_VASE).getInt();
		ITEMID_EARTHBOWLRAW		= config.getItem("EarthbowlRaw", ITEMID_EARTHBOWLRAW).getInt();
		ITEMID_EARTHBOWL		= config.getItem("Earthbowl", ITEMID_EARTHBOWL).getInt();
		ITEMID_EARTHBOWLSOUP	= config.getItem("EarthbowlSoup", ITEMID_EARTHBOWLSOUP).getInt();
		ITEMID_BOMB				= config.getItem("Bomb", ITEMID_BOMB).getInt();
		ITEMID_BOMBLIT			= config.getItem("BombLit", ITEMID_BOMBLIT).getInt();
		ITEMID_PEATCLUMP		= config.getItem("PeatClump", ITEMID_PEATCLUMP).getInt();
		ITEMID_PEATBRICK		= config.getItem("PeatBrick", ITEMID_PEATBRICK).getInt();
		ITEMID_PEATMOSSSPECIMEN	= config.getItem("PeatMossSpecimen", ITEMID_PEATMOSSSPECIMEN).getInt();
		ITEMID_LIQUIDMILK		= config.getItem("LiquidMilk", ITEMID_LIQUIDMILK).getInt();
		ITEMID_LIQUIDSOUP		= config.getItem("LiquidSoup", ITEMID_LIQUIDSOUP).getInt();
		if (config.hasChanged()) {
			config.save();
		}
        DynamicEarth.registerBlocks();
        DynamicEarth.registerItems();
        DynamicEarth.registerEntities();
        FluidHandler.registerLiquids();
	}
	
	@EventHandler
	public void initialize(FMLInitializationEvent event) {
        DynamicEarth.registerDispenserHandlers();
        DynamicEarth.setBlockHarvestLevels();
        RecipeManager.addRecipes();
        RecipeManager.addSmelting();
        CraftingHandler.register();
        WorldGenMudMod.register();
    	DEFuelHandler.register();
        DEEventHandler.register();
        CommonProxy.proxy.registerNames();
        CommonProxy.proxy.registerLocalizations();
        CommonProxy.proxy.registerRenderInformation();
    }
	
	@EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
        ModHandler.integrateMods();
	}
    
    private static void registerBlocks() {
    	Block.dirt.setTickRandomly(true);
        mud = new BlockMud(BLOCKID_MUD);
        farmland = new BlockDynamicFarmland(BLOCKID_FARMLAND);
	    GameRegistry.registerBlock(DynamicEarth.mud, ItemBlockMud.class, "mud");
	    GameRegistry.registerBlock(DynamicEarth.farmland, "farmland");
	    if (DynamicEarth.includeAdobe) {
		    adobe = new BlockAdobe(BLOCKID_ADOBE);
		    adobeWet = new BlockAdobeWet(BLOCKID_ADOBEWET);
		    adobeDoubleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBEDOUBLESLAB, true);
		    adobeSingleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBESINGLESLAB, false);    
		    adobeStairs = (new BlockAdobeStairs(BLOCKID_ADOBESTAIRS, adobe, 0)).setUnlocalizedName("adobeStairs");
	        GameRegistry.registerBlock(DynamicEarth.adobeWet, "adobeWet");
	        GameRegistry.registerBlock(DynamicEarth.adobe, "adobe");
	        GameRegistry.registerBlock(DynamicEarth.adobeSingleSlab, ItemAdobeSlab.class, "adobeSingleSlab");
	        GameRegistry.registerBlock(DynamicEarth.adobeDoubleSlab, ItemAdobeSlab.class, "adobeDoubleSlab");
	        GameRegistry.registerBlock(DynamicEarth.adobeStairs, "adobeStairs");
	    }
	    if (DynamicEarth.includeMudBrick) {
		    blockMudBrick = new BlockMudBrick(BLOCKID_MUDBRICKBLOCK);
		    mudBrickStairs = (new BlockAdobeStairs(BLOCKID_MUDBRICKSTAIRS, blockMudBrick, 0)).setUnlocalizedName("mudBrickStairs");    
		    mudBrickWall = (new BlockMudBrickWall(BLOCKID_MUDBRICKWALL, blockMudBrick)).setUnlocalizedName("mudBrickWall");    
			GameRegistry.registerBlock(DynamicEarth.blockMudBrick, "blockMudBrick");
			GameRegistry.registerBlock(DynamicEarth.mudBrickStairs, "mudBrickStairs");
			GameRegistry.registerBlock(DynamicEarth.mudBrickWall, "mudBrickWall");
	    }
	    if (DynamicEarth.includePermafrost) {
	    	permafrost = new BlockPermafrost(BLOCKID_PERMAFROST);
	        GameRegistry.registerBlock(DynamicEarth.permafrost, "permafrost");
	    }
	    if (DynamicEarth.includeDirtSlabs) {
		    dirtSlab = (BlockHalfSlab) (new BlockDirtSlab(BLOCKID_DIRTSLAB, false)).setUnlocalizedName("dirtSlab");    
		    dirtDoubleSlab = (BlockHalfSlab) (new BlockDirtSlab(BLOCKID_DIRTDOUBLESLAB, true)).setUnlocalizedName("dirtDoubleSlab");    
		    grassSlab = (BlockHalfSlab) (new BlockGrassSlab(BLOCKID_GRASSSLAB, false)).setUnlocalizedName("grassSlab");
		    grassDoubleSlab = (BlockHalfSlab) (new BlockGrassSlab(BLOCKID_GRASSDOUBLESLAB, true)).setUnlocalizedName("grassDoubleSlab");
	        GameRegistry.registerBlock(DynamicEarth.dirtSlab, ItemDirtSlab.class, "dirtSlab");
	        GameRegistry.registerBlock(DynamicEarth.dirtDoubleSlab, ItemDirtSlab.class, "dirtDoubleSlab");
	        GameRegistry.registerBlock(DynamicEarth.grassSlab, ItemGrassSlab.class, "grassSlab");
	        GameRegistry.registerBlock(DynamicEarth.grassDoubleSlab, ItemGrassSlab.class, "grassDoubleSlab");
	    }
	    if (DynamicEarth.includePeat) {
		    peatMoss = new BlockPeatMoss(BLOCKID_PEATMOSS);
			peat = new BlockPeat(BLOCKID_PEAT);
	        GameRegistry.registerBlock(DynamicEarth.peatMoss, "peatMoss");
	        GameRegistry.registerBlock(DynamicEarth.peat, ItemBlockPeat.class, "peat");
	    }
	    if (DynamicEarth.includeFertileSoil) {
	    	fertileSoil = new BlockFertileSoil(BLOCKID_FERTILESOIL);
	    	GameRegistry.registerBlock(DynamicEarth.fertileSoil, ItemBlockFertileSoil.class, "fertileSoil");
	    }
	    if (DynamicEarth.includeSandySoil) {
	    	sandySoil = new BlockSandySoil(BLOCKID_SANDYSOIL);
	    	GameRegistry.registerBlock(DynamicEarth.sandySoil, ItemBlockSandySoil.class, "sandySoil");
	    }
	}
    
    private static void registerItems() {
	    mudBlob = (ItemClump) new ItemClump(ITEMID_MUDBLOB, ItemIcon.MUDBLOB).setThrowable(true).setUnlocalizedName("mudBlob");
	    dirtClod = (ItemClump) new ItemClump(ITEMID_DIRTCLOD, ItemIcon.DIRTCLOD).setWetClump(DynamicEarth.mudBlob.itemID).setUnlocalizedName("dirtClod");
	    OreDictionary.registerOre("dirtClump", DynamicEarth.dirtClod);
	    OreDictionary.registerOre("mudBlob", DynamicEarth.mudBlob);
	    if (DynamicEarth.includeMudBrick) {
	    	mudBrick = new ItemMudMod(ITEMID_MUDBRICK, ItemIcon.MUDBRICK).setUnlocalizedName("mudBrick").setCreativeTab(CreativeTabs.tabMaterials);
	    }
	    if (DynamicEarth.includeAdobe) {
		    adobeBlob = (ItemClump) new ItemClump(ITEMID_ADOBEBLOB, ItemIcon.ADOBEBLOB).setUnlocalizedName("adobeBlob");
		    adobeDust = (ItemClump) new ItemClump(ITEMID_ADOBEDUST, ItemIcon.ADOBEDUST).setWetClump(DynamicEarth.adobeBlob.itemID).setUnlocalizedName("adobeDust");
		    vaseRaw = new ItemMudMod(ITEMID_VASERAW, ItemIcon.VASERAW).setUnlocalizedName("vaseRaw").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMaterials);
		    vase = (ItemVase) new ItemVase(ITEMID_VASE).setUnlocalizedName("vase");
		    earthbowlRaw = new ItemMudMod(ITEMID_EARTHBOWLRAW, ItemIcon.EARTHBOWLRAW).setUnlocalizedName("earthbowlRaw").setMaxStackSize(16).setCreativeTab(CreativeTabs.tabMaterials);
		    earthbowl = new ItemMudMod(ITEMID_EARTHBOWL, ItemIcon.EARTHBOWL).setUnlocalizedName("earthbowl").setCreativeTab(CreativeTabs.tabMaterials);
		    earthbowlSoup = new ItemEarthbowlSoup(ITEMID_EARTHBOWLSOUP).setUnlocalizedName("earthbowlSoup");
		    liquidMilk = new ItemLiquid(ITEMID_LIQUIDMILK, ItemIcon.MILK).setUnlocalizedName("liquidMilk");
		    liquidSoup = new ItemLiquid(ITEMID_LIQUIDSOUP, ItemIcon.SOUP).setUnlocalizedName("liquidSoup");
		    if (DynamicEarth.includeBombs) {
			    bomb = new ItemBomb(ITEMID_BOMB, ItemIcon.BOMB).setUnlocalizedName("bomb");
			    bombLit = new ItemBombLit(ITEMID_BOMBLIT, ItemIcon.BOMBLIT).setUnlocalizedName("bombLit");    	
		    }
	    }
	    if (DynamicEarth.includePeat) {
	    	peatClump = (ItemClump) new ItemClump(ITEMID_PEATCLUMP, ItemIcon.PEATCLUMP).setUnlocalizedName("peatClump");
	    	peatBrick = new ItemMudMod(ITEMID_PEATBRICK, ItemIcon.PEATBRICK).setUnlocalizedName("peatBrick").setCreativeTab(CreativeTabs.tabMaterials);
	    	peatMossSpecimen = (new ItemPeatMossSpecimen(ITEMID_PEATMOSSSPECIMEN, ItemIcon.PEATMOSSSPECIMEN)).setUnlocalizedName("peatMossSpecimen");
	    }
	}
    
    public static void registerEntities() {
    	EntityRegistry.registerModEntity(EntityFallingBlock.class, "fallingBlock", 0, instance, 250, 5, true);
        EntityRegistry.registerModEntity(EntityMudball.class, "mudball", 1, instance, 250, 1, true);
        if (DynamicEarth.includeAdobe) {
	        if (DynamicEarth.includeClayGolems) {
	        	EntityRegistry.registerModEntity(EntityAdobeGolem.class, "clayGolem", 2, instance, 250, 5, true);
	        }
	        if (DynamicEarth.includeBombs) {
	        	EntityRegistry.registerModEntity(EntityBomb.class, "bomb", 3, instance, 250, 1, true);
	        }
        }
    }
	
    public static void registerDispenserHandlers() {
    	BlockDispenser.dispenseBehaviorRegistry.putObject(DynamicEarth.mudBlob, new BehaviorMudballDispense());
    	if (DynamicEarth.includeAdobe) {
	        BlockDispenser.dispenseBehaviorRegistry.putObject(DynamicEarth.vase, new BehaviorVaseDispense());
	        if (DynamicEarth.includeBombs) {
	        	BlockDispenser.dispenseBehaviorRegistry.putObject(DynamicEarth.bomb, new BehaviorBombDispense());
	        }
    	}
	}
	
    public static void setBlockHarvestLevels() {
    	final int
			WOOD = EnumToolMaterial.WOOD.getHarvestLevel(),
			DIAMOND = EnumToolMaterial.EMERALD.getHarvestLevel();
        MinecraftForge.setBlockHarvestLevel(DynamicEarth.mud, "shovel", WOOD);
        MinecraftForge.setBlockHarvestLevel(DynamicEarth.farmland, "shovel", WOOD);
       if (DynamicEarth.includeAdobe) {
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.adobeWet, "shovel", WOOD);
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.adobe, "pickaxe", WOOD);
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.adobeSingleSlab, "pickaxe", WOOD);
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.adobeDoubleSlab, "pickaxe", WOOD);
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.adobeStairs, "pickaxe", WOOD);
        }
        if (DynamicEarth.includeMudBrick) {
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.blockMudBrick, "pickaxe", WOOD);
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.mudBrickStairs, "pickaxe", WOOD);
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.mudBrickWall, "pickaxe", WOOD);
        }
        if (DynamicEarth.includePermafrost) {
        	MinecraftForge.setBlockHarvestLevel(DynamicEarth.permafrost, "pickaxe", WOOD);
          	MinecraftForge.setBlockHarvestLevel(DynamicEarth.permafrost, "shovel", DIAMOND);
                  }
        if (DynamicEarth.includeDirtSlabs) {
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.dirtSlab, "shovel", WOOD);
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.grassSlab, "shovel", WOOD);
        }
        if (DynamicEarth.includePeat) {
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.peatMoss, "shovel", WOOD);
	        MinecraftForge.setBlockHarvestLevel(DynamicEarth.peat, "shovel", WOOD);
        }
        if (DynamicEarth.includeFertileSoil) {
        	MinecraftForge.setBlockHarvestLevel(DynamicEarth.fertileSoil, "shovel", WOOD);
        }
        if (DynamicEarth.includeSandySoil) {
        	MinecraftForge.setBlockHarvestLevel(DynamicEarth.sandySoil, "shovel", WOOD);
        }
	}

}
