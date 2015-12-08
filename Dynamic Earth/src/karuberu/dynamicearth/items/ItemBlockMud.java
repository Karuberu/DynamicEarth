package karuberu.dynamicearth.items;

import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockFertileSoil;
import karuberu.dynamicearth.blocks.BlockMud;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.blocks.BlockSandySoil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemBlockMud extends ItemBlock {

	public ItemBlockMud(int id) {
		super(id);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		switch (itemStack.getItemDamage()) {
		case BlockMud.NORMAL:
			return super.getUnlocalizedName() + ".mud";
		case BlockMud.WET:
			return super.getUnlocalizedName() + ".wet";
		case BlockMud.FERTILE:
			return super.getUnlocalizedName() + ".fertile";
		case BlockMud.FERTILE_WET:
			return super.getUnlocalizedName() + ".fertileWet";
		default:
			return super.getUnlocalizedName();
		}
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}