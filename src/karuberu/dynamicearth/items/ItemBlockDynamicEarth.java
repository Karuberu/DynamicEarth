package karuberu.dynamicearth.items;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.core.util.Helper;
import karuberu.core.util.client.LanguageHelper;
import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockDynamicEarth extends ItemBlock {
	
	public ItemBlockDynamicEarth(String unlocalizedName) {
		super(DynamicEarth.config.getItemID(unlocalizedName));
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
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}
