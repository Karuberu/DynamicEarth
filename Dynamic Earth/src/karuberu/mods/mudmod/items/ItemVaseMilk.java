package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
        this.setTextureFile(MudMod.itemsFile);
    }
    
    @Override
    public ItemStack onFoodEaten(ItemStack itemStack, World world, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            --itemStack.stackSize;
        }

        if (!world.isRemote) {
            player.curePotionEffects(itemStack);
        }

        return itemStack.stackSize <= 0 ? new ItemStack(this.getContainerItem()) : itemStack;
    }
}
