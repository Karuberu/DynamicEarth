package karuberu.dynamicearth.items.crafting;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.items.ItemVase;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
public class RecipeVase implements IRecipe {
	public static final RecipeVase
		instance = new RecipeVase();
	private ItemStack result;

	@Override
	public boolean matches(InventoryCrafting inventorycrafting, World world) {
		ItemStack vase = null;
		ItemStack container = null;
		FluidStack liquid = null;
		for (int i = 0; i < inventorycrafting.getSizeInventory(); i++) {
			ItemStack item = inventorycrafting.getStackInSlot(i);
			if (item != null) {
				if (item.itemID == DynamicEarth.vase.itemID) {
					if (vase != null) {
						return false;
					} else {
						vase = item;
					}
				} else if (FluidContainerRegistry.isFilledContainer(item)) {
					FluidStack containerLiquid = FluidContainerRegistry.getFluidForFilledItem(item);
					if (liquid == null) {
						liquid = containerLiquid;
					} else if (liquid.isFluidEqual(containerLiquid)) {
						liquid.amount += containerLiquid.amount;
					} else {
						return false;
					}
				} else if (FluidContainerRegistry.isEmptyContainer(item)) {
					if (container == null) {
						container = item.copy();
						container.stackSize = 1;
					} else if (container.getItem().equals(item.getItem())) {
						container.stackSize++;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		if (vase == null) {
			return false;
		} else {
			FluidStack vaseContents = ItemVase.getFluidStack(vase.getItemDamage());
			if (container != null && vaseContents != null) {
				ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(vaseContents, container);
				if (filledContainer != null
				&& container.stackSize <= filledContainer.getMaxStackSize()) {
					NBTTagCompound tagCompound = vase.getTagCompound();
					if (tagCompound == null) {
						tagCompound = new NBTTagCompound();
					}
					tagCompound.setInteger(ItemVase.CRAFTING_DRAIN, FluidContainerRegistry.getFluidForFilledItem(filledContainer).amount);
					vase.setTagCompound(tagCompound);
					result = filledContainer.copy();
					result.stackSize = container.stackSize;
					return true;
				}
			} else if (liquid != null
			&& !liquid.isFluidEqual(FluidRegistry.getFluidStack("Lava", 1000))) {
				/* Combines the liquid in the vase and the liquid from the
				 * container. If this amount exceeds the capacity of the vase,
				 * prevent the item from crafting. */
				if (liquid.isFluidEqual(vaseContents)) {
					liquid.amount += vaseContents.amount;
				} else if (vaseContents != null
				&& vaseContents.amount > 0) {
					return false;
				}
				if (liquid.amount > ItemVase.capacity) {
					return false;
				}
				/* Copy the vase and add the new liquid to its NBT data, then
				 * set this as the result. */
				NBTTagCompound tagCompound = new NBTTagCompound();
				liquid.writeToNBT(tagCompound);
				this.result = vase.copy();
				this.result.setTagCompound(tagCompound);
				this.result.setItemDamage(ItemVase.getDamage(liquid));
				return true;
			}
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
