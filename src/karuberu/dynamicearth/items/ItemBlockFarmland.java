package karuberu.dynamicearth.items;

import karuberu.dynamicearth.blocks.BlockDynamicFarmland;
import net.minecraft.item.ItemStack;

public class ItemBlockFarmland extends ItemBlockDynamicEarth {

	public ItemBlockFarmland(String unlocalizedName) {
		super(unlocalizedName);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		switch (itemStack.getItemDamage()) {
		case BlockDynamicFarmland.MUD:
			return super.getUnlocalizedName() + ".mud";
		case BlockDynamicFarmland.FERTILE_DRY:
		case BlockDynamicFarmland.FERTILE_WET:
			return super.getUnlocalizedName() + ".fertile";
		case BlockDynamicFarmland.SANDY_DRY:
			return super.getUnlocalizedName() + ".sandy";
		case BlockDynamicFarmland.PEAT_1:
		case BlockDynamicFarmland.PEAT_2:
		case BlockDynamicFarmland.PEAT_3:
		case BlockDynamicFarmland.PEAT_4:
		case BlockDynamicFarmland.PEAT_5:
		case BlockDynamicFarmland.PEAT_6:
		case BlockDynamicFarmland.PEAT_7:
		case BlockDynamicFarmland.PEAT_DRY:
		case BlockDynamicFarmland.PEAT_WET:
			return super.getUnlocalizedName() + ".peat";
		default:
			return super.getUnlocalizedName();
		}
	}
}
