package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "karuberu-mudMod", name = "MudMod", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MudMod {

	public static Block
		peatGrass,
    	mud,
    	adobeWet,
     	adobe,
    	blockMudBrick,
    	blockPeat,
    	muddyWaterStill,
    	muddyWaterMoving;
	public static Item
    	mudBlob,
    	mudBrick,
    	adobeBlob,
    	adobeDust,
    	vaseRaw,
    	vase,
    	vaseWater,
    	vaseMilk,
    	earthbowl,
    	earthbowlRaw,
    	earthbowlSoup;
	public static enum BlockTexture {
		MUD, MUDWET, ADOBEWET, ADOBEDRY, MUDBRICK, PEAT
	}
	public static enum ItemIcon {
		MUDBLOB, MUDBRICK, ADOBEDUST, ADOBEBLOB, VASERAW, VASE, VASEWATER, VASEMILK, EARTHBOWLRAW, EARTHBOWL, EARTHBOWLSOUP
	}
	private static enum BlockID {
		MUD, ADOBEWET, ADOBE, MUDBRICK, PEAT, MUDDYWATERFLOWING, MUDDYWATERSTILL, GRASS
	}
	private static enum ItemID {
		MUDBLOB, MUDBRICK, ADOBEDUST, ADOBEBLOB, VASERAW, VASE, VASEWATER, VASEMILK, EARTHBOWLRAW, EARTHBOWL, EARTHBOWLSOUP
	}
	public static final int IDOFFSET = 200;
	protected static String
		terrainFile = "/karuberu/mods/mudmod/mudTerrain.png",
		itemsFile = "/karuberu/mods/mudmod/mudItems.png";
	
    @Init
	public void load(FMLInitializationEvent event) {
    	peatGrass = (new BlockGrass_mod(BlockID.GRASS.ordinal()+IDOFFSET)).setHardness(0.5F).setStepSound(Block.soundGravelFootstep).setBlockName("peatGrass");
    	mud = (new BlockMud(BlockID.MUD.ordinal()+IDOFFSET, BlockTexture.MUD.ordinal())).setHardness(0.5F).setStepSound(Block.soundGravelFootstep).setBlockName("mud");
	    adobeWet = (new BlockAdobeWet(BlockID.ADOBEWET.ordinal()+IDOFFSET, BlockTexture.ADOBEWET.ordinal())).setHardness(0.5F).setStepSound(Block.soundGravelFootstep).setBlockName("adobeWet");
	    adobe = (new BlockAdobe(BlockID.ADOBE.ordinal()+IDOFFSET, BlockTexture.ADOBEDRY.ordinal())).setHardness(1.5F).setResistance(5.0F).setStepSound(Block.soundStoneFootstep).setBlockName("adobeDry");
	    blockMudBrick = (new BlockMudBrick(BlockID.MUDBRICK.ordinal()+IDOFFSET, BlockTexture.MUDBRICK.ordinal())).setHardness(1.5F).setResistance(5.0F).setStepSound(Block.soundStoneFootstep).setBlockName("blockMudBrick");
	    blockPeat = (new BlockPeat(BlockID.PEAT.ordinal()+IDOFFSET)).setHardness(0.5F).setStepSound(Block.soundGravelFootstep).setBlockName("blockPeat");
	    muddyWaterMoving = (new BlockMuddyFlowing(BlockID.MUDDYWATERFLOWING.ordinal()+IDOFFSET, Material.water)).setHardness(100.0F).setLightOpacity(3).setBlockName("water");
	    muddyWaterStill = (new BlockMuddyStationary(BlockID.MUDDYWATERSTILL.ordinal()+IDOFFSET, Material.water)).setHardness(100.0F).setLightOpacity(3).setBlockName("water");
	    mudBlob = (new ItemMudBlob(ItemID.MUDBLOB.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.MUDBLOB.ordinal(), 0).setItemName("mudBlob");
	    mudBrick = (new ItemMudBrick(ItemID.MUDBRICK.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.MUDBRICK.ordinal(), 0).setItemName("mudBrick");
	    adobeDust = (new ItemAdobeDry(ItemID.ADOBEDUST.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.ADOBEDUST.ordinal(), 0).setItemName("adobeLump");
	    adobeBlob = (new ItemMudBrick(ItemID.ADOBEBLOB.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.ADOBEBLOB.ordinal(), 0).setItemName("adobeBlob");
	    vaseRaw = (new ItemMudBrick(ItemID.VASERAW.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.VASERAW.ordinal(), 0).setItemName("vaseRaw").setMaxStackSize(1);
	    vase = (new ItemVase(ItemID.VASE.ordinal()+IDOFFSET, 0)).setIconCoord(ItemIcon.VASE.ordinal(), 0).setItemName("vase");
	    vaseWater = (new ItemVase(ItemID.VASEWATER.ordinal()+IDOFFSET, Block.waterMoving.blockID)).setIconCoord(ItemIcon.VASEWATER.ordinal(), 0).setItemName("vaseWater").setContainerItem(vase);
	    vaseMilk = (new ItemVaseMilk(ItemID.VASEMILK.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.VASEMILK.ordinal(), 0).setItemName("vaseMilk").setContainerItem(vase);
	    earthbowlRaw = (new ItemMudBrick(ItemID.EARTHBOWLRAW.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.EARTHBOWLRAW.ordinal(), 0).setItemName("earthbowlRaw").setMaxStackSize(16);
	    earthbowl = (new ItemMudBrick(ItemID.EARTHBOWL.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.EARTHBOWL.ordinal(), 0).setItemName("earthbowl");
	    earthbowlSoup = (new ItemEarthbowlSoup(ItemID.EARTHBOWLSOUP.ordinal()+IDOFFSET, 8)).setIconCoord(ItemIcon.EARTHBOWLSOUP.ordinal(), 0).setItemName("earthbowlSoup").setContainerItem(vase);
        LanguageRegistry.addName(peatGrass, "Peat Grass");
        LanguageRegistry.addName(mud, "Mud");
        LanguageRegistry.addName(mudBlob, "Mud Blob");
        LanguageRegistry.addName(mudBrick, "Mud Brick");
        LanguageRegistry.addName(adobeWet, "Moist Adobe");
        LanguageRegistry.addName(adobe, "Adobe");
        LanguageRegistry.addName(blockMudBrick, "Mud Brick");
        LanguageRegistry.addName(blockPeat, "Peat");
        LanguageRegistry.addName(adobeBlob, "Moist Adobe Blob");
        LanguageRegistry.addName(adobeDust, "Adobe Dust");
        LanguageRegistry.addName(vaseRaw, "Unfired Vase");
        LanguageRegistry.addName(vase, "Vase");
        LanguageRegistry.addName(vaseWater, "Water Vase");
        LanguageRegistry.addName(vaseMilk, "Milk Vase");
        LanguageRegistry.addName(earthbowlRaw, "Unfired Bowl");
        LanguageRegistry.addName(earthbowl, "Earthenware Bowl");
        LanguageRegistry.addName(earthbowlSoup, "Mushroom Stew");
        GameRegistry.registerBlock(peatGrass);
        GameRegistry.registerBlock(mud);
        GameRegistry.registerBlock(adobeWet);
        GameRegistry.registerBlock(adobe);
        GameRegistry.registerBlock(blockMudBrick);
        GameRegistry.registerBlock(blockPeat);
        GameRegistry.registerBlock(muddyWaterStill);
        GameRegistry.registerBlock(muddyWaterMoving);
        MinecraftForgeClient.preloadTexture(MudMod.terrainFile);
        MinecraftForgeClient.preloadTexture(MudMod.itemsFile);
        OreDictionary.registerOre(OreDictionary.getOreID("bucketWater"), Item.bucketWater);
        OreDictionary.registerOre(OreDictionary.getOreID("bucketWater"), vaseWater);
        OreDictionary.registerOre(OreDictionary.getOreID("bucketMilk"), Item.bucketMilk);
        OreDictionary.registerOre(OreDictionary.getOreID("bucketMilk"), vaseMilk);
        
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
