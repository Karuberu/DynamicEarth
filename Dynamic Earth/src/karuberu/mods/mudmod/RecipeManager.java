package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeManager {
	
	public static void addRecipes() {
        OreDictionary.registerOre(OreDictionary.getOreID("bucketWater"), Item.bucketWater);
        OreDictionary.registerOre(OreDictionary.getOreID("bucketWater"), MudMod.vaseWater);
        OreDictionary.registerOre(OreDictionary.getOreID("bucketMilk"), Item.bucketMilk);
        OreDictionary.registerOre(OreDictionary.getOreID("bucketMilk"), MudMod.vaseMilk);
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.mud, 1),
	        	new Object[] {
		            "##",
		            "##",
		            '#', MudMod.mudBlob
		        }
	        );
	    CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(
	    		new ItemStack(MudMod.mud, 1),
	            Block.dirt, "bucketWater"
	    	));
	    CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
	    		new ItemStack(MudMod.mud, 4),
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
	    		new ItemStack(MudMod.mud, 8),
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
	        	new ItemStack(MudMod.mudBlob, 4),
	        	new Object[] {MudMod.mud}
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.adobeWet, 2),
	        	new Object[] {
		            "#X#",
		            "XCX",
		            "#X#",
		            '#', MudMod.mudBlob,
		            'X', Item.wheat,
		            'C', Item.clay
		        }
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.adobeWet, 2),
	        	new Object[] {
		            "#X#",
		            "XCX",
		            "#X#",
		            '#', MudMod.mudBlob,
		            'X', Item.reed,
		            'C', Item.clay
		        }
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.blockMudBrick, 1),
	        	new Object[] {
		            "##",
		            "##",
		            '#', MudMod.mudBrick
		        }
	        );

	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.adobeSingleSlab, 6, 0),
	        	new Object[] {
		            "###",
		            '#', MudMod.adobe
		        }
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.adobeSingleSlab, 6, 1),
	        	new Object[] {
		            "###",
		            '#', MudMod.blockMudBrick
		        }
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.dirtSlab, 6, 0),
	        	new Object[] {
		            "###",
		            '#', Block.dirt
		        }
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.adobeStairs, 4),
	        	new Object[] {
		            "#  ",
		            "## ",
		            "###",
		            '#', MudMod.adobe
		        }
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.adobeStairs, 4),
	        	new Object[] {
		            "  #",
		            " ##",
		            "###",
		            '#', MudMod.adobe
		        }
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.mudBrickStairs, 4),
	        	new Object[] {
	        		"#  ",
	        		"## ",
		            "###",
		            '#', MudMod.blockMudBrick
		        }
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.mudBrickStairs, 4),
	        	new Object[] {
	        		"  #",
	        		" ##",
		            "###",
		            '#', MudMod.blockMudBrick
		        }
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.mudBrickWall, 6),
	        	new Object[] {
	        		"###",
		            "###",
		            '#', MudMod.blockMudBrick
		        }
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.adobeWet, 1),
	        	new Object[] {
		            " # ",
		            "#W#",
		            " # ",
		            '#', MudMod.adobeDust,
		            'W', Item.bucketWater
		        }
	        );
	    CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
	    		new ItemStack(MudMod.adobeWet, 1),
	    		true,
	    		new Object[]{
		            " # ",
		            "#W#",
		            " # ",
		            '#', MudMod.adobeDust,
		            'W', "bucketWater",
				}
	    	));
	    CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
	    		new ItemStack(MudMod.adobeWet, 2),
	    		true,
	    		new Object[]{
		            "###",
		            "#W#",
		            "###",
		            '#', MudMod.adobeDust,
		            'W', "bucketWater",
				}
	    	));
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.adobeBlob, 4),
	        	new Object[] {
		            "#",
		            '#', MudMod.adobeWet
		        }
	        );
	    GameRegistry.addRecipe(
		        	new ItemStack(MudMod.adobeWet, 1),
		        	new Object[] {
			            "##",
			            "##",
			            '#', MudMod.adobeBlob
			        }
		        );
	    GameRegistry.addRecipe(
		        	new ItemStack(MudMod.vaseRaw, 1),
		        	new Object[] {
			            "# #",
			            "# #",
			            " # ",
			            '#', MudMod.adobeBlob
			        }
		        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.earthbowlRaw, 1),
	        	new Object[] {
		            "# #",
		            " # ",
		            '#', MudMod.adobeBlob
		        }
	        );
	    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.earthbowlRaw, 4),
	        	new Object[] {
		            "# #",
		            " # ",
		            '#', MudMod.adobeWet
		        }
	        );
	    GameRegistry.addShapelessRecipe(
			new ItemStack(MudMod.earthbowlSoup),
			new Object[] {
				MudMod.earthbowl, Block.mushroomBrown, Block.mushroomRed
			}
		);
	    GameRegistry.addShapelessRecipe(
			new ItemStack(MudMod.bomb),
			new Object[] {
				MudMod.earthbowl, MudMod.earthbowl, Item.gunpowder, Item.silk
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
	}
	
	public static void addSmelting() {
	    GameRegistry.addSmelting(MudMod.adobeWet.blockID, new ItemStack(MudMod.adobe), 0.1F);
	    GameRegistry.addSmelting(MudMod.mud.blockID, new ItemStack(Block.dirt), 0.1F);
	    GameRegistry.addSmelting(MudMod.mudBlob.shiftedIndex, new ItemStack(MudMod.mudBrick), 0.1F);
	    GameRegistry.addSmelting(MudMod.vaseRaw.shiftedIndex, new ItemStack(MudMod.vase), 0.1F);
	    GameRegistry.addSmelting(MudMod.earthbowlRaw.shiftedIndex, new ItemStack(MudMod.earthbowl), 0.1F);
	    GameRegistry.addSmelting(MudMod.permafrost.blockID, new ItemStack(Block.dirt), 0.1F);
	}
}
