package karuberu.dynamicearth;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public final class FuelHandler implements IFuelHandler {

	public static FuelHandler
		instance = new FuelHandler();
	public static int
		peatBurnTime = 1200,
		peatForestryBurnTime = 4500;
	
	@Override
	public int getBurnTime(ItemStack fuel) {
		if (fuel == null) {
			return 0;
		}
		if (DynamicEarth.includePeat
		&& fuel.itemID == DynamicEarth.peatBrick.itemID) {
			return peatBurnTime;
		}
		return 0;
	}
	
	public static void register() {
    	GameRegistry.registerFuelHandler(instance);
	}
	
	public static void registerForestryFuels() {
		if (forestry.api.fuels.FuelManager.copperEngineFuel != null) {
			forestry.api.fuels.FuelManager.copperEngineFuel.put(
				new ItemStack(DynamicEarth.peatBrick),
				new forestry.api.fuels.EngineCopperFuel(
					new ItemStack(DynamicEarth.peatBrick),
					1,
					peatForestryBurnTime
				)
			);
		}
	}
}
