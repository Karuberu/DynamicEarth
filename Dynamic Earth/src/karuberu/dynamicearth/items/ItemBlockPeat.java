package karuberu.dynamicearth.items;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.dynamicearth.blocks.BlockPeat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPeat extends ItemBlock {
	public static String[]
		hintText;
	
	public ItemBlockPeat(int id) {
		super(id);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		switch (itemStack.getItemDamage()) {
		case BlockPeat.WET:
			return super.getUnlocalizedName() + ".wet";
		case BlockPeat.DRY:
			return super.getUnlocalizedName() + ".dry";
		default:
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