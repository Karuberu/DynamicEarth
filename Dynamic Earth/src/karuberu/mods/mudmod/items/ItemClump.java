package karuberu.mods.mudmod.items;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import karuberu.mods.mudmod.entity.EntityMudball;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemClump extends ItemMudMod {
	public static final int
		DIRTCLOD = 0,
		MUDBLOB = 1,
		ADOBEDUST = 2,
		ADOBEBLOB = 3,
		PEATCLUMP = 4;
	private int
		wetClump;
	private boolean
		isThrowable;
		
	public ItemClump(int id, Texture icon) {
		super(id, icon);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
    @Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        itemStack.getItemDamage();
    	if (this.wetClump > 0
    	&& world.getBlockId(x, y, z) == Block.cauldron.blockID) {
        	int cauldronMeta = world.getBlockMetadata(x, y, z);
        	if (cauldronMeta > 0) {
        		world.setBlockMetadataWithNotify(x, y, z, cauldronMeta - 1, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
	        	if (!player.inventory.addItemStackToInventory(new ItemStack(this.wetClump, 1, 0))) {
	                player.dropPlayerItem(new ItemStack(this.wetClump, 1, 0));
	            }
	        	if (!player.capabilities.isCreativeMode) {
	                itemStack.stackSize--;
	            }
	        	return false;
        	}
        }
        return false;
    }
    
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.getItemDamage();
		if (this.isThrowable) {
			if (!player.capabilities.isCreativeMode) {
				--itemStack.stackSize;
			}
			world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!world.isRemote) {
				world.spawnEntityInWorld(new EntityMudball(world, player));
			}
		} else if (this.wetClump > 0) {
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
	                	if (!player.inventory.addItemStackToInventory(new ItemStack(this.wetClump, 1, 0))) {
	                        player.dropPlayerItem(new ItemStack(this.wetClump, 1, 0));
	                    }
	                	if (!player.capabilities.isCreativeMode) {
	                        itemStack.stackSize--;
	                    }
	                }
	            }
	            return itemStack;
	        }
		}
		return itemStack;
	}
	
	public ItemClump setWetClump(int id) {
		this.wetClump = id;
		return this;
	}
	
	public ItemClump setThrowable(boolean bool) {
		this.isThrowable = bool;
		return this;
	}
}
