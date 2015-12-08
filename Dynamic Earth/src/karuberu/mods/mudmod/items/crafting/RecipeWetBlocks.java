package karuberu.mods.mudmod.items.crafting;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.liquids.LiquidHandler;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

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
				if (LiquidContainerRegistry.isFilledContainer(item)) {
					LiquidStack containerLiquid = LiquidContainerRegistry.getLiquidForFilledItem(item);
					if (containerLiquid.isLiquidEqual(LiquidDictionary.getLiquid(LiquidHandler.WATER, LiquidContainerRegistry.BUCKET_VOLUME))) {
						if (hasWater) {
							return false;
						}
						hasWater = true;
					}
				} else if (item.itemID == MudMod.dirtClod.itemID) {
					if (count == 0) {
						result = new ItemStack(MudMod.mudBlob, 1);
					} else if (result.itemID != MudMod.mudBlob.itemID) {
						return false;
					}
					count++;
				} else if (item.itemID == Block.dirt.blockID) {
					if (count == 0) {
						result = new ItemStack(MudMod.mudBlob, 4);
					} else if (result.itemID != MudMod.mudBlob.itemID) {
						return false;
					}
					count += 4;
				} else if (MudMod.includeAdobe) {
					if (item.itemID == MudMod.earthbowlRaw.itemID) {
						if (count == 0) {
							result = new ItemStack(MudMod.adobeBlob, 3);
						} else if (result.itemID != MudMod.adobeBlob.itemID) {
							return false;
						}
						count += 3;
					} else if (item.itemID == MudMod.vaseRaw.itemID) {
						if (count == 0) {
							result = new ItemStack(MudMod.adobeBlob, 5);
						} else if (result.itemID != MudMod.adobeBlob.itemID) {
							return false;
						}
						count += 5;
					} else if (item.itemID == MudMod.adobeDust.itemID) {
						if (count == 0) {
							result = new ItemStack(MudMod.adobeBlob, 1);
						} else if (result.itemID != MudMod.adobeBlob.itemID) {
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
			if (result.itemID == MudMod.adobeBlob.itemID
			&& count % 4 == 0) {
				result = new ItemStack(MudMod.adobeWet, count / 4);
			} else if (result.itemID == MudMod.adobeBlob.itemID
			&& count % 4 == 0) {
				result = new ItemStack(MudMod.mud, count / 4);
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
