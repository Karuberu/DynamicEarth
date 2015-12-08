package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemMudMod extends Item {
	private Texture
	iconTexture;

	public ItemMudMod(int id, Texture icon) {
		super(id);
		this.iconTexture = icon;
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = TextureManager.instance().getItemTexture(this.iconTexture);
	}
}
