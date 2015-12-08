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
import karuberu.dynamicearth.creativetab.CreativeTabDynamicEarth;
import karuberu.dynamicearth.entity.EntityAdobeGolem;
import karuberu.dynamicearth.entity.EntityBomb;
import karuberu.dynamicearth.entity.EntityFallingBlock;
import karuberu.dynamicearth.entity.EntityMudball;
import karuberu.dynamicearth.fluids.FluidHandler;
import karuberu.dynamicearth.fluids.BlockLiquid;
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
import karuberu.dynamicearth.items.ItemDynamicEarth;
import karuberu.dynamicearth.items.ItemPeatMossSpecimen;
import karuberu.dynamicearth.items.ItemVase;
import karuberu.dynamicearth.items.crafting.CraftingHandler;
import karuberu.dynamicearth.items.crafting.RecipeManager;
import karuberu.dynamicearth.world.WorldGenMudMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
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
	version = "1.7.3",
	dependencies = "required-after:Forge@[9.10.0.776,); " +
		"required-after:KaruberuCore@[1.2.0];" +
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
    	sandySoil,
    	liquidMilk,
    	liquidSoup;
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
    	peatMossSpecimen;
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
	public static boolean
		showSnowyBottomSlabs,
		enableBottomSlabGrassKilling,
		enableDeepMud,
		enableDeepPeat,
		enableGrassBurning,
		enableMoreDestructiveMudslides,
		enableMyceliumTilling,
		useSimpleHydration,
		includeAdobe,
		includeBombs,
		includeAdobeGolems,
		includeDirtSlabs,
		includeFertileSoil,
		includeMudBrick,
		includePeat,
		includePermafrost,
		includeSandySoil,
		restoreDirtOnChunkLoad,
		useCustomCreativeTab;
	
	@EventHandler
	public void preInitialization(FMLPreInitializationEvent event) {
		ConfigurationManager.setConfigurationFile(new Configuration(event.getSuggestedConfigurationFile()));
		ConfigurationManager.configAdjustments();
		ConfigurationManager.configCrafting();
		ConfigurationManager.configFeatures();
		ConfigurationManager.configMaintenance();
		ConfigurationManager.configModHandling();
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
		BLOCKID_SANDYSOIL		= ConfigurationManager.getBlockID("SandySoil", BLOCKID_SANDYSOIL);
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
        DynamicEarth.registerBlocks();
        DynamicEarth.registerItems();
        DynamicEarth.registerEntities();
	}
	
	@EventHandler
	public void initialize(FMLInitializationEvent event) {
        FluidHandler.registerLiquids();
        DynamicEarth.registerDispenserHandlers();
        DynamicEarth.setBlockHarvestLevels();
        RecipeManager.addRecipes();
        RecipeManager.addSmelting();
        CraftingHandler.register();
        WorldGenMudMod.register();
    	FuelHandler.register();
        EventManager.register();
        CommonProxy.proxy.registerNames();
        CommonProxy.proxy.registerLocalizations();
        CommonProxy.proxy.registerRenderInformation();
        ModHandler.integrateThermalExpansion();
    }
	
	@EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
        ModHandler.integrateMods();
	}
	
	private static void setCreativeTabs() {
		if (DynamicEarth.useCustomCreativeTab) {
			BlockAdobe.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockAdobeSlab.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockAdobeStairs.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockAdobeWet.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockDirtSlab.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockFertileSoil.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockGrassSlab.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockMud.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockMudBrick.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockMudBrickWall.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockPeat.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockPermafrost.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			BlockSandySoil.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemBomb.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemClump.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemEarthbowlSoup.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemDynamicEarth.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemPeatMossSpecimen.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
			ItemVase.creativeTab = CreativeTabDynamicEarth.tabDynamicEarth;
		}
	}
    
    private static void registerBlocks() {
    	Block.dirt.setTickRandomly(true);
		DynamicEarth.setCreativeTabs();
		
        mud = new BlockMud(BLOCKID_MUD);
        farmland = new BlockDynamicFarmland(BLOCKID_FARMLAND);
	    GameRegistry.registerBlock(DynamicEarth.mud, ItemBlockMud.class, "mud");
	    GameRegistry.registerBlock(DynamicEarth.farmland, "farmland");
	    if (DynamicEarth.includeAdobe) {
		    adobe = new BlockAdobe(BLOCKID_ADOBE);
		    adobeWet = new BlockAdobeWet(BLOCKID_ADOBEWET);
		    adobeDoubleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBEDOUBLESLAB, true);
		    adobeSingleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBESLAB, false);    
		    adobeStairs = (new BlockAdobeStairs(BLOCKID_ADOBESTAIRS, adobe, 0)).setUnlocalizedName("adobeStairs");
		    liquidMilk = new BlockLiquid(BLOCKID_LIQUIDMILK, Material.water, BlockTexture.MILK).setUnlocalizedName("liquidMilk");
		    liquidSoup = new BlockLiquid(BLOCKID_LIQUIDSOUP, Material.water, BlockTexture.SOUP).setUnlocalizedName("liquidSoup");
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
	    	mudBrick = new ItemDynamicEarth(ITEMID_MUDBRICK, ItemIcon.MUDBRICK).setUnlocalizedName("mudBrick");
	    }
	    if (DynamicEarth.includeAdobe) {
		    adobeBlob = (ItemClump) new ItemClump(ITEMID_ADOBEBLOB, ItemIcon.ADOBEBLOB).setUnlocalizedName("adobeBlob");
		    adobeDust = (ItemClump) new ItemClump(ITEMID_ADOBEDUST, ItemIcon.ADOBEDUST).setWetClump(DynamicEarth.adobeBlob.itemID).setUnlocalizedName("adobeDust");
		    vaseRaw = new ItemDynamicEarth(ITEMID_VASERAW, ItemIcon.VASERAW).setUnlocalizedName("vaseRaw").setMaxStackSize(1);
		    vase = (ItemVase) new ItemVase(ITEMID_VASE).setUnlocalizedName("vase");
		    earthbowlRaw = new ItemDynamicEarth(ITEMID_EARTHBOWLRAW, ItemIcon.EARTHBOWLRAW).setUnlocalizedName("earthbowlRaw").setMaxStackSize(16);
		    earthbowl = new ItemDynamicEarth(ITEMID_EARTHBOWL, ItemIcon.EARTHBOWL).setUnlocalizedName("earthbowl");
		    earthbowlSoup = new ItemEarthbowlSoup(ITEMID_EARTHBOWLSOUP).setUnlocalizedName("earthbowlSoup");
		    if (DynamicEarth.includeBombs) {
			    bomb = new ItemBomb(ITEMID_BOMB, ItemIcon.BOMB).setUnlocalizedName("bomb");
			    bombLit = new ItemBombLit(ITEMID_BOMBLIT, ItemIcon.BOMBLIT).setUnlocalizedName("bombLit");    	
		    }
	    }
	    if (DynamicEarth.includePeat) {
	    	peatClump = (ItemClump) new ItemClump(ITEMID_PEATCLUMP, ItemIcon.PEATCLUMP).setUnlocalizedName("peatClump");
	    	peatBrick = new ItemDynamicEarth(ITEMID_PEATBRICK, ItemIcon.PEATBRICK).setUnlocalizedName("peatBrick");
	    	peatMossSpecimen = (new ItemPeatMossSpecimen(ITEMID_PEATMOSSSPECIMEN, ItemIcon.PEATMOSSSPECIMEN)).setUnlocalizedName("peatMossSpecimen");
	    }
	}
    
    public static void registerEntities() {
    	EntityRegistry.registerModEntity(EntityFallingBlock.class, "fallingBlock", 0, instance, 250, 5, true);
        EntityRegistry.registerModEntity(EntityMudball.class, "mudball", 1, instance, 250, 1, true);
        if (DynamicEarth.includeAdobe) {
	        if (DynamicEarth.includeAdobeGolems) {
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
