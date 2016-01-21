package karuberu.dynamicearth.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import karuberu.core.util.Coordinates;
import karuberu.core.util.Helper;
import karuberu.core.util.client.fx.FXHelper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.api.IItemStackHandler;
import karuberu.dynamicearth.client.TextureManager.ItemIcon;

public class ItemMudBlob extends ItemClump implements IItemStackHandler {
	public static final int
		NORMAL = 0,
		FERTILE = NORMAL + 1,
		GLOWING = FERTILE + 1;
	public static final String
		TAG_SPECIAL = "SpecialExpiration";
	
	public ItemMudBlob(String unlocalizedName, ItemIcon icon) {
		super(unlocalizedName, icon);
		this.setHasSubtypes(true);
		this.setThrowable(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int damage = itemStack.getItemDamage();
		if (damage == ItemMudBlob.NORMAL) {
			return super.getUnlocalizedName() + ".normal";
		} else if (damage == ItemMudBlob.FERTILE) {
			return super.getUnlocalizedName() + ".fertile";
		} else if (damage == ItemMudBlob.GLOWING) {
			return super.getUnlocalizedName() + ".glowing";
		} else {
			return super.getUnlocalizedName();
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (DynamicEarth.includeMudLayers
		&& player.isSneaking()) {
			MovingObjectPosition movingObjectPos = this.getMovingObjectPositionFromPlayer(world, player, true);
			if (!world.isRemote
			&& movingObjectPos != null
			&& movingObjectPos.typeOfHit == EnumMovingObjectType.TILE)  {
				int x = movingObjectPos.blockX;
				int y = movingObjectPos.blockY;
				int z = movingObjectPos.blockZ;
				int side = movingObjectPos.sideHit;
				if (player.canPlayerEdit(x, y, z, side, itemStack)
				&& world.canMineBlock(player, x, y, z)) {
					boolean blockPlaced = this.tryToPlaceMudLayer(world, x, y, z, 1);
					if (!blockPlaced) {
						Coordinates coords = Coordinates.getCoordsForBlockPlacement(movingObjectPos);
						x = coords.x;
						y = coords.y;
						z = coords.z;
						blockPlaced = this.tryToPlaceMudLayer(world, x, y, z, 1);
					}
					if (blockPlaced) {
						FXHelper.playBlockPlacementSound(DynamicEarth.mudLayer, world, x, y, z);
						itemStack.stackSize--;
					}
				}
				return itemStack;
			}
		}
		return super.onItemRightClick(itemStack, world, player);
	}
	
	private boolean tryToPlaceMudLayer(World world, int x, int y, int z, int metadataToPlace) {
		if (!world.checkNoEntityCollision(DynamicEarth.mudLayer.getCollisionBoundingBoxFromPool(world, x, y, z))) {
			return false;
		}
		if (world.getBlockId(x, y, z) == DynamicEarth.mudLayer.blockID) {
			int metadata = world.getBlockMetadata(x, y, z);
			if (metadata < 7) {
				metadata = metadata + (1 + metadataToPlace);
				if (metadata > 7) {
					metadata = 7;
				}
			}
			world.setBlock(x, y, z, DynamicEarth.mudLayer.blockID, metadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
			return true;
		} else if (world.getBlockMaterial(x, y, z).isReplaceable()
		&& !world.getBlockMaterial(x, y, z).isLiquid()
		&& DynamicEarth.mudLayer.canPlaceBlockAt(world, x, y, z)) {
			world.setBlock(x, y, z, DynamicEarth.mudLayer.blockID, metadataToPlace, Helper.NOTIFY_AND_UPDATE_REMOTE);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onPickup(World world, int x, int y, int z, ItemStack itemStack) {
		if (!DynamicEarth.enableMudslideBlockPreservation) {
			return true;
		}
		if (itemStack.hasTagCompound()) {
			NBTTagCompound tagCompound = itemStack.getTagCompound();
			if (tagCompound.hasKey(TAG_SPECIAL)) {
				tagCompound.removeTag(TAG_SPECIAL);
				if (tagCompound.hasNoTags()) {
					itemStack.setTagCompound(null);
				} else {
					itemStack.setTagCompound(tagCompound);
				}
			}
		}
		return true;
	}
	
	@Override
	public int onExpire(World world, int x, int y, int z, ItemStack itemStack) {
		if (!DynamicEarth.enableMudslideBlockPreservation) {
			return 0;
		}
		int extraLife = 0;
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound != null
		&& tagCompound.getBoolean(TAG_SPECIAL)
		&& world.isAirBlock(x, y, z)
		|| world.getBlockId(x, y, z) == DynamicEarth.mudLayer.blockID) {
			int id;
			int metadata = itemStack.getItemDamage();
			if (itemStack.stackSize >= 8) {
				if (metadata == ItemMudBlob.FERTILE) {
					id = DynamicEarth.fertileMud.blockID;
					metadata = DynamicEarth.fertileMud.NORMAL;
				} else if (metadata == ItemMudBlob.GLOWING) {
					id = DynamicEarth.glowingMud.blockID;
					metadata = DynamicEarth.glowingMud.NORMAL;
				} else {
					id = DynamicEarth.mud.blockID;
					metadata = DynamicEarth.mud.NORMAL;
				}
				if (itemStack.stackSize > 8) {
					itemStack.stackSize -= 8;
					extraLife = 2500;
				}
			} else {
				id = DynamicEarth.mudLayer.blockID;
				metadata = itemStack.stackSize - 1;
			}
			if (world.getBlockId(x, y, z) == DynamicEarth.mudLayer.blockID
			&& id == DynamicEarth.mudLayer.blockID) {
				metadata += world.getBlockMetadata(x, y, z);
				if (metadata > 7) {
					metadata = 7;
				}
			}
			world.setBlock(x, y, z, id, metadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
		}
		return extraLife;
	}
}
