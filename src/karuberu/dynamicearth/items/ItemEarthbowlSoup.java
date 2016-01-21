package karuberu.dynamicearth.items;

import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemEarthbowlSoup extends ItemSoup {
	
	public static CreativeTabs
		creativeTab = CreativeTabs.tabFood;

	public ItemEarthbowlSoup(String unlocalizedName) {
		super(DynamicEarth.config.getItemID(unlocalizedName), 8);
		this.setContainerItem(DynamicEarth.earthbowl);
		this.setCreativeTab(creativeTab);
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
