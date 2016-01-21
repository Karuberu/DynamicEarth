package karuberu.dynamicearth.items;

import karuberu.core.util.FluidHelper.FluidReference;
import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
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
		ItemVase vase = (ItemVase)itemStack.getItem();

		if (vase.tryPlaceContainedLiquid(itemStack, dispenser.getWorld(), x, y, z, x + facing.getFrontOffsetX(), y + facing.getFrontOffsetY(), z + facing.getFrontOffsetZ())) {
			itemStack.setItemDamage(0);
			return itemStack;
		} else if (DynamicEarth.vase.getFluidStack(itemStack.getItemDamage()) == null){
			x += facing.getFrontOffsetX();
			y += facing.getFrontOffsetY();
			z += facing.getFrontOffsetZ();
			id = world.getBlockId(x, y, z);
			Fluid liquid;
			if (id == Block.waterMoving.blockID) { // fix for Forge registering the wrong id...
				liquid = FluidReference.WATER.getFluid();
			} else {
				liquid = FluidRegistry.lookupFluidForBlock(Block.blocksList[id]);				
			}
			if (liquid != null) {
				itemStack.setItemDamage(DynamicEarth.vase.getDamage(new FluidStack(liquid, 1000)));
				world.setBlockToAir(x, y, z);
				return itemStack;
			}
		}
		return super.dispenseStack(dispenser, itemStack);
	}
}
