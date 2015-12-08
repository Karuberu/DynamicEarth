package karuberu.dynamicearth.fluids;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.DELogger;
import karuberu.dynamicearth.client.TextureManager;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.items.ItemVase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidHandler {
	public static Fluid
		milk,
		soup;
	public static final String
		WATER = "water",
		LAVA = "lava",
		MILK = "milk",
		SOUP = "soup";

	public static void registerLiquids() {
		if (DynamicEarth.includeAdobe) {
	    	Item.bowlSoup.setContainerItem(Item.bowlEmpty);
	    	Item.potion.setContainerItem(Item.glassBottle);
	    	if (FluidRegistry.getFluid(MILK) == null) {
	    		milk = new Fluid(MILK).setBlockID(DynamicEarth.liquidMilk.itemID);
				FluidRegistry.registerFluid(milk);
	    	}
	    	if (FluidRegistry.getFluid(SOUP) == null) {
	    		soup = new Fluid(SOUP).setBlockID(DynamicEarth.liquidSoup.itemID);
				FluidRegistry.registerFluid(soup);
	    	}
	    	FluidContainerData[] data = FluidContainerRegistry.getRegisteredFluidContainerData();
	    	for (FluidContainerData dataPoint : data) {
	    		if (dataPoint.fluid.getFluid() == FluidRegistry.WATER) {
	    			dataPoint.fluid.amount = 250;
	    			break;
	    		}
	    	}
//			FluidContainerRegistry.registerFluidContainer(new FluidContainerData(FluidRegistry.getFluidStack(WATER, 250), new ItemStack(Item.potion, 1, 0), new ItemStack(Item.glassBottle)));
	    	FluidContainerRegistry.registerFluidContainer(new FluidContainerData(FluidRegistry.getFluidStack(MILK, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(Item.bucketMilk), new ItemStack(Item.bucketEmpty)));
	    	FluidContainerRegistry.registerFluidContainer(new FluidContainerData(FluidRegistry.getFluidStack(SOUP, 250), new ItemStack(Item.bowlSoup), new ItemStack(Item.bowlEmpty)));
	    	FluidContainerRegistry.registerFluidContainer(new FluidContainerData(FluidRegistry.getFluidStack(SOUP, 250), new ItemStack(DynamicEarth.earthbowlSoup), new ItemStack(DynamicEarth.earthbowl)));
        	int lavaID = FluidRegistry.getFluid(LAVA).getBlockID();
	    	for (Fluid liquid : FluidRegistry.getRegisteredFluids().values()) {
        		if (liquid.getBlockID() != lavaID) {
        			FluidStack FluidStack = new FluidStack(liquid, 1000);
	        		ItemStack itemStack = new ItemStack(DynamicEarth.vase, 1, ItemVase.getDamage(FluidStack));
	        		ItemVase.liquids.add(ItemVase.getDamage(FluidStack));
	        		FluidContainerRegistry.registerFluidContainer(new FluidContainerData(FluidStack, itemStack, new ItemStack(DynamicEarth.vase)));
        		}
        	}
		}
	}
}
