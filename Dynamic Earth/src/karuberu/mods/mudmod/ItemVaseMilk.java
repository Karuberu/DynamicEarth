package karuberu.mods.mudmod;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBucketMilk;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemVaseMilk extends ItemBucketMilk
{
    public ItemVaseMilk(int par1) {
        super(par1);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setContainerItem(MudMod.vase);
    }
    
    @Override
    public String getTextureFile() {
    	return MudMod.itemsFile;
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

