package karuberu.mods.mudmod.items.crafting;

import java.util.List;
import java.util.Map.Entry;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.blocks.BlockGrassSlab;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.Property;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.recipes.RecipeManagers;

public final class RecipeManager {
	
	public static boolean
		canCraftBombs,
		canCraftMossyStone;

	public static void addRecipes() {
        RecipeManager.addMudRecipes();
        if (MudMod.includeAdobe) {
            OreDictionary.registerOre(OreDictionary.getOreID("bucketWater"), Item.bucketWater);
            OreDictionary.registerOre(OreDictionary.getOreID("bucketMilk"), Item.bucketMilk);
            OreDictionary.registerOre(OreDictionary.getOreID("bucketWater"), MudMod.vaseWater);
            OreDictionary.registerOre(OreDictionary.getOreID("bucketMilk"), MudMod.vaseMilk);
            RecipeManager.addAdobeRecipes();
        }
        if (MudMod.includePeat) {
        	RecipeManager.addPeatRecipes();
        }
	    RecipeManager.addStairAndSlabRecipes();
	    RecipeManager.addModRecipes();
	}

	public static void addSmelting() {
	    GameRegistry.addSmelting(MudMod.mud.blockID, new ItemStack(Block.dirt), 0.1F);
	    if (MudMod.includeMudBrick) {
	    	GameRegistry.addSmelting(MudMod.mudBlob.itemID, new ItemStack(MudMod.mudBrick), 0.1F);
	    }
	    if (MudMod.includePermafrost) {
	    	GameRegistry.addSmelting(MudMod.permafrost.blockID, new ItemStack(Block.dirt), 0.1F);
	    }
	    if (MudMod.includePeat) {
	    	GameRegistry.addSmelting(MudMod.peatClump.itemID, new ItemStack(MudMod.peatBrick), 0.1F);
	    }
	}
	
	private static void addMudRecipes() {
	    GameRegistry.addRecipe(
        	new ItemStack(MudMod.mud, 1),
        	new Object[] {
	            "##",
	            "##",
	            '#', MudMod.mudBlob
	        }
        );
	    CraftingManager.getInstance().getRecipeList().add(
	    	new ShapelessOreRecipe(
	    		new ItemStack(MudMod.mud, 1),
	            Block.dirt, "bucketWater"
	    	)
	    );
	    CraftingManager.getInstance().getRecipeList().add(
	    	new ShapedOreRecipe(
	    		new ItemStack(MudMod.mud, 4),
	    		true,
	    		new Object[]{
		            " # ",
		            "#W#",
		            " # ",
		            '#', Block.dirt,
		            'W', "bucketWater",
				}
	    	)
	    );
	    CraftingManager.getInstance().getRecipeList().add(
	    	new ShapedOreRecipe(
	    		new ItemStack(MudMod.mud, 8),
	    		true,
	    		new Object[]{
		            "###",
		            "#W#",
		            "###",
		            '#', Block.dirt,
		            'W', "bucketWater",
				}
	    	)
	    );
	    GameRegistry.addShapelessRecipe(
        	new ItemStack(MudMod.mudBlob, 4),
        	new Object[] {MudMod.mud}
        );
	    if (MudMod.includeMudBrick) {
		    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.blockMudBrick, 1),
	        	new Object[] {
		            "##",
		            "##",
		            '#', MudMod.mudBrick
		        }
	        );
	    }
	}
	
	private static void addAdobeRecipes() {
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
	    CraftingManager.getInstance().getRecipeList().add(
	    	new ShapedOreRecipe(
	    		new ItemStack(MudMod.adobeWet, 1),
	    		true,
	    		new Object[]{
		            " # ",
		            "#W#",
		            " # ",
		            '#', MudMod.adobeDust,
		            'W', "bucketWater",
				}
	    	)
	    );
	    CraftingManager.getInstance().getRecipeList().add(
	    	new ShapedOreRecipe(
	    		new ItemStack(MudMod.adobeWet, 2),
	    		true,
	    		new Object[]{
		            "###",
		            "#W#",
		            "###",
		            '#', MudMod.adobeDust,
		            'W', "bucketWater",
				}
	    	)
	    );
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
	    CraftingManager.getInstance().getRecipeList().add(
	    	new ShapedOreRecipe(
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
			)
	    );
	    if (MudMod.includeBombs && RecipeManager.canCraftBombs) {
		    GameRegistry.addRecipe(RecipeBombs.instance);
	    }
	    GameRegistry.addSmelting(MudMod.adobeWet.blockID, new ItemStack(MudMod.adobe), 0.1F);
	    GameRegistry.addSmelting(MudMod.vaseRaw.itemID, new ItemStack(MudMod.vase), 0.1F);
	    GameRegistry.addSmelting(MudMod.earthbowlRaw.itemID, new ItemStack(MudMod.earthbowl), 0.1F);
	}
	
	private static void addPeatRecipes() {
		GameRegistry.addRecipe(
			new ItemStack(MudMod.peat, 1, 0),
			new Object[] {
				"##",
				"##",
				'#', MudMod.peatClump
			}
		);
		GameRegistry.addShapelessRecipe(
			new ItemStack(MudMod.peatClump, 4),
			new Object[] {
				MudMod.peat
			}
		);
		GameRegistry.addShapelessRecipe(
			new ItemStack(MudMod.peatBrick, 4),
			new Object[] {
				MudMod.peatDry
			}
		);
		if (RecipeManager.canCraftMossyStone) {
			GameRegistry.addShapelessRecipe(
				new ItemStack(Block.cobblestoneMossy),
				new Object[] {
					MudMod.peatMossSpecimen,
					Block.cobblestone
				}
			);
			GameRegistry.addShapelessRecipe(
				new ItemStack(Block.stoneBrick, 1, 1),
				new Object[] {
					MudMod.peatMossSpecimen,
					new ItemStack(Block.stoneBrick, 1, 0)
				}
			);
		}
	}
	
	private static void addStairAndSlabRecipes() {
		if (MudMod.includeAdobe) {
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
		}
	    if (MudMod.includeMudBrick) {
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
	    }
		
	    if (MudMod.includeDirtSlabs) {
		    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.dirtSlab, 6, 0),
	        	new Object[] {
		            "###",
		            '#', Block.dirt
		        }
	        );
		    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.grassSlab, 3, 0),
	        	new Object[] {
		            "###",
		            '#', Block.grass
		        }
	        );
		    GameRegistry.addRecipe(
	        	new ItemStack(MudMod.grassSlab, 3, 1),
	        	new Object[] {
		            "###",
		            '#', Block.mycelium
		        }
	        );
	    }
	}
	
	private static void addModRecipes() {
		// Forestry recipes
		if (MudMod.enableForestryIntegration) {
		    if (RecipeManagers.squeezerManager != null) {
				RecipeManagers.squeezerManager.addRecipe(
					10,
					new ItemStack[] {
						new ItemStack(MudMod.mudBlob)
					},
					LiquidDictionary.getLiquid("Water", 100)
				);
				if (MudMod.includeAdobe) {
					RecipeManagers.squeezerManager.addRecipe(
						10,
						new ItemStack[] {
							new ItemStack(MudMod.adobeBlob)
						},
						LiquidDictionary.getLiquid("Water", 100),
						new ItemStack(MudMod.adobeDust),
						80
					);
				}
				if (MudMod.includePeat) {
					RecipeManagers.squeezerManager.addRecipe(
						10,
						new ItemStack[] {
							new ItemStack(MudMod.peatClump)
						},
						LiquidDictionary.getLiquid("Water", 100),
						new ItemStack(MudMod.peatBrick),
						100
					);
				}
		    }
		    if (RecipeManagers.moistenerManager != null) {
		    	if (MudMod.includeDirtSlabs) {
				    RecipeManagers.moistenerManager.addRecipe(
				    	new ItemStack(MudMod.dirtSlab),
				    	new ItemStack(MudMod.grassSlab, 1, BlockGrassSlab.MYCELIUM),
				    	5000
				    );
		    	}
		    }
		}
	}
}
