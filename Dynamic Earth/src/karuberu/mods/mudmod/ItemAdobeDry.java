package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.BlockJukeBox;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class ItemAdobeDry extends Item {

    protected ItemAdobeDry(int par1) {
		super(par1);
		this.setTabToDisplayOn(CreativeTabs.tabMaterials);
	}

    @Override
    public String getTextureFile() {
    	return MudMod.itemsFile;
    }
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    	MovingObjectPosition movingObjectPos = this.getMovingObjectPositionFromPlayer(world, player, true);

        if (movingObjectPos == null) {
            return itemStack;
        } else {
            if (movingObjectPos.typeOfHit == EnumMovingObjectType.TILE)  {
                int x = movingObjectPos.blockX;
                int y = movingObjectPos.blockY;
                int z = movingObjectPos.blockZ;

                if (!world.canMineBlock(player, x, y, z)) {
                    return itemStack;
                }

                if (!player.canPlayerEdit(x, y, z)) {
                    return itemStack;
                }

                if (world.getBlockMaterial(x, y, z) == Material.water) {
                    if (!player.inventory.addItemStackToInventory(new ItemStack(MudMod.adobeBlob, 1))) {
                        player.dropPlayerItem(new ItemStack(MudMod.adobeBlob.shiftedIndex, 1, 0));
                    }
                	if (!player.capabilities.isCreativeMode) {
                        itemStack.stackSize--;
                    }
                }
            }
            return itemStack;
        }
    }

}
