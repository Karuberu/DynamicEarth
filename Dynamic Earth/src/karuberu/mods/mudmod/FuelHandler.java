package karuberu.mods.mudmod;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) {
		if (fuel != null && fuel.getItem() != null) {
			int id = fuel.getItem().itemID;
			if (id == MudMod.peatBrick.itemID) {
				return 1600;
			}
		}
		return 0;
	}
}
