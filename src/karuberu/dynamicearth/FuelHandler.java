package karuberu.dynamicearth;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public final class FuelHandler implements IFuelHandler {

	public static FuelHandler
		instance = new FuelHandler();
	public static int
		peatBurnTime = 1200;
	
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
}
