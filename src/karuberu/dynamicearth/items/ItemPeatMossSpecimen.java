package karuberu.dynamicearth.items;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.core.util.Helper;
import karuberu.core.util.block.BlockSide;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockPeatMoss;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class ItemPeatMossSpecimen extends ItemDynamicEarth implements IPlantable {
	public static String[]
		hintText;
	public static CreativeTabs
		creativeTab = CreativeTabs.tabDecorations;
	
	public ItemPeatMossSpecimen(String unlocalizedName, ItemIcon icon) {
		super(unlocalizedName, icon);
		this.setCreativeTab(creativeTab);
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
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
		if (side != BlockSide.TOP.code) {
			return false;
		} else if (player.canPlayerEdit(x, y, z, side, itemStack) && player.canPlayerEdit(x, y + 1, z, side, itemStack)) {
			int id = world.getBlockId(x, y, z);
			Block soil = Block.blocksList[id];
			if (soil != null && soil.canSustainPlant(world, x, y, z, ForgeDirection.UP, this) && world.isAirBlock(x, y + 1, z)) {
				world.setBlock(x, y + 1, z, DynamicEarth.peatMoss.blockID, BlockPeatMoss.GROWTHSTAGE_1, Helper.NOTIFY_AND_UPDATE_REMOTE);
				--itemStack.stackSize;
				return true;
			} else if (player.capabilities.isCreativeMode && BlockPeatMoss.soilIsValid(id, BlockPeatMoss.GROWTHSTAGE_FULLGROWN)) {
				world.setBlock(x, y + 1, z, DynamicEarth.peatMoss.blockID, BlockPeatMoss.GROWTHSTAGE_FULLGROWN, Helper.NOTIFY_AND_UPDATE_REMOTE);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public EnumPlantType getPlantType(World world, int x, int y, int z) {
		return EnumPlantType.Crop;
	}

	@Override
	public int getPlantID(World world, int x, int y, int z) {
		return DynamicEarth.peatMoss.blockID;
	}

	@Override
	public int getPlantMetadata(World world, int x, int y, int z) {
		return 0;
	}
}
