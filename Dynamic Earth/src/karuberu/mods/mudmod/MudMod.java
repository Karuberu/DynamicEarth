package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.BlockDispenser;
import net.minecraft.src.BlockHalfSlab;
import net.minecraft.src.BlockWall;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemSlab;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(
	modid = "karuberu-mudMod",
	name = "Mud Mod",
	version = "1.2.0"
)
@NetworkMod(
	clientSideRequired = true,
	serverSideRequired = false
)
public class MudMod {

	public static Block
    	mud,
    	adobeWet,
     	adobe,
    	blockMudBrick,
    	adobeStairs,
    	mudBrickStairs,
    	mudBrickWall,
    	muddyWaterStill,
    	muddyWaterMoving;
	public static BlockHalfSlab
		adobeSingleSlab,
		adobeDoubleSlab;
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
    	earthbowlSoup;
	public static ItemVase
		vase,
		vaseWater;
	public static enum BlockTexture {
		MUD,
		MUDWET,
		ADOBEWET,
		ADOBEDRY,
		MUDBRICK
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
	private static enum BlockID {
		MUD,
		ADOBEWET,
		ADOBE,
		MUDBRICK,
		ADOBEDOUBLESLAB,
		ADOBESINGLESLAB,
		ADOBESTAIRS,
		MUDBRICKSTAIRS,
		MUDBRICKWALL,
		MUDDYWATERFLOWING,
		MUDDYWATERSTILL
	}
	private static enum ItemID {
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
	public static final int
		IDOFFSET = 200;
	public static String
		terrainFile = "/karuberu/mods/mudmod/mudTerrain.png",
		itemsFile = "/karuberu/mods/mudmod/mudItems.png",
		clayGolemFile = "/karuberu/mods/mudmod/clayGolem.png";
	
    @Init
	public void load(FMLInitializationEvent event) {
        mud = (new BlockMud(BlockID.MUD.ordinal()+IDOFFSET, BlockTexture.MUD.ordinal()));
	    adobeWet = (new BlockAdobeWet(BlockID.ADOBEWET.ordinal()+IDOFFSET, BlockTexture.ADOBEWET.ordinal()));
	    adobe = (new BlockAdobe(BlockID.ADOBE.ordinal()+IDOFFSET, BlockTexture.ADOBEDRY.ordinal()));
	    blockMudBrick = (new BlockMudBrick(BlockID.MUDBRICK.ordinal()+IDOFFSET, BlockTexture.MUDBRICK.ordinal()));
	    adobeDoubleSlab = (BlockHalfSlab) (new BlockAdobeSlab(BlockID.ADOBEDOUBLESLAB.ordinal()+IDOFFSET, true));
	    adobeSingleSlab = (BlockHalfSlab) (new BlockAdobeSlab(BlockID.ADOBESINGLESLAB.ordinal()+IDOFFSET, false));    
	    adobeStairs = (new BlockAdobeStairs(BlockID.ADOBESTAIRS.ordinal()+IDOFFSET, adobe, BlockTexture.ADOBEDRY.ordinal())).setBlockName("adobeStairs");
	    mudBrickStairs = (new BlockAdobeStairs(BlockID.MUDBRICKSTAIRS.ordinal()+IDOFFSET, blockMudBrick, BlockTexture.MUDBRICK.ordinal())).setBlockName("mudBrickStairs");    
	    mudBrickWall = (new BlockMudBrickWall(BlockID.MUDBRICKWALL.ordinal()+IDOFFSET, blockMudBrick)).setBlockName("mudBrickWall");    
	    muddyWaterMoving = (new BlockMuddyFlowing(BlockID.MUDDYWATERFLOWING.ordinal()+IDOFFSET, Material.water));
	    muddyWaterStill = (new BlockMuddyStationary(BlockID.MUDDYWATERSTILL.ordinal()+IDOFFSET, Material.water));
	    mudBlob = (new ItemMudBlob(ItemID.MUDBLOB.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.MUDBLOB.ordinal(), 0).setItemName("mudBlob");
	    mudBrick = (new ItemMudMod(ItemID.MUDBRICK.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.MUDBRICK.ordinal(), 0).setItemName("mudBrick");
	    adobeDust = (new ItemAdobeDry(ItemID.ADOBEDUST.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.ADOBEDUST.ordinal(), 0).setItemName("adobeLump");
	    adobeBlob = (new ItemMudMod(ItemID.ADOBEBLOB.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.ADOBEBLOB.ordinal(), 0).setItemName("adobeBlob");
	    vaseRaw = (new ItemMudMod(ItemID.VASERAW.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.VASERAW.ordinal(), 0).setItemName("vaseRaw").setMaxStackSize(1);
	    vase = (ItemVase) (new ItemVase(ItemID.VASE.ordinal()+IDOFFSET, 0)).setIconCoord(ItemIcon.VASE.ordinal(), 0).setItemName("vase");
	    vaseWater = (ItemVase) (new ItemVase(ItemID.VASEWATER.ordinal()+IDOFFSET, Block.waterMoving.blockID)).setIconCoord(ItemIcon.VASEWATER.ordinal(), 0).setItemName("vaseWater");
	    vaseMilk = (new ItemVaseMilk(ItemID.VASEMILK.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.VASEMILK.ordinal(), 0).setItemName("vaseMilk");
	    earthbowlRaw = (new ItemMudMod(ItemID.EARTHBOWLRAW.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.EARTHBOWLRAW.ordinal(), 0).setItemName("earthbowlRaw").setMaxStackSize(16);
	    earthbowl = (new ItemMudMod(ItemID.EARTHBOWL.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.EARTHBOWL.ordinal(), 0).setItemName("earthbowl");
	    earthbowlSoup = (new ItemEarthbowlSoup(ItemID.EARTHBOWLSOUP.ordinal()+IDOFFSET, 8)).setIconCoord(ItemIcon.EARTHBOWLSOUP.ordinal(), 0).setItemName("earthbowlSoup");
        GameRegistry.registerBlock(mud);
        GameRegistry.registerBlock(adobeWet);
        GameRegistry.registerBlock(adobe);
        GameRegistry.registerBlock(blockMudBrick);
        GameRegistry.registerBlock(adobeSingleSlab, ItemAdobeSlab.class);
        GameRegistry.registerBlock(adobeDoubleSlab, ItemAdobeSlab.class);
        GameRegistry.registerBlock(adobeStairs);
        GameRegistry.registerBlock(mudBrickStairs);
        GameRegistry.registerBlock(mudBrickWall);
        GameRegistry.registerBlock(muddyWaterStill);
        GameRegistry.registerBlock(muddyWaterMoving);
        GameRegistry.registerDispenserHandler(vase);
        GameRegistry.registerDispenserHandler(vaseWater);
        LanguageRegistry.addName(mud, "Mud");
        LanguageRegistry.addName(mudBlob, "Mud Blob");
        LanguageRegistry.addName(mudBrick, "Mud Brick");
        LanguageRegistry.addName(adobeWet, "Moist Adobe");
        LanguageRegistry.addName(adobe, "Adobe");
        LanguageRegistry.addName(blockMudBrick, "Mud Brick");
        LanguageRegistry.addName(new ItemStack(adobeSingleSlab, 1, 0), "Adobe Slab");
        LanguageRegistry.addName(new ItemStack(adobeDoubleSlab, 1, 0), "Adobe Slab");
        LanguageRegistry.addName(new ItemStack(adobeSingleSlab, 1, 1), "Mud Brick Slab");
        LanguageRegistry.addName(new ItemStack(adobeDoubleSlab, 1, 1), "Mud Brick Slab");
        LanguageRegistry.addName(adobeStairs, "Adobe Stairs");
        LanguageRegistry.addName(mudBrickStairs, "Mud Brick Stairs");
        LanguageRegistry.addName(mudBrickWall, "Mud Brick Wall");
        LanguageRegistry.addName(adobeBlob, "Moist Adobe Blob");
        LanguageRegistry.addName(adobeDust, "Adobe Dust");
        LanguageRegistry.addName(vaseRaw, "Unfired Vase");
        LanguageRegistry.addName(vase, "Vase");
        LanguageRegistry.addName(vaseWater, "Water Vase");
        LanguageRegistry.addName(vaseMilk, "Milk Vase");
        LanguageRegistry.addName(earthbowlRaw, "Unfired Bowl");
        LanguageRegistry.addName(earthbowl, "Earthenware Bowl");
        LanguageRegistry.addName(earthbowlSoup, "Mushroom Stew");
        LanguageRegistry.instance().addStringLocalization("entity.karuberu-mudMod.clayGolem.name", "en_US", "Clay Golem");
        OreDictionary.registerOre(OreDictionary.getOreID("bucketWater"), Item.bucketWater);
        OreDictionary.registerOre(OreDictionary.getOreID("bucketWater"), vaseWater);
        OreDictionary.registerOre(OreDictionary.getOreID("bucketMilk"), Item.bucketMilk);
        OreDictionary.registerOre(OreDictionary.getOreID("bucketMilk"), vaseMilk);
        EntityRegistry.registerModEntity(EntityMudball.class, "mudball", 0, this, 250, 1, true);
        EntityRegistry.registerModEntity(EntityClayGolem.class, "clayGolem", 1, this, 250, 5, true);
        MinecraftForgeClient.preloadTexture(MudMod.terrainFile);
        MinecraftForgeClient.preloadTexture(MudMod.itemsFile);
        ClientProxy.registerRenderInformation();
        
        GameRegistry.addRecipe(
            	new ItemStack(mud, 1),
            	new Object[] {
    	            "##",
    	            "##",
    	            '#', mudBlob
    	        }
            );
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
        		new ItemStack(mud, 1),
        		true,
        		new Object[]{
    	            "#",
    	            "W",
    	            '#', Block.dirt,
    	            'W', "bucketWater",
    			}
        	));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
        		new ItemStack(mud, 4),
        		true,
        		new Object[]{
    	            " # ",
    	            "#W#",
    	            " # ",
    	            '#', Block.dirt,
    	            'W', "bucketWater",
    			}
        	));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
        		new ItemStack(mud, 8),
        		true,
        		new Object[]{
    	            "###",
    	            "#W#",
    	            "###",
    	            '#', Block.dirt,
    	            'W', "bucketWater",
    			}
        	));
        GameRegistry.addShapelessRecipe(
            	new ItemStack(mudBlob, 4),
            	new Object[] {mud}
            );
        GameRegistry.addRecipe(
	        	new ItemStack(adobeWet, 2),
	        	new Object[] {
		            "#X#",
		            "XCX",
		            "#X#",
		            '#', mudBlob,
		            'X', Item.wheat,
		            'C', Item.clay
		        }
	        );
        GameRegistry.addRecipe(
	        	new ItemStack(adobeWet, 2),
	        	new Object[] {
		            "#X#",
		            "XCX",
		            "#X#",
		            '#', mudBlob,
		            'X', Item.reed,
		            'C', Item.clay
		        }
	        );
        GameRegistry.addRecipe(
	        	new ItemStack(blockMudBrick, 1),
	        	new Object[] {
		            "##",
		            "##",
		            '#', mudBrick
		        }
	        );

        GameRegistry.addRecipe(
	        	new ItemStack(adobeSingleSlab, 6, 0),
	        	new Object[] {
		            "###",
		            '#', adobe
		        }
	        );
        GameRegistry.addRecipe(
	        	new ItemStack(adobeSingleSlab, 6, 1),
	        	new Object[] {
		            "###",
		            '#', blockMudBrick
		        }
	        );
        GameRegistry.addRecipe(
	        	new ItemStack(adobeStairs, 4),
	        	new Object[] {
		            "#  ",
		            "## ",
		            "###",
		            '#', adobe
		        }
	        );
        GameRegistry.addRecipe(
	        	new ItemStack(adobeStairs, 4),
	        	new Object[] {
		            "  #",
		            " ##",
		            "###",
		            '#', adobe
		        }
	        );
        GameRegistry.addRecipe(
	        	new ItemStack(mudBrickStairs, 4),
	        	new Object[] {
	        		"#  ",
	        		"## ",
		            "###",
		            '#', blockMudBrick
		        }
	        );
        GameRegistry.addRecipe(
	        	new ItemStack(mudBrickStairs, 4),
	        	new Object[] {
	        		"  #",
	        		" ##",
		            "###",
		            '#', blockMudBrick
		        }
	        );
        GameRegistry.addRecipe(
	        	new ItemStack(adobeWet, 1),
	        	new Object[] {
		            " # ",
		            "#W#",
		            " # ",
		            '#', adobeDust,
		            'W', Item.bucketWater
		        }
	        );
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
        		new ItemStack(adobeWet, 1),
        		true,
        		new Object[]{
		            " # ",
		            "#W#",
		            " # ",
		            '#', adobeDust,
    	            'W', "bucketWater",
    			}
        	));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
        		new ItemStack(adobeWet, 2),
        		true,
        		new Object[]{
		            "###",
		            "#W#",
		            "###",
		            '#', adobeDust,
    	            'W', "bucketWater",
    			}
        	));
        GameRegistry.addRecipe(
	        	new ItemStack(adobeBlob, 4),
	        	new Object[] {
		            "#",
		            '#', adobeWet
		        }
	        );
        GameRegistry.addRecipe(
    	        	new ItemStack(adobeWet, 1),
    	        	new Object[] {
    		            "##",
    		            "##",
    		            '#', adobeBlob
    		        }
    	        );
        GameRegistry.addRecipe(
    	        	new ItemStack(vaseRaw, 1),
    	        	new Object[] {
    		            "# #",
    		            "# #",
    		            " # ",
    		            '#', adobeBlob
    		        }
    	        );
        GameRegistry.addRecipe(
	        	new ItemStack(earthbowlRaw, 1),
	        	new Object[] {
		            "# #",
		            " # ",
		            '#', adobeBlob
		        }
	        );
        GameRegistry.addRecipe(
	        	new ItemStack(earthbowlRaw, 4),
	        	new Object[] {
		            "# #",
		            " # ",
		            '#', adobeWet
		        }
	        );
        GameRegistry.addShapelessRecipe(
    		new ItemStack(earthbowlSoup),
    		new Object[] {
    			earthbowl, Block.mushroomBrown, Block.mushroomRed
    		}
    	);
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
        	Item.cake,
    		true,
    		new Object[]{
	            "MMM",
	            "SES",
	            "WWW",
	            'M', "bucketMilk",
	            'S', Item.sugar,
	            'E', Item.egg,
	            'W', Item.wheat
			}
    	));
	    GameRegistry.addSmelting(adobeWet.blockID, new ItemStack(adobe), 0.1F);
	    GameRegistry.addSmelting(mud.blockID, new ItemStack(Block.dirt), 0.1F);
	    GameRegistry.addSmelting(mudBlob.shiftedIndex, new ItemStack(mudBrick), 0.1F);
	    GameRegistry.addSmelting(vaseRaw.shiftedIndex, new ItemStack(vase), 0.1F);
	    GameRegistry.addSmelting(earthbowlRaw.shiftedIndex, new ItemStack(earthbowl), 0.1F);
    }
}
