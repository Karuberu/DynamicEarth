package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.entity.EntityBomb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBombLit extends Item {

	public static final int maxFuseLength = 30;

	public ItemBombLit(int i) {
		super(i);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxFuseLength);
		this.setNoRepair();
        this.setTextureFile(MudMod.itemsFile);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		ItemBombLit.spawnThrownBomb(world, player, itemStack);
		if (!player.capabilities.isCreativeMode) {
			itemStack.stackSize--;
			if (player.inventory.hasItem(MudMod.bomb.shiftedIndex)) {
				player.inventory.consumeInventoryItem(MudMod.bomb.shiftedIndex);
				return new ItemStack(MudMod.bomb);
			}
		}
        return itemStack;
    }
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
    	if (entity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)entity;
	    	int damage = itemStack.getItemDamage();
	    	if (itemStack.stackSize == 0) {
	            player.inventory.clearInventory(itemStack.getItem().shiftedIndex, itemStack.getItemDamage());
	    	} else {
		    	if (damage >= this.getMaxDamage()) {
		            this.spawnThrownBomb(world, player, itemStack);
		            itemStack.stackSize--;
		    	} else if (damage > 0) {
					itemStack.damageItem(1, player);
				}
	    	}
	    }
	}
    
	public static void spawnThrownBomb(World world, EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote) {
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            world.spawnEntityInWorld(new EntityBomb(world, player, maxFuseLength - itemStack.getItemDamage()));
        }
	}
}
