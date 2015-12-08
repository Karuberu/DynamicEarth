package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class BehaviorVaseFullDispense extends BehaviorDefaultDispenseItem {
    private final BehaviorDefaultDispenseItem defaultItemDispenseBehavior;

    public BehaviorVaseFullDispense() {
        this.defaultItemDispenseBehavior = new BehaviorDefaultDispenseItem();
    }

    public ItemStack dispenseStack(IBlockSource dispenser, ItemStack itemStack) {
        ItemVase vase = (ItemVase)itemStack.getItem();
        int x = dispenser.getXInt();
        int y = dispenser.getYInt();
        int z = dispenser.getZInt();
        EnumFacing facing = EnumFacing.getFront(dispenser.func_82620_h());

        if (vase.tryPlaceContainedLiquid(dispenser.getWorld(), (double)x, (double)y, (double)z, x + facing.getFrontOffsetX(), y, z + facing.getFrontOffsetZ())) {
            itemStack.itemID = MudMod.vase.itemID;
            itemStack.stackSize = 1;
            return itemStack;
        } else {
            return this.defaultItemDispenseBehavior.dispense(dispenser, itemStack);
        }
    }
}

