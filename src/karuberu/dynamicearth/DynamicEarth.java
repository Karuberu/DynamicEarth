package karuberu.dynamicearth;

import karuberu.core.util.KaruberuLogger;
import karuberu.core.util.Helper;
import karuberu.core.util.RegistrationHelper;
import karuberu.dynamicearth.api.ITillable;
import karuberu.dynamicearth.api.Reference;
import karuberu.dynamicearth.api.grass.GrassyBlockRegistry;
import karuberu.dynamicearth.api.mud.MudRegistry;
import karuberu.dynamicearth.blocks.BlockAdobe;
import karuberu.dynamicearth.blocks.BlockAdobeSlab;
import karuberu.dynamicearth.blocks.BlockAdobeStairs;
import karuberu.dynamicearth.blocks.BlockAdobeWet;
import karuberu.dynamicearth.blocks.BlockBurningSoil;
import karuberu.dynamicearth.blocks.BlockDirtSlab;
import karuberu.dynamicearth.blocks.BlockFertileMud;
import karuberu.dynamicearth.blocks.BlockFertileSoil;
import karuberu.dynamicearth.blocks.BlockGlowingMud;
import karuberu.dynamicearth.blocks.BlockGlowingSoil;
import karuberu.dynamicearth.blocks.BlockGrassSlab;
import karuberu.dynamicearth.blocks.BlockMud;
import karuberu.dynamicearth.blocks.BlockMudBrick;
import karuberu.dynamicearth.blocks.BlockMudBrickWall;
import karuberu.dynamicearth.blocks.BlockDynamicFarmland;
import karuberu.dynamicearth.blocks.BlockMudLayer;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.blocks.BlockPeatMoss;
import karuberu.dynamicearth.blocks.BlockPermafrost;
import karuberu.dynamicearth.blocks.BlockSandySoil;
import karuberu.dynamicearth.blocks.BlockSoil;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import karuberu.dynamicearth.creativetab.CreativeTabDynamicEarth;
import karuberu.dynamicearth.entity.EntityAdobeGolem;
import karuberu.dynamicearth.entity.EntityBomb;
import karuberu.dynamicearth.entity.EntityFallingBlock;
import karuberu.dynamicearth.entity.EntityMudball;
import karuberu.dynamicearth.event.EventManager;
import karuberu.dynamicearth.fluids.FluidHandler;
import karuberu.dynamicearth.fluids.BlockLiquid;
import karuberu.dynamicearth.items.BehaviorBombDispense;
import karuberu.dynamicearth.items.BehaviorMudballDispense;
import karuberu.dynamicearth.items.BehaviorVaseDispense;
import karuberu.dynamicearth.items.ItemAdobeSlab;
import karuberu.dynamicearth.items.ItemBlockBurningSoil;
import karuberu.dynamicearth.items.ItemBlockFertileMud;
import karuberu.dynamicearth.items.ItemBlockFertileSoil;
import karuberu.dynamicearth.items.ItemBlockGlowingMud;
import karuberu.dynamicearth.items.ItemBlockGlowingSoil;
import karuberu.dynamicearth.items.ItemBlockMud;
import karuberu.dynamicearth.items.ItemBlockPeat;
import karuberu.dynamicearth.items.ItemBlockPermafrost;
import karuberu.dynamicearth.items.ItemBlockSandySoil;
import karuberu.dynamicearth.items.ItemBomb;
import karuberu.dynamicearth.items.ItemBombLit;
import karuberu.dynamicearth.items.ItemClump;
import karuberu.dynamicearth.items.ItemDirtSlab;
import karuberu.dynamicearth.items.ItemEarthbowlSoup;
import karuberu.dynamicearth.items.ItemGrassSlab;
import karuberu.dynamicearth.items.ItemDynamicEarth;
import karuberu.dynamicearth.items.ItemMudBlob;
import karuberu.dynamicearth.items.ItemPeatMossSpecimen;
import karuberu.dynamicearth.items.ItemVase;
import karuberu.dynamicearth.items.crafting.CraftingHandler;
import karuberu.dynamicearth.items.crafting.RecipeManager;
import karuberu.dynamicearth.plugins.PluginHandler;
import karuberu.dynamicearth.world.WorldGenDynamicEarth;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;

@Mod(
	useMetadata=true,
	modid = DynamicEarth.modID,
	name = "Dynamic Earth",
	version = "1.8.0",
	dependencies = "required-after:Forge@[9.10.0.776,); " +
		"required-after:KaruberuCore@[1.2.1];" +
		"after:BiomesOPlenty;" +
		"after:Forestry;" +
		"after:IC2;" +
		"after:Thaumcraft;" +
		"after:ThermalExpansion",
	acceptedMinecraftVersions = "[1.6.2, 1.6.4]"
)
@NetworkMod(
	clientSideRequired = true,
	serverSideRequired = false
)
public class DynamicEarth {
	@Mod.Instance("DynamicEarth")
	public static DynamicEarth instance;
	public static final String
		modID = "DynamicEarth";
	public static final KaruberuLogger
		logger = new KaruberuLogger(DynamicEarth.modID);
	private static final RegistrationHelper
		registrationHelper = new RegistrationHelper(DynamicEarth.logger);
	public static BlockMud
    	mud,
    	fertileMud,
    	glowingMud;
	public static BlockSoil
		fertileSoil,
		sandySoil,
		glowingSoil;
	public static BlockBurningSoil
		burningSoil;
	public static BlockDynamicFarmland
		farmland;
	public static Block
    	permafrost,
    	adobeWet,
     	adobe,
    	blockMudBrick,
    	adobeStairs,
    	mudBrickStairs,
    	mudBrickWall,
    	peatMoss,
    	peat,
    	liquidMilk,
    	liquidSoup,
		mudLayer;
	public static BlockHalfSlab
		adobeSingleSlab,
		adobeDoubleSlab,
		dirtSlab,
		dirtDoubleSlab,
		grassSlab,
		grassDoubleSlab;
	public static ItemDynamicEarth
    	mudBrick,
    	adobeSingleSlabItem,
    	adobeDoubleSlabItem,
    	vaseRaw,
    	earthbowl,
    	earthbowlRaw,
    	bomb,
    	bombLit,
    	peatBrick,
    	peatMossSpecimen;
	public static Item
		earthbowlSoup;
	public static ItemClump
		dirtClod,
		mudBlob,
		adobeDust,
		adobeBlob,
		peatClump;
	public static ItemVase
		vase;
	public static int
		peatMossRenderID;
	private static int
		BLOCKID_MUD					= 4000,
		BLOCKID_FERTILEMUD			= BLOCKID_MUD+1,
		BLOCKID_GLOWINGMUD			= BLOCKID_FERTILEMUD+1,
		BLOCKID_PERMAFROST			= BLOCKID_GLOWINGMUD+1,
		BLOCKID_ADOBEWET			= BLOCKID_PERMAFROST+1,
		BLOCKID_ADOBE				= BLOCKID_ADOBEWET+1,
		BLOCKID_MUDBRICKBLOCK		= BLOCKID_ADOBE+1,
		BLOCKID_ADOBESLAB			= BLOCKID_MUDBRICKBLOCK+1,
		BLOCKID_ADOBEDOUBLESLAB		= BLOCKID_ADOBESLAB+1,
		BLOCKID_ADOBESTAIRS			= BLOCKID_ADOBEDOUBLESLAB+1,
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
		BLOCKID_LIQUIDMILK			= BLOCKID_SANDYSOIL+1,
		BLOCKID_LIQUIDSOUP			= BLOCKID_LIQUIDMILK+1,
		BLOCKID_GLOWINGSOIL			= BLOCKID_LIQUIDSOUP+1,
		BLOCKID_BURNINGSOIL			= BLOCKID_GLOWINGSOIL+1,
		BLOCKID_MUDLAYER				= BLOCKID_BURNINGSOIL+1,
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
		ITEMID_PEATBRICK			= ITEMID_PEATCLUMP+1;
	private static int
		ENTITYID_FALLINGBLOCK = 0,
		ENTITYID_GOLEM = 1,
		ENTITYID_MUDBALL = 2,
		ENTITYID_BOMB = 3;
	public static boolean
		enableBottomSlabGrassKilling,
		enableDeepMud,
		enableDeepPeat,
		enableEndermanBlockDrops,
		enableGrassBurning,
		enableFancyMudslides,
		enableMudslideBlockPreservation,
		enableMoreDestructiveMudslides,
		enableMoreDestructiveRain,
		enableMyceliumTilling,
		enableThrownMudLayers,
		enableUnderwaterMudslides,
		includeAdobe,
		includeAdobeGolems,
		includeBombs,
		includeBurningSoil,
		includeDirtSlabs,
		includeFertileSoil,
		includeGlowingSoil,
		includeMud,
		includeMudBrick,
		includeMudLayers,
		includeNetherGrass,
		includePeat,
		includePermafrost,
		includeSandySoil,
		restoreDirtOnChunkLoad,
		showSnowyBottomSlabs,
		useAdjustedBottleVolume,
		useCustomCreativeTab,
		useSimpleHydration;
	
	@EventHandler
	public void preInitialization(FMLPreInitializationEvent event) {
		ConfigurationManager.setConfigurationFile(event.getSuggestedConfigurationFile());
		ConfigurationManager.configAdjustments();
		ConfigurationManager.configCrafting();
		ConfigurationManager.configFeatures();
		ConfigurationManager.configMaintenance();
		ConfigurationManager.configWorldGen();
		BLOCKID_MUD				= ConfigurationManager.getBlockID("Mud", BLOCKID_MUD);
		BLOCKID_PERMAFROST		= ConfigurationManager.getBlockID("Permafrost", BLOCKID_PERMAFROST);
		BLOCKID_ADOBEWET		= ConfigurationManager.getBlockID("AdobeMoist", BLOCKID_ADOBEWET);
		BLOCKID_ADOBE			= ConfigurationManager.getBlockID("Adobe", BLOCKID_ADOBE);
		BLOCKID_MUDBRICKBLOCK	= ConfigurationManager.getBlockID("MudBrick", BLOCKID_MUDBRICKBLOCK);
		BLOCKID_ADOBEDOUBLESLAB	= ConfigurationManager.getBlockID("AdobeDoubleSlab", BLOCKID_ADOBEDOUBLESLAB);
		BLOCKID_ADOBESLAB		= ConfigurationManager.getBlockID("AdobeSingleSlab", BLOCKID_ADOBESLAB);
		BLOCKID_ADOBESTAIRS		= ConfigurationManager.getBlockID("AdobeStairs", BLOCKID_ADOBESTAIRS);
		BLOCKID_MUDBRICKSTAIRS	= ConfigurationManager.getBlockID("MudBrickStairs", BLOCKID_MUDBRICKSTAIRS);
		BLOCKID_MUDBRICKWALL	= ConfigurationManager.getBlockID("MudBrickWall", BLOCKID_MUDBRICKWALL);
		BLOCKID_DIRTSLAB		= ConfigurationManager.getBlockID("DirtSlab", BLOCKID_DIRTSLAB);
		BLOCKID_DIRTDOUBLESLAB	= ConfigurationManager.getBlockID("DirtDoubleSlab", BLOCKID_DIRTDOUBLESLAB);
		BLOCKID_GRASSSLAB		= ConfigurationManager.getBlockID("GrassSlab", BLOCKID_GRASSSLAB);
		BLOCKID_GRASSDOUBLESLAB = ConfigurationManager.getBlockID("GrassDoubleSlab", BLOCKID_GRASSDOUBLESLAB);
		BLOCKID_PEATMOSS		= ConfigurationManager.getBlockID("PeatMoss", BLOCKID_PEATMOSS);
		BLOCKID_PEAT			= ConfigurationManager.getBlockID("Peat", BLOCKID_PEAT);
		BLOCKID_FARMLAND		= ConfigurationManager.getBlockID("Farmland", BLOCKID_FARMLAND);
		BLOCKID_FERTILESOIL		= ConfigurationManager.getBlockID("FertileSoil", BLOCKID_FERTILESOIL);
		BLOCKID_FERTILEMUD		= ConfigurationManager.getBlockID("FertileMud", BLOCKID_FERTILEMUD);
		BLOCKID_SANDYSOIL		= ConfigurationManager.getBlockID("SandySoil", BLOCKID_SANDYSOIL);
		BLOCKID_GLOWINGSOIL		= ConfigurationManager.getBlockID("GlowingSoil", BLOCKID_GLOWINGSOIL);
		BLOCKID_GLOWINGMUD		= ConfigurationManager.getBlockID("GlowingMud", BLOCKID_GLOWINGMUD);
		BLOCKID_BURNINGSOIL		= ConfigurationManager.getBlockID("BurningSoil", BLOCKID_BURNINGSOIL);
		BLOCKID_MUDLAYER		= ConfigurationManager.getBlockID("MudLayer", BLOCKID_MUDLAYER);
		BLOCKID_LIQUIDMILK		= ConfigurationManager.getBlockID("LiquidMilk", BLOCKID_LIQUIDMILK);
		BLOCKID_LIQUIDSOUP		= ConfigurationManager.getBlockID("LiquidSoup", BLOCKID_LIQUIDSOUP);
		ITEMID_DIRTCLOD			= ConfigurationManager.getItemID("DirtClod", ITEMID_DIRTCLOD);
		ITEMID_MUDBLOB			= ConfigurationManager.getItemID("MudBlob", ITEMID_MUDBLOB);
		ITEMID_MUDBRICK			= ConfigurationManager.getItemID("MudBrick", ITEMID_MUDBRICK);
		ITEMID_ADOBEDUST		= ConfigurationManager.getItemID("AdobeDust", ITEMID_ADOBEDUST);
		ITEMID_ADOBEBLOB		= ConfigurationManager.getItemID("AdobeBlob", ITEMID_ADOBEBLOB);
		ITEMID_VASERAW			= ConfigurationManager.getItemID("VaseRaw", ITEMID_VASERAW);
		ITEMID_VASE				= ConfigurationManager.getItemID("Vase", ITEMID_VASE);
		ITEMID_EARTHBOWLRAW		= ConfigurationManager.getItemID("EarthbowlRaw", ITEMID_EARTHBOWLRAW);
		ITEMID_EARTHBOWL		= ConfigurationManager.getItemID("Earthbowl", ITEMID_EARTHBOWL);
		ITEMID_EARTHBOWLSOUP	= ConfigurationManager.getItemID("EarthbowlSoup", ITEMID_EARTHBOWLSOUP);
		ITEMID_BOMB				= ConfigurationManager.getItemID("Bomb", ITEMID_BOMB);
		ITEMID_BOMBLIT			= ConfigurationManager.getItemID("BombLit", ITEMID_BOMBLIT);
		ITEMID_PEATCLUMP		= ConfigurationManager.getItemID("PeatClump", ITEMID_PEATCLUMP);
		ITEMID_PEATBRICK		= ConfigurationManager.getItemID("PeatBrick", ITEMID_PEATBRICK);
		ITEMID_PEATMOSSSPECIMEN	= ConfigurationManager.getItemID("PeatMossSpecimen", ITEMID_PEATMOSSSPECIMEN);
		ConfigurationManager.closeConfig();
		PluginHandler.handlePluginPreInitialization(event);
        DynamicEarth.registerBlocks();
        DynamicEarth.registerItems();
        DynamicEarth.registerEntities();
        DynamicEarth.initializeAPI();
	}
	
	@EventHandler
	public void initialize(FMLInitializationEvent event) {
        FluidHandler.registerLiquids();
        DynamicEarth.registerOreDictionaryTerms();
		DynamicEarth.registerGrassyBlocks();
        DynamicEarth.registerDispenserHandlers();
        DynamicEarth.setBlockHarvestLevels();
        RecipeManager.addRecipes();
        RecipeManager.addSmelting();
        CraftingHandler.register();
        WorldGenDynamicEarth.register();
    	FuelHandler.register();
        EventManager.register();
        TickHandler.register();
        CommonProxy.proxy.registerNames();
        CommonProxy.proxy.registerLocalizations();
        CommonProxy.proxy.registerRenderInformation();
    }
	
	@EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
        PluginHandler.initializePlugins();
        DynamicEarth.registerMudslideBlocks();
 	}
	
	private static void setCreativeTabs() {
		if (DynamicEarth.useCustomCreativeTab) {
			BlockAdobe.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockAdobeSlab.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockAdobeStairs.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockAdobeWet.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockDirtSlab.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockFertileSoil.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockFertileMud.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockGrassSlab.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockMud.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockMudBrick.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockMudBrickWall.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockPeat.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockPermafrost.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockSandySoil.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockGlowingSoil.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockBurningSoil.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemBomb.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemClump.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemEarthbowlSoup.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemDynamicEarth.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemPeatMossSpecimen.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemVase.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
		}
	}
    
	private static void initializeAPI() {
		Reference.logger = DynamicEarth.logger.getLogger();
		Reference.mycelium = new ITillable() {
			@Override
			public boolean onTilled(World world, int x, int y, int z) {
				if (DynamicEarth.enableMyceliumTilling) {
					world.setBlock(x, y, z, Block.tilledField.blockID);
					return true;
				}
				return false;
			}
		};
		Reference.fallingBlockEntityClass = EntityFallingBlock.class;
	}
	
    private static void registerBlocks() {
		DynamicEarth.setCreativeTabs();
    	if (DynamicEarth.includeMud
    	|| DynamicEarth.includePermafrost) {
    		Block.dirt.setTickRandomly(true);
    	}
        farmland = new BlockDynamicFarmland(BLOCKID_FARMLAND);
        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.farmland);
	    if (DynamicEarth.includeMud) {
			mud = new BlockMud(BLOCKID_MUD);
			DynamicEarth.registrationHelper.registerBlock(DynamicEarth.mud, ItemBlockMud.class);
			if (DynamicEarth.includeMudLayers) {
				mudLayer = new BlockMudLayer(BLOCKID_MUDLAYER);
				DynamicEarth.registrationHelper.registerBlock(DynamicEarth.mudLayer);
			}
		}
	    if (DynamicEarth.includeAdobe) {
		    adobe = new BlockAdobe(BLOCKID_ADOBE);
		    adobeWet = new BlockAdobeWet(BLOCKID_ADOBEWET);
		    adobeDoubleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBEDOUBLESLAB, true).setUnlocalizedName("adobeSlab");
		    adobeSingleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBESLAB, false).setUnlocalizedName("adobeSlabDouble");    
		    adobeStairs = (new BlockAdobeStairs(BLOCKID_ADOBESTAIRS, adobe, 0)).setUnlocalizedName("adobeStairs");
		    liquidMilk = new BlockLiquid(BLOCKID_LIQUIDMILK, Material.water, BlockTexture.MILK).setUnlocalizedName("liquidMilk");
		    liquidSoup = new BlockLiquid(BLOCKID_LIQUIDSOUP, Material.water, BlockTexture.SOUP).setUnlocalizedName("liquidSoup");
	        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.adobeSingleSlab, ItemAdobeSlab.class);
	        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.adobeDoubleSlab, ItemAdobeSlab.class);
		    DynamicEarth.registrationHelper.registerBlocks(
		    	DynamicEarth.adobeWet,
		    	DynamicEarth.adobe,
		    	DynamicEarth.adobeStairs
		    );
	    }
	    if (DynamicEarth.includeMudBrick) {
		    blockMudBrick = new BlockMudBrick(BLOCKID_MUDBRICKBLOCK);
		    mudBrickStairs = (new BlockAdobeStairs(BLOCKID_MUDBRICKSTAIRS, blockMudBrick, 0)).setUnlocalizedName("mudBrickStairs");    
		    mudBrickWall = (new BlockMudBrickWall(BLOCKID_MUDBRICKWALL, blockMudBrick)).setUnlocalizedName("mudBrickWall");    
		    DynamicEarth.registrationHelper.registerBlocks(
		    	DynamicEarth.blockMudBrick,
		    	DynamicEarth.mudBrickStairs,
		    	DynamicEarth.mudBrickWall
		    );
			if (!DynamicEarth.includeAdobe) {
			    adobeDoubleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBEDOUBLESLAB, true);
			    adobeSingleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBESLAB, false);    
			    DynamicEarth.registrationHelper.registerBlock(DynamicEarth.adobeSingleSlab, ItemAdobeSlab.class);
		        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.adobeDoubleSlab, ItemAdobeSlab.class);
			}
	    }
	    if (DynamicEarth.includePermafrost) {
	    	permafrost = new BlockPermafrost(BLOCKID_PERMAFROST);
	    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.permafrost, ItemBlockPermafrost.class);
	    }
	    if (DynamicEarth.includeDirtSlabs) {
		    dirtSlab = (BlockHalfSlab) (new BlockDirtSlab(BLOCKID_DIRTSLAB, false)).setUnlocalizedName("dirtSlab");    
		    dirtDoubleSlab = (BlockHalfSlab) (new BlockDirtSlab(BLOCKID_DIRTDOUBLESLAB, true)).setUnlocalizedName("dirtDoubleSlab");    
		    grassSlab = (BlockHalfSlab) (new BlockGrassSlab(BLOCKID_GRASSSLAB, false)).setUnlocalizedName("grassSlab");
		    grassDoubleSlab = (BlockHalfSlab) (new BlockGrassSlab(BLOCKID_GRASSDOUBLESLAB, true)).setUnlocalizedName("grassDoubleSlab");
		    DynamicEarth.registrationHelper.registerBlock(DynamicEarth.dirtSlab, ItemDirtSlab.class);
	        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.dirtDoubleSlab, ItemDirtSlab.class);
	        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.grassSlab, ItemGrassSlab.class);
	        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.grassDoubleSlab, ItemGrassSlab.class);
	    }
	    if (DynamicEarth.includePeat) {
		    peatMoss = new BlockPeatMoss(BLOCKID_PEATMOSS);
			peat = new BlockPeat(BLOCKID_PEAT);
			DynamicEarth.registrationHelper.registerBlock(DynamicEarth.peatMoss);
			DynamicEarth.registrationHelper.registerBlock(DynamicEarth.peat, ItemBlockPeat.class);
	    }
	    if (DynamicEarth.includeFertileSoil) {
	    	fertileSoil = new BlockFertileSoil(BLOCKID_FERTILESOIL);
	    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.fertileSoil, ItemBlockFertileSoil.class);
	    	if (DynamicEarth.includeMud) {
		    	fertileMud = new BlockFertileMud(BLOCKID_FERTILEMUD);
		    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.fertileMud, ItemBlockFertileMud.class);	    		
	    	}
	    }
	    if (DynamicEarth.includeSandySoil) {
	    	sandySoil = new BlockSandySoil(BLOCKID_SANDYSOIL);
	    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.sandySoil, ItemBlockSandySoil.class);
	    }
	    if (DynamicEarth.includeGlowingSoil) {
	    	glowingSoil = new BlockGlowingSoil(BLOCKID_GLOWINGSOIL);
	    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.glowingSoil, ItemBlockGlowingSoil.class);
	    	if (DynamicEarth.includeMud) {
		    	glowingMud = new BlockGlowingMud(BLOCKID_GLOWINGMUD);
		    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.glowingMud, ItemBlockGlowingMud.class);	    		
	    	}
	    }
	    if (DynamicEarth.includeBurningSoil) {
	    	burningSoil = new BlockBurningSoil(BLOCKID_BURNINGSOIL);
	    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.burningSoil, ItemBlockBurningSoil.class);
	    }
	}
    
    private static void registerGrassyBlocks() {
		GrassyBlockRegistry.registerGrassyBlock(
			new ItemStack(Block.dirt),
			new ItemStack(Block.grass),
			new ItemStack(Block.mycelium)
		);
    }
    
    private static void registerMudslideBlocks() {
    	MudRegistry.registerMudslideBlock(DynamicEarth.permafrost.blockID, 6, 0.75F);
    	MudRegistry.registerOreDictionaryItems("blockCobble", 6, 0.75F);
    	MudRegistry.registerOreDictionaryItems("cobblestone", 6, 0.75F);
    	MudRegistry.registerOreDictionaryItems("blockStone", 6, 0.75F);
    	MudRegistry.registerOreDictionaryItems("stone", 7, 0.7F);
    	MudRegistry.registerOreDictionaryItems("blockDirt", 5, 0.8F);
    	MudRegistry.registerOreDictionaryItems("dirt", 5, 0.8F);
    	MudRegistry.registerOreDictionaryItems("blockGrass", 5, 0.75F);
    	MudRegistry.registerOreDictionaryItems("blockMycelium", 5, 0.75F);
    	MudRegistry.registerOreDictionaryItems("mycelium", 5, 0.8F);
    	MudRegistry.registerOreDictionaryItems("blockMud", 0, 1.0F);
    	MudRegistry.registerOreDictionaryItems("mud", 0, 1.0F);
    	MudRegistry.registerOreDictionaryItems("blockSnow", 0, 1.0F);
    	MudRegistry.registerOreDictionaryItems("snow", 0, 1.0F);
    }
    
    private static void registerItems() {
    	dirtClod = (ItemClump) new ItemClump(ITEMID_DIRTCLOD, ItemIcon.DIRTCLOD).setUnlocalizedName("dirtClod");
    	DynamicEarth.registrationHelper.registerItem(dirtClod);
    	if (DynamicEarth.includeMud) {
    		mudBlob = (ItemClump) new ItemMudBlob(ITEMID_MUDBLOB, ItemIcon.MUDBLOB);
    	    dirtClod.setWetClump(new ItemStack(mudBlob.itemID, 1, ItemMudBlob.NORMAL));
        	DynamicEarth.registrationHelper.registerItem(mudBlob);
    	}
	    if (DynamicEarth.includeMudBrick) {
	    	mudBrick = (ItemDynamicEarth) new ItemDynamicEarth(ITEMID_MUDBRICK, ItemIcon.MUDBRICK).setUnlocalizedName("mudBrick");
        	DynamicEarth.registrationHelper.registerItem(mudBrick);
	    }
	    if (DynamicEarth.includeAdobe) {
		    adobeBlob = (ItemClump) new ItemClump(ITEMID_ADOBEBLOB, ItemIcon.ADOBEBLOB).setUnlocalizedName("adobeBlob");
		    adobeDust = (ItemClump) new ItemClump(ITEMID_ADOBEDUST, ItemIcon.ADOBEDUST).setWetClump(new ItemStack(DynamicEarth.adobeBlob)).setUnlocalizedName("adobeDust");
		    vaseRaw = (ItemDynamicEarth) new ItemDynamicEarth(ITEMID_VASERAW, ItemIcon.VASERAW).setUnlocalizedName("vaseRaw").setMaxStackSize(1);
		    vase = (ItemVase) new ItemVase(ITEMID_VASE).setUnlocalizedName("vase");
		    earthbowlRaw = (ItemDynamicEarth) new ItemDynamicEarth(ITEMID_EARTHBOWLRAW, ItemIcon.EARTHBOWLRAW).setUnlocalizedName("earthbowlRaw").setMaxStackSize(16);
		    earthbowl = (ItemDynamicEarth) new ItemDynamicEarth(ITEMID_EARTHBOWL, ItemIcon.EARTHBOWL).setUnlocalizedName("earthbowl");
		    earthbowlSoup = new ItemEarthbowlSoup(ITEMID_EARTHBOWLSOUP).setUnlocalizedName("earthbowlSoup");
        	DynamicEarth.registrationHelper.registerItems(
		    	adobeBlob,
		    	adobeDust,
		    	vaseRaw,
		    	vase,
		    	earthbowlRaw,
		    	earthbowl,
		    	earthbowlSoup
		    );
		    if (DynamicEarth.includeBombs) {
			    bomb = (ItemDynamicEarth) new ItemBomb(ITEMID_BOMB, ItemIcon.BOMB).setUnlocalizedName("bomb");
			    bombLit = (ItemDynamicEarth) new ItemBombLit(ITEMID_BOMBLIT, ItemIcon.BOMBLIT).setUnlocalizedName("bombLit");    	
	        	DynamicEarth.registrationHelper.registerItems(bomb, bombLit);
		    }
	    }
	    if (DynamicEarth.includePeat) {
	    	peatClump = (ItemClump) new ItemClump(ITEMID_PEATCLUMP, ItemIcon.PEATCLUMP).setUnlocalizedName("peatClump");
	    	peatBrick = (ItemDynamicEarth) new ItemDynamicEarth(ITEMID_PEATBRICK, ItemIcon.PEATBRICK).setUnlocalizedName("peatBrick");
	    	peatMossSpecimen = (ItemDynamicEarth) (new ItemPeatMossSpecimen(ITEMID_PEATMOSSSPECIMEN, ItemIcon.PEATMOSSSPECIMEN)).setUnlocalizedName("peatMossSpecimen");
        	DynamicEarth.registrationHelper.registerItems(
        		peatClump,
        		peatBrick,
        		peatMossSpecimen
        	);
	    }
	}
        
    public static void registerEntities() {
    	EntityRegistry.registerModEntity(EntityFallingBlock.class, "fallingBlock", DynamicEarth.ENTITYID_FALLINGBLOCK, instance, 250, 5, true);
        EntityRegistry.registerModEntity(EntityMudball.class, "mudball", DynamicEarth.ENTITYID_MUDBALL, instance, 250, 1, true);
        if (DynamicEarth.includeAdobe) {
	        if (DynamicEarth.includeAdobeGolems) {
	        	EntityRegistry.registerModEntity(EntityAdobeGolem.class, "clayGolem", DynamicEarth.ENTITYID_GOLEM, instance, 250, 5, true);
	        }
	        if (DynamicEarth.includeBombs) {
	        	EntityRegistry.registerModEntity(EntityBomb.class, "bomb", DynamicEarth.ENTITYID_BOMB, instance, 250, 1, true);
	        }
        }
    }
	
    public static void registerDispenserHandlers() {
    	if (DynamicEarth.includeMud) {
    		BlockDispenser.dispenseBehaviorRegistry.putObject(DynamicEarth.mudBlob, new BehaviorMudballDispense());
    	}
    	if (DynamicEarth.includeAdobe) {
	        BlockDispenser.dispenseBehaviorRegistry.putObject(DynamicEarth.vase, new BehaviorVaseDispense());
	        if (DynamicEarth.includeBombs) {
	        	BlockDispenser.dispenseBehaviorRegistry.putObject(DynamicEarth.bomb, new BehaviorBombDispense());
	        }
    	}
	}
	
    public static void setBlockHarvestLevels() {
    	MinecraftForge.setBlockHarvestLevel(DynamicEarth.farmland, "shovel", Helper.HARVEST_LEVEL_WOOD);
    	if (DynamicEarth.includeMud) {
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.mud, "shovel", Helper.HARVEST_LEVEL_WOOD);
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.mudLayer, "shovel", Helper.HARVEST_LEVEL_WOOD);
    	}
    	if (DynamicEarth.includeAdobe) {
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.adobeWet, "shovel", Helper.HARVEST_LEVEL_WOOD);
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.adobe, "pickaxe", Helper.HARVEST_LEVEL_WOOD);
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.adobeSingleSlab, "pickaxe", Helper.HARVEST_LEVEL_WOOD);
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.adobeDoubleSlab, "pickaxe", Helper.HARVEST_LEVEL_WOOD);
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.adobeStairs, "pickaxe", Helper.HARVEST_LEVEL_WOOD);
    	}
    	if (DynamicEarth.includeMudBrick) {
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.blockMudBrick, "pickaxe", Helper.HARVEST_LEVEL_WOOD);
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.mudBrickStairs, "pickaxe", Helper.HARVEST_LEVEL_WOOD);
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.mudBrickWall, "pickaxe", Helper.HARVEST_LEVEL_WOOD);
    	}
    	if (DynamicEarth.includePermafrost) {
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.permafrost, "pickaxe", Helper.HARVEST_LEVEL_WOOD);
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.permafrost, "shovel", Helper.HARVEST_LEVEL_DIAMOND);
    	}
    	if (DynamicEarth.includeDirtSlabs) {
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.dirtSlab, "shovel", Helper.HARVEST_LEVEL_WOOD);
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.grassSlab, "shovel", Helper.HARVEST_LEVEL_WOOD);
    	}
    	if (DynamicEarth.includePeat) {
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.peatMoss, "shovel", Helper.HARVEST_LEVEL_WOOD);
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.peat, "shovel", Helper.HARVEST_LEVEL_WOOD);
    	}
    	if (DynamicEarth.includeFertileSoil) {
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.fertileSoil, "shovel", Helper.HARVEST_LEVEL_WOOD);
        	if (DynamicEarth.includeMud) {
        		MinecraftForge.setBlockHarvestLevel(DynamicEarth.fertileMud, "shovel", Helper.HARVEST_LEVEL_WOOD);
        	}
    	}
    	if (DynamicEarth.includeSandySoil) {
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.sandySoil, "shovel", Helper.HARVEST_LEVEL_WOOD);
    	}
    	if (DynamicEarth.includeGlowingSoil) {
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.glowingSoil, "shovel", Helper.HARVEST_LEVEL_WOOD);
        	if (DynamicEarth.includeMud) {
        		MinecraftForge.setBlockHarvestLevel(DynamicEarth.glowingMud, "shovel", Helper.HARVEST_LEVEL_WOOD);
        	}
    	}
    	if (DynamicEarth.includeBurningSoil) {
    		MinecraftForge.setBlockHarvestLevel(DynamicEarth.burningSoil, "shovel", Helper.HARVEST_LEVEL_WOOD);
    	}
	}

    private static void registerOreDictionaryTerms() {
    	OreDictionary.registerOre("blockDirt", Block.dirt);
    	OreDictionary.registerOre("blockGrass", Block.grass);
    	OreDictionary.registerOre("blockMycelium", Block.mycelium);
    	OreDictionary.registerOre("blockSnow", Block.blockSnow);
    	OreDictionary.registerOre("dirtClump", DynamicEarth.dirtClod);
    	if (DynamicEarth.includeMud) {
    		OreDictionary.registerOre("mudBlob", DynamicEarth.mudBlob);
        	OreDictionaryHelper.registerMudBlock(DynamicEarth.mud);
        	if (DynamicEarth.includeFertileSoil) {
            	OreDictionaryHelper.registerMudBlock(DynamicEarth.fertileMud);
        	}
        	if (DynamicEarth.includeGlowingSoil) {
            	OreDictionaryHelper.registerMudBlock(DynamicEarth.glowingMud);
        	}
    	}
    	if (DynamicEarth.includePermafrost) {
    		OreDictionaryHelper.registerOre("blockPermafrost", DynamicEarth.permafrost, 0);
    	}
    	if (DynamicEarth.includeFertileSoil) {
    		OreDictionaryHelper.registerSoilBlock(DynamicEarth.fertileSoil);
    	}
    	if (DynamicEarth.includeSandySoil) {
    		OreDictionaryHelper.registerSoilBlock(DynamicEarth.sandySoil);
    	}
    	if (DynamicEarth.includeBurningSoil) {
    		OreDictionaryHelper.registerOre("blockDirt", DynamicEarth.burningSoil, DynamicEarth.burningSoil.DIRT);
    	}
    	if (DynamicEarth.includeGlowingSoil) {
    		OreDictionaryHelper.registerSoilBlock(DynamicEarth.glowingSoil);
    	}
    }
}
