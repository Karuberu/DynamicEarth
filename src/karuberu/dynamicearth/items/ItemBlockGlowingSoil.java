package karuberu.dynamicearth.items;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockGlowingSoil extends ItemBlock {
	public static String[]
		hintText;

	public ItemBlockGlowingSoil(int id) {
		super(id);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		if (damage == DynamicEarth.glowingSoil.DIRT) {
			return super.getUnlocalizedName() + ".dirt";
		} else if (damage == DynamicEarth.glowingSoil.GRASS) {
			return super.getUnlocalizedName() + ".grass";
		} else if (damage == DynamicEarth.glowingSoil.MYCELIUM) {
			return super.getUnlocalizedName() + ".mycelium";
		} else {
			return super.getUnlocalizedName();
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List information, boolean bool) {
		if (player.capabilities.isCreativeMode) {
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