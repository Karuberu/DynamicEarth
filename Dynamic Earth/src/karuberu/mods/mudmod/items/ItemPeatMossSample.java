package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.Reference;
import karuberu.mods.mudmod.blocks.BlockPeatMoss;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class ItemPeatMossSample extends ItemMudMod implements IPlantable {

	public ItemPeatMossSample(int id) {
		super(id);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (side != Reference.SIDE_TOP) {
            return false;
        } else if (player.canPlayerEdit(x, y, z, side, itemStack) && player.canPlayerEdit(x, y + 1, z, side, itemStack)) {
            int id = world.getBlockId(x, y, z);
            Block soil = Block.blocksList[id];
            if (soil != null && soil.canSustainPlant(world, x, y, z, ForgeDirection.UP, this) && world.isAirBlock(x, y + 1, z)) {
                world.setBlockAndMetadataWithNotify(x, y + 1, z, MudMod.peatMoss.blockID, BlockPeatMoss.GROWTHSTAGE_1);
                --itemStack.stackSize;
                return true;
            } else if (player.capabilities.isCreativeMode && BlockPeatMoss.soilIsValid(id, BlockPeatMoss.GROWTHSTAGE_FULLGROWN)) {
            	world.setBlockAndMetadataWithNotify(x, y + 1, z, MudMod.peatMoss.blockID, BlockPeatMoss.GROWTHSTAGE_FULLGROWN);
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
        return MudMod.peatMoss.blockID;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z) {
        return 0;
    }
}
