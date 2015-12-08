package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import karuberu.mods.mudmod.entity.EntityBomb;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBombLit extends ItemMudMod {

	public static int fuseLength = 30;

	public ItemBombLit(int id, Texture icon) {
		super(id, icon);
		this.setMaxStackSize(1);
		this.setMaxDamage(fuseLength);
		this.setNoRepair();
    }
	
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		ItemBombLit.spawnThrownBomb(world, player, itemStack);
		if (!player.capabilities.isCreativeMode) {
			itemStack.stackSize--;
			if (player.inventory.hasItem(MudMod.bomb.itemID)) {
				player.inventory.consumeInventoryItem(MudMod.bomb.itemID);
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
	            player.inventory.clearInventory(itemStack.getItem().itemID, itemStack.getItemDamage());
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
            world.spawnEntityInWorld(new EntityBomb(world, player, fuseLength - itemStack.getItemDamage()));
        }
	}
}
