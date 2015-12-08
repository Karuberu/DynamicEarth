package karuberu.dynamicearth.items;

import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockFertileSoil;
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

public class ItemBlockFertileSoil extends ItemBlock {

	public ItemBlockFertileSoil(int id) {
		super(id);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		switch (itemStack.getItemDamage()) {
		case BlockFertileSoil.SOIL:
			return super.getUnlocalizedName() + ".fertileSoil";
		case BlockFertileSoil.GRASS:
			return super.getUnlocalizedName() + ".fertileGrass";
		case BlockFertileSoil.MYCELIUM:
			return super.getUnlocalizedName() + ".fertileMycelium";
		default:
			return super.getUnlocalizedName();
		}
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}