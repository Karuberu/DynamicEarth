package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.BlockDispenser;
import net.minecraft.src.BlockHalfSlab;
import net.minecraft.src.BlockWall;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.GameRules;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemSlab;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

@Mod(
	modid = "karuberu-mudMod",
	name = "Mud Mod",
	version = "1.4.0"
)
@NetworkMod(
	clientSideRequired = true,
	serverSideRequired = false
)
public class MudMod {
	public static Block
    	mud,
    	permafrost,
    	adobeWet,
     	adobe,
    	blockMudBrick,
    	adobeStairs,
    	mudBrickStairs,
    	mudBrickWall;
	public static BlockHalfSlab
		adobeSingleSlab,
		adobeDoubleSlab,
		dirtSlab;
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
    	bomb;
	public static ItemVase
		vase,
		vaseWater;
	public static enum BlockTexture {
		MUD,
		MUDWET,
		ADOBEWET,
		ADOBEDRY,
		MUDBRICK,
		PERMAFROST
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
		EARTHBOWLSOUP
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
		ITEMID_BOMB					= ITEMID_EARTHBOWLSOUP+1;

	public static String
		terrainFile = "/karuberu/mods/mudmod/mudTerrain.png",
		itemsFile = "/karuberu/mods/mudmod/mudItems.png",
		clayGolemFile = "/karuberu/mods/mudmod/clayGolem.png";
	
	@PreInit
	public void loadConfiguration(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		WorldGenManager.doGenerateMud = config.get("Terrain Generation", "doGenerateMud", true).getBoolean(true);
		WorldGenManager.doGeneratePermafrost = config.get("Terrain Generation", "doGeneratePermafrost", true).getBoolean(true);
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
		config.save();
	}
	
    @Init
	public void initialize(FMLInitializationEvent event) {
        mud = (new BlockMud(BLOCKID_MUD, BlockTexture.MUD.ordinal()));
	    adobe = (new BlockAdobe(BLOCKID_ADOBE, BlockTexture.ADOBEDRY.ordinal()));
	    adobeWet = (new BlockAdobeWet(BLOCKID_ADOBEWET, BlockTexture.ADOBEWET.ordinal()));
	    blockMudBrick = (new BlockMudBrick(BLOCKID_MUDBRICKBLOCK, BlockTexture.MUDBRICK.ordinal()));
	    adobeDoubleSlab = (BlockHalfSlab) (new BlockAdobeSlab(BLOCKID_ADOBEDOUBLESLAB, true));
	    adobeSingleSlab = (BlockHalfSlab) (new BlockAdobeSlab(BLOCKID_ADOBESINGLESLAB, false));    
	    adobeStairs = (new BlockAdobeStairs(BLOCKID_ADOBESTAIRS, adobe, BlockTexture.ADOBEDRY.ordinal())).setBlockName("adobeStairs");
	    mudBrickStairs = (new BlockAdobeStairs(BLOCKID_MUDBRICKSTAIRS, blockMudBrick, BlockTexture.MUDBRICK.ordinal())).setBlockName("mudBrickStairs");    
	    mudBrickWall = (new BlockMudBrickWall(BLOCKID_MUDBRICKWALL, blockMudBrick)).setBlockName("mudBrickWall");    
	    permafrost = (new BlockPermafrost(BLOCKID_PERMAFROST, BlockTexture.PERMAFROST.ordinal()));
	    dirtSlab = (BlockHalfSlab) (new BlockDirtSlab(BLOCKID_DIRTSLAB, false));    
	    mudBlob = (new ItemMudBlob(ITEMID_MUDBLOB)).setIconCoord(ItemIcon.MUDBLOB.ordinal(), 0).setItemName("mudBlob");
	    mudBrick = (new ItemMudMod(ITEMID_MUDBRICK)).setIconCoord(ItemIcon.MUDBRICK.ordinal(), 0).setItemName("mudBrick");
	    adobeDust = (new ItemAdobeDry(ITEMID_ADOBEDUST)).setIconCoord(ItemIcon.ADOBEDUST.ordinal(), 0).setItemName("adobeLump");
	    adobeBlob = (new ItemMudMod(ITEMID_ADOBEBLOB)).setIconCoord(ItemIcon.ADOBEBLOB.ordinal(), 0).setItemName("adobeBlob");
	    vaseRaw = (new ItemMudMod(ITEMID_VASERAW)).setIconCoord(ItemIcon.VASERAW.ordinal(), 0).setItemName("vaseRaw").setMaxStackSize(1);
	    vase = (ItemVase) (new ItemVase(ITEMID_VASE, 0)).setIconCoord(ItemIcon.VASE.ordinal(), 0).setItemName("vase");
	    vaseWater = (ItemVase) (new ItemVase(ITEMID_VASEWATER, Block.waterMoving.blockID)).setIconCoord(ItemIcon.VASEWATER.ordinal(), 0).setItemName("vaseWater");
	    vaseMilk = (new ItemVaseMilk(ITEMID_VASEMILK)).setIconCoord(ItemIcon.VASEMILK.ordinal(), 0).setItemName("vaseMilk");
	    earthbowlRaw = (new ItemMudMod(ITEMID_EARTHBOWLRAW)).setIconCoord(ItemIcon.EARTHBOWLRAW.ordinal(), 0).setItemName("earthbowlRaw").setMaxStackSize(16);
	    earthbowl = (new ItemEarthbowl(ITEMID_EARTHBOWL)).setIconCoord(ItemIcon.EARTHBOWL.ordinal(), 0).setItemName("earthbowl");
	    earthbowlSoup = (new ItemEarthbowlSoup(ITEMID_EARTHBOWLSOUP, 8)).setIconCoord(ItemIcon.EARTHBOWLSOUP.ordinal(), 0).setItemName("earthbowlSoup");
	    bomb = (new ItemBomb(ITEMID_BOMB)).setIconCoord(ItemIcon.EARTHBOWL.ordinal(), 0).setItemName("bomb");
        GameRegistry.registerBlock(mud);
        GameRegistry.registerBlock(permafrost);
        GameRegistry.registerBlock(adobeWet);
        GameRegistry.registerBlock(adobe);
        GameRegistry.registerBlock(blockMudBrick);
        GameRegistry.registerBlock(adobeSingleSlab, ItemAdobeSlab.class);
        GameRegistry.registerBlock(adobeDoubleSlab, ItemAdobeSlab.class);
        GameRegistry.registerBlock(adobeStairs);
        GameRegistry.registerBlock(mudBrickStairs);
        GameRegistry.registerBlock(mudBrickWall);
        GameRegistry.registerBlock(dirtSlab, ItemDirtSlab.class);
        BlockDispenser.dispenseBehaviorRegistry.putObject(vase, new BehaviorVaseEmptyDispense());
        BlockDispenser.dispenseBehaviorRegistry.putObject(vaseWater, new BehaviorVaseFullDispense());
        BlockDispenser.dispenseBehaviorRegistry.putObject(mudBlob, new BehaviorMudballDispense());
        EntityRegistry.registerModEntity(EntityMudball.class, "mudball", 0, this, 250, 1, true);
        EntityRegistry.registerModEntity(EntityClayGolem.class, "clayGolem", 1, this, 250, 5, true);
        EntityRegistry.registerModEntity(EntityBomb.class, "bomb", 2, this, 250, 1, true);
        CommonProxy.proxy.registerNames();
        CommonProxy.proxy.registerLocalizations();
        CommonProxy.proxy.registerRenderInformation();
        RecipeManager.addRecipes();
        RecipeManager.addSmelting();
        WorldGenManager.registerWorldGen();
    }
}
