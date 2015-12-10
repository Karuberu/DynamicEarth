package karuberu.dynamicearth.plugins.nei;

import java.util.ArrayList;
import java.util.LinkedList;
import karuberu.core.util.Helper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.items.ItemVase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import codechicken.nei.recipe.ShapelessRecipeHandler;

public class UsageHandlerVase extends ShapelessRecipeHandler {
	
	public class CachedVaseRecipe extends CachedShapelessRecipe {
		LinkedList<ItemStack>
			itemList = new LinkedList<ItemStack>();
		final ItemStack
			vase;
		final FluidContainerData[]
			containers;

		public CachedVaseRecipe(ItemStack vase, FluidContainerData[] containers) {
			this.vase = vase;
			this.containers = containers;
			this.cycle();
		}

		public void cycle() {
			int permutationTicks = cycleticks / 20;
			FluidContainerData container = this.containers[permutationTicks % this.containers.length];
			this.itemList.clear();
			this.itemList.add(this.vase);
			this.itemList.add(container.emptyContainer);
			this.setIngredients(this.itemList);
			this.setResult(container.filledContainer);
		}
	}

	private ArrayList<CachedVaseRecipe>
		cachedRecipes = new ArrayList<CachedVaseRecipe>();

	public UsageHandlerVase() {
		super();
		this.loadAllRecipes();
	}

	@Override
	public void onUpdate() {
		if (Helper.shiftKey()) {
			return;
		}
		this.cycleticks++;
		if (this.cycleticks % 20 == 0) {
			for (CachedRecipe crecipe : this.arecipes) {
				((CachedVaseRecipe)crecipe).cycle();
			}
		}
	}
	
	private void loadAllRecipes() {
		FluidContainerData[] data = FluidContainerRegistry.getRegisteredFluidContainerData();
		FluidStack fluid;
		ItemStack vase;
		ArrayList<FluidContainerData> containers = new ArrayList<FluidContainerData>();
		for (int damage : ItemVase.liquids) {
			vase = new ItemStack(DynamicEarth.vase, 1, damage);
			fluid = ItemVase.getFluidStack(damage);
			containers.clear();
			for (FluidContainerData dataPoint : data) {
				if (dataPoint.fluid.fluidID == fluid.fluidID
				&& dataPoint.filledContainer.itemID != vase.itemID) {
					containers.add(dataPoint);
				}
			}
			if (!containers.isEmpty()) {
				cachedRecipes.add(new CachedVaseRecipe(vase, containers.toArray(new FluidContainerData[0])));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		for (CachedVaseRecipe recipe : this.cachedRecipes) {
			if (ingredient.itemID != recipe.vase.itemID
			|| ingredient.getItemDamage() != recipe.vase.getItemDamage()) {
				continue;
			}
			recipe.cycle();
			this.arecipes.add(recipe);
		}
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("crafting")
		&& super.getClass() == UsageHandlerVase.class) {
			this.arecipes.addAll(this.cachedRecipes);
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}
}
