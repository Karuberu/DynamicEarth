package karuberu.mods.mudmod.items.crafting;

import java.util.ArrayList;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.items.ItemBombLit;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

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
				if (item.itemID == Item.bucketWater.itemID
				|| item.itemID == MudMod.vaseWater.itemID) {
					if (hasWater) {
						return false;
					}
					hasWater = true;
				} else if (item.itemID == Block.dirt.blockID) {
					if (count == 0) {
						result = new ItemStack(MudMod.mud, 1);
					} else if (result.itemID != MudMod.mud.blockID) {
						return false;
					}
					count++;
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
