package karuberu.dynamicearth.items;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBurningSoil extends ItemBlock {
	public static String[]
		dirtHintText,
		grassHintText;
	
	public ItemBlockBurningSoil(int id) {
		super(id);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		if (damage == DynamicEarth.burningSoil.DIRT) {
			return super.getUnlocalizedName() + ".burningSoil";
		} else if (damage == DynamicEarth.burningSoil.GRASS) {
			return super.getUnlocalizedName() + ".burningGrass";
		} else {
			return super.getUnlocalizedName();
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack itemStack, EntityPlayer player, List information, boolean bool) {
		if (player.capabilities.isCreativeMode) {
			int damage = itemStack.getItemDamage();
			if (damage == DynamicEarth.burningSoil.DIRT) {
				for (String line : dirtHintText) {
					information.add(line);
				}
			} else if (damage == DynamicEarth.burningSoil.GRASS) {
				for (String line : grassHintText) {
					information.add(line);
				}
			}
		}
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}