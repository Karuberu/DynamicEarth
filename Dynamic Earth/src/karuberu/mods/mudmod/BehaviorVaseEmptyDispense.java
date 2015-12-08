package karuberu.mods.mudmod;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BehaviorDefaultDispenseItem;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IBlockSource;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntityDispenser;
import net.minecraft.src.World;

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
        EnumFacing facing = EnumFacing.func_82600_a(dispenser.func_82620_h());
        World world = dispenser.getWorld();
        int x = dispenser.getXInt() + facing.func_82601_c();
        int y = dispenser.getYInt();
        int z = dispenser.getZInt() + facing.func_82599_e();
        Material material = world.getBlockMaterial(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        Item item;

        if (Material.water.equals(material) && metadata == 0) {
            item = MudMod.vaseWater;
        } else {
            return super.dispenseStack(dispenser, itemStack);
        }
        world.setBlockWithNotify(x, y, z, 0);
        if (--itemStack.stackSize == 0) {
            itemStack.itemID = item.shiftedIndex;
            itemStack.stackSize = 1;
        }  else if (((TileEntityDispenser)dispenser.func_82619_j()).func_70360_a(new ItemStack(item)) < 0) {
            this.defaultItemDispenseBehavior.dispense(dispenser, new ItemStack(item));
        }
        return itemStack;
    }
}
