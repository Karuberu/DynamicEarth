package karuberu.dynamicearth.items.crafting;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.fluids.FluidHelper.FluidReference;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class RecipeWetBlocks implements IRecipe {
	public static final RecipeWetBlocks
		instance = new RecipeWetBlocks();
    private ItemStack result;

	@Override
	public boolean matches(InventoryCrafting inventorycrafting, World world) {
		int count = 0;
		boolean hasWater = false;
		
		for (int i = 0; i < inventorycrafting.getSizeInventory(); i++) {
			ItemStack item = inventorycrafting.getStackInSlot(i);
			if (item != null) {
				if (FluidContainerRegistry.isFilledContainer(item)) {
					FluidStack containerLiquid = FluidContainerRegistry.getFluidForFilledItem(item);
					if (containerLiquid.isFluidEqual(FluidReference.WATER.getBucketVolumeStack())) {
						if (hasWater) {
							return false;
						}
						hasWater = true;
					}
				} else if (DynamicEarth.includeMud) {
					if (item.itemID == DynamicEarth.dirtClod.itemID) {
						if (count == 0) {
							result = new ItemStack(DynamicEarth.mudBlob, 1);
						} else if (result.itemID != DynamicEarth.mudBlob.itemID) {
							return false;
						}
						count++;
					} else if (item.itemID == Block.dirt.blockID) {
						if (count == 0) {
							result = new ItemStack(DynamicEarth.mudBlob, 4);
						} else if (result.itemID != DynamicEarth.mudBlob.itemID) {
							return false;
						}
						count += 4;
					}
				} else if (DynamicEarth.includeAdobe) {
					if (item.itemID == DynamicEarth.earthbowlRaw.itemID) {
						if (count == 0) {
							result = new ItemStack(DynamicEarth.adobeBlob, 3);
						} else if (result.itemID != DynamicEarth.adobeBlob.itemID) {
							return false;
						}
						count += 3;
					} else if (item.itemID == DynamicEarth.vaseRaw.itemID) {
						if (count == 0) {
							result = new ItemStack(DynamicEarth.adobeBlob, 5);
						} else if (result.itemID != DynamicEarth.adobeBlob.itemID) {
							return false;
						}
						count += 5;
					} else if (item.itemID == DynamicEarth.adobeDust.itemID) {
						if (count == 0) {
							result = new ItemStack(DynamicEarth.adobeBlob, 1);
						} else if (result.itemID != DynamicEarth.adobeBlob.itemID) {
							return false;
						}
						count++;
					}
				} else {
					return false;
				}
			}
		}
		if (count > 0 && hasWater) {
			if (DynamicEarth.includeAdobe
			&& result.itemID == DynamicEarth.adobeBlob.itemID
			&& count % 4 == 0) {
				result = new ItemStack(DynamicEarth.adobeWet, count / 4);
			} else if (DynamicEarth.includeMud
			&& result.itemID == DynamicEarth.mudBlob.itemID
			&& count % 4 == 0) {
				result = new ItemStack(DynamicEarth.mud, count / 4);
			} else {
				result.stackSize = count;
			}
			return true;
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		return result;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}
}
