package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import karuberu.mods.mudmod.entity.EntityBomb;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBomb extends ItemMudMod {
	
	public ItemBomb(int id, Texture icon) {
		super(id, icon);
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setMaxStackSize(16);
	}
	
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    	int damage = itemStack.getItemDamage();
    	if (player.capabilities.isCreativeMode) {
    		ItemBombLit.spawnThrownBomb(world, player, itemStack);
    	} else {
        	world.playSoundAtEntity(player, "random.fuse", 1, 1);
        	ItemStack litBomb = new ItemStack(MudMod.bombLit, 1, 1);
        	player.inventory.setInventorySlotContents(player.inventory.currentItem, litBomb);
        	if (itemStack.stackSize > 1) {
            	player.inventory.addItemStackToInventory(new ItemStack(itemStack.getItem(), itemStack.stackSize - 1));
        	}
        	return litBomb;
    	}
        return itemStack;
    }
}
