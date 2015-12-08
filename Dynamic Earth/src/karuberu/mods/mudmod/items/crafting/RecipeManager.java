package karuberu.mods.mudmod.items.crafting;

import karuberu.mods.mudmod.ModHandler;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.blocks.BlockAdobeSlab;
import karuberu.mods.mudmod.blocks.BlockFertileSoil;
import karuberu.mods.mudmod.blocks.BlockMud;
import karuberu.mods.mudmod.blocks.BlockPeat;
import karuberu.mods.mudmod.blocks.BlockSandySoil;
import karuberu.mods.mudmod.liquids.LiquidHandler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public final class RecipeManager {
	
	public static boolean
		canCraftBombs,
		canCraftMossyStone;

	public static void addRecipes() {
        RecipeManager.addMudRecipes();
        if (MudMod.includeAdobe) {
        	RecipeManager.addAdobeRecipes();
        }
        if (MudMod.includePeat) {
        	RecipeManager.addPeatRecipes();
        }
        if (MudMod.includeFertileSoil) {
        	RecipeManager.addFertileSoilRecipes();
        }
        if (MudMod.includeSandySoil) {
        	RecipeManager.addSandySoilRecipes();
        }
	    RecipeManager.addStairAndSlabRecipes();
	    GameRegistry.addRecipe(RecipeWetBlocks.instance);
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
	    	GameRegistry.addSmelting(MudMod.peatClump.itemID, ModHandler.getPeatBrick(), 0.1F);
	    }
	}
	
	private static void addMudRecipes() {
	    GameRegistry.addShapelessRecipe(
        	new ItemStack(MudMod.dirtClod, 4),
        	Block.dirt
        );
	    GameRegistry.addRecipe(
        	new ItemStack(Block.dirt, 1),
        	new Object[] {
	            "##",
	            "##",
	            '#', MudMod.dirtClod
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(MudMod.mud, 1),
        	new Object[] {
	            "##",
	            "##",
	            '#', MudMod.mudBlob
	        }
        );
	    GameRegistry.addShapelessRecipe(
        	new ItemStack(MudMod.mudBlob, 4),
        	MudMod.mud
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
	    GameRegistry.addRecipe(RecipeCake.instance);
	    GameRegistry.addRecipe(RecipeSealedVase.instance);
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
			new ItemStack(ModHandler.getPeatBrick().getItem(), 4),
			new Object[] {
				new ItemStack(MudMod.peat, 1, BlockPeat.DRY)
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
	
	private static void addFertileSoilRecipes() {
		if (MudMod.includePeat) {
			GameRegistry.addShapelessRecipe(
				new ItemStack(MudMod.fertileSoil, 1, BlockFertileSoil.SOIL),
				MudMod.dirtClod,
				MudMod.dirtClod,
				MudMod.peatClump,
				new ItemStack(Item.dyePowder, 1, 15)
			);
		} else {
			GameRegistry.addRecipe(
				new ItemStack(MudMod.fertileSoil, 5, BlockFertileSoil.SOIL),
				new Object[] {
					"B#B",
					"XBX",
					"B#B",
					'#', Block.mycelium,
					'X', Block.dirt,
					'B', new ItemStack(Item.dyePowder, 1, 15)
				}
			);
			GameRegistry.addRecipe(
				new ItemStack(MudMod.fertileSoil, 5, BlockFertileSoil.SOIL),
				new Object[] {
					"BXB",
					"#B#",
					"BXB",
					'#', Block.mycelium,
					'X', Block.dirt,
					'B', new ItemStack(Item.dyePowder, 1, 15)
				}
			);
		}
		GameRegistry.addShapelessRecipe(
			new ItemStack(Block.dirt, 2),
			MudMod.fertileSoil,
			Block.dirt
		);
		if (MudMod.includeSandySoil) {
			GameRegistry.addShapelessRecipe(
				new ItemStack(Block.dirt, 2),
				MudMod.fertileSoil,
				MudMod.sandySoil
			);
		}
	}
	
	private static void addSandySoilRecipes() {
		GameRegistry.addRecipe(
			new ItemStack(MudMod.sandySoil, 4, BlockSandySoil.DIRT),
			new Object[] {
				"X#",
				"#X",
				'#', Block.sand,
				'X', Block.dirt
			}
		);
		GameRegistry.addRecipe(
			new ItemStack(MudMod.sandySoil, 4, BlockSandySoil.DIRT),
			new Object[] {
				"#X",
				"X#",
				'#', Block.sand,
				'X', Block.dirt
			}
		);
		GameRegistry.addShapelessRecipe(
			new ItemStack(Block.dirt, 4),
			MudMod.sandySoil,
			Block.dirt,
			Block.dirt,
			Block.dirt
		);
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
	
	public static void addForestryRecipes() {
		if (MudMod.includePeat
		&& !ModHandler.useForestryPeat
		&& forestry.api.core.ItemInterface.getItem("bituminousPeat") != null) {
		    GameRegistry.addRecipe(
	        	new ItemStack(forestry.api.core.ItemInterface.getItem("bituminousPeat").getItem(), 1),
	        	new Object[] {
	        		" A ",
		            "POP",
		            " A ",
		            'P', MudMod.peatBrick,
		            'O', forestry.api.core.ItemInterface.getItem("propolis").getItem(),
		            'A', forestry.api.core.ItemInterface.getItem("ash").getItem()
		        }
	        );
		}
	    if (forestry.api.recipes.RecipeManagers.carpenterManager != null) {
			forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
				10,
				LiquidDictionary.getLiquid(LiquidHandler.WATER, 50),
				null,
				new ItemStack(MudMod.mudBlob),
				new Object[] {
					"#",
					'#', new ItemStack(MudMod.dirtClod)
				}
			);
			forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
				10,
				LiquidDictionary.getLiquid(LiquidHandler.WATER, 200),
				null,
				new ItemStack(MudMod.mud.blockID, 1, BlockMud.NORMAL),
				new Object[] {
					"##",
					"##",
					'#', new ItemStack(MudMod.dirtClod)
				}
			);
	    	forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
				10,
				LiquidDictionary.getLiquid(LiquidHandler.WATER, 200),
				null,
				new ItemStack(MudMod.mud.blockID, 1, BlockMud.NORMAL),
				new Object[] {
					"#",
					'#', new ItemStack(Block.dirt)
				}
			);
	    	if (MudMod.includeFertileSoil) {
		    	forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
					10,
					LiquidDictionary.getLiquid(LiquidHandler.WATER, 200),
					null,
					new ItemStack(MudMod.mud.blockID, 1, BlockMud.FERTILE),
					new Object[] {
						"#",
						'#', new ItemStack(MudMod.fertileSoil.blockID, 1, BlockFertileSoil.SOIL)
					}
				);
	    	}
			if (MudMod.includeAdobe) {
				forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
					10,
					LiquidDictionary.getLiquid(LiquidHandler.WATER, 50),
					null,
					new ItemStack(MudMod.adobeBlob),
					new Object[] {
						"#",
						'#', new ItemStack(MudMod.adobeDust)
					}
				);
				forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
					10,
					LiquidDictionary.getLiquid(LiquidHandler.WATER, 200),
					null,
					new ItemStack(MudMod.adobeWet),
					new Object[] {
						"##",
						"##",
						'#', new ItemStack(MudMod.adobeDust)
					}
				);
			}
	    }
	    if (forestry.api.recipes.RecipeManagers.squeezerManager != null) {
    		forestry.api.recipes.RecipeManagers.squeezerManager.addRecipe(
				10,
				new ItemStack[] {
					new ItemStack(MudMod.mudBlob)
				},
				LiquidDictionary.getLiquid(LiquidHandler.WATER, 50),
				new ItemStack(MudMod.dirtClod),
				100
			);
			if (MudMod.includeAdobe) {
				forestry.api.recipes.RecipeManagers.squeezerManager.addRecipe(
					10,
					new ItemStack[] {
						new ItemStack(MudMod.adobeBlob)
					},
					LiquidDictionary.getLiquid(LiquidHandler.WATER, 50),
					new ItemStack(MudMod.adobeDust),
					100
				);
				forestry.api.recipes.RecipeManagers.squeezerManager.addRecipe(
					30,
					new ItemStack[] {
						new ItemStack(MudMod.earthbowlRaw)
					},
					LiquidDictionary.getLiquid(LiquidHandler.WATER, 150),
					new ItemStack(MudMod.adobeDust, 3),
					100
				);
				forestry.api.recipes.RecipeManagers.squeezerManager.addRecipe(
					30,
					new ItemStack[] {
						new ItemStack(MudMod.vaseRaw)
					},
					LiquidDictionary.getLiquid(LiquidHandler.WATER, 250),
					new ItemStack(MudMod.adobeDust, 5),
					100
				);
			}
			if (MudMod.includePeat) {
				forestry.api.recipes.RecipeManagers.squeezerManager.addRecipe(
					10,
					new ItemStack[] {
						new ItemStack(MudMod.peatClump)
					},
					LiquidDictionary.getLiquid(LiquidHandler.WATER, 100),
					ModHandler.getPeatBrick(),
					100
				);
			}
	    }
//	    if (forestry.api.recipes.RecipeManagers.moistenerManager != null) {
//	    	if (MudMod.includeDirtSlabs) {
//			    forestry.api.recipes.RecipeManagers.moistenerManager.addRecipe(
//			    	new ItemStack(MudMod.dirtSlab),
//			    	new ItemStack(MudMod.grassSlab, 1, BlockGrassSlab.MYCELIUM),
//			    	5000
//			    );
//	    	}
//	    	if (MudMod.includePeat) {
//			    forestry.api.recipes.RecipeManagers.moistenerManager.addRecipe(
//			    	new ItemStack(MudMod.peatMossSpecimen),
//			    	new ItemStack(MudMod.peatMossSpecimen, 2),
//			    	3000
//			    );
//	    	}
//	    	if (MudMod.includeFertileSoil) {
//			    forestry.api.recipes.RecipeManagers.moistenerManager.addRecipe(
//			    	new ItemStack(MudMod.fertileSoil, 1, BlockFertileSoil.SOIL),
//			    	new ItemStack(MudMod.fertileSoil, 1, BlockFertileSoil.MYCELIUM),
//			    	5000
//			    );
//	    	}
//	    	if (MudMod.includeSandySoil) {
//			    forestry.api.recipes.RecipeManagers.moistenerManager.addRecipe(
//			    	new ItemStack(MudMod.sandySoil, 1, BlockSandySoil.DIRT),
//			    	new ItemStack(MudMod.sandySoil, 1, BlockSandySoil.MYCELIUM),
//			    	5000
//			    );
//	    	}
//	    }
	}

	public static void addThermalExpansionRecipes() {
		if (thermalexpansion.api.crafting.CraftingManagers.pulverizerManager != null) {
			if (MudMod.includePeat) {
				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
					320,
					new ItemStack(Block.cobblestoneMossy),
					new ItemStack(Block.sand),
					new ItemStack(MudMod.peatMossSpecimen),
					5
				);
			}
			if (MudMod.includeAdobe) {
				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
					320,
					new ItemStack(MudMod.adobe),
					new ItemStack(MudMod.adobeDust, 4)
				);
				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
					160,
					new ItemStack(MudMod.adobeSingleSlab, 1, BlockAdobeSlab.ADOBE),
					new ItemStack(MudMod.adobeDust, 2)
				);
				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
					320,
					new ItemStack(MudMod.adobeStairs),
					new ItemStack(MudMod.adobeDust, 4)
				);
				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
					80,
					new ItemStack(MudMod.earthbowl),
					new ItemStack(MudMod.adobeDust, 3)
				);
				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
					160,
					new ItemStack(MudMod.vase),
					new ItemStack(MudMod.adobeDust, 5)
				);
			}
		}
	}
	
	public static void addRailcraftRecipes() {
		if (mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher != null) {
			if (MudMod.includePeat) {
				mods.railcraft.api.crafting.IRockCrusherRecipe mossyCobblestoneRecipe = mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(Block.cobblestoneMossy), false, false);
				mossyCobblestoneRecipe.addOutput(new ItemStack(Block.gravel), 1.0F);
				mossyCobblestoneRecipe.addOutput(new ItemStack(MudMod.peatMossSpecimen), 0.1F);
			}
			if (MudMod.includeAdobe) {
				mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(MudMod.adobe), false, false).addOutput(new ItemStack(MudMod.adobeDust, 4), 1.0F);
				mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(MudMod.adobeSingleSlab), false, false).addOutput(new ItemStack(MudMod.adobeDust, 2), 1.0F);
				mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(MudMod.adobeStairs), false, false).addOutput(new ItemStack(MudMod.adobeDust, 4), 1.0F);
				mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(MudMod.earthbowl), false, false).addOutput(new ItemStack(MudMod.adobeDust, 3), 1.0F);
				mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(MudMod.vase), false, false).addOutput(new ItemStack(MudMod.adobeDust, 5), 1.0F);
			}
		}
	}
	
	public static void addIndustrialCraftRecipes() {
		if (ic2.api.recipe.Recipes.macerator != null) {
			if (MudMod.includeAdobe) {
				ic2.api.recipe.Recipes.macerator.addRecipe(
					new ItemStack(MudMod.adobe),
					new ItemStack(MudMod.adobeDust, 4)
				);
				ic2.api.recipe.Recipes.macerator.addRecipe(
					new ItemStack(MudMod.adobeSingleSlab, 1, BlockAdobeSlab.ADOBE),
					new ItemStack(MudMod.adobeDust, 2)
				);
				ic2.api.recipe.Recipes.macerator.addRecipe(
					new ItemStack(MudMod.adobeStairs),
					new ItemStack(MudMod.adobeDust, 4)
				);
				ic2.api.recipe.Recipes.macerator.addRecipe(
					new ItemStack(MudMod.earthbowl),
					new ItemStack(MudMod.adobeDust, 3)
				);
				ic2.api.recipe.Recipes.macerator.addRecipe(
					new ItemStack(MudMod.vase),
					new ItemStack(MudMod.adobeDust, 5)
				);
			}
		}
		if (ic2.api.recipe.Recipes.compressor != null) {
			ic2.api.recipe.Recipes.compressor.addRecipe(
				new ItemStack(MudMod.dirtClod, 4),
				new ItemStack(Block.dirt)
			);
			ic2.api.recipe.Recipes.compressor.addRecipe(
				new ItemStack(MudMod.mudBlob, 4),
				new ItemStack(MudMod.mud.blockID, 1, BlockMud.NORMAL)
			);
			if (MudMod.includeAdobe) {
				ic2.api.recipe.Recipes.compressor.addRecipe(
					new ItemStack(MudMod.adobeBlob, 4),
					new ItemStack(MudMod.adobe)
				);
				ic2.api.recipe.Recipes.compressor.addRecipe(
					new ItemStack(MudMod.earthbowlRaw),
					new ItemStack(MudMod.adobeDust, 3)
				);
				ic2.api.recipe.Recipes.compressor.addRecipe(
					new ItemStack(MudMod.vaseRaw),
					new ItemStack(MudMod.adobeDust, 5)
				);
			}
			if (MudMod.includePeat) {
				ic2.api.recipe.Recipes.compressor.addRecipe(
					new ItemStack(MudMod.peatClump),
					ModHandler.getPeatBrick()
				);
			}
		}
		if (ic2.api.recipe.Recipes.scrapboxDrops != null) {
			if (MudMod.includePeat) {
				ic2.api.recipe.Recipes.scrapboxDrops.addRecipe(
					new ItemStack(MudMod.peatMossSpecimen), 0.2F
				);
			}
		}
	}
}
