package karuberu.dynamicearth.items;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.core.util.Helper;
import karuberu.core.util.client.LanguageHelper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDynamicEarth extends Item {
	private ItemIcon
		iconTexture;
	public static CreativeTabs
		creativeTab = CreativeTabs.tabMaterials;

	public ItemDynamicEarth(String unlocalizedName, ItemIcon icon) {
		super(DynamicEarth.config.getItemID(unlocalizedName));
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
		String[] hintText = LanguageHelper.getHintText(itemStack);
		if ((Helper.usingAdvancedTooltips() || player.capabilities.isCreativeMode)
		&& hintText != null) {
			for (String line : hintText) {
				information.add(line);
			}
		}
	}
}
