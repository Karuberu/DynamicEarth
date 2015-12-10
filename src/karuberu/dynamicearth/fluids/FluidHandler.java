package karuberu.dynamicearth.fluids;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.fluids.FluidHelper.FluidReference;
import karuberu.dynamicearth.items.ItemVase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Used to register and manage the fluids created by Dynamic Earth and
 * manage the fluids available to the vase.
 * @author Karuberu
 */
public class FluidHandler {
	public static FluidDynamicEarth
		milk,
		soup;

	public static void registerLiquids() {
		if (DynamicEarth.includeAdobe) {
	    	Item.bowlSoup.setContainerItem(Item.bowlEmpty);
	    	Item.potion.setContainerItem(Item.glassBottle);
	    	if (FluidReference.MILK.getFluid() == null) {
	    		milk = new FluidDynamicEarth(FluidReference.MILK.name);
	    		milk.setDensity(1035); // density of whole milk in kg/m^3
	    		milk.setViscosity(1035); // average viscosity of milk; ranges 1.02 - 1.05
	    		// milk.setColor(0xFEFEFE);
	    		milk.setRarity(EnumRarity.common);
				FluidRegistry.registerFluid(milk);
	    	}
	    	if (FluidReference.SOUP.getFluid() == null) {
	    		soup = new FluidDynamicEarth(FluidReference.SOUP.name);
	    		soup.setDensity(1025); // average density of soup according to online sources
	    		soup.setViscosity(1025); // complete guess
	    		// soup.setColor(0xCC9978);
	    		soup.setRarity(EnumRarity.common);
				FluidRegistry.registerFluid(soup);
	    	}
	    	FluidContainerData[] data = FluidContainerRegistry.getRegisteredFluidContainerData();
	    	for (FluidContainerData dataPoint : data) {
	    		if (dataPoint.fluid.getFluid() == FluidRegistry.WATER) {
	    			dataPoint.fluid.amount = FluidHelper.BOTTLE_VOLUME;
	    			break;
	    		}
	    	}
	    	FluidContainerRegistry.registerFluidContainer(new FluidContainerData(
	    		FluidReference.MILK.getBucketVolumeStack(),
	    		new ItemStack(Item.bucketMilk),
	    		new ItemStack(Item.bucketEmpty))
	    	);
	    	FluidContainerRegistry.registerFluidContainer(new FluidContainerData(
	    		FluidReference.SOUP.getBowlVolumeStack(),
	    		new ItemStack(Item.bowlSoup),
	    		new ItemStack(Item.bowlEmpty))
	    	);
	    	FluidContainerRegistry.registerFluidContainer(new FluidContainerData(
	    		FluidReference.SOUP.getBowlVolumeStack(),
	    		new ItemStack(DynamicEarth.earthbowlSoup),
	    		new ItemStack(DynamicEarth.earthbowl))
	    	);
	    	for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
        		FluidHandler.addVaseLiquid(fluid);
        	}
		}
	}
	
	public static void addVaseLiquid(Fluid fluid) {
		String name = fluid.getName().toLowerCase();
		if (name == null) {
			return;
		}
		if (!ItemVase.whitelist.contains(name)) {
			if (ItemVase.blacklist.contains(name)) {
				logSkippedFluid(name, "Blacklisted.");
				return;
			}
			if (fluid.getTemperature() > ItemVase.maxTemperature) {
				logSkippedFluid(name, "Too hot (" + fluid.getTemperature() + " Kelvin)");
				return;
			}
			if (fluid.getTemperature() < ItemVase.minTemperature) {
				logSkippedFluid(name, "Too cold (" + fluid.getTemperature() + " Kelvin)");
				return;
			}
			if (name.matches("(^|\\s)molten(\\s|$)")) {
				logSkippedFluid(name, "Name contained \"molten\"");
				return;
			}
			if (name.matches("^lava(\\s|$)")) {
				logSkippedFluid(name, "Name contained \"lava\"");
				return;
			}
		}
		FluidStack FluidStack = new FluidStack(fluid, 1000);
		ItemStack itemStack = new ItemStack(DynamicEarth.vase, 1, ItemVase.getDamage(FluidStack));
		ItemVase.liquids.add(ItemVase.getDamage(FluidStack));
		FluidContainerRegistry.registerFluidContainer(new FluidContainerData(FluidStack, itemStack, new ItemStack(DynamicEarth.vase)));
		DynamicEarth.logger.finer("Fluid \"" + name + "\" was added as a vase fluid.");
	}
	
	private static void logSkippedFluid(String fluidName, String reason) {
		DynamicEarth.logger.finer("Fluid \"" + fluidName + "\" could not be added as a vase fluid. Reason: " + reason);
	}
}
