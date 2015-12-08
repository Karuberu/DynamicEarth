package karuberu.mods.mudmod;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemSoup;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemEarthbowlSoup extends ItemSoup {

    public ItemEarthbowlSoup(int par1, int par2) {
		super(par1, par2);
	}

    @Override
    public String getTextureFile() {
    	return MudMod.itemsFile;
    }
    
    @Override
	public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
        return new ItemStack(MudMod.earthbowl);
    }
}
