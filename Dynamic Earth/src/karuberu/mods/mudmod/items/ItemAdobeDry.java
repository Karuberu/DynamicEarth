package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemAdobeDry extends ItemMudMod {

    public ItemAdobeDry(int par1) {
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
	                player.dropPlayerItem(new ItemStack(MudMod.adobeBlob.itemID, 1, 0));
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

                if (!player.canPlayerEdit(x, y, z, movingObjectPos.sideHit, itemStack)) {
                    return itemStack;
                }
                
                if (!world.canMineBlock(player, x, y, z)) {
                    return itemStack;
                }

                if (world.getBlockMaterial(x, y, z) == Material.water) {
                	if (!player.inventory.addItemStackToInventory(new ItemStack(MudMod.adobeBlob, 1))) {
                        player.dropPlayerItem(new ItemStack(MudMod.adobeBlob.itemID, 1, 0));
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
