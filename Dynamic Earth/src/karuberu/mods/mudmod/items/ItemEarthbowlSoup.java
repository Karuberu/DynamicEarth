package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEarthbowlSoup extends ItemSoup {

    public ItemEarthbowlSoup(int par1, int par2) {
		super(par1, par2);
        this.setTextureFile(MudMod.itemsFile);
	}
    
    @Override
	public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
        return new ItemStack(MudMod.earthbowl);
    }
}