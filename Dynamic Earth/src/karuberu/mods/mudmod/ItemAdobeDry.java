package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class ItemAdobeDry extends ItemMudMod {

    protected ItemAdobeDry(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
    
    @Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.getBlockId(x, y, z) == Block.cauldron.blockID) {
        	int cauldronMeta = world.getBlockMetadata(x, y, z);
        	if (cauldronMeta > 0) {
        		world.setBlockMetadataWithNotify(x, y, z, cauldronMeta - 1);
	        	if (!player.inventory.addItemStackToInventory(new ItemStack(MudMod.adobeBlob, 1))) {
	                player.dropPlayerItem(new ItemStack(MudMod.adobeBlob.shiftedIndex, 1, 0));
	            }
	        	if (!player.capabilities.isCreativeMode) {
	                itemStack.stackSize--;
	            }
	        	return false;
        	}
        }
        return false;
    }
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    	MovingObjectPosition movingObjectPos = this.getMovingObjectPositionFromPlayer(world, player, true);

        if (movingObjectPos == null) {
            return itemStack;
        } else {
            if (movingObjectPos.typeOfHit == EnumMovingObjectType.TILE)  {
                int x = movingObjectPos.blockX;
                int y = movingObjectPos.blockY;
                int z = movingObjectPos.blockZ;

                if (!player.func_82247_a(x, y, z, movingObjectPos.sideHit, itemStack)) {
                    return itemStack;
                }
                
                if (!world.canMineBlock(player, x, y, z)) {
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
