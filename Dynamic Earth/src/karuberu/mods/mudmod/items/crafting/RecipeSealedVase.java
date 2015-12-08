package karuberu.mods.mudmod.items.crafting;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.items.ItemVase;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class RecipeSealedVase implements IRecipe {
	public static final RecipeSealedVase
		instance = new RecipeSealedVase();
	private ItemStack result;

	@Override
	public boolean matches(InventoryCrafting inventorycrafting, World world) {
		ItemStack vase = null;
		ItemStack container = null;
		LiquidStack liquid = null;
		for (int i = 0; i < inventorycrafting.getSizeInventory(); i++) {
			ItemStack item = inventorycrafting.getStackInSlot(i);
			if (item != null) {
				if (item.itemID == MudMod.vase.itemID) {
					if (vase != null) {
						return false;
					} else {
						vase = item;
					}
				} else if (LiquidContainerRegistry.isFilledContainer(item)) {
					LiquidStack containerLiquid = LiquidContainerRegistry.getLiquidForFilledItem(item);
					if (liquid == null) {
						liquid = containerLiquid;
					} else if (liquid.isLiquidEqual(containerLiquid)) {
						liquid.amount += containerLiquid.amount;
					} else {
						return false;
					}
				} else if (LiquidContainerRegistry.isEmptyContainer(item)) {
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
			LiquidStack vaseContents = ItemVase.getLiquidStack(vase.getItemDamage());
			if (container != null && vaseContents != null) {
				ItemStack filledContainer = LiquidContainerRegistry.fillLiquidContainer(vaseContents, container);
				if (filledContainer != null
				&& container.stackSize <= filledContainer.getMaxStackSize()) {
					NBTTagCompound tagCompound = vase.getTagCompound();
					if (tagCompound == null) {
						tagCompound = new NBTTagCompound();
					}
					tagCompound.setInteger("craftingDrain", LiquidContainerRegistry.getLiquidForFilledItem(filledContainer).amount);
					vase.setTagCompound(tagCompound);
					result = filledContainer.copy();
					result.stackSize = container.stackSize;
					return true;
				}
			} else if (liquid != null
			&& !liquid.isLiquidEqual(LiquidDictionary.getLiquid("Lava", 1000))) {
				/* Combines the liquid in the vase and the liquid from the
				 * container. If this amount exceeds the capacity of the vase,
				 * prevent the item from crafting. */
				if (liquid.isLiquidEqual(vaseContents)) {
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
