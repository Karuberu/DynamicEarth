package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
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
        EnumFacing facing = BlockDispenser.getFacing(dispenser.getBlockMetadata());

        if (vase.tryPlaceContainedLiquid(dispenser.getWorld(), (double)x, (double)y, (double)z, x + facing.getFrontOffsetX(), y + facing.getFrontOffsetY(), z + facing.getFrontOffsetZ())) {
            itemStack.itemID = MudMod.vase.itemID;
            itemStack.stackSize = 1;
            return itemStack;
        } else {
            return this.defaultItemDispenseBehavior.dispense(dispenser, itemStack);
        }
    }
}

