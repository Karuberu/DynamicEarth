package karuberu.dynamicearth.items;

import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAdobeSlab extends ItemBlock {
	private final boolean isDoubleSlab;
	private final BlockHalfSlab singleSlab;
	private final BlockHalfSlab doubleSlab;

	public ItemAdobeSlab(int id) {
		super(id);
		this.singleSlab = DynamicEarth.adobeSingleSlab;
		this.doubleSlab = DynamicEarth.adobeDoubleSlab;
		if (this.itemID == DynamicEarth.adobeDoubleSlab.blockID - 256) {
			this.isDoubleSlab = true;
		} else  {
			this.isDoubleSlab = false;
		}
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int damageValue) {
		return Block.blocksList[this.itemID].getIcon(2, damageValue);
	}

	@Override
	public int getMetadata(int damageValue) {
		return damageValue;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.singleSlab.getFullSlabName(itemStack.getItemDamage());
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (this.isDoubleSlab) {
			return super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
		} else if (itemStack.stackSize == 0) {
			return false;
		} else if (!player.canPlayerEdit(x, y, z, side, itemStack)) {
			return false;
		} else {
			int blockId = world.getBlockId(x, y, z);
			int metadata = world.getBlockMetadata(x, y, z);
			int slabType = metadata & 7;
			boolean var14 = (metadata & 8) != 0;

			if ((side == 1 && !var14 || side == 0 && var14)
					&& blockId == this.singleSlab.blockID
					&& slabType == itemStack.getItemDamage()) {
				if (world.checkBlockCollision(this.doubleSlab.getCollisionBoundingBoxFromPool(world, x, y, z)) && world.setBlock(x, y, z, this.doubleSlab.blockID, slabType, MCHelper.NOTIFY_AND_UPDATE_REMOTE)) {
					world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, this.doubleSlab.stepSound.getStepSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getPitch() * 0.8F);
					--itemStack.stackSize;
				}
				return true;
			} else {
				return this.makeDoubleSlab(itemStack, player, world, x, y, z, side) ? true : super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canPlaceItemBlockOnSide(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack itemStack) {
		int originalX = x;
		int originalY = y;
		int originalZ = z;
		int blockId = world.getBlockId(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		int slabType = metadata & 7;
		boolean var14 = (metadata & 8) != 0;

		if ((side == 1 && !var14 || side == 0 && var14)
				&& blockId == this.singleSlab.blockID
				&& slabType == itemStack.getItemDamage()) {
			return true;
		} else {
			if (side == 0) {
				--y;
			}
			if (side == 1) {
				++y;
			}
			if (side == 2) {
				--z;
			}
			if (side == 3) {
				++z;
			}
			if (side == 4) {
				--x;
			}
			if (side == 5) {
				++x;
			}
			blockId = world.getBlockId(x, y, z);
			metadata = world.getBlockMetadata(x, y, z);
			slabType = metadata & 7;
			var14 = (metadata & 8) != 0;
			return blockId == this.singleSlab.blockID && slabType == itemStack.getItemDamage() ? true : super.canPlaceItemBlockOnSide(world, originalX, originalY, originalZ, side, player, itemStack);
		}
	}

	private boolean makeDoubleSlab(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side) {
		if (side == 0) {
			--y;
		}
		if (side == 1) {
			++y;
		}
		if (side == 2) {
			--z;
		}
		if (side == 3) {
			++z;
		}
		if (side == 4) {
			--x;
		}
		if (side == 5) {
			++x;
		}
		int blockId = world.getBlockId(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		int slabType = metadata & 7;

		if (blockId == this.singleSlab.blockID && slabType == itemStack.getItemDamage()) {
			if (world.checkBlockCollision(this.doubleSlab.getCollisionBoundingBoxFromPool(world, x, y, z)) && world.setBlock(x, y, z, this.doubleSlab.blockID, slabType, MCHelper.NOTIFY_AND_UPDATE_REMOTE)) {
				world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, this.doubleSlab.stepSound.getStepSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getPitch() * 0.8F);
				--itemStack.stackSize;
			}
			return true;
		} else {
			return false;
		}
	}
}