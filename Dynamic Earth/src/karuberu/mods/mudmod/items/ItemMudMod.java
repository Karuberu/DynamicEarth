package karuberu.mods.mudmod.items;

import org.bouncycastle.util.Strings;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemMudMod extends Item {

	private Texture iconTexture;
		
	public ItemMudMod(int id, Texture icon) {
		super(id);
		this.iconTexture = icon;
	}
	
	@Override
	public void func_94581_a(IconRegister iconRegister) {
		this.iconIndex = TextureManager.instance().getItemTexture(iconTexture);
	}
}
