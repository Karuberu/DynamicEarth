package karuberu.dynamicearth.items;

import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemDynamicEarth extends Item {
	private ItemIcon
		iconTexture;
	public static CreativeTabs
		creativeTab = CreativeTabs.tabMaterials;

	public ItemDynamicEarth(int id, ItemIcon icon) {
		super(id);
		this.iconTexture = icon;
		this.setCreativeTab(creativeTab);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(this.iconTexture.getIconPath());
	}
}
