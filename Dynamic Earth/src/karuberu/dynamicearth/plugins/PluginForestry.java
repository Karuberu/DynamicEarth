package karuberu.dynamicearth.plugins;

import karuberu.dynamicearth.ConfigurationManager;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockGrassSlab;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.fluids.FluidHelper.FluidReference;
import karuberu.dynamicearth.items.crafting.RecipeManager;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.core.ItemInterface;
import forestry.api.recipes.RecipeManagers;

public class PluginForestry implements IDynamicEarthPlugin {

	private static int
		peatBurnTimeEngine = 4500;
	private static boolean
		useForestryPeat = false;
	private static final int
		DIGGER = 1,
		FORESTER = 2;
	
	@Override
	public String getName() {
		return "Forestry Plugin";
	}

	@Override
	public String getErrorReportRequestMessage() {
		return PluginHandler.pleaseNotify;
	}

	@Override
	public boolean requiredModsAreLoaded() {
		return Loader.isModLoaded("Forestry");
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {
		ConfigurationManager.setConfigurationFile(event.getSuggestedConfigurationFile());
		PluginForestry.useForestryPeat = ConfigurationManager.get(
			"Forestry", 
			"useForestryPeat", 
			PluginForestry.useForestryPeat, 
			"If this setting is enabled and Forestry is present, Dynamic Earth will " +
			"use Forestry's peat item instead of creating a new one."
		);
		PluginForestry.peatBurnTimeEngine = ConfigurationManager.get(
			"Forestry", 
			"peatBurnTime", 
			PluginForestry.peatBurnTimeEngine, 
			"The amount of time Dynamic Earth's peat will burn in a Peat-fired Engine " +
			"from Forestry. By default, this is lower than Forestry's peat."
		);
		ConfigurationManager.closeConfig();
	}

	@Override
	public void initialize() throws NoClassDefFoundError, NoSuchMethodError, Exception {
		this.addBackpackItems();
    	if (PluginHandler.useForestryPeat) {
    		OreDictionary.registerOre(OreDictionary.getOreID("brickPeat"), DynamicEarth.peatBrick);
    		RecipeManager.setPeatBrick(ItemInterface.getItem("brickPeat"));
    	}
    	this.addForestryRecipes();
    	this.registerForestryFuels();
	}
	
	private void addBackpackItems() {
    	if (forestry.api.storage.BackpackManager.backpackItems != null) {
	    	if (forestry.api.storage.BackpackManager.backpackItems.length >= 4) {
	    		if (DynamicEarth.includeMud) {
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.mud));
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.mudBlob));
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.dirtClod));
	    		}
		    	if (DynamicEarth.includeDirtSlabs) {
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.dirtSlab));
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.GRASS));
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.MYCELIUM));
	    		}
		    	if (DynamicEarth.includePermafrost) {
	    			forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.permafrost));
	    		}
	    		if (DynamicEarth.includePeat) {
		    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(DynamicEarth.peat, 1, BlockPeat.WET));
		    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(DynamicEarth.peat, 1, BlockPeat.DRY));
		    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(DynamicEarth.peatClump));
		    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(DynamicEarth.peatBrick));
		    		forestry.api.storage.BackpackManager.backpackItems[FORESTER].add(new ItemStack(DynamicEarth.peatMossSpecimen));
	    		}
	    		if (DynamicEarth.includeFertileSoil) {
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.fertileSoil, 1, DynamicEarth.fertileSoil.DIRT));
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.fertileSoil, 1, DynamicEarth.fertileSoil.GRASS));
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.fertileSoil, 1, DynamicEarth.fertileSoil.MYCELIUM));
	    		}
	    		if (DynamicEarth.includeSandySoil) {
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.sandySoil, 1, DynamicEarth.sandySoil.DIRT));
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.sandySoil, 1, DynamicEarth.sandySoil.GRASS));
		    		forestry.api.storage.BackpackManager.backpackItems[DIGGER].add(new ItemStack(DynamicEarth.sandySoil, 1, DynamicEarth.sandySoil.MYCELIUM));
	    		}
	    	}
    	}
	}

	private void registerForestryFuels() {
		if (forestry.api.fuels.FuelManager.copperEngineFuel != null) {
			forestry.api.fuels.FuelManager.copperEngineFuel.put(
				new ItemStack(DynamicEarth.peatBrick),
				new forestry.api.fuels.EngineCopperFuel(
					new ItemStack(DynamicEarth.peatBrick),
					1,
					PluginForestry.peatBurnTimeEngine
				)
			);
		}
	}
	
	private void addForestryRecipes() {
		if (DynamicEarth.includePeat
		&& !PluginHandler.useForestryPeat
		&& ItemInterface.getItem("bituminousPeat") != null) {
		    GameRegistry.addRecipe(
	        	new ItemStack(ItemInterface.getItem("bituminousPeat").getItem(), 1),
	        	new Object[] {
	        		" A ",
		            "POP",
		            " A ",
		            'P', DynamicEarth.peatBrick,
		            'O', ItemInterface.getItem("propolis").getItem(),
		            'A', ItemInterface.getItem("ash").getItem()
		        }
	        );
		}
	    if (RecipeManagers.carpenterManager != null) {
			if (DynamicEarth.includeMud) {
				RecipeManagers.carpenterManager.addRecipe(
					10,
					FluidReference.WATER.getFluidStack(50),
					null,
					new ItemStack(DynamicEarth.mudBlob),
					new Object[] {
						"#",
						'#', new ItemStack(DynamicEarth.dirtClod)
					}
				);
				RecipeManagers.carpenterManager.addRecipe(
					10,
					FluidReference.WATER.getFluidStack(200),
					null,
					new ItemStack(DynamicEarth.mud.blockID, 1, DynamicEarth.mud.NORMAL),
					new Object[] {
						"##",
						"##",
						'#', new ItemStack(DynamicEarth.dirtClod)
					}
				);
		    	RecipeManagers.carpenterManager.addRecipe(
					10,
					FluidReference.WATER.getFluidStack(200),
					null,
					new ItemStack(DynamicEarth.mud.blockID, 1, DynamicEarth.mud.NORMAL),
					new Object[] {
						"#",
						'#', new ItemStack(Block.dirt)
					}
				);
			}
	    	if (DynamicEarth.includeFertileSoil) {
		    	RecipeManagers.carpenterManager.addRecipe(
					10,
					FluidReference.WATER.getFluidStack(200),
					null,
					new ItemStack(DynamicEarth.fertileMud.blockID, 1, DynamicEarth.fertileMud.NORMAL),
					new Object[] {
						"#",
						'#', new ItemStack(DynamicEarth.fertileSoil.blockID, 1, DynamicEarth.fertileSoil.DIRT)
					}
				);
	    	}
			if (DynamicEarth.includeAdobe) {
				RecipeManagers.carpenterManager.addRecipe(
					10,
					FluidReference.WATER.getFluidStack(50),
					null,
					new ItemStack(DynamicEarth.adobeBlob),
					new Object[] {
						"#",
						'#', new ItemStack(DynamicEarth.adobeDust)
					}
				);
				RecipeManagers.carpenterManager.addRecipe(
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
	    if (RecipeManagers.squeezerManager != null) {
    		RecipeManagers.squeezerManager.addRecipe(
				10,
				new ItemStack[] {
					new ItemStack(DynamicEarth.mudBlob)
				},
				FluidReference.WATER.getFluidStack(50),
				new ItemStack(DynamicEarth.dirtClod),
				100
			);
			if (DynamicEarth.includeAdobe) {
				RecipeManagers.squeezerManager.addRecipe(
					10,
					new ItemStack[] {
						new ItemStack(DynamicEarth.adobeBlob)
					},
					FluidReference.WATER.getFluidStack(50),
					new ItemStack(DynamicEarth.adobeDust),
					100
				);
				RecipeManagers.squeezerManager.addRecipe(
					30,
					new ItemStack[] {
						new ItemStack(DynamicEarth.earthbowlRaw)
					},
					FluidReference.WATER.getFluidStack(150),
					new ItemStack(DynamicEarth.adobeDust, 3),
					100
				);
				RecipeManagers.squeezerManager.addRecipe(
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
				RecipeManagers.squeezerManager.addRecipe(
					10,
					new ItemStack[] {
						new ItemStack(DynamicEarth.peatClump)
					},
					FluidReference.WATER.getFluidStack(100),
					RecipeManager.getPeatBrick(),
					100
				);
			}
	    }
	    if (RecipeManagers.moistenerManager != null) {
	    	if (DynamicEarth.includeDirtSlabs) {
			    RecipeManagers.moistenerManager.addRecipe(
			    	new ItemStack(DynamicEarth.dirtSlab),
			    	new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.MYCELIUM),
			    	5000
			    );
	    	}
	    	if (DynamicEarth.includeFertileSoil) {
			    RecipeManagers.moistenerManager.addRecipe(
			    	new ItemStack(DynamicEarth.fertileSoil, 1, DynamicEarth.fertileSoil.DIRT),
			    	new ItemStack(DynamicEarth.fertileSoil, 1, DynamicEarth.fertileSoil.MYCELIUM),
			    	5000
			    );
	    	}
	    	if (DynamicEarth.includeSandySoil) {
			    RecipeManagers.moistenerManager.addRecipe(
			    	new ItemStack(DynamicEarth.sandySoil, 1, DynamicEarth.sandySoil.DIRT),
			    	new ItemStack(DynamicEarth.sandySoil, 1, DynamicEarth.sandySoil.MYCELIUM),
			    	5000
			    );
	    	}
	    }
	}
}
