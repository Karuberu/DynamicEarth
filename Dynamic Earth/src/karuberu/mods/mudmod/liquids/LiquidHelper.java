package karuberu.mods.mudmod.liquids;

import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class LiquidHelper {
	public static final String
		WATER = "Water",
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

	public static int getLiquidColor(LiquidStack liquid) {
		if (liquid != null) {
			if (liquid.containsLiquid(LiquidDictionary.getLiquid(WATER, 0))) {
				return 0x7690DA;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(MILK, 0))) {
				return 0xFFFFFF;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(SOUP, 0))) {
				return 0xCC9978;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(REDSTONE, 0))) {
				return 0xC40000;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(ENDER, 0))) {
				return 0x105E51;
//			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(GLOWSTONE, 0))) {
//				return 0xFECB03;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(CREOSOTE, 0))) {
				return 0x676311;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(OIL, 0))
			|| liquid.containsLiquid(LiquidDictionary.getLiquid("Oil", 0))) {
				return 0x202020;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(FUEL, 0))
			|| liquid.containsLiquid(LiquidDictionary.getLiquid("Fuel", 0))) {
				return 0xC1B920;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(BIOMASS, 0))) {
				return 0x29B000;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(ETHANOL, 0))) {
				return 0xFF7100;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(SEEDOIL, 0))) {
				return 0xFFFFA7;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(HONEY, 0))) {
				return 0xFFD801;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(JUICE, 0))) {
				return 0x3BC900;
			} else if (liquid.containsLiquid(LiquidDictionary.getLiquid(ICE, 0))) {
				return 0x53D9F1;
			} 
		}
		return -1;
	}
}
