package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEarthbowlSoup extends ItemSoup {

    public ItemEarthbowlSoup(int id) {
		super(id, 8);
		this.setContainerItem(MudMod.earthbowl);
	}
    
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = TextureManager.instance().getItemTexture(Texture.EARTHBOWLSOUP);
	}
	
    @Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
        return new ItemStack(MudMod.earthbowl);
    }
}
