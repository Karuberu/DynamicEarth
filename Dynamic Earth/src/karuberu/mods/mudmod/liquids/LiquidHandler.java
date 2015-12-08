package karuberu.mods.mudmod.liquids;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.items.ItemVase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class LiquidHandler {
	public static final String
		WATER = "Water",
		MILK = "milk",
		SOUP = "soup";

	public static void registerLiquids() {
		if (MudMod.includeAdobe) {
	    	Item.bowlSoup.setContainerItem(Item.bowlEmpty);
	    	Item.potion.setContainerItem(Item.glassBottle);
	    	if (LiquidDictionary.getCanonicalLiquid(MILK) == null) {
				LiquidDictionary.getOrCreateLiquid(MILK, new LiquidStack(MudMod.liquidMilk.itemID, LiquidContainerRegistry.BUCKET_VOLUME));
	    	}
	    	if (LiquidDictionary.getCanonicalLiquid(SOUP) == null) {
				LiquidDictionary.getOrCreateLiquid(SOUP, new LiquidStack(MudMod.liquidSoup, LiquidContainerRegistry.BUCKET_VOLUME));
	    	}
			LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid(WATER, 250), new ItemStack(Item.potion, 1, 0), new ItemStack(Item.glassBottle)));
	    	LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid(MILK, LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(Item.bucketMilk), new ItemStack(Item.bucketEmpty)));
	    	LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid(SOUP, 250), new ItemStack(Item.bowlSoup), new ItemStack(Item.bowlEmpty)));
	    	LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid(SOUP, 250), new ItemStack(MudMod.earthbowlSoup), new ItemStack(MudMod.earthbowl)));
        	int lavaID = LiquidDictionary.getCanonicalLiquid("Lava").itemID;
	    	for (LiquidStack liquid : LiquidDictionary.getLiquids().values()) {
        		if (liquid.itemID != lavaID) {
	        		LiquidStack liquidCopy = liquid.copy();
	        		liquidCopy.amount = 1000;
	        		ItemStack itemStack = new ItemStack(MudMod.vase, 1, ItemVase.getDamage(liquidCopy));
	        		ItemVase.liquids.add(ItemVase.getDamage(liquidCopy));
	        		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(liquidCopy, itemStack, new ItemStack(MudMod.vase)));
        		}
        	}
		}
	}
}
