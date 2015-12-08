package karuberu.mods.mudmod;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.fuels.EngineCopperFuel;
import forestry.api.fuels.FuelManager;

public class FuelHandler implements IFuelHandler {

	public static FuelHandler
		instance = new FuelHandler();
	public static int
		peatBurnTime = 1600,
		peatForestryBurnTime = 4500;
	
	@Override
	public int getBurnTime(ItemStack fuel) {
		if (fuel == null) {
			return 0;
		}
		if (MudMod.includePeat
		&& fuel.itemID == MudMod.peatBrick.itemID) {
			return peatBurnTime;
		}
		return 0;
	}
	
	public static void register() {
    	GameRegistry.registerFuelHandler(instance);
    	addAdditionalFuels();
	}
	
	public static void addAdditionalFuels() {
		// Forestry fuels
		if (MudMod.enableForestryIntegration) {
			if (FuelManager.copperEngineFuel != null) {
				FuelManager.copperEngineFuel.put(
					new ItemStack(MudMod.peatBrick),
					new EngineCopperFuel(
						new ItemStack(MudMod.peatBrick),
						1,
						peatForestryBurnTime
					)
				);
			}
		}
	}
}
