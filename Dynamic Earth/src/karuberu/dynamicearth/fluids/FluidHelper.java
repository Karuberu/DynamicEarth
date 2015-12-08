package karuberu.dynamicearth.fluids;

import karuberu.dynamicearth.DELogger;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidHelper {
	public static final String
		WATER = "water",
		MILK = "milk",
		SOUP = "soup",
		REDSTONE = "redstone",
		ENDER = "ender",
		GLOWSTONE = "glowstone",
		CREOSOTE = "Creosote Oil",
		OIL = "oil",
		FUEL = "fuel",
		BIOMASS = "biomass",
		ETHANOL = "ethanol",
		SEEDOIL = "seedoil",
		HONEY = "honey",
		JUICE = "juice",
		ICE = "ice";

	public static int getFluidColor(FluidStack fluidStack) {
		if (fluidStack == null) {
			return -1;
		} else if (fluidStack.getFluid().getColor() != 0xFFFFFF) {
			DELogger.debug(fluidStack.getFluid().getColor());
			return fluidStack.getFluid().getColor();
		} else {
			if (fluidStack.containsFluid(FluidRegistry.getFluidStack(WATER, 0))) {
				return 0x7690DA;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(MILK, 0))) {
				return 0xFFFFFF;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(SOUP, 0))) {
				return 0xCC9978;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(REDSTONE, 0))) {
				return 0xC40000;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(ENDER, 0))) {
				return 0x105E51;
//			} else if (liquid.containsLiquid(FluidRegistry.getFluidStack(GLOWSTONE, 0))) {
//				return 0xFECB03;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(CREOSOTE, 0))) {
				return 0x676311;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(OIL, 0))
			|| fluidStack.containsFluid(FluidRegistry.getFluidStack("Oil", 0))) {
				return 0x202020;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(FUEL, 0))
			|| fluidStack.containsFluid(FluidRegistry.getFluidStack("Fuel", 0))) {
				return 0xC1B920;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(BIOMASS, 0))) {
				return 0x29B000;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(ETHANOL, 0))) {
				return 0xFF7100;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(SEEDOIL, 0))) {
				return 0xFFFFA7;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(HONEY, 0))) {
				return 0xFFD801;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(JUICE, 0))) {
				return 0x3BC900;
			} else if (fluidStack.containsFluid(FluidRegistry.getFluidStack(ICE, 0))) {
				return 0x53D9F1;
			}
		}
		return -1;
	}
}
