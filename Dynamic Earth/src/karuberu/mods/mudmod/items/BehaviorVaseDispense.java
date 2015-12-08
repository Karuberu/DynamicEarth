package karuberu.mods.mudmod.items;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class BehaviorVaseDispense extends BehaviorDefaultDispenseItem {
    @Override
	public ItemStack dispenseStack(IBlockSource dispenser, ItemStack itemStack) {
        EnumFacing facing = BlockDispenser.getFacing(dispenser.getBlockMetadata());
        World world = dispenser.getWorld();
        int x = dispenser.getXInt();
        int y = dispenser.getYInt();
        int z = dispenser.getZInt();
        int id = world.getBlockId(x, y, z);
        ItemVase vase = (ItemVase)itemStack.getItem();

        if (vase.tryPlaceContainedLiquid(itemStack, dispenser.getWorld(), (double)x, (double)y, (double)z, x + facing.getFrontOffsetX(), y + facing.getFrontOffsetY(), z + facing.getFrontOffsetZ())) {
            itemStack.setItemDamage(0);
            return itemStack;
        } else {
            x += facing.getFrontOffsetX();
            y += facing.getFrontOffsetY();
            z += facing.getFrontOffsetZ();
            id = world.getBlockId(x, y, z);
            LiquidStack liquid = new LiquidStack(id, 1000);
            if (LiquidDictionary.getCanonicalLiquid(liquid) != null) {
                itemStack.setItemDamage(ItemVase.getDamage(liquid));
                world.setBlockToAir(x, y, z);
                return itemStack;
           } else {
               return super.dispenseStack(dispenser, itemStack);
           }
        }
    }
}
