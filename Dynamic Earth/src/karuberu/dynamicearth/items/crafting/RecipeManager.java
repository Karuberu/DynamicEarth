package karuberu.dynamicearth.items.crafting;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.items.ItemMudBlob;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public final class RecipeManager {
	
	public static boolean
		canCraftBombs,
		canCraftMossyStone;
	private static ItemStack
		peat;
	
	public static void setPeatBrick(ItemStack peatBrick) {
		RecipeManager.peat = peatBrick;
	}
	
	public static ItemStack getPeatBrick() {
		return peat == null ? new ItemStack(DynamicEarth.peatBrick) : peat;
	}
	
	public static ItemStack getPeatBrick(int stackSize) {
		ItemStack peat = RecipeManager.getPeatBrick().copy();
		peat.stackSize = stackSize;
		return peat;
	}

	public static void addRecipes() {
		RecipeManager.addDirtRecipes();
		if (DynamicEarth.includeMud) {
			RecipeManager.addMudRecipes();
		}
		if (DynamicEarth.includeMudBrick) {
			RecipeManager.addMudBrickRecipes();
		}
        if (DynamicEarth.includeAdobe) {
        	RecipeManager.addAdobeRecipes();
        }
        if (DynamicEarth.includeDirtSlabs) {
        	RecipeManager.addDirtSlabRecipes();
        }
        if (DynamicEarth.includePeat) {
        	RecipeManager.addPeatRecipes();
        }
        if (DynamicEarth.includeFertileSoil) {
        	RecipeManager.addFertileSoilRecipes();
        }
        if (DynamicEarth.includeSandySoil) {
        	RecipeManager.addSandySoilRecipes();
        }
        if (DynamicEarth.includeGlowingSoil) {
        	RecipeManager.addGlowingSoilRecipes();
        }
        if (DynamicEarth.includeBurningSoil) {
        	RecipeManager.addBurningSoilRecipes();
        }
	    GameRegistry.addRecipe(RecipeWetBlocks.instance);
	}

	public static void addSmelting() {
		if (DynamicEarth.includeMud) {
			FurnaceRecipes.smelting().addSmelting(DynamicEarth.mud.blockID, DynamicEarth.mud.NORMAL, new ItemStack(Block.dirt), 0.1F);
			FurnaceRecipes.smelting().addSmelting(DynamicEarth.mud.blockID, DynamicEarth.mud.WET, new ItemStack(Block.dirt), 0.1F);
		}
		if (DynamicEarth.includeMudBrick) {
			if (DynamicEarth.includeMud) {
		    	GameRegistry.addSmelting(DynamicEarth.mudBlob.itemID, new ItemStack(DynamicEarth.mudBrick), 0.1F);
			} else {
				GameRegistry.addSmelting(DynamicEarth.dirtClod.itemID, new ItemStack(DynamicEarth.mudBrick), 0.1F);
			}
		}
	    if (DynamicEarth.includeAdobe) {
		    GameRegistry.addSmelting(DynamicEarth.adobeWet.blockID, new ItemStack(DynamicEarth.adobe), 0.1F);
		    GameRegistry.addSmelting(DynamicEarth.vaseRaw.itemID, new ItemStack(DynamicEarth.vase), 0.1F);
		    GameRegistry.addSmelting(DynamicEarth.earthbowlRaw.itemID, new ItemStack(DynamicEarth.earthbowl), 0.1F);
	    }
	    if (DynamicEarth.includePermafrost) {
	    	GameRegistry.addSmelting(DynamicEarth.permafrost.blockID, new ItemStack(Block.dirt), 0.1F);
	    }
	    if (DynamicEarth.includePeat) {
	    	GameRegistry.addSmelting(DynamicEarth.peatClump.itemID, RecipeManager.getPeatBrick(), 0.1F);
	    }
	    if (DynamicEarth.includeFertileSoil) {
			FurnaceRecipes.smelting().addSmelting(DynamicEarth.fertileMud.blockID, DynamicEarth.fertileMud.NORMAL, new ItemStack(DynamicEarth.fertileSoil), 0.1F);
			FurnaceRecipes.smelting().addSmelting(DynamicEarth.fertileMud.blockID, DynamicEarth.fertileMud.WET, new ItemStack(DynamicEarth.fertileSoil), 0.1F);
	    }
	}
	
	private static void addDirtRecipes() {
	    GameRegistry.addShapelessRecipe(
        	new ItemStack(DynamicEarth.dirtClod, 4),
        	Block.dirt
        );
	    GameRegistry.addRecipe(
        	new ItemStack(Block.dirt, 1),
        	new Object[] {
	            "##",
	            "##",
	            '#', DynamicEarth.dirtClod
	        }
        );
	}
	
	private static void addMudRecipes() {
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.mud, 1),
        	new Object[] {
	            "##",
	            "##",
	            '#', DynamicEarth.mudBlob
	        }
        );
	    GameRegistry.addShapelessRecipe(
        	new ItemStack(DynamicEarth.mudBlob, 4, ItemMudBlob.NORMAL),
        	DynamicEarth.mud
        );
	}
	
	private static void addMudBrickRecipes() {
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.blockMudBrick, 1),
        	new Object[] {
	            "##",
	            "##",
	            '#', DynamicEarth.mudBrick
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.adobeSingleSlab, 6, 1),
        	new Object[] {
	            "###",
	            '#', DynamicEarth.blockMudBrick
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.mudBrickStairs, 4),
        	new Object[] {
        		"#  ",
        		"## ",
	            "###",
	            '#', DynamicEarth.blockMudBrick
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.mudBrickStairs, 4),
        	new Object[] {
        		"  #",
        		" ##",
	            "###",
	            '#', DynamicEarth.blockMudBrick
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.mudBrickWall, 6),
        	new Object[] {
        		"###",
	            "###",
	            '#', DynamicEarth.blockMudBrick
	        }
        );
	}
	
	private static void addAdobeRecipes() {
	    GameRegistry.addRecipe(new ShapedOreRecipe(
        	new ItemStack(DynamicEarth.adobeWet, 2),
        	new Object[] {
	            "#X#",
	            "XCX",
	            "#X#",
	            '#', "mudBlob",
	            'X', Item.wheat,
	            'C', Item.clay
	        }
		));
	    GameRegistry.addRecipe(new ShapedOreRecipe(
	    	new ItemStack(DynamicEarth.adobeWet, 2),
	    	new Object[] {
	            "#X#",
	            "XCX",
	            "#X#",
	            '#', "mudBlob",
	            'X', Item.reed,
	            'C', Item.clay
	        }
	    ));
	    if (!DynamicEarth.includeMud) {
	    	GameRegistry.addRecipe(new ShapedOreRecipe(
	        	new ItemStack(DynamicEarth.adobeWet, 2),
	        	new Object[] {
		            "#X#",
		            "XCX",
		            "#X#",
		            '#', "dirtClump",
		            'X', Item.wheat,
		            'C', Item.clay
		        }
	    	));
		    GameRegistry.addRecipe(new ShapedOreRecipe(
		    	new ItemStack(DynamicEarth.adobeWet, 2),
		    	new Object[] {
		            "#X#",
		            "XCX",
		            "#X#",
		            '#', "dirtClump",
		            'X', Item.reed,
		            'C', Item.clay
		        }
		    ));
	    }
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.adobeSingleSlab, 6, 0),
        	new Object[] {
	            "###",
	            '#', DynamicEarth.adobe
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.adobeStairs, 4),
        	new Object[] {
	            "#  ",
	            "## ",
	            "###",
	            '#', DynamicEarth.adobe
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.adobeStairs, 4),
        	new Object[] {
	            "  #",
	            " ##",
	            "###",
	            '#', DynamicEarth.adobe
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.adobeBlob, 4),
        	new Object[] {
	            "#",
	            '#', DynamicEarth.adobeWet
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.adobeWet, 1),
        	new Object[] {
	            "##",
	            "##",
	            '#', DynamicEarth.adobeBlob
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.vaseRaw, 1),
        	new Object[] {
	            "# #",
	            "# #",
	            " # ",
	            '#', DynamicEarth.adobeBlob
	        }
        );
	    GameRegistry.addRecipe(
	    	new ItemStack(DynamicEarth.earthbowlRaw, 1),
	    	new Object[] {
	            "# #",
	            " # ",
	            '#', DynamicEarth.adobeBlob
	        }
	    );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.earthbowlRaw, 4),
        	new Object[] {
	            "# #",
	            " # ",
	            '#', DynamicEarth.adobeWet
	        }
        );
	    GameRegistry.addShapelessRecipe(
			new ItemStack(DynamicEarth.earthbowlSoup),
			new Object[] {
				DynamicEarth.earthbowl, Block.mushroomBrown, Block.mushroomRed
			}
		);
	    GameRegistry.addRecipe(RecipeCake.instance);
	    GameRegistry.addRecipe(RecipeVase.instance);
	    if (DynamicEarth.includeBombs && RecipeManager.canCraftBombs) {
		    GameRegistry.addRecipe(RecipeBombs.instance);
	    }
	}
	
	private static void addDirtSlabRecipes() {
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.dirtSlab, 6, 0),
        	new Object[] {
	            "###",
	            '#', Block.dirt
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.grassSlab, 3, 0),
        	new Object[] {
	            "###",
	            '#', Block.grass
	        }
        );
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.grassSlab, 3, 1),
        	new Object[] {
	            "###",
	            '#', Block.mycelium
	        }
        );
	}
	
	private static void addPeatRecipes() {
		GameRegistry.addRecipe(
			new ItemStack(DynamicEarth.peat, 1, 0),
			new Object[] {
				"##",
				"##",
				'#', DynamicEarth.peatClump
			}
		);
		GameRegistry.addShapelessRecipe(
			new ItemStack(DynamicEarth.peatClump, 4),
			new Object[] {
				DynamicEarth.peat
			}
		);
		GameRegistry.addShapelessRecipe(
			RecipeManager.getPeatBrick(4),
			new Object[] {
				new ItemStack(DynamicEarth.peat, 1, BlockPeat.DRY)
			}
		);
		if (RecipeManager.canCraftMossyStone) {
			GameRegistry.addShapelessRecipe(
				new ItemStack(Block.cobblestoneMossy),
				new Object[] {
					DynamicEarth.peatMossSpecimen,
					Block.cobblestone
				}
			);
			GameRegistry.addShapelessRecipe(
				new ItemStack(Block.stoneBrick, 1, 1),
				new Object[] {
					DynamicEarth.peatMossSpecimen,
					new ItemStack(Block.stoneBrick, 1, 0)
				}
			);
		}
	}
	
	private static void addFertileSoilRecipes() {
		if (DynamicEarth.includePeat) {
			GameRegistry.addShapelessRecipe(
				new ItemStack(DynamicEarth.fertileSoil, 1, DynamicEarth.fertileSoil.DIRT),
				DynamicEarth.dirtClod,
				DynamicEarth.dirtClod,
				DynamicEarth.peatClump,
				new ItemStack(Item.dyePowder, 1, 15)
			);
			if (DynamicEarth.includeMud) {
				GameRegistry.addShapelessRecipe(
					new ItemStack(DynamicEarth.fertileMud, 1, DynamicEarth.fertileMud.NORMAL),
					DynamicEarth.mudBlob,
					DynamicEarth.mudBlob,
					DynamicEarth.peatClump,
					new ItemStack(Item.dyePowder, 1, 15)
				);
			}
		} else {
			GameRegistry.addRecipe(
				new ItemStack(DynamicEarth.fertileSoil, 5, DynamicEarth.fertileSoil.DIRT),
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
				new ItemStack(DynamicEarth.fertileSoil, 5, DynamicEarth.fertileSoil.DIRT),
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
		if (DynamicEarth.includeMud) {
		    GameRegistry.addRecipe(
	        	new ItemStack(DynamicEarth.fertileMud, 1),
	        	new Object[] {
		            "##",
		            "##",
		            '#', new ItemStack(DynamicEarth.mudBlob, 1, ItemMudBlob.FERTILE)
		        }
	        );
			GameRegistry.addShapelessRecipe(
				new ItemStack(DynamicEarth.mudBlob, 4, ItemMudBlob.FERTILE),
				new Object[] {
					DynamicEarth.fertileMud
				}
			);
		}
		GameRegistry.addShapelessRecipe(
			new ItemStack(Block.dirt, 2),
			DynamicEarth.fertileSoil,
			Block.dirt
		);
		if (DynamicEarth.includeSandySoil) {
			GameRegistry.addShapelessRecipe(
				new ItemStack(Block.dirt, 2),
				DynamicEarth.fertileSoil,
				DynamicEarth.sandySoil
			);
		}
	}
	
	private static void addSandySoilRecipes() {
		GameRegistry.addRecipe(
			new ItemStack(DynamicEarth.sandySoil, 4, DynamicEarth.sandySoil.DIRT),
			new Object[] {
				"X#",
				"#X",
				'#', Block.sand,
				'X', Block.dirt
			}
		);
		GameRegistry.addRecipe(
			new ItemStack(DynamicEarth.sandySoil, 4, DynamicEarth.sandySoil.DIRT),
			new Object[] {
				"#X",
				"X#",
				'#', Block.sand,
				'X', Block.dirt
			}
		);
		GameRegistry.addShapelessRecipe(
			new ItemStack(Block.dirt, 4),
			DynamicEarth.sandySoil,
			Block.dirt,
			Block.dirt,
			Block.dirt
		);
	}
	
	private static void addGlowingSoilRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(
			new ItemStack(DynamicEarth.glowingSoil, 1, DynamicEarth.glowingSoil.DIRT),
			new Object[] {
				"X#",
				"#X",
				'#', Item.glowstone,
				'X', "dirtClump"
			}
		));
		GameRegistry.addRecipe(new ShapedOreRecipe(
			new ItemStack(DynamicEarth.glowingSoil, 1, DynamicEarth.glowingSoil.DIRT),
			new Object[] {
				"#X",
				"X#",
				'#', Item.glowstone,
				'X', "dirtClump"
			}
		));
		if (DynamicEarth.includeMud) {
			GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(DynamicEarth.glowingSoil, 1, DynamicEarth.glowingSoil.DIRT),
				new Object[] {
					"X#",
					"#X",
					'#', Item.glowstone,
					'X', "mudBlob"
				}
			));
			GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(DynamicEarth.glowingSoil, 1, DynamicEarth.glowingSoil.DIRT),
				new Object[] {
					"#X",
					"X#",
					'#', Item.glowstone,
					'X', "mudBlob"
				}
			));
		    GameRegistry.addRecipe(
	        	new ItemStack(DynamicEarth.glowingMud, 1),
	        	new Object[] {
		            "##",
		            "##",
		            '#', new ItemStack(DynamicEarth.mudBlob, 1, ItemMudBlob.GLOWING)
		        }
	        );
			GameRegistry.addShapelessRecipe(
				new ItemStack(DynamicEarth.mudBlob, 4, ItemMudBlob.GLOWING),
				new Object[] {
					DynamicEarth.glowingMud
				}
			);
		}
	}
	
	private static void addBurningSoilRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(
			new ItemStack(DynamicEarth.burningSoil, 1, DynamicEarth.burningSoil.DIRT),
			new Object[] {
				"X#",
				"#X",
				'#', Item.blazePowder,
				'X', "dirtClump"
			}
		));
		GameRegistry.addRecipe(new ShapedOreRecipe(
			new ItemStack(DynamicEarth.burningSoil, 1, DynamicEarth.burningSoil.DIRT),
			new Object[] {
				"#X",
				"X#",
				'#', Item.blazePowder,
				'X', "dirtClump"
			}
		));
	}
}
