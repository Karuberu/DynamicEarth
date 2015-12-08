package karuberu.mods.mudmod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import net.minecraft.src.Block;
import net.minecraft.src.BlockDirt;
import net.minecraft.src.BlockFlowing;
import net.minecraft.src.BlockHalfSlab;
import net.minecraft.src.BlockReed;
import net.minecraft.src.BlockStairs;
import net.minecraft.src.BlockStationary;
import net.minecraft.src.BlockStep;
import net.minecraft.src.BlockWoodSlab;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemSoup;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.StepSound;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "karuberu-mudMod", name = "MudMod", version = "1.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MudMod {

	public static Block
    	mud,
    	adobeWet,
     	adobe,
    	mudBrickBlock,
    	muddyWaterStill,
    	muddyWaterFlowing;
	public static Item
    	mudBlob,
    	mudBrick,
    	adobeBlob,
    	adobeDust,
    	vaseRaw,
    	vase,
    	vaseWater,
    	earthbowl,
    	earthbowlRaw,
    	earthbowlSoup;
	public static enum BlockTexture {
		MUD, MUDWET, ADOBEWET, ADOBEDRY, MUDBRICK
	}
	public static enum ItemIcon {
		MUDBLOB, MUDBRICK, ADOBEDUST, ADOBEBLOB, VASERAW, VASE, VASEWATER, EARTHBOWLRAW, EARTHBOWL, EARTHBOWLSOUP
	}
	private static enum BlockID {
		MUD, ADOBEWET, ADOBE, MUDBRICK, MUDDYWATERSTILL, MUDDYWATERFLOWING
	}
	private static enum ItemID {
		MUDBLOB, MUDBRICK, ADOBEDUST, ADOBEBLOB, VASERAW, VASE, VASEWATER, EARTHBOWLRAW, EARTHBOWL, EARTHBOWLSOUP
	}
	private static final int IDOFFSET = 200;
	protected static String
		terrainFile = "/karuberu/mods/mudmod/mudTerrain.png",
		itemsFile = "/karuberu/mods/mudmod/mudItems.png";
		
    
    @Init
	public void load(FMLInitializationEvent event) {
    	mud = (new BlockMud(BlockID.MUD.ordinal()+IDOFFSET, BlockTexture.MUD.ordinal())).setHardness(0.5F).setStepSound(Block.soundGravelFootstep).setBlockName("mud");
	    adobeWet = (new BlockAdobeWet(BlockID.ADOBEWET.ordinal()+IDOFFSET, BlockTexture.ADOBEWET.ordinal())).setHardness(0.5F).setStepSound(Block.soundGravelFootstep).setBlockName("adobeWet");
	    adobe = (new BlockAdobe(BlockID.ADOBE.ordinal()+IDOFFSET, BlockTexture.ADOBEDRY.ordinal())).setHardness(1.5F).setResistance(5.0F).setStepSound(Block.soundStoneFootstep).setBlockName("adobeDry");
	    mudBrickBlock = (new BlockMudBrick(BlockID.MUDBRICK.ordinal()+IDOFFSET, BlockTexture.MUDBRICK.ordinal())).setHardness(1.5F).setResistance(5.0F).setStepSound(Block.soundStoneFootstep).setBlockName("mudBrickBlock");
	    muddyWaterStill = (new BlockMuddyStationary(BlockID.MUDDYWATERSTILL.ordinal()+IDOFFSET, Material.water)).setHardness(100.0F).setLightOpacity(3).setBlockName("water");
	    muddyWaterFlowing = (new BlockMuddyFlowing(BlockID.MUDDYWATERFLOWING.ordinal()+IDOFFSET, Material.water)).setHardness(100.0F).setLightOpacity(3).setBlockName("water");
	    mudBlob = (new ItemMudBlob(ItemID.MUDBLOB.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.MUDBLOB.ordinal(), 0).setItemName("mudBlob");
	    mudBrick = (new ItemMudBrick(ItemID.MUDBRICK.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.MUDBRICK.ordinal(), 0).setItemName("mudBrick");
	    adobeDust = (new ItemAdobeDry(ItemID.ADOBEDUST.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.ADOBEDUST.ordinal(), 0).setItemName("adobeLump");
	    adobeBlob = (new ItemMudBrick(ItemID.ADOBEBLOB.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.ADOBEBLOB.ordinal(), 0).setItemName("adobeBlob");
	    vaseRaw = (new ItemMudBrick(ItemID.VASERAW.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.VASERAW.ordinal(), 0).setItemName("vaseRaw").setMaxStackSize(1);
	    vase = (new ItemVase(ItemID.VASE.ordinal()+IDOFFSET, 0)).setIconCoord(ItemIcon.VASE.ordinal(), 0).setItemName("vase");
	    vaseWater = (new ItemVase(ItemID.VASEWATER.ordinal()+IDOFFSET, Block.waterMoving.blockID)).setIconCoord(ItemIcon.VASEWATER.ordinal(), 0).setItemName("vaseWater").setContainerItem(vase);
	    earthbowlRaw = (new ItemMudBrick(ItemID.EARTHBOWLRAW.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.EARTHBOWLRAW.ordinal(), 0).setItemName("earthbowlRaw").setMaxStackSize(16);
	    earthbowl = (new ItemMudBrick(ItemID.EARTHBOWL.ordinal()+IDOFFSET)).setIconCoord(ItemIcon.EARTHBOWL.ordinal(), 0).setItemName("earthbowl");
	    earthbowlSoup = (new ItemEarthbowlSoup(ItemID.EARTHBOWLSOUP.ordinal()+IDOFFSET, 8)).setIconCoord(ItemIcon.EARTHBOWLSOUP.ordinal(), 0).setItemName("earthbowlSoup").setContainerItem(vase);
        LanguageRegistry.addName(mud, "Mud");
        LanguageRegistry.addName(mudBlob, "Mud Blob");
        LanguageRegistry.addName(mudBrick, "Mud Brick");
        LanguageRegistry.addName(adobeWet, "Moist Adobe");
        LanguageRegistry.addName(adobe, "Adobe Brick");
        LanguageRegistry.addName(adobeBlob, "Moist Adobe Blob");
        LanguageRegistry.addName(adobeDust, "Adobe Dust");
        LanguageRegistry.addName(mudBrickBlock, "Mud Brick");
        LanguageRegistry.addName(vaseRaw, "Unfired Vase");
        LanguageRegistry.addName(vase, "Vase");
        LanguageRegistry.addName(vaseWater, "Water Vase");
        LanguageRegistry.addName(earthbowlRaw, "Unfired Bowl");
        LanguageRegistry.addName(earthbowl, "Earthenware Bowl");
        LanguageRegistry.addName(earthbowlSoup, "Mushroom Stew");
        GameRegistry.registerBlock(mud);
        GameRegistry.registerBlock(adobeWet);
        GameRegistry.registerBlock(adobe);
        GameRegistry.registerBlock(mudBrickBlock);
        GameRegistry.registerBlock(muddyWaterStill);
        GameRegistry.registerBlock(muddyWaterFlowing);
        MinecraftForgeClient.preloadTexture(MudMod.terrainFile);
        MinecraftForgeClient.preloadTexture(MudMod.itemsFile);
        
        GameRegistry.addRecipe(
            	new ItemStack(mud, 1),
            	new Object[] {
    	            "##",
    	            "##",
    	            '#', mudBlob
    	        }
            );
        GameRegistry.addRecipe(
            	new ItemStack(mud, 1),
            	new Object[] {
    	            "#",
    	            "W",
    	            '#', Block.dirt,
    	            'W', Item.bucketWater
    	        }
            );
        GameRegistry.addRecipe(
                	new ItemStack(mud, 4),
                	new Object[] {
        	            " # ",
        	            "#W#",
        	            " # ",
        	            '#', Block.dirt,
        	            'W', Item.bucketWater
        	        }
                );
        GameRegistry.addRecipe(
                	new ItemStack(mud, 4),
                	new Object[] {
        	            " # ",
        	            "#W#",
        	            " # ",
        	            '#', Block.dirt,
        	            'W', vaseWater
        	        }
                );
        GameRegistry.addRecipe(
                	new ItemStack(mud, 8),
                	new Object[] {
        	            "###",
        	            "#W#",
        	            "###",
        	            '#', Block.dirt,
        	            'W', Item.bucketWater
        	        }
                );
        GameRegistry.addRecipe(
                	new ItemStack(mud, 8),
                	new Object[] {
        	            "###",
        	            "#W#",
        	            "###",
        	            '#', Block.dirt,
        	            'W', vaseWater
        	        }
                );
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
    	        	new ItemStack(mudBrickBlock, 1),
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
        GameRegistry.addRecipe(
    	        	new ItemStack(adobeWet, 1),
    	        	new Object[] {
    		            " # ",
    		            "#W#",
    		            " # ",
    		            '#', adobeDust,
    		            'W', vaseWater
    		        }
    	        );
        GameRegistry.addRecipe(
    	        	new ItemStack(adobeWet, 2),
    	        	new Object[] {
    		            "###",
    		            "#W#",
    		            "###",
    		            '#', adobeDust,
    		            'W', Item.bucketWater
    		        }
    	        );
        GameRegistry.addRecipe(
    	        	new ItemStack(adobeWet, 2),
    	        	new Object[] {
    		            "###",
    		            "#W#",
    		            "###",
    		            '#', adobeDust,
    		            'W', vaseWater
    		        }
    	        );
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
	    GameRegistry.addSmelting(adobeWet.blockID, new ItemStack(adobe), 0.1F);
	    GameRegistry.addSmelting(mud.blockID, new ItemStack(Block.dirt), 0.1F);
	    GameRegistry.addSmelting(mudBlob.shiftedIndex, new ItemStack(mudBrick), 0.1F);
	    GameRegistry.addSmelting(vaseRaw.shiftedIndex, new ItemStack(vase), 0.1F);
	    GameRegistry.addSmelting(earthbowlRaw.shiftedIndex, new ItemStack(earthbowl), 0.1F);
    }
}
