package karuberu.dynamicearth.items.crafting;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.ModHandler;
import karuberu.dynamicearth.blocks.BlockAdobeSlab;
import karuberu.dynamicearth.blocks.BlockDirtSlab;
import karuberu.dynamicearth.blocks.BlockFertileSoil;
import karuberu.dynamicearth.blocks.BlockGrassSlab;
import karuberu.dynamicearth.blocks.BlockMud;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.blocks.BlockPermafrost;
import karuberu.dynamicearth.blocks.BlockSandySoil;
import karuberu.dynamicearth.fluids.FluidHelper.FluidReference;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public final class RecipeManager {
	
	public static boolean
		canCraftBombs,
		canCraftMossyStone;

	public static void addRecipes() {
        RecipeManager.addMudRecipes();
        if (DynamicEarth.includeAdobe) {
        	RecipeManager.addAdobeRecipes();
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
	    RecipeManager.addStairAndSlabRecipes();
	    GameRegistry.addRecipe(RecipeWetBlocks.instance);
	}

	public static void addSmelting() {
		FurnaceRecipes.smelting().addSmelting(DynamicEarth.mud.blockID, BlockMud.NORMAL, new ItemStack(Block.dirt), 0.1F);
		FurnaceRecipes.smelting().addSmelting(DynamicEarth.mud.blockID, BlockMud.WET, new ItemStack(Block.dirt), 0.1F);
	    if (DynamicEarth.includeMudBrick) {
	    	GameRegistry.addSmelting(DynamicEarth.mudBlob.itemID, new ItemStack(DynamicEarth.mudBrick), 0.1F);
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
	    	GameRegistry.addSmelting(DynamicEarth.peatClump.itemID, ModHandler.getPeatBrick(), 0.1F);
	    }
	    if (DynamicEarth.includeFertileSoil) {
			FurnaceRecipes.smelting().addSmelting(DynamicEarth.mud.blockID, BlockMud.FERTILE, new ItemStack(DynamicEarth.fertileSoil), 0.1F);
			FurnaceRecipes.smelting().addSmelting(DynamicEarth.mud.blockID, BlockMud.FERTILE_WET, new ItemStack(DynamicEarth.fertileSoil), 0.1F);
	    }
	}
	
	private static void addMudRecipes() {
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
	    GameRegistry.addRecipe(
        	new ItemStack(DynamicEarth.mud, 1),
        	new Object[] {
	            "##",
	            "##",
	            '#', DynamicEarth.mudBlob
	        }
        );
	    GameRegistry.addShapelessRecipe(
        	new ItemStack(DynamicEarth.mudBlob, 4),
        	DynamicEarth.mud
        );
	    if (DynamicEarth.includeMudBrick) {
		    GameRegistry.addRecipe(
	        	new ItemStack(DynamicEarth.blockMudBrick, 1),
	        	new Object[] {
		            "##",
		            "##",
		            '#', DynamicEarth.mudBrick
		        }
	        );
	    }
	}
	
	private static void addAdobeRecipes() {
	    GameRegistry.addRecipe(
	    	new ShapedOreRecipe(
	        	new ItemStack(DynamicEarth.adobeWet, 2),
	        	new Object[] {
		            "#X#",
		            "XCX",
		            "#X#",
		            '#', "mudBlob",
		            'X', Item.wheat,
		            'C', Item.clay
		        }
			)
        );
	    GameRegistry.addRecipe(
	    	new ShapedOreRecipe(
		    	new ItemStack(DynamicEarth.adobeWet, 2),
		    	new Object[] {
		            "#X#",
		            "XCX",
		            "#X#",
		            '#', "mudBlob",
		            'X', Item.reed,
		            'C', Item.clay
		        }
		    )
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
			new ItemStack(ModHandler.getPeatBrick().getItem(), 4),
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
				new ItemStack(DynamicEarth.fertileSoil, 1, BlockFertileSoil.SOIL),
				DynamicEarth.dirtClod,
				DynamicEarth.dirtClod,
				DynamicEarth.peatClump,
				new ItemStack(Item.dyePowder, 1, 15)
			);
			GameRegistry.addShapelessRecipe(
				new ItemStack(DynamicEarth.mud, 1, BlockMud.FERTILE),
				DynamicEarth.mudBlob,
				DynamicEarth.mudBlob,
				DynamicEarth.peatClump,
				new ItemStack(Item.dyePowder, 1, 15)
			);
		} else {
			GameRegistry.addRecipe(
				new ItemStack(DynamicEarth.fertileSoil, 5, BlockFertileSoil.SOIL),
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
				new ItemStack(DynamicEarth.fertileSoil, 5, BlockFertileSoil.SOIL),
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
			new ItemStack(DynamicEarth.sandySoil, 4, BlockSandySoil.DIRT),
			new Object[] {
				"X#",
				"#X",
				'#', Block.sand,
				'X', Block.dirt
			}
		);
		GameRegistry.addRecipe(
			new ItemStack(DynamicEarth.sandySoil, 4, BlockSandySoil.DIRT),
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
	
	private static void addStairAndSlabRecipes() {
		if (DynamicEarth.includeAdobe) {
		    GameRegistry.addRecipe(
	        	new ItemStack(DynamicEarth.adobeSingleSlab, 6, 0),
	        	new Object[] {
		            "###",
		            '#', DynamicEarth.adobe
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
		}
	    if (DynamicEarth.includeMudBrick) {
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
		
	    if (DynamicEarth.includeDirtSlabs) {
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
	}
	
	public static void addForestryRecipes() throws NoClassDefFoundError, NoSuchMethodError {
		if (DynamicEarth.includePeat
		&& !ModHandler.useForestryPeat
		&& forestry.api.core.ItemInterface.getItem("bituminousPeat") != null) {
		    GameRegistry.addRecipe(
	        	new ItemStack(forestry.api.core.ItemInterface.getItem("bituminousPeat").getItem(), 1),
	        	new Object[] {
	        		" A ",
		            "POP",
		            " A ",
		            'P', DynamicEarth.peatBrick,
		            'O', forestry.api.core.ItemInterface.getItem("propolis").getItem(),
		            'A', forestry.api.core.ItemInterface.getItem("ash").getItem()
		        }
	        );
		}
	    if (forestry.api.recipes.RecipeManagers.carpenterManager != null) {
			forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
				10,
				FluidReference.WATER.getFluidStack(50),
				null,
				new ItemStack(DynamicEarth.mudBlob),
				new Object[] {
					"#",
					'#', new ItemStack(DynamicEarth.dirtClod)
				}
			);
			forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
				10,
				FluidReference.WATER.getFluidStack(200),
				null,
				new ItemStack(DynamicEarth.mud.blockID, 1, BlockMud.NORMAL),
				new Object[] {
					"##",
					"##",
					'#', new ItemStack(DynamicEarth.dirtClod)
				}
			);
	    	forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
				10,
				FluidReference.WATER.getFluidStack(200),
				null,
				new ItemStack(DynamicEarth.mud.blockID, 1, BlockMud.NORMAL),
				new Object[] {
					"#",
					'#', new ItemStack(Block.dirt)
				}
			);
	    	if (DynamicEarth.includeFertileSoil) {
		    	forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
					10,
					FluidReference.WATER.getFluidStack(200),
					null,
					new ItemStack(DynamicEarth.mud.blockID, 1, BlockMud.FERTILE),
					new Object[] {
						"#",
						'#', new ItemStack(DynamicEarth.fertileSoil.blockID, 1, BlockFertileSoil.SOIL)
					}
				);
	    	}
			if (DynamicEarth.includeAdobe) {
				forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
					10,
					FluidReference.WATER.getFluidStack(50),
					null,
					new ItemStack(DynamicEarth.adobeBlob),
					new Object[] {
						"#",
						'#', new ItemStack(DynamicEarth.adobeDust)
					}
				);
				forestry.api.recipes.RecipeManagers.carpenterManager.addRecipe(
					10,
					FluidReference.WATER.getFluidStack(200),
					null,
					new ItemStack(DynamicEarth.adobeWet),
					new Object[] {
						"##",
						"##",
						'#', new ItemStack(DynamicEarth.adobeDust)
					}
				);
			}
	    }
	    if (forestry.api.recipes.RecipeManagers.squeezerManager != null) {
    		forestry.api.recipes.RecipeManagers.squeezerManager.addRecipe(
				10,
				new ItemStack[] {
					new ItemStack(DynamicEarth.mudBlob)
				},
				FluidReference.WATER.getFluidStack(50),
				new ItemStack(DynamicEarth.dirtClod),
				100
			);
			if (DynamicEarth.includeAdobe) {
				forestry.api.recipes.RecipeManagers.squeezerManager.addRecipe(
					10,
					new ItemStack[] {
						new ItemStack(DynamicEarth.adobeBlob)
					},
					FluidReference.WATER.getFluidStack(50),
					new ItemStack(DynamicEarth.adobeDust),
					100
				);
				forestry.api.recipes.RecipeManagers.squeezerManager.addRecipe(
					30,
					new ItemStack[] {
						new ItemStack(DynamicEarth.earthbowlRaw)
					},
					FluidReference.WATER.getFluidStack(150),
					new ItemStack(DynamicEarth.adobeDust, 3),
					100
				);
				forestry.api.recipes.RecipeManagers.squeezerManager.addRecipe(
					30,
					new ItemStack[] {
						new ItemStack(DynamicEarth.vaseRaw)
					},
					FluidReference.WATER.getFluidStack(250),
					new ItemStack(DynamicEarth.adobeDust, 5),
					100
				);
			}
			if (DynamicEarth.includePeat) {
				forestry.api.recipes.RecipeManagers.squeezerManager.addRecipe(
					10,
					new ItemStack[] {
						new ItemStack(DynamicEarth.peatClump)
					},
					FluidReference.WATER.getFluidStack(100),
					ModHandler.getPeatBrick(),
					100
				);
			}
	    }
	    if (forestry.api.recipes.RecipeManagers.moistenerManager != null) {
	    	if (DynamicEarth.includeDirtSlabs) {
			    forestry.api.recipes.RecipeManagers.moistenerManager.addRecipe(
			    	new ItemStack(DynamicEarth.dirtSlab),
			    	new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.MYCELIUM),
			    	5000
			    );
	    	}
	    	if (DynamicEarth.includeFertileSoil) {
			    forestry.api.recipes.RecipeManagers.moistenerManager.addRecipe(
			    	new ItemStack(DynamicEarth.fertileSoil, 1, BlockFertileSoil.SOIL),
			    	new ItemStack(DynamicEarth.fertileSoil, 1, BlockFertileSoil.MYCELIUM),
			    	5000
			    );
	    	}
	    	if (DynamicEarth.includeSandySoil) {
			    forestry.api.recipes.RecipeManagers.moistenerManager.addRecipe(
			    	new ItemStack(DynamicEarth.sandySoil, 1, BlockSandySoil.DIRT),
			    	new ItemStack(DynamicEarth.sandySoil, 1, BlockSandySoil.MYCELIUM),
			    	5000
			    );
	    	}
	    }
	}
	
	public static void addThermalExpansionRecipes() {
		if (DynamicEarth.includePeat) {
			RecipeManager.addPulverizerRecipe(
				350,
				new ItemStack(Block.cobblestoneMossy),
				new ItemStack(Block.sand),
				new ItemStack(DynamicEarth.peatMossSpecimen),
				5
			);
		}
		if (DynamicEarth.includeAdobe) {
			RecipeManager.addPulverizerRecipe(
				320,
				new ItemStack(DynamicEarth.adobe),
				new ItemStack(DynamicEarth.adobeDust, 4)
			);
			RecipeManager.addPulverizerRecipe(
				160,
				new ItemStack(DynamicEarth.adobeSingleSlab, 1, BlockAdobeSlab.ADOBE),
				new ItemStack(DynamicEarth.adobeDust, 2)
			);
			RecipeManager.addPulverizerRecipe(
				320,
				new ItemStack(DynamicEarth.adobeStairs),
				new ItemStack(DynamicEarth.adobeDust, 4)
			);
			RecipeManager.addPulverizerRecipe(
				80,
				new ItemStack(DynamicEarth.earthbowl),
				new ItemStack(DynamicEarth.adobeDust, 3)
			);
			RecipeManager.addPulverizerRecipe(
				160,
				new ItemStack(DynamicEarth.vase),
				new ItemStack(DynamicEarth.adobeDust, 5)
			);
		}
	}
	
	private static void addPulverizerRecipe(int energy, ItemStack inputStack, ItemStack outputStack) {
		RecipeManager.addPulverizerRecipe(energy, inputStack, outputStack, null, 0);		
	}

	private static void addPulverizerRecipe(int energy, ItemStack input, ItemStack output, ItemStack bonus, int chance) {
		NBTTagCompound comm = new NBTTagCompound();
		comm.setCompoundTag("input", input.writeToNBT(new NBTTagCompound()));
		comm.setCompoundTag("primaryOutput", output.writeToNBT(new NBTTagCompound()));
		comm.setInteger("energy", energy);
		if (chance > 0) {
			comm.setInteger("secondaryChance", chance);
		}
		if (bonus != null) {
			comm.setCompoundTag("secondaryOutput", bonus.writeToNBT(new NBTTagCompound()));			
		}
		FMLInterModComms.sendMessage("ThermalExpansion", "PulverizerRecipe", comm);
	}

//	public static void addThermalExpansionRecipes() throws NoClassDefFoundError, NoSuchMethodError {
//		if (thermalexpansion.api.crafting.CraftingManagers.pulverizerManager != null) {
//			if (DynamicEarth.includePeat) {
//				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
//					320,
//					new ItemStack(Block.cobblestoneMossy),
//					new ItemStack(Block.sand),
//					new ItemStack(DynamicEarth.peatMossSpecimen),
//					5
//				);
//			}
//			if (DynamicEarth.includeAdobe) {
//				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
//					320,
//					new ItemStack(DynamicEarth.adobe),
//					new ItemStack(DynamicEarth.adobeDust, 4)
//				);
//				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
//					160,
//					new ItemStack(DynamicEarth.adobeSingleSlab, 1, BlockAdobeSlab.ADOBE),
//					new ItemStack(DynamicEarth.adobeDust, 2)
//				);
//				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
//					320,
//					new ItemStack(DynamicEarth.adobeStairs),
//					new ItemStack(DynamicEarth.adobeDust, 4)
//				);
//				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
//					80,
//					new ItemStack(DynamicEarth.earthbowl),
//					new ItemStack(DynamicEarth.adobeDust, 3)
//				);
//				thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.addRecipe(
//					160,
//					new ItemStack(DynamicEarth.vase),
//					new ItemStack(DynamicEarth.adobeDust, 5)
//				);
//			}
//		}
//	}
	
	public static void addRailcraftRecipes() throws NoClassDefFoundError, NoSuchMethodError {
		if (mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher != null) {
			if (DynamicEarth.includePeat) {
				mods.railcraft.api.crafting.IRockCrusherRecipe mossyCobblestoneRecipe = mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(Block.cobblestoneMossy), false, false);
				mossyCobblestoneRecipe.addOutput(new ItemStack(Block.gravel), 1.0F);
				mossyCobblestoneRecipe.addOutput(new ItemStack(DynamicEarth.peatMossSpecimen), 0.1F);
			}
			if (DynamicEarth.includeAdobe) {
				mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(DynamicEarth.adobe), false, false).addOutput(new ItemStack(DynamicEarth.adobeDust, 4), 1.0F);
				mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(DynamicEarth.adobeSingleSlab), false, false).addOutput(new ItemStack(DynamicEarth.adobeDust, 2), 1.0F);
				mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(DynamicEarth.adobeStairs), false, false).addOutput(new ItemStack(DynamicEarth.adobeDust, 4), 1.0F);
				mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(DynamicEarth.earthbowl), false, false).addOutput(new ItemStack(DynamicEarth.adobeDust, 3), 1.0F);
				mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(
					new ItemStack(DynamicEarth.vase), false, false).addOutput(new ItemStack(DynamicEarth.adobeDust, 5), 1.0F);
			}
		}
	}
	
	public static void addIndustrialCraftRecipes() throws NoClassDefFoundError, NoSuchMethodError {
		if (ic2.api.recipe.Recipes.macerator != null) {
			ic2.api.recipe.Recipes.macerator.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(Block.dirt, 1, 0)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			ic2.api.recipe.Recipes.macerator.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(Block.grass, 1, 0)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			ic2.api.recipe.Recipes.macerator.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(Block.mycelium, 1, 0)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			ic2.api.recipe.Recipes.macerator.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.permafrost, 1, BlockPermafrost.META_PERMAFROST)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			ic2.api.recipe.Recipes.macerator.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.dirtSlab, 1, BlockDirtSlab.DIRT)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 2)
			);
			ic2.api.recipe.Recipes.macerator.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.GRASS)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 2)
			);
			ic2.api.recipe.Recipes.macerator.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.MYCELIUM)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 2)
			);
			ic2.api.recipe.Recipes.macerator.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.mudBrick)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 1)
			);
			ic2.api.recipe.Recipes.macerator.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.blockMudBrick)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			ic2.api.recipe.Recipes.macerator.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.adobeSingleSlab, 1, BlockAdobeSlab.MUDBRICK)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 2)
			);
			ic2.api.recipe.Recipes.macerator.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.mudBrickStairs)),
				null,
				new ItemStack(DynamicEarth.dirtClod, 4)
			);
			if (DynamicEarth.includeAdobe) {
				ic2.api.recipe.Recipes.macerator.addRecipe(
					new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.adobe)),
					null,
					new ItemStack(DynamicEarth.adobeDust, 4)
				);
				ic2.api.recipe.Recipes.macerator.addRecipe(
					new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.adobeSingleSlab, 1, BlockAdobeSlab.ADOBE)),
					null,
					new ItemStack(DynamicEarth.adobeDust, 2)
				);
				ic2.api.recipe.Recipes.macerator.addRecipe(
					new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.adobeStairs)),
					null,
					new ItemStack(DynamicEarth.adobeDust, 4)
				);
				ic2.api.recipe.Recipes.macerator.addRecipe(
					new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.earthbowl)),
					null,
					new ItemStack(DynamicEarth.adobeDust, 3)
				);
				ic2.api.recipe.Recipes.macerator.addRecipe(
					new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.vase, 1, 0)),
					null,
					new ItemStack(DynamicEarth.adobeDust, 5)
				);
			}
		}
		if (ic2.api.recipe.Recipes.compressor != null) {
			ic2.api.recipe.Recipes.compressor.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.dirtClod, 4)),
				null,
				new ItemStack(Block.dirt)
			);
			ic2.api.recipe.Recipes.compressor.addRecipe(
				new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.mudBlob, 4)),
				null,
				new ItemStack(DynamicEarth.mud.blockID, 1, BlockMud.NORMAL)
			);
			if (DynamicEarth.includeAdobe) {
				ic2.api.recipe.Recipes.compressor.addRecipe(
					new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.adobeBlob, 4)),
					null,
					new ItemStack(DynamicEarth.adobe)
				);
				ic2.api.recipe.Recipes.compressor.addRecipe(
					new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.earthbowlRaw)),
					null,
					new ItemStack(DynamicEarth.adobeBlob, 3)
				);
				ic2.api.recipe.Recipes.compressor.addRecipe(
					new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.vaseRaw)),
					null,
					new ItemStack(DynamicEarth.adobeBlob, 5)
				);
			}
			if (DynamicEarth.includePeat) {
				ic2.api.recipe.Recipes.compressor.addRecipe(
					new ic2.api.recipe.RecipeInputItemStack(new ItemStack(DynamicEarth.peatClump)),
					null,
					ModHandler.getPeatBrick()
				);
			}
		}
		if (ic2.api.recipe.Recipes.scrapboxDrops != null) {
			if (DynamicEarth.includePeat) {
				ic2.api.recipe.Recipes.scrapboxDrops.addDrop(
					new ItemStack(DynamicEarth.peatMossSpecimen), 0.2F
				);
			}
		}
	}
}