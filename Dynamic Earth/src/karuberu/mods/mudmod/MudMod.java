package karuberu.mods.mudmod;

import java.lang.reflect.Field;
import java.util.Arrays;

import karuberu.mods.mudmod.blocks.*;
import karuberu.mods.mudmod.entity.*;
import karuberu.mods.mudmod.items.*;
import karuberu.mods.mudmod.items.crafting.RecipeBombs;
import karuberu.mods.mudmod.items.crafting.RecipeManager;
import karuberu.mods.mudmod.world.WorldGenMudMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.storage.BackpackManager;

@Mod(
	useMetadata=true,
	modid = "karuberu-mudMod",
	name = "Karuberu's Mud Mod",
	version = "1.6.0",
	dependencies = "required-after:karuberu-core; before: IC2; before:Thaumcraft; after:Forestry")
@NetworkMod(
	clientSideRequired = true,
	serverSideRequired = false
)
public class MudMod {
	@Mod.Instance("karuberu-mudMod")
	public static MudMod instance;
	public static Block
    	mud,
    	mudDirt,
    	permafrost,
    	adobeWet,
     	adobe,
    	blockMudBrick,
    	adobeStairs,
    	mudBrickStairs,
    	mudBrickWall,
    	peatMoss,
    	peat,
    	peatDry;
	public static BlockHalfSlab
		adobeSingleSlab,
		adobeDoubleSlab,
		dirtSlab,
		dirtDoubleSlab,
		grassSlab,
		grassDoubleSlab;
	public static Item
    	mudBlob,
    	mudBrick,
    	adobeBlob,
    	adobeDust,
    	adobeSingleSlabItem,
    	adobeDoubleSlabItem,
    	vaseRaw,
    	vaseMilk,
    	earthbowl,
    	earthbowlRaw,
    	earthbowlSoup,
    	bomb,
    	bombLit,
    	peatClump,
    	peatBrick,
    	peatMossSpecimen;
	public static ItemVase
		vase,
		vaseWater;
	public static int
		overlayBlockRenderID;
	public static enum BlockTexture {
		MUD,
		MUDWET,
		ADOBEWET,
		ADOBEDRY,
		MUDBRICK,
		PERMAFROST,
		DIRT,
		GRASS,
		SNOW,
		GRASSSLABSIDE,
		GRASSSLABSNOWSIDE,
		GRASSSLABSIDEOVERLAY,
		MYCELIUM,
		MYCELIUMSLABSIDE,
		PEATMOSS,
		PEATMOSSYTOPOVERLAY,
		PEATMOSSYSIDEOVERLAY,
		PEAT,
		PEATSIDE1,
		PEATSIDE2,
		PEATSIDE3,
		PEATSIDE4,
		PEATSIDE5,
		PEATSIDE6,
		PEATSIDE7,
		PEATDRY
	}
	public static enum ItemIcon {
		MUDBLOB,
		MUDBRICK,
		ADOBEDUST,
		ADOBEBLOB,
		VASERAW,
		VASE,
		VASEWATER,
		VASEMILK,
		EARTHBOWLRAW,
		EARTHBOWL,
		EARTHBOWLSOUP,
		BOMB,
		PEATCLUMP,
		PEATBRICK,
		PEATMOSSSPECIMEN;
	}
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
		BLOCKID_PEATDRY				= BLOCKID_PEAT+1,
		ITEMID_MUDBLOB				= 10000,
		ITEMID_MUDBRICK				= ITEMID_MUDBLOB+1,
		ITEMID_ADOBEDUST			= ITEMID_MUDBRICK+1,
		ITEMID_ADOBEBLOB			= ITEMID_ADOBEDUST+1,
		ITEMID_VASERAW				= ITEMID_ADOBEBLOB+1,
		ITEMID_VASE					= ITEMID_VASERAW+1,
		ITEMID_VASEWATER			= ITEMID_VASE+1,
		ITEMID_VASEMILK				= ITEMID_VASEWATER+1,
		ITEMID_EARTHBOWLRAW			= ITEMID_VASEMILK+1,
		ITEMID_EARTHBOWL			= ITEMID_EARTHBOWLRAW+1,
		ITEMID_EARTHBOWLSOUP		= ITEMID_EARTHBOWL+1,
		ITEMID_BOMB					= ITEMID_EARTHBOWLSOUP+1,
		ITEMID_BOMBLIT				= ITEMID_BOMB+1,
		ITEMID_PEATMOSSSPECIMEN		= ITEMID_BOMBLIT+1,
		ITEMID_PEATCLUMP			= ITEMID_PEATMOSSSPECIMEN+1,
		ITEMID_PEATBRICK			= ITEMID_PEATCLUMP+1;
	public static boolean
		showSnowyBottomSlabs,
		enableDeepMud,
		enableDeepPeat,
		enableGrassBurning,
		includeAdobe,
		includeBombs,
		includeClayGolems,
		includeDirtSlabs,
		includeMudBrick,
		includePeat,
		includePermafrost,
		restoreDirtOnChunkLoad,
		enableForestryIntegration;
	public static final String
		terrainFile = "/karuberu/mods/mudmod/mudTerrain.png",
		itemsFile = "/karuberu/mods/mudmod/mudItems.png",
		clayGolemFile = "/karuberu/mods/mudmod/clayGolem.png";
	
	@PreInit
	public void loadConfiguration(FMLPreInitializationEvent event) {       
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		RecipeManager.canCraftBombs = config.get("Crafting", "canCraftBombs", true).getBoolean(true);
		RecipeManager.canCraftMossyStone = config.get("Crafting", "canCraftMossyStone", true).getBoolean(true);
		MudMod.enableDeepMud = config.get("Features", "enableDeepMud", true).getBoolean(true);
		MudMod.enableDeepPeat = config.get("Features", "enableDeepPeat", true).getBoolean(true);
		MudMod.enableGrassBurning = config.get("Features", "enableGrassBurning", true).getBoolean(true);
		MudMod.includeAdobe = config.get("Features", "includeAdobe", true).getBoolean(true);
		MudMod.includeBombs = config.get("Features", "includeBombs", true).getBoolean(true);
		MudMod.includeClayGolems = config.get("Features", "includeClayGolems", true).getBoolean(true);
		MudMod.includeDirtSlabs = config.get("Features", "includeDirtSlabs", true).getBoolean(true);
		MudMod.includeMudBrick = config.get("Features", "includeMudBrick", true).getBoolean(true);
		MudMod.includePeat = config.get("Features", "includePeat", true).getBoolean(true);
		MudMod.includePermafrost = config.get("Features", "includePermafrost", true).getBoolean(true);
		MudMod.showSnowyBottomSlabs = config.get("Features", "showSnowyBottomSlabs", true).getBoolean(true);
		MudMod.restoreDirtOnChunkLoad = config.get("Maintenance", "restoreDirtOnChunkLoad", false).getBoolean(false);
		WorldGenMudMod.doGenerateMud = config.get("Terrain Generation", "doGenerateMud", true).getBoolean(true);
		WorldGenMudMod.doGeneratePermafrost = config.get("Terrain Generation", "doGeneratePermafrost", true).getBoolean(true);
		WorldGenMudMod.doGeneratePeat = config.get("Terrain Generation", "doGeneratePeat", true).getBoolean(true);
		FuelHandler.peatBurnTime = config.get("Adjustments", "peatBurnTime", FuelHandler.peatBurnTime).getInt();
		RecipeBombs.maxGunpowder = config.get("Adjustments", "maxGunpowderForBombs", RecipeBombs.maxGunpowder).getInt();
		ItemBombLit.maxFuseLength = config.get("Adjustments", "maxFuseLengthForBombs", ItemBombLit.maxFuseLength).getInt();
		MudMod.enableForestryIntegration = config.get("Forestry", "enableIntegration", true).getBoolean(true);
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
		BLOCKID_PEATDRY			= config.getBlock("PeatDry", BLOCKID_PEATDRY).getInt();
		ITEMID_MUDBLOB			= config.getItem("MudBlob", ITEMID_MUDBLOB).getInt();
		ITEMID_MUDBRICK			= config.getItem("MudBrick", ITEMID_MUDBRICK).getInt();
		ITEMID_ADOBEDUST		= config.getItem("AdobeDust", ITEMID_ADOBEDUST).getInt();
		ITEMID_ADOBEBLOB		= config.getItem("AdobeBlob", ITEMID_ADOBEBLOB).getInt();
		ITEMID_VASERAW			= config.getItem("VaseRaw", ITEMID_VASERAW).getInt();
		ITEMID_VASE				= config.getItem("Vase", ITEMID_VASE).getInt();
		ITEMID_VASEWATER		= config.getItem("VaseWater", ITEMID_VASEWATER).getInt();
		ITEMID_VASEMILK			= config.getItem("VaseMilk", ITEMID_VASEMILK).getInt();
		ITEMID_EARTHBOWLRAW		= config.getItem("EarthbowlRaw", ITEMID_EARTHBOWLRAW).getInt();
		ITEMID_EARTHBOWL		= config.getItem("Earthbowl", ITEMID_EARTHBOWL).getInt();
		ITEMID_EARTHBOWLSOUP	= config.getItem("EarthbowlSoup", ITEMID_EARTHBOWLSOUP).getInt();
		ITEMID_BOMB				= config.getItem("Bomb", ITEMID_BOMB).getInt();
		ITEMID_BOMBLIT			= config.getItem("BombLit", ITEMID_BOMBLIT).getInt();
		ITEMID_PEATCLUMP		= config.getItem("PeatClump", ITEMID_PEATCLUMP).getInt();
		ITEMID_PEATBRICK		= config.getItem("PeatBrick", ITEMID_PEATBRICK).getInt();
		ITEMID_PEATMOSSSPECIMEN	= config.getItem("PeatMossSpecimen", ITEMID_PEATMOSSSPECIMEN).getInt();
		config.save();
	}
	
    @Init
	public void initialize(FMLInitializationEvent event) {
        MudMod.registerBlocks();
        MudMod.registerItems();
        MudMod.registerEntities();
        MudMod.registerDispenserHandlers();
        MudMod.setBlockHarvestLevels();
        RecipeManager.addRecipes();
        RecipeManager.addSmelting();
        CommonProxy.proxy.registerNames();
        CommonProxy.proxy.registerLocalizations();
        CommonProxy.proxy.registerRenderInformation();
        WorldGenMudMod.register();
    	FuelHandler.register();
        EventHandler.register();
    }
    
    private static void registerBlocks() {
    	Block.dirt.setTickRandomly(true);
        mud = new BlockMud(BLOCKID_MUD, BlockTexture.MUD.ordinal());
	    GameRegistry.registerBlock(MudMod.mud, "mud");
	    if (MudMod.includeAdobe) {
		    adobe = new BlockAdobe(BLOCKID_ADOBE, BlockTexture.ADOBEDRY.ordinal());
		    adobeWet = new BlockAdobeWet(BLOCKID_ADOBEWET, BlockTexture.ADOBEWET.ordinal());
		    adobeDoubleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBEDOUBLESLAB, true);
		    adobeSingleSlab = (BlockHalfSlab) new BlockAdobeSlab(BLOCKID_ADOBESINGLESLAB, false);    
		    adobeStairs = (new BlockAdobeStairs(BLOCKID_ADOBESTAIRS, adobe, 0)).setBlockName("adobeStairs");
	        GameRegistry.registerBlock(MudMod.adobeWet, "adobeWet");
	        GameRegistry.registerBlock(MudMod.adobe, "adobe");
	        GameRegistry.registerBlock(MudMod.adobeSingleSlab, ItemAdobeSlab.class, "adobeSingleSlab");
	        GameRegistry.registerBlock(MudMod.adobeDoubleSlab, ItemAdobeSlab.class, "adobeDoubleSlab");
	        GameRegistry.registerBlock(MudMod.adobeStairs, "adobeStairs");
	    }
	    if (MudMod.includeMudBrick) {
		    blockMudBrick = new BlockMudBrick(BLOCKID_MUDBRICKBLOCK, BlockTexture.MUDBRICK.ordinal());
		    mudBrickStairs = (new BlockAdobeStairs(BLOCKID_MUDBRICKSTAIRS, blockMudBrick, 0)).setBlockName("mudBrickStairs");    
		    mudBrickWall = (new BlockMudBrickWall(BLOCKID_MUDBRICKWALL, blockMudBrick)).setBlockName("mudBrickWall");    
			GameRegistry.registerBlock(MudMod.blockMudBrick, "blockMudBrick");
			GameRegistry.registerBlock(MudMod.mudBrickStairs, "mudBrickStairs");
			GameRegistry.registerBlock(MudMod.mudBrickWall, "mudBrickWall");
	    }
	    if (MudMod.includePermafrost) {
	    	permafrost = new BlockPermafrost(BLOCKID_PERMAFROST, BlockTexture.PERMAFROST.ordinal());
	        GameRegistry.registerBlock(MudMod.permafrost, "permafrost");
	    }
	    if (MudMod.includeDirtSlabs) {
		    dirtSlab = (BlockHalfSlab) (new BlockDirtSlab(BLOCKID_DIRTSLAB, false)).setBlockName("dirtSlab");    
		    dirtDoubleSlab = (BlockHalfSlab) (new BlockDirtSlab(BLOCKID_DIRTDOUBLESLAB, true)).setBlockName("dirtDoubleSlab");    
		    grassSlab = (BlockHalfSlab) (new BlockGrassSlab(BLOCKID_GRASSSLAB, false)).setBlockName("grassSlab");
		    grassDoubleSlab = (BlockHalfSlab) (new BlockGrassSlab(BLOCKID_GRASSDOUBLESLAB, true)).setBlockName("grassDoubleSlab");
	        GameRegistry.registerBlock(MudMod.dirtSlab, ItemDirtSlab.class, "dirtSlab");
	        GameRegistry.registerBlock(MudMod.dirtDoubleSlab, ItemDirtSlab.class, "dirtDoubleSlab");
	        GameRegistry.registerBlock(MudMod.grassSlab, ItemGrassSlab.class, "grassSlab");
	        GameRegistry.registerBlock(MudMod.grassDoubleSlab, ItemGrassSlab.class, "grassDoubleSlab");
	    }
	    if (MudMod.includePeat) {
		    peatMoss = new BlockPeatMoss(BLOCKID_PEATMOSS, BlockTexture.PEATMOSS.ordinal());
			peat = new BlockPeat(BLOCKID_PEAT, BlockTexture.PEAT.ordinal());
			peatDry = new BlockPeatDry(BLOCKID_PEATDRY, BlockTexture.PEATDRY.ordinal());
	        GameRegistry.registerBlock(MudMod.peatMoss, "peatMoss");
	        GameRegistry.registerBlock(MudMod.peat, "peat");
	        GameRegistry.registerBlock(MudMod.peatDry, "peatDry");
	    }
	}
    
    private static void registerItems() {
	    mudBlob = (new ItemMudBlob(ITEMID_MUDBLOB, ItemIcon.MUDBLOB.ordinal())).setItemName("mudBlob");
	    if (MudMod.includeMudBrick) {
	    	mudBrick = (new ItemMudMod(ITEMID_MUDBRICK, ItemIcon.MUDBRICK.ordinal())).setItemName("mudBrick").setCreativeTab(CreativeTabs.tabMaterials);
	    }
	    if (MudMod.includeAdobe) {
		    adobeDust = (new ItemAdobeDust(ITEMID_ADOBEDUST, ItemIcon.ADOBEDUST.ordinal())).setItemName("adobeDust").setCreativeTab(CreativeTabs.tabMaterials);
		    adobeBlob = (new ItemMudMod(ITEMID_ADOBEBLOB, ItemIcon.ADOBEBLOB.ordinal())).setItemName("adobeBlob").setCreativeTab(CreativeTabs.tabMaterials);
		    vaseRaw = (new ItemMudMod(ITEMID_VASERAW, ItemIcon.VASERAW.ordinal())).setItemName("vaseRaw").setMaxStackSize(1).setCreativeTab(CreativeTabs.tabMaterials);
		    vase = (ItemVase) (new ItemVase(ITEMID_VASE, 0, ItemIcon.VASE.ordinal())).setItemName("vase");
		    vaseWater = (ItemVase) (new ItemVase(ITEMID_VASEWATER, Block.waterMoving.blockID, ItemIcon.VASEWATER.ordinal())).setItemName("vaseWater");
		    vaseMilk = (new ItemVaseMilk(ITEMID_VASEMILK, ItemIcon.VASEMILK.ordinal())).setItemName("vaseMilk");
		    earthbowlRaw = (new ItemMudMod(ITEMID_EARTHBOWLRAW, ItemIcon.EARTHBOWLRAW.ordinal())).setItemName("earthbowlRaw").setMaxStackSize(16).setCreativeTab(CreativeTabs.tabMaterials);
		    earthbowl = (new ItemMudMod(ITEMID_EARTHBOWL, ItemIcon.EARTHBOWL.ordinal())).setItemName("earthbowl").setCreativeTab(CreativeTabs.tabMaterials);
		    earthbowlSoup = (new ItemEarthbowlSoup(ITEMID_EARTHBOWLSOUP, ItemIcon.EARTHBOWLSOUP.ordinal())).setItemName("earthbowlSoup");
		    if (MudMod.includeBombs) {
			    bomb = (new ItemBomb(ITEMID_BOMB, ItemIcon.BOMB.ordinal())).setItemName("bomb");
			    bombLit = (new ItemBombLit(ITEMID_BOMBLIT, ItemIcon.BOMB.ordinal())).setItemName("bombLit");    	
		    }
	    }
	    if (MudMod.includePeat) {
	    	peatClump = (new ItemMudMod(ITEMID_PEATCLUMP, ItemIcon.PEATCLUMP.ordinal())).setItemName("peatClump").setCreativeTab(CreativeTabs.tabMaterials);
	    	peatBrick = (new ItemMudMod(ITEMID_PEATBRICK, ItemIcon.PEATBRICK.ordinal())).setItemName("peatBrick").setCreativeTab(CreativeTabs.tabMaterials);
	    	peatMossSpecimen = (new ItemPeatMossSpecimen(ITEMID_PEATMOSSSPECIMEN, ItemIcon.PEATMOSSSPECIMEN.ordinal())).setItemName("peatMossSpecimen");
	    }
	    if (MudMod.enableForestryIntegration
	    && BackpackManager.backpackItems != null) {
	    	if (BackpackManager.backpackItems.length >= 4) {
	    		BackpackManager.backpackItems[1].add(new ItemStack(MudMod.mud));
	    		BackpackManager.backpackItems[1].add(new ItemStack(MudMod.mudBlob));
	    		if (MudMod.includeDirtSlabs) {
		    		BackpackManager.backpackItems[1].add(new ItemStack(MudMod.dirtSlab));
		    		BackpackManager.backpackItems[1].add(new ItemStack(MudMod.grassSlab, 1, BlockGrassSlab.GRASS));
		    		BackpackManager.backpackItems[1].add(new ItemStack(MudMod.grassSlab, 1, BlockGrassSlab.MYCELIUM));
	    		}
		    	if (MudMod.includePermafrost) {
	    			BackpackManager.backpackItems[1].add(new ItemStack(MudMod.permafrost));
	    		}
	    		if (MudMod.includePeat) {
		    		BackpackManager.backpackItems[2].add(new ItemStack(MudMod.peat));
		    		BackpackManager.backpackItems[2].add(new ItemStack(MudMod.peatDry));
		    		BackpackManager.backpackItems[2].add(new ItemStack(MudMod.peatClump));
		    		BackpackManager.backpackItems[2].add(new ItemStack(MudMod.peatBrick));
		    		BackpackManager.backpackItems[2].add(new ItemStack(MudMod.peatMossSpecimen));
	    		}
	    	}
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
	        BlockDispenser.dispenseBehaviorRegistry.putObject(MudMod.vase, new BehaviorVaseEmptyDispense());
	        BlockDispenser.dispenseBehaviorRegistry.putObject(MudMod.vaseWater, new BehaviorVaseFullDispense());
	        if (MudMod.includeBombs) {
	        	BlockDispenser.dispenseBehaviorRegistry.putObject(MudMod.bomb, new BehaviorBombDispense());
	        }
    	}
	}
	
    public static void setBlockHarvestLevels() {
    	final int
    		WOOD = EnumToolMaterial.WOOD.getHarvestLevel();
        MinecraftForge.setBlockHarvestLevel(MudMod.mud, "shovel", WOOD);
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
        }
        if (MudMod.includeDirtSlabs) {
	        MinecraftForge.setBlockHarvestLevel(MudMod.dirtSlab, "shovel", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.dirtSlab, "shovel", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.grassSlab, "shovel", WOOD);
        }
        if (MudMod.includePeat) {
	        MinecraftForge.setBlockHarvestLevel(MudMod.peatMoss, "shovel", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.peat, "shovel", WOOD);
	        MinecraftForge.setBlockHarvestLevel(MudMod.peatDry, "shovel", WOOD);
        }
	}

}
