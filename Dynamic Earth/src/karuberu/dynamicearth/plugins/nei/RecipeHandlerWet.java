package karuberu.dynamicearth.plugins.nei;

import java.util.ArrayList;
import java.util.LinkedList;
import karuberu.core.util.Helper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import codechicken.nei.recipe.ShapelessRecipeHandler;

public class RecipeHandlerWet extends ShapelessRecipeHandler {

	private static final class ItemStackPair {
		public final ItemStack dry, wet;
		ItemStackPair(ItemStack dry, ItemStack wet) {
			this.dry = dry;
			this.wet = wet;
		}
	}
	
	public class CachedWetRecipe extends CachedShapelessRecipe {
		LinkedList<ItemStack>
			itemList = new LinkedList<ItemStack>();
		ItemStackPair
			blockPair;

		public CachedWetRecipe(ItemStackPair blockPair) {
			this.blockPair = blockPair;
			this.cycle();
		}

		public void cycle() {
			int permutationTicks = cycleticks / 20;
			itemList.clear();
			itemList.add(waterContainers.get(permutationTicks % waterContainers.size()));
			for (int i = 0; i <= permutationTicks % 8; i++) {
				itemList.add(this.blockPair.dry);
			}
			this.setIngredients(itemList);
			ItemStack newResult;
			newResult = this.blockPair.wet.copy();
			newResult.stackSize *= itemList.size() - 1;
			this.setResult(newResult);
		}
	}

	private ArrayList<CachedWetRecipe>
		cachedRecipes = new ArrayList<CachedWetRecipe>();
	protected static ArrayList<ItemStackPair>
		blocks = new ArrayList<ItemStackPair>();
	protected ArrayList<ItemStack>
		waterContainers = new ArrayList<ItemStack>();

	public RecipeHandlerWet() {
		super();
		this.populateWaterContainers();
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
				((CachedWetRecipe)crecipe).cycle();
			}
		}
	}

	private void populateWaterContainers() {
		FluidContainerData[] data = FluidContainerRegistry.getRegisteredFluidContainerData();
		for (FluidContainerData dataPoint : data) {
			if (dataPoint.fluid.fluidID == FluidRegistry.WATER.getID()) {
				waterContainers.add(dataPoint.filledContainer);
			}
		}
	}
	
	private void loadAllRecipes() {
		for (ItemStackPair blockPair : RecipeHandlerWet.blocks) {
			cachedRecipes.add(new CachedWetRecipe(blockPair));
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for (CachedWetRecipe recipe : this.cachedRecipes) {
			if (recipe.result.item.itemID != result.itemID
			|| recipe.result.item.getItemDamage() != result.getItemDamage()) {
				continue;
			}
			recipe.cycle();
			this.arecipes.add(recipe);
		}
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("crafting")
		&& super.getClass() == RecipeHandlerWet.class) {
			this.arecipes.addAll(this.cachedRecipes);
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}
	
	public static void addWetRecipe(ItemStack dry, ItemStack wet) {
		blocks.add(new ItemStackPair(dry, wet));
	}
}
