package karuberu.mods.mudmod.items.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class RecipeCake implements IRecipe {
	public static final RecipeCake
		instance = new RecipeCake();
	
	@Override
	public boolean matches(InventoryCrafting inventorycrafting, World world) {
		for (int i = 0; i < inventorycrafting.getSizeInventory(); i++) {
			ItemStack item = inventorycrafting.getStackInSlot(i);
			if (item == null) {
				return false;
			}
			switch (i) {
			case 0:
			case 1:
			case 2:
				if (LiquidContainerRegistry.isFilledContainer(item)) {
					LiquidStack containerLiquid = LiquidContainerRegistry.getLiquidForFilledItem(item);
					if (!containerLiquid.isLiquidEqual(LiquidDictionary.getLiquid("milk", LiquidContainerRegistry.BUCKET_VOLUME))) {
						return false;
					}
				} else {
					return false;
				}
				break;
			case 3:
			case 5:
				if (item.itemID != Item.sugar.itemID) {
					return false;
				}
				break;
			case 4:
				if (item.itemID != Item.egg.itemID) {
					return false;
				}
				break;
			case 6:
			case 7:
			case 8:
				if (item.itemID != Item.wheat.itemID) {
					return false;
				}
				break;
			}
		}
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		return new ItemStack(Item.cake);
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(Item.cake);
	}
}
