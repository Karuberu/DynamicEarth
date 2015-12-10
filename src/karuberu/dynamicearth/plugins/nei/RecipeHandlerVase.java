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

public class RecipeHandlerVase extends ShapelessRecipeHandler {
	
	public class CachedVaseRecipe extends CachedShapelessRecipe {
		LinkedList<ItemStack>
			itemList = new LinkedList<ItemStack>();
		final ItemStack
			vase;
		final FluidContainerData
			container;

		public CachedVaseRecipe(ItemStack vase, FluidContainerData container) {
			this.vase = vase;
			this.container = container;
			this.cycle();
		}

		public void cycle() {
			itemList.clear();
			itemList.add(this.vase);
			itemList.add(this.container.emptyContainer);
			this.setIngredients(itemList);
			this.setResult(this.container.filledContainer);
		}
	}

	private ArrayList<CachedVaseRecipe>
		cachedRecipes = new ArrayList<CachedVaseRecipe>();

	public RecipeHandlerVase() {
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
		for (int damage : ItemVase.liquids) {
			vase = new ItemStack(DynamicEarth.vase, 1, damage);
			fluid = ItemVase.getFluidStack(damage);
			for (FluidContainerData dataPoint : data) {
				if (dataPoint.fluid.fluidID == fluid.fluidID
				&& dataPoint.filledContainer.itemID != vase.itemID) {
					cachedRecipes.add(new CachedVaseRecipe(vase, dataPoint));
				}
			}
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for (CachedVaseRecipe recipe : this.cachedRecipes) {
			if (result.itemID != recipe.result.item.itemID
			|| result.getItemDamage() != recipe.result.item.getItemDamage()) {
				continue;
			}
			recipe.cycle();
			this.arecipes.add(recipe);
		}
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("crafting")
		&& super.getClass() == RecipeHandlerVase.class) {
			this.arecipes.addAll(this.cachedRecipes);
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}
}
