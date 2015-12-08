package karuberu.dynamicearth.items;

import karuberu.dynamicearth.client.TextureManager;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemMudMod extends Item {
	private ItemIcon iconTexture;

	public ItemMudMod(int id, ItemIcon icon) {
		super(id);
		this.iconTexture = icon;
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(this.iconTexture.getIconPath());
	}
}
