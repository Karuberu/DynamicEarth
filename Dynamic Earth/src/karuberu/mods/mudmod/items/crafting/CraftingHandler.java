package karuberu.mods.mudmod.items.crafting;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingHandler implements ICraftingHandler {
	
	public static final CraftingHandler
		instance = new CraftingHandler();
	
	public static void register() {
		GameRegistry.registerCraftingHandler(CraftingHandler.instance);
	}

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
		if (item.itemID == MudMod.vase.itemID) {
			for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
				ItemStack craftingItem = craftMatrix.getStackInSlot(i);
				if (craftingItem != null) {
					if (craftingItem.itemID == MudMod.vase.itemID) {
						craftMatrix.setInventorySlotContents(i, null);
					}
				}
			}
		} else if (item.itemID == MudMod.grassSlab.blockID) {
			ItemStack dirtSlabs = new ItemStack(MudMod.dirtSlab, 3);
			if (!player.inventory.addItemStackToInventory(dirtSlabs)) {
				player.dropPlayerItem(dirtSlabs);
			}
		} else if (item.itemID == MudMod.bomb.itemID) {
			for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
				ItemStack craftingItem = craftMatrix.getStackInSlot(i);
				if (craftingItem != null) {
					if (craftingItem.itemID == Item.potion.itemID
					&& ItemPotion.isSplash(craftingItem.getItemDamage())) {
						craftMatrix.setInventorySlotContents(i, null);
					}
				}
			}
		}
	}
	
	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {}

}
