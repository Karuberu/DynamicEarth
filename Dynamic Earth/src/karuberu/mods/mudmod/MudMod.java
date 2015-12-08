package karuberu.mods.mudmod;

import karuberu.mods.mudmod.blocks.BlockAdobe;
import karuberu.mods.mudmod.blocks.BlockAdobeSlab;
import karuberu.mods.mudmod.blocks.BlockAdobeStairs;
import karuberu.mods.mudmod.blocks.BlockAdobeWet;
import karuberu.mods.mudmod.blocks.BlockDirtSlab;
import karuberu.mods.mudmod.blocks.BlockFertileSoil;
import karuberu.mods.mudmod.blocks.BlockGrassSlab;
import karuberu.mods.mudmod.blocks.BlockMud;
import karuberu.mods.mudmod.blocks.BlockMudBrick;
import karuberu.mods.mudmod.blocks.BlockMudBrickWall;
import karuberu.mods.mudmod.blocks.BlockMudModFarmland;
import karuberu.mods.mudmod.blocks.BlockPeat;
import karuberu.mods.mudmod.blocks.BlockPeatMoss;
import karuberu.mods.mudmod.blocks.BlockPermafrost;
import karuberu.mods.mudmod.blocks.BlockSandySoil;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import karuberu.mods.mudmod.entity.EntityBomb;
import karuberu.mods.mudmod.entity.EntityClayGolem;
import karuberu.mods.mudmod.entity.EntityFallingBlock;
import karuberu.mods.mudmod.entity.EntityMudball;
import karuberu.mods.mudmod.items.BehaviorBombDispense;
import karuberu.mods.mudmod.items.BehaviorMudballDispense;
import karuberu.mods.mudmod.items.BehaviorVaseDispense;
import karuberu.mods.mudmod.items.ItemAdobeSlab;
import karuberu.mods.mudmod.items.ItemBlockFertileSoil;
import karuberu.mods.mudmod.items.ItemBlockMud;
import karuberu.mods.mudmod.items.ItemBlockPeat;
import karuberu.mods.mudmod.items.ItemBlockSandySoil;
import karuberu.mods.mudmod.items.ItemBomb;
import karuberu.mods.mudmod.items.ItemBombLit;
import karuberu.mods.mudmod.items.ItemClump;
import karuberu.mods.mudmod.items.ItemDirtSlab;
import karuberu.mods.mudmod.items.ItemEarthbowlSoup;
import karuberu.mods.mudmod.items.ItemGrassSlab;
import karuberu.mods.mudmod.items.ItemLiquid;
import karuberu.mods.mudmod.items.ItemMudMod;
import karuberu.mods.mudmod.items.ItemPeatMossSpecimen;
import karuberu.mods.mudmod.items.ItemVase;
import karuberu.mods.mudmod.items.crafting.CraftingHandler;
import karuberu.mods.mudmod.items.crafting.RecipeBombs;
import karuberu.mods.mudmod.items.crafting.RecipeManager;
import karuberu.mods.mudmod.liquids.LiquidHandler;
import karuberu.mods.mudmod.world.WorldGenMudMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
	useMetadata=true,
	modid = "DynamicEarth",
	name = "Dynamic Earth",
	version = "1.7.1.1",
	dependencies = "required-after:Forge; required-after:karuberu-core; after:Thaumcraft; after:Forestry; after:ThermalExpansion")
@NetworkMod(
	clientSideRequired = true,
	serverSideRequired = false
)
public class MudMod {
	@Mod.Instance("DynamicEarth")
	public static MudMod instance;
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
	
	@PreInit
	public void loadConfiguration(FMLPreInitializationEvent event) {       
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		RecipeManager.canCraftBombs = config.get("Crafting", "canCraftBombs", true).getBoolean(true);
		RecipeManager.canCraftMossyStone = config.get("Crafting", "canCraftMossyStone", true).getBoolean(true);
		MudMod.enableDeepMud = config.get("Features", "enableDeepMud", true).getBoolean(true);
		MudMod.enableDeepPeat = config.get("Features", "enableDeepPeat", true).getBoolean(true);
		MudMod.enableGrassBurning = config.get("Features", "enableGrassBurning", true).getBoolean(true);
		MudMod.enableMyceliumTilling = config.get("Features", "enableMyceliumTilling", false).getBoolean(false);
		MudMod.includeAdobe = config.get("Features", "includeAdobe", true).getBoolean(true);
		MudMod.includeBombs = config.get("Features", "includeBombs", true).getBoolean(true);
		MudMod.includeClayGolems = config.get("Features", "includeClayGolems", true).getBoolean(true);
		MudMod.includeDirtSlabs = config.get("Features", "includeDirtSlabs", true).getBoolean(true);
		MudMod.includeFertileSoil = config.get("Features", "includeFertileSoil", true).getBoolean(true);
		MudMod.includeMudBrick = config.get("Features", "includeMudBrick", true).getBoolean(true);
		MudMod.includePeat = config.get("Features", "includePeat", true).getBoolean(true);
		MudMod.includePermafrost = config.get("Features", "includePermafrost", true).getBoolean(true);
		MudMod.includeSandySoil = config.get("Features", "includeSandySoil", true).getBoolean(true);
		MudMod.showSnowyBottomSlabs = config.get("Features", "showSnowyBottomSlabs", true).getBoolean(true);
		MudMod.restoreDirtOnChunkLoad = config.get("Maintenance", "restoreDirtOnChunkLoad", false).getBoolean(false);
		WorldGenMudMod.doGenerateMud = config.get("Terrain Generation", "doGenerateMud", true).getBoolean(true);
		WorldGenMudMod.doGeneratePermafrost = config.get("Terrain Generation", "doGeneratePermafrost", true).getBoolean(true);
		WorldGenMudMod.doGeneratePeat = config.get("Terrain Generation", "doGeneratePeat", true).getBoolean(true);
		FuelHandler.peatBurnTime = config.get("Adjustments", "peatBurnTime", FuelHandler.peatBurnTime).getInt();
		RecipeBombs.maxGunpowder = config.get("Adjustments", "maxGunpowderForBombs", RecipeBombs.maxGunpowder).getInt();
		ItemBombLit.maxFuseLength = config.get("Adjustments", "maxFuseLengthForBombs", ItemBombLit.maxFuseLength).getInt();
		ItemVase.showMeasurement = config.get("Adjustments", "showVaseMeasurements", true).getBoolean(true);
		MudMod.useSimpleHydration = config.get("Adjustments", "useSimpleHydration", false).getBoolean(false);
		ModHandler.enableForestryIntegration = config.get("Forestry", "enableIntegration", true).getBoolean(true);
		ModHandler.useForestryPeat = config.get("Forestry", "useForestryPeat", false).getBoolean(false);
		FuelHandler.peatForestryBurnTime = config.get("Forestry", "peatBurnTime", FuelHandler.peatForestryBurnTime).getInt();
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
	}
	
    @Init
	public void initialize(FMLInitializationEvent event) {
        MudMod.registerBlocks();
        MudMod.registerItems();
        MudMod.registerEntities();
        MudMod.registerDispenserHandlers();
        MudMod.setBlockHarvestLevels();
        LiquidHandler.registerLiquids();
        RecipeManager.addRecipes();
        RecipeManager.addSmelting();
        CraftingHandler.register();
        WorldGenMudMod.register();
    	FuelHandler.register();
        EventHandler.register();
        CommonProxy.proxy.registerNames();
        CommonProxy.proxy.registerLocalizations();
        CommonProxy.proxy.registerRenderInformation();
        ModHandler.integrateMods();
    }
    
    private static void registerBlocks() {
    	Block.dirt.setTickRandomly(true);
        mud = new BlockMud(BLOCKID_MUD);
        farmland = new BlockMudModFarmland(BLOCKID_FARMLAND);
	    GameRegistry.registerBlock(MudMod.mud, ItemBlockMud.class, "mud");
	    GameRegistry.registerBlock(MudMod.farmland, "farmland");
	    if (MudMod.includeAdobe) {
		    adobe = new BlockAdobe(BLOCKID_ADOBE);
		    adobeWet = new BlockAdobeWet(BLOCKID_ADOBEWET);
		    adobeDoubleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBEDOUBLESLAB, true);
		    adobeSingleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBESINGLESLAB, false);    
		    adobeStairs = (new BlockAdobeStairs(BLOCKID_ADOBESTAIRS, adobe, 0)).setUnlocalizedName("adobeStairs");
	        GameRegistry.registerBlock(MudMod.adobeWet, "adobeWet");
	        GameRegistry.registerBlock(MudMod.adobe, "adobe");
	        GameRegistry.registerBlock(MudMod.adobeSingleSlab, ItemAdobeSlab.class, "adobeSingleSlab");
	        GameRegistry.registerBlock(MudMod.adobeDoubleSlab, ItemAdobeSlab.class, "adobeDoubleSlab");
	        GameRegistry.registerBlock(MudMod.adobeStairs, "adobeStairs");
	    }
	    if (MudMod.includeMudBrick) {
		    blockMudBrick = new BlockMudBrick(BLOCKID_MUDBRICKBLOCK);
		    mudBrickStairs = (new BlockAdobeStairs(BLOCKID_MUDBRICKSTAIRS, blockMudBrick, 0)).setUnlocalizedName("mudBrickStairs");    
		    mudBrickWall = (new BlockMudBrickWall(BLOCKID_MUDBRICKWALL, blockMudBrick)).setUnlocalizedName("mudBrickWall");    
			GameRegistry.registerBlock(MudMod.blockMudBrick, "blockMudBrick");
			GameRegistry.registerBlock(MudMod.mudBrickStairs, "mudBrickStairs");
			GameRegistry.registerBlock(MudMod.mudBrickWall, "mudBrickWall");
	    }
	    if (MudMod.includePermafrost) {
	    	permafrost = new BlockPermafrost(BLOCKID_PERMAFROST);
	        GameRegistry.registerBlock(MudMod.permafrost, "permafrost");
	    }
	    if (MudMod.includeDirtSlabs) {
		    dirtSlab = (BlockHalfSlab) (new BlockDirtSlab(BLOCKID_DIRTSLAB, false)).setUnlocalizedName("dirtSlab");    
		    dirtDoubleSlab = (BlockHalfSlab) (new BlockDirtSlab(BLOCKID_DIRTDOUBLESLAB, true)).setUnlocalizedName("dirtDoubleSlab");    
		    grassSlab = (BlockHalfSlab) (new BlockGrassSlab(BLOCKID_GRASSSLAB, false)).setUnlocalizedName("grassSlab");
		    grassDoubleSlab = (BlockHalfSlab) (new BlockGrassSlab(BLOCKID_GRASSDOUBLESLAB, true)).setUnlocalizedName("grassDoubleSlab");
	        GameRegistry.registerBlock(MudMod.dirtSlab, ItemDirtSlab.class, "dirtSlab");
	        GameRegistry.registerBlock(MudMod.dirtDoubleSlab, ItemDirtSlab.class, "dirtDoubleSlab");
	        GameRegistry.registerBlock(MudMod.grassSlab, ItemGrassSlab.class, "grassSlab");
	        GameRegistry.registerBlock(MudMod.grassDoubleSlab, ItemGrassSlab.class, "grassDoubleSlab");
	    }
	    if (MudMod.includePeat) {
		    peatMoss = new BlockPeatMoss(BLOCKID_PEATMOSS);
			peat = new BlockPeat(BLOCKID_PEAT);
	        GameRegistry.registerBlock(MudMod.peatMoss, "peatMoss");
	        GameRegistry.registerBlock(MudMod.peat, ItemBlockPeat.class, "peat");
	    }
	    if (MudMod.includeFertileSoil) {
	    	fertileSoil = new BlockFertileSoil(BLOCKID_FERTILESOIL);
	    	GameRegistry.registerBlock(MudMod.fertileSoil, ItemBlockFertileSoil.class, "fertileSoil");
	    }
	    if (MudMod.includeSandySoil) {
	    	sandySoil = new BlockSandySoil(BLOCKID_SANDYSOIL);
	    	GameRegistry.registerBlock(MudMod.sandySoil, ItemBlockSandySoil.class, "sandySoil");
	    }
	}
    
    private static void registerItems() {
	    mudBlob = (ItemClump) new ItemClump(ITEMID_MUDBLOB, Texture.MUDBLOB).setThrowable(true).setUnlocalizedName("mudBlob");
	    dirtClod = (ItemClump) new ItemClump(ITEMID_DIRTCLOD, Texture.DIRTCLOD).setWetClump(MudMod.mudBlob.itemID).setUnlocalizedName("dirtClod");
	    OreDictionary.registerOre("dirtClump", MudMod.dirtClod);
	    OreDictionary.registerOre("mudBlob", MudMod.mudBlob);
	    if (MudMod.includeMudBrick) {
	    	mudBrick = new ItemMudMod(ITEMID_MUDBRICK, Texture.MUDBRICK).setUnlocalizedName("mudBrick").setCreativeTab(CreativeTabs.tabMaterials);
	    }
	    if (MudMod.includeAdobe) {
		    adobeBlob = (ItemClump) new ItemClump(ITEMID_ADOBEBLOB, Texture.ADOBEBLOB).setUnlocalizedName("adobeBlob");
		    adobeDust = (ItemClump) new ItemClump(ITEMID_ADOBEDUST, Texture.ADOBEDUST).setWetClump(MudMod.adobeBlob.itemID).setUnlocalizedName("adobeDust");
		    vaseRaw = new ItemMudMod(ITEMID_VASERAW, Texture.VASERAW).setUnlocalizedName("vaseRaw").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMaterials);
		    vase = (ItemVase) new ItemVase(ITEMID_VASE).setUnlocalizedName("vase");
		    earthbowlRaw = new ItemMudMod(ITEMID_EARTHBOWLRAW, Texture.EARTHBOWLRAW).setUnlocalizedName("earthbowlRaw").setMaxStackSize(16).setCreativeTab(CreativeTabs.tabMaterials);
		    earthbowl = new ItemMudMod(ITEMID_EARTHBOWL, Texture.EARTHBOWL).setUnlocalizedName("earthbowl").setCreativeTab(CreativeTabs.tabMaterials);
		    earthbowlSoup = new ItemEarthbowlSoup(ITEMID_EARTHBOWLSOUP).setUnlocalizedName("earthbowlSoup");
		    liquidMilk = new ItemLiquid(ITEMID_LIQUIDMILK, Texture.MILK).setUnlocalizedName("liquidMilk");
		    liquidSoup = new ItemLiquid(ITEMID_LIQUIDSOUP, Texture.SOUP).setUnlocalizedName("liquidSoup");
		    if (MudMod.includeBombs) {
			    bomb = new ItemBomb(ITEMID_BOMB, Texture.BOMB).setUnlocalizedName("bomb");
			    bombLit = new ItemBombLit(ITEMID_BOMBLIT, Texture.BOMBLIT).setUnlocalizedName("bombLit");    	
		    }
	    }
	    if (MudMod.includePeat) {
	    	peatClump = (ItemClump) new ItemClump(ITEMID_PEATCLUMP, Texture.PEATCLUMP).setUnlocalizedName("peatClump");
	    	peatBrick = new ItemMudMod(ITEMID_PEATBRICK, Texture.PEATBRICK).setUnlocalizedName("peatBrick").setCreativeTab(CreativeTabs.tabMaterials);
	    	peatMossSpecimen = (new ItemPeatMossSpecimen(ITEMID_PEATMOSSSPECIMEN, Texture.PEATMOSSSPECIMEN)).setUnlocalizedName("peatMossSpecimen");
	    }
	}
    
    public static void registerEntities() {
    	EntityRegistry.registerModEntity(EntityFallingBlock.class, "fallingBlock", 0, instance, 250, 5, true);
        EntityRegistry.registerModEntity(EntityMudball.class, "mudball", 1, instance, 250, 1, true);
        if (MudMod.includeAdobe) {
	        if (MudMod.includeClayGolems) {
	        	EntityRegistry.registerModEntity(EntityClayGolem.class, "clayGolem", 2, instance, 250, 5, true);
	        }
	        if (MudMod.includeBombs) {
	        	EntityRegistry.registerModEntity(EntityBomb.class, "bomb", 3, instance, 250, 1, true);
	        }
        }
    }
	
    public static void registerDispenserHandlers() {
    	BlockDispenser.dispenseBehaviorRegistry.putObject(MudMod.mudBlob, new BehaviorMudballDispense());
    	if (MudMod.includeAdobe) {
	        BlockDispenser.dispenseBehaviorRegistry.putObject(MudMod.vase, new BehaviorVaseDispense());
	        if (MudMod.includeBombs) {
	        	BlockDispenser.dispenseBehaviorRegistry.putObject(MudMod.bomb, new BehaviorBombDispense());
	        }
    	}
	}
	
    public static void setBlockHarvestLevels() {
    	final int
			WOOD = EnumToolMaterial.WOOD.getHarvestLevel(),
			DIAMOND = EnumToolMaterial.EMERALD.getHarvestLevel();
        MinecraftForge.setBlockHarvestLevel(MudMod.mud, "shovel", WOOD);
        MinecraftForge.setBlockHarvestLevel(MudMod.farmland, "shovel", WOOD);
       if (MudMod.includeAdobe) {
	        MinecraftForge.setBlockHarvestLevel(MudMod.adobeWet, "shovel", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.adobe, "pickaxe", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.adobeSingleSlab, "pickaxe", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.adobeDoubleSlab, "pickaxe", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.adobeStairs, "pickaxe", WOOD);
        }
        if (MudMod.includeMudBrick) {
	        MinecraftForge.setBlockHarvestLevel(MudMod.blockMudBrick, "pickaxe", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.mudBrickStairs, "pickaxe", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.mudBrickWall, "pickaxe", WOOD);
        }
        if (MudMod.includePermafrost) {
        	MinecraftForge.setBlockHarvestLevel(MudMod.permafrost, "pickaxe", WOOD);
          	MinecraftForge.setBlockHarvestLevel(MudMod.permafrost, "shovel", DIAMOND);
                  }
        if (MudMod.includeDirtSlabs) {
	        MinecraftForge.setBlockHarvestLevel(MudMod.dirtSlab, "shovel", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.grassSlab, "shovel", WOOD);
        }
        if (MudMod.includePeat) {
	        MinecraftForge.setBlockHarvestLevel(MudMod.peatMoss, "shovel", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.peat, "shovel", WOOD);
        }
        if (MudMod.includeFertileSoil) {
        	MinecraftForge.setBlockHarvestLevel(MudMod.fertileSoil, "shovel", WOOD);
        }
        if (MudMod.includeSandySoil) {
        	MinecraftForge.setBlockHarvestLevel(MudMod.sandySoil, "shovel", WOOD);
        }
	}

}
