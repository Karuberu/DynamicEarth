package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorVaseEmptyDispense extends BehaviorDefaultDispenseItem
{
    /** Reference to the BehaviorDefaultDispenseItem object. */
    private final BehaviorDefaultDispenseItem defaultItemDispenseBehavior;

    public BehaviorVaseEmptyDispense() {
        this.defaultItemDispenseBehavior = new BehaviorDefaultDispenseItem();
    }

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseStack(IBlockSource dispenser, ItemStack itemStack) {
        EnumFacing facing = EnumFacing.getFront(dispenser.func_82620_h());
        World world = dispenser.getWorld();
        int x = dispenser.getXInt() + facing.getFrontOffsetX();
        int y = dispenser.getYInt();
        int z = dispenser.getZInt() + facing.getFrontOffsetZ();
        Material material = world.getBlockMaterial(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        Item item;

        if (Material.water.equals(material) && metadata == 0) {
            item = MudMod.vaseWater;
        } else {
            return super.dispenseStack(dispenser, itemStack);
        }
        world.func_94571_i(x, y, z);
        if (--itemStack.stackSize == 0) {
            itemStack.itemID = item.itemID;
            itemStack.stackSize = 1;
        }  else if (((TileEntityDispenser)dispenser.func_82619_j()).addItem(new ItemStack(item)) < 0) {
            this.defaultItemDispenseBehavior.dispense(dispenser, new ItemStack(item));
        }
        return itemStack;
    }
}
