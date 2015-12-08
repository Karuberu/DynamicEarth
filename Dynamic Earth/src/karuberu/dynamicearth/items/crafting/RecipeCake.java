package karuberu.dynamicearth.items.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
				if (FluidContainerRegistry.isFilledContainer(item)) {
					FluidStack containerLiquid = FluidContainerRegistry.getFluidForFilledItem(item);
					if (!containerLiquid.isFluidEqual(FluidRegistry.getFluidStack("milk", FluidContainerRegistry.BUCKET_VOLUME))) {
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
