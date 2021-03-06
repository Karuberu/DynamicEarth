package karuberu.dynamicearth.items;

import karuberu.core.util.Helper;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;
import karuberu.dynamicearth.entity.EntityMudball;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemClump extends ItemDynamicEarth {
	public static final int
		DIRTCLOD = 0,
		MUDBLOB = 1,
		ADOBEDUST = 2,
		ADOBEBLOB = 3,
		PEATCLUMP = 4;
	private ItemStack
		wetClump;
	private boolean
		isThrowable;
	public static CreativeTabs
		creativeTab = CreativeTabs.tabMaterials;

	public ItemClump(String unlocalizedName, ItemIcon icon) {
		super(unlocalizedName, icon);
		this.setCreativeTab(creativeTab);
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (this.wetClump != null
		&& world.getBlockId(x, y, z) == Block.cauldron.blockID) {
			int cauldronMeta = world.getBlockMetadata(x, y, z);
			if (cauldronMeta > 0) {
				world.setBlockMetadataWithNotify(x, y, z, cauldronMeta - 1, Helper.NOTIFY_AND_UPDATE_REMOTE);
				this.hydrateClump(player, itemStack);
				return false;
			}
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (this.isThrowable) {
			if (!player.capabilities.isCreativeMode) {
				--itemStack.stackSize;
			}
			world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!world.isRemote) {
				world.spawnEntityInWorld(new EntityMudball(world, player));
			}
		} else if (this.wetClump != null) {
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
						this.hydrateClump(player, itemStack);
					}
				}
				return itemStack;
			}
		}
		return itemStack;
	}
	
	protected void hydrateClump(EntityPlayer player, ItemStack itemStack) {
		if (!player.inventory.addItemStackToInventory(this.wetClump.copy())) {
			player.dropPlayerItem(this.wetClump.copy());
		}
		if (!player.capabilities.isCreativeMode) {
			itemStack.stackSize--;
		}
	}

	public ItemClump setWetClump(ItemStack itemStack) {
		this.wetClump = itemStack;
		return this;
	}

	public ItemClump setThrowable(boolean bool) {
		this.isThrowable = bool;
		return this;
	}
}
