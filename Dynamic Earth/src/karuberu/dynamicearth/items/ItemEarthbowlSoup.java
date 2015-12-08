package karuberu.dynamicearth.items;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.TextureManager;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEarthbowlSoup extends ItemSoup {

    public ItemEarthbowlSoup(int id) {
		super(id, 8);
		this.setContainerItem(DynamicEarth.earthbowl);
	}
    
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(ItemIcon.EARTHBOWLSOUP.getIconPath());
	}
	
    @Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
        return new ItemStack(DynamicEarth.earthbowl);
    }
}
