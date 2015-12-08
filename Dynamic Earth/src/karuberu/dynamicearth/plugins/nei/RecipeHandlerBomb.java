package karuberu.dynamicearth.plugins.nei;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import karuberu.core.util.Helper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.items.ItemBombLit;
import karuberu.dynamicearth.items.crafting.RecipeBombs;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.nei.InventoryCraftingDummy;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RecipeHandlerBomb extends ShapelessRecipeHandler {

	public class CachedBombRecipe extends CachedShapelessRecipe {
		LinkedList<ItemStack>
			itemList = new LinkedList<ItemStack>();
		public int
			recipeType;
		
		public CachedBombRecipe(int type) {
			super(new ItemStack(DynamicEarth.bomb));
			this.recipeType = type;
			this.cycle();
		}
		
		public void cycle() {
			int permutationTicks = cycleticks / 20;
			ItemStack
				string = getNEIItemStack(Item.silk),
				gunpowder = getNEIItemStack(Item.gunpowder),
				explosive = getNEIItemStack(explosives[cycleticks / 120 % explosives.length]),
				shell = getNEIItemStack(shells[cycleticks / 120 % shells.length]);
			itemList.clear();
			itemList.add(shell);
			itemList.add(shell);
			itemList.add(explosive);
			itemList.add(string);
			if (recipeType == 0) {
				int extras = permutationTicks % (10 - itemList.size());
				DynamicEarth.logger.debugFiltered(extras + itemList.size() > 9, "Attempting to add too much string.");
				for(int i = 0; i < extras && i < ItemBombLit.maxFuseLength - 1; i++) {
			   		itemList.add(string);
				}
			} else if (recipeType == 1) {
				int extras = permutationTicks % (10 - itemList.size());
				DynamicEarth.logger.debugFiltered(extras + itemList.size() > 9, "Attempting to add too much gunpowder.");
				int maxGunpowder = RecipeBombs.maxGunpowder - (explosive.itemID == Item.gunpowder.itemID ? 1 : 0);
				for(int i = 0; i < extras && i < maxGunpowder; i++) {
			   		itemList.add(gunpowder);
				}
			} else if (recipeType >= 2) {
				Item additive = additives[recipeType - 2];
				if (additive.itemID == Item.fireworkCharge.itemID) {
					int extras = permutationTicks % (10 - itemList.size());
					for(int i = 0; i < extras; i++) {
				   		itemList.add(getNEIItemStack(additive));
					}
				} else if (additive.itemID != Item.fireballCharge.itemID
				|| explosive.itemID != Item.fireballCharge.itemID) {
					itemList.add(getNEIItemStack(additive));
				}
			}
			this.setIngredients(itemList);
			List<PositionedStack> ingreds = this.getIngredients();
			for(int i = 0; i < 9; i++) {
				inventoryCrafting.setInventorySlotContents(i, i < ingreds.size() ? ingreds.get(i).item : null);
			}
			
			if(!recipeBombs.matches(inventoryCrafting, null)) {
				throw new RuntimeException("Invalid Recipe?");
			}
			this.setResult(recipeBombs.getCraftingResult(null));
		}
	}
	
	private InventoryCrafting
		inventoryCrafting = new InventoryCraftingDummy();
	private RecipeBombs
		recipeBombs = new RecipeBombs();
	private ArrayList<CachedBombRecipe>
		cachedRecipes = new ArrayList<CachedBombRecipe>();
	private static final int
		NEI_DAMAGE = Short.MAX_VALUE;
	protected final Item[]
		shells = new Item[] {DynamicEarth.earthbowl},
		explosives = new Item[] {Item.gunpowder, Item.fireballCharge, Item.fireworkCharge},
		additives = new Item[] {Item.potion, Item.fireballCharge, Item.fireworkCharge};
	
	public RecipeHandlerBomb() {
		super();
		stackorder = new int[][]{
			{0,0},
			{1,0},
			{2,0},
			{0,1},
			{1,1},
			{2,1},
			{0,2},
			{1,2},
			{2,2}};
		this.loadAllBombs();
	}

	@Override
	public String getRecipeName() {
		return "Earthenware Hand-bomb";
	}
	
	public void onUpdate() {
		if (Helper.shiftKey()) {
			return;
		}
		this.cycleticks += 1;
		if (this.cycleticks % 20 == 0) {
			for (TemplateRecipeHandler.CachedRecipe crecipe : this.arecipes) {
				((CachedBombRecipe)crecipe).cycle();
			}
		}
	}

	private void loadAllBombs() {
		for (int i = 0; i < 2 + additives.length; i++) {
			cachedRecipes.add(new CachedBombRecipe(i));
		}
	}
	
	private ItemStack getNEIItemStack(Item item) {
		return item == null ? null : new ItemStack(item, 1, NEI_DAMAGE);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for (CachedBombRecipe recipe : this.cachedRecipes) {
			if (recipe.result.item.itemID != result.itemID) {
				continue;
			}
			recipe.cycle();
			this.arecipes.add(recipe);
		}
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("crafting")
		&& super.getClass() == RecipeHandlerBomb.class) {
			this.arecipes.addAll(this.cachedRecipes);
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}
}
