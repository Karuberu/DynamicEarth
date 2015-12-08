package karuberu.mods.mudmod;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class ItemBomb extends ItemMudMod {

	protected ItemBomb(int i) {
		super(i);
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setMaxStackSize(16);
		this.setMaxDamage(25);
		this.setNoRepair();
	}
	
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    	int damage = itemStack.getItemDamage();
    	if (damage == 0) {
        	itemStack.damageItem(1, player);
        	world.playSoundAtEntity(player, "random.fuse", 1, 1);
        	return itemStack;
    	} else if (damage > 0) {
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            if (!world.isRemote) {
                world.spawnEntityInWorld(new EntityBomb(world, player, this.getMaxDamage() - damage));
            	itemStack.setItemDamage(0);
            }
        	if (!player.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
        }
        return itemStack;
    }
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
    	int damage = itemStack.getItemDamage(),
    		maxDamage = itemStack.getMaxDamage();
    	if (damage >= maxDamage) {
            if (!world.isRemote) {
                world.spawnEntityInWorld(new EntityBomb(world, (EntityPlayer)entity, maxDamage - damage));
            }
        	itemStack.setItemDamage(0);
        	itemStack.stackSize--;
    	} else if (damage > 0) {
			itemStack.damageItem(1, (EntityPlayer)entity);
		}
	}
}
