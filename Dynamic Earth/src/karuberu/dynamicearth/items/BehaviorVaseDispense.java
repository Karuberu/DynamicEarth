package karuberu.dynamicearth.items;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.fluids.FluidHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BehaviorVaseDispense extends BehaviorDefaultDispenseItem {
    @Override
	public ItemStack dispenseStack(IBlockSource dispenser, ItemStack itemStack) {
        EnumFacing facing = BlockDispenser.getFacing(dispenser.getBlockMetadata());
        World world = dispenser.getWorld();
        int x = dispenser.getXInt();
        int y = dispenser.getYInt();
        int z = dispenser.getZInt();
        int id = world.getBlockId(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        ItemVase vase = (ItemVase)itemStack.getItem();

        if (vase.tryPlaceContainedLiquid(itemStack, dispenser.getWorld(), (double)x, (double)y, (double)z, x + facing.getFrontOffsetX(), y + facing.getFrontOffsetY(), z + facing.getFrontOffsetZ())) {
            itemStack.setItemDamage(0);
            return itemStack;
        } else {
            x += facing.getFrontOffsetX();
            y += facing.getFrontOffsetY();
            z += facing.getFrontOffsetZ();
            id = world.getBlockId(x, y, z);
            Fluid liquid = FluidRegistry.getFluid(id);
            if (liquid != null) {
                itemStack.setItemDamage(ItemVase.getDamage(new FluidStack(liquid, 1000)));
                world.setBlockToAir(x, y, z);
                return itemStack;
           } else {
               return super.dispenseStack(dispenser, itemStack);
           }
        }
    }
}
