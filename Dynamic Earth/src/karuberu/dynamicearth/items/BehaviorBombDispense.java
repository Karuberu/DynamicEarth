package karuberu.dynamicearth.items;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.entity.EntityBomb;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorBombDispense extends BehaviorProjectileDispense {
	
	@Override
    public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
        World world = blockSource.getWorld();
        IPosition iposition = BlockDispenser.getIPositionFromBlockSource(blockSource);
        EnumFacing enumfacing = BlockDispenser.getFacing(blockSource.getBlockMetadata());
        EntityBomb bomb;
        if (itemStack != null && itemStack.getItem() instanceof ItemBomb) {
        	ItemStack itemBombLit = new ItemStack(DynamicEarth.bombLit);
        	itemBombLit.setTagCompound(itemStack.getTagCompound());
        	itemBombLit.getItem().onCreated(itemBombLit, world, null);
        	bomb = new EntityBomb(world, iposition.getX(), iposition.getY(), iposition.getZ(), itemBombLit);
        } else {
        	bomb = new EntityBomb(world, iposition.getX(), iposition.getY(), iposition.getZ(), 1, 1);
        }
    	bomb.setThrowableHeading((double)enumfacing.getFrontOffsetX(), (double)((float)enumfacing.getFrontOffsetY() + 0.1F), (double)enumfacing.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
        world.spawnEntityInWorld((Entity)bomb);
        itemStack.splitStack(1);
        return itemStack;
    }

	@Override
	protected IProjectile getProjectileEntity(World world, IPosition iposition) {
		return null;
	}
	
	
}
