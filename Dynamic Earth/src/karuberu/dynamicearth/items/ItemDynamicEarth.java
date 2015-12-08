package karuberu.dynamicearth.items;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDynamicEarth extends Item {
	private ItemIcon
		iconTexture;
	private String[]
		hintText;
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

	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List information, boolean bool) {
		if (player.capabilities.isCreativeMode
		&& this.hintText != null) {
			for (String line : this.hintText) {
				information.add(line);
			}
		}
	}
	
	public ItemDynamicEarth setHintText(String... text) {
		this.hintText = text;
		return this;
	}
}
