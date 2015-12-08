package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemVaseMilk extends ItemBucketMilk
{
    public ItemVaseMilk(int par1) {
        super(par1);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setContainerItem(MudMod.vase);
    }
    
	@Override
	public void updateIcons(IconRegister iconRegister) {
		this.iconIndex = TextureManager.instance().getItemTexture(Texture.VASEMILK);
	}
	
    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            --itemStack.stackSize;
        }

        if (!world.isRemote) {
            player.curePotionEffects(new ItemStack(Item.bucketMilk));
        }

        return itemStack.stackSize <= 0 ? new ItemStack(this.getContainerItem()) : itemStack;
    }
}

