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
import karuberu.dynamicearth.blocks.BlockLiquid;
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
import karuberu.dynamicearth.items.BehaviorBombDispense;
import karuberu.dynamicearth.items.BehaviorMudballDispense;
import karuberu.dynamicearth.items.BehaviorVaseDispense;
import karuberu.dynamicearth.items.ItemAdobeSlab;
import karuberu.dynamicearth.items.ItemBlockBurningSoil;
import karuberu.dynamicearth.items.ItemBlockDynamicEarth;
import karuberu.dynamicearth.items.ItemBlockFertileMud;
import karuberu.dynamicearth.items.ItemBlockFertileSoil;
import karuberu.dynamicearth.items.ItemBlockGlowingMud;
import karuberu.dynamicearth.items.ItemBlockGlowingSoil;
import karuberu.dynamicearth.items.ItemBlockMud;
import karuberu.dynamicearth.items.ItemBlockPeat;
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
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

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
	public static final String
		modID = "DynamicEarth";
	@Mod.Instance(DynamicEarth.modID)
	public static DynamicEarth
		instance;
	public static final KaruberuLogger
		logger = new KaruberuLogger(DynamicEarth.modID);
	private static final RegistrationHelper
		registrationHelper = new RegistrationHelper(DynamicEarth.logger);
	public static final ConfigurationManager
		config = new ConfigurationManager(DynamicEarth.logger);
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
	private static final String
		BLOCK_MUD,
		BLOCK_FERTILEMUD,
		BLOCK_GLOWINGMUD,
		BLOCK_PERMAFROST,
		BLOCK_ADOBEWET,
		BLOCK_ADOBE,
		BLOCK_MUDBRICK,
		BLOCK_ADOBESLAB,
		BLOCK_ADOBEDOUBLESLAB,
		BLOCK_ADOBESTAIRS,
		BLOCK_MUDBRICKSTAIRS,
		BLOCK_MUDBRICKWALL,
		BLOCK_DIRTSLAB,
		BLOCK_DIRTDOUBLESLAB,
		BLOCK_GRASSSLAB,
		BLOCK_GRASSDOUBLESLAB,
		BLOCK_PEATMOSS,
		BLOCK_PEAT,
		BLOCK_FARMLAND,
		BLOCK_FERTILESOIL,
		BLOCK_SANDYSOIL,
		BLOCK_LIQUIDMILK,
		BLOCK_LIQUIDSOUP,
		BLOCK_GLOWINGSOIL,
		BLOCK_BURNINGSOIL,
		BLOCK_MUDLAYER,
		ITEM_DIRTCLOD,
		ITEM_MUDBLOB,
		ITEM_MUDBRICK,
		ITEM_ADOBEDUST,
		ITEM_ADOBEBLOB,
		ITEM_VASERAW,
		ITEM_VASE,
		ITEM_EARTHBOWLRAW,
		ITEM_EARTHBOWL,
		ITEM_EARTHBOWLSOUP,
		ITEM_BOMB,
		ITEM_BOMBLIT,
		ITEM_PEATMOSSSPECIMEN,
		ITEM_PEATCLUMP,
		ITEM_PEATBRICK;
	static {
		DynamicEarth.config.initializeBlockIDs(4000,
			BLOCK_MUD = "mud",
			BLOCK_FERTILEMUD = "fertileMud",
			BLOCK_GLOWINGMUD = "glowingMud",
			BLOCK_PERMAFROST = "permafrost",
			BLOCK_ADOBEWET = "adobeWet",
			BLOCK_ADOBE = "adobeDry",
			BLOCK_MUDBRICK = "blockMudBrick",
			BLOCK_ADOBESLAB = "adobeSlab",
			BLOCK_ADOBEDOUBLESLAB = "adobeSlabDouble",
			BLOCK_ADOBESTAIRS = "adobeStairs",
			BLOCK_MUDBRICKSTAIRS = "mudBrickStairs",
			BLOCK_MUDBRICKWALL = "mudBrickWall",
			BLOCK_DIRTSLAB = "dirtSlab",
			BLOCK_DIRTDOUBLESLAB = "dirtSlabDouble",
			BLOCK_GRASSSLAB = "grassSlab",
			BLOCK_GRASSDOUBLESLAB = "grassSlabDouble",
			BLOCK_PEATMOSS = "peatMoss",
			BLOCK_PEAT = "peat",
			BLOCK_FARMLAND = "dynamicFarmland",
			BLOCK_FERTILESOIL = "fertileSoil",
			BLOCK_SANDYSOIL = "sandySoil",
			BLOCK_LIQUIDMILK = "liquidMilk",
			BLOCK_LIQUIDSOUP = "liquidSoup",
			BLOCK_GLOWINGSOIL = "glowingSoil",
			BLOCK_BURNINGSOIL = "burningSoil",
			BLOCK_MUDLAYER = "mudLayer"
		);
		DynamicEarth.config.initializeItemIDs(10000,
			ITEM_DIRTCLOD = "dirtClod",
			ITEM_MUDBLOB = "mudBlob",
			ITEM_MUDBRICK = "mudBrick",
			ITEM_ADOBEDUST = "adobeDust",
			ITEM_ADOBEBLOB = "adobeBlob",
			ITEM_VASERAW = "vaseRaw",
			ITEM_VASE = "vase",
			ITEM_EARTHBOWLRAW = "earthbowlRaw",
			ITEM_EARTHBOWL = "earthbowl",
			ITEM_EARTHBOWLSOUP = "earthbowlSoup",
			ITEM_BOMB = "bomb",
			ITEM_BOMBLIT = "bombLit",
			ITEM_PEATMOSSSPECIMEN = "peatMossSpecimen",
			ITEM_PEATCLUMP = "peatClump",
			ITEM_PEATBRICK = "peatBrick"
		);
	}
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
		DynamicEarth.config.setConfigurationFile(new Configuration(event.getSuggestedConfigurationFile()));
		DynamicEarth.config.configBlockIDs();
		DynamicEarth.config.configItemIDs();
		DynamicEarth.config.configAdjustments();
		DynamicEarth.config.configCrafting();
		DynamicEarth.config.configFeatures();
		DynamicEarth.config.configMaintenance();
		DynamicEarth.config.configWorldGen();
		DynamicEarth.config.closeConfig();
		PluginHandler.instance.handlePluginPreInitialization(event);
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
        PluginHandler.instance.addPlugins();
    }
	
	@EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
        PluginHandler.instance.initializePlugins();
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
        farmland = new BlockDynamicFarmland(BLOCK_FARMLAND);
        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.farmland);
	    if (DynamicEarth.includeMud) {
			mud = new BlockMud(BLOCK_MUD);
			DynamicEarth.registrationHelper.registerBlock(DynamicEarth.mud, ItemBlockMud.class);
			if (DynamicEarth.includeMudLayers) {
				mudLayer = new BlockMudLayer(BLOCK_MUDLAYER);
				DynamicEarth.registrationHelper.registerBlock(DynamicEarth.mudLayer);
			}
		}
	    if (DynamicEarth.includeAdobe) {
		    adobe = new BlockAdobe(BLOCK_ADOBE);
		    adobeWet = new BlockAdobeWet(BLOCK_ADOBEWET);
		    adobeDoubleSlab = new BlockAdobeSlab(BLOCK_ADOBEDOUBLESLAB, true);
		    adobeSingleSlab = new BlockAdobeSlab(BLOCK_ADOBESLAB, false);    
		    adobeStairs = (new BlockAdobeStairs(BLOCK_ADOBESTAIRS, adobe, 0));
		    liquidMilk = new BlockLiquid(BLOCK_LIQUIDMILK, Material.water, BlockTexture.MILK);
		    liquidSoup = new BlockLiquid(BLOCK_LIQUIDSOUP, Material.water, BlockTexture.SOUP);
	        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.adobeSingleSlab, ItemAdobeSlab.class);
	        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.adobeDoubleSlab, ItemAdobeSlab.class);
		    DynamicEarth.registrationHelper.registerBlocks(
		    	DynamicEarth.adobeWet,
		    	DynamicEarth.adobe,
		    	DynamicEarth.adobeStairs
		    );
	    }
	    if (DynamicEarth.includeMudBrick) {
		    blockMudBrick = new BlockMudBrick(BLOCK_MUDBRICK);
		    mudBrickStairs = (new BlockAdobeStairs(BLOCK_MUDBRICKSTAIRS, blockMudBrick, 0));    
		    mudBrickWall = (new BlockMudBrickWall(BLOCK_MUDBRICKWALL, blockMudBrick));    
		    DynamicEarth.registrationHelper.registerBlocks(
		    	DynamicEarth.blockMudBrick,
		    	DynamicEarth.mudBrickStairs,
		    	DynamicEarth.mudBrickWall
		    );
			if (!DynamicEarth.includeAdobe) {
			    adobeDoubleSlab = new BlockAdobeSlab(BLOCK_ADOBEDOUBLESLAB, true);
			    adobeSingleSlab = new BlockAdobeSlab(BLOCK_ADOBESLAB, false);    
			    DynamicEarth.registrationHelper.registerBlock(DynamicEarth.adobeSingleSlab, ItemAdobeSlab.class);
		        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.adobeDoubleSlab, ItemAdobeSlab.class);
			}
	    }
	    if (DynamicEarth.includePermafrost) {
	    	permafrost = new BlockPermafrost(BLOCK_PERMAFROST);
	    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.permafrost, ItemBlockDynamicEarth.class);
	    }
	    if (DynamicEarth.includeDirtSlabs) {
		    dirtSlab = (new BlockDirtSlab(BLOCK_DIRTSLAB, false));    
		    dirtDoubleSlab = (new BlockDirtSlab(BLOCK_DIRTDOUBLESLAB, true));    
		    grassSlab = (new BlockGrassSlab(BLOCK_GRASSSLAB, false));
		    grassDoubleSlab = (new BlockGrassSlab(BLOCK_GRASSDOUBLESLAB, true));
		    DynamicEarth.registrationHelper.registerBlock(DynamicEarth.dirtSlab, ItemDirtSlab.class);
	        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.dirtDoubleSlab, ItemDirtSlab.class);
	        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.grassSlab, ItemGrassSlab.class);
	        DynamicEarth.registrationHelper.registerBlock(DynamicEarth.grassDoubleSlab, ItemGrassSlab.class);
	    }
	    if (DynamicEarth.includePeat) {
		    peatMoss = new BlockPeatMoss(BLOCK_PEATMOSS);
			peat = new BlockPeat(BLOCK_PEAT);
			DynamicEarth.registrationHelper.registerBlock(DynamicEarth.peatMoss);
			DynamicEarth.registrationHelper.registerBlock(DynamicEarth.peat, ItemBlockPeat.class);
	    }
	    if (DynamicEarth.includeFertileSoil) {
	    	fertileSoil = new BlockFertileSoil(BLOCK_FERTILESOIL);
	    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.fertileSoil, ItemBlockFertileSoil.class);
	    	if (DynamicEarth.includeMud) {
		    	fertileMud = new BlockFertileMud(BLOCK_FERTILEMUD);
		    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.fertileMud, ItemBlockFertileMud.class);	    		
	    	}
	    }
	    if (DynamicEarth.includeSandySoil) {
	    	sandySoil = new BlockSandySoil(BLOCK_SANDYSOIL);
	    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.sandySoil, ItemBlockSandySoil.class);
	    }
	    if (DynamicEarth.includeGlowingSoil) {
	    	glowingSoil = new BlockGlowingSoil(BLOCK_GLOWINGSOIL);
	    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.glowingSoil, ItemBlockGlowingSoil.class);
	    	if (DynamicEarth.includeMud) {
		    	glowingMud = new BlockGlowingMud(BLOCK_GLOWINGMUD);
		    	DynamicEarth.registrationHelper.registerBlock(DynamicEarth.glowingMud, ItemBlockGlowingMud.class);	    		
	    	}
	    }
	    if (DynamicEarth.includeBurningSoil) {
	    	burningSoil = new BlockBurningSoil(BLOCK_BURNINGSOIL);
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
    	dirtClod = (ItemClump) new ItemClump(ITEM_DIRTCLOD, ItemIcon.DIRTCLOD).setUnlocalizedName("dirtClod");
    	DynamicEarth.registrationHelper.registerItem(dirtClod);
    	if (DynamicEarth.includeMud) {
    		mudBlob = new ItemMudBlob(ITEM_MUDBLOB, ItemIcon.MUDBLOB);
    	    dirtClod.setWetClump(new ItemStack(mudBlob.itemID, 1, ItemMudBlob.NORMAL));
        	DynamicEarth.registrationHelper.registerItem(mudBlob);
    	}
	    if (DynamicEarth.includeMudBrick) {
	    	mudBrick = (ItemDynamicEarth) new ItemDynamicEarth(ITEM_MUDBRICK, ItemIcon.MUDBRICK).setUnlocalizedName("mudBrick");
        	DynamicEarth.registrationHelper.registerItem(mudBrick);
	    }
	    if (DynamicEarth.includeAdobe) {
		    adobeBlob = (ItemClump) new ItemClump(ITEM_ADOBEBLOB, ItemIcon.ADOBEBLOB).setUnlocalizedName("adobeBlob");
		    adobeDust = (ItemClump) new ItemClump(ITEM_ADOBEDUST, ItemIcon.ADOBEDUST).setWetClump(new ItemStack(DynamicEarth.adobeBlob)).setUnlocalizedName("adobeDust");
		    vaseRaw = (ItemDynamicEarth) new ItemDynamicEarth(ITEM_VASERAW, ItemIcon.VASERAW).setUnlocalizedName("vaseRaw").setMaxStackSize(1);
		    vase = (ItemVase) new ItemVase(ITEM_VASE).setUnlocalizedName("vase");
		    earthbowlRaw = (ItemDynamicEarth) new ItemDynamicEarth(ITEM_EARTHBOWLRAW, ItemIcon.EARTHBOWLRAW).setUnlocalizedName("earthbowlRaw").setMaxStackSize(16);
		    earthbowl = (ItemDynamicEarth) new ItemDynamicEarth(ITEM_EARTHBOWL, ItemIcon.EARTHBOWL).setUnlocalizedName("earthbowl");
		    earthbowlSoup = new ItemEarthbowlSoup(ITEM_EARTHBOWLSOUP).setUnlocalizedName("earthbowlSoup");
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
			    bomb = (ItemDynamicEarth) new ItemBomb(ITEM_BOMB, ItemIcon.BOMB).setUnlocalizedName("bomb");
			    bombLit = (ItemDynamicEarth) new ItemBombLit(ITEM_BOMBLIT, ItemIcon.BOMBLIT).setUnlocalizedName("bombLit");    	
	        	DynamicEarth.registrationHelper.registerItems(bomb, bombLit);
		    }
	    }
	    if (DynamicEarth.includePeat) {
	    	peatClump = (ItemClump) new ItemClump(ITEM_PEATCLUMP, ItemIcon.PEATCLUMP).setUnlocalizedName("peatClump");
	    	peatBrick = (ItemDynamicEarth) new ItemDynamicEarth(ITEM_PEATBRICK, ItemIcon.PEATBRICK).setUnlocalizedName("peatBrick");
	    	peatMossSpecimen = (ItemDynamicEarth) (new ItemPeatMossSpecimen(ITEM_PEATMOSSSPECIMEN, ItemIcon.PEATMOSSSPECIMEN)).setUnlocalizedName("peatMossSpecimen");
        	DynamicEarth.registrationHelper.registerItems(
        		peatClump,
        		peatBrick,
        		peatMossSpecimen
        	);
	    }
	}
        
    public static void registerEntities() {
    	DynamicEarth.registrationHelper.registerEntity(EntityFallingBlock.class, "fallingBlock", DynamicEarth.ENTITYID_FALLINGBLOCK, DynamicEarth.instance, 250, 5, true);
    	DynamicEarth.registrationHelper.registerEntity(EntityMudball.class, "mudball", DynamicEarth.ENTITYID_MUDBALL, DynamicEarth.instance, 250, 1, true);
        if (DynamicEarth.includeAdobe) {
	        if (DynamicEarth.includeAdobeGolems) {
	        	DynamicEarth.registrationHelper.registerEntity(EntityAdobeGolem.class, "clayGolem", DynamicEarth.ENTITYID_GOLEM, DynamicEarth.instance, 250, 5, true);
	        }
	        if (DynamicEarth.includeBombs) {
	        	DynamicEarth.registrationHelper.registerEntity(EntityBomb.class, "bomb", DynamicEarth.ENTITYID_BOMB, DynamicEarth.instance, 250, 1, true);
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
