package karuberu.dynamicearth.items;

import karuberu.core.util.Coordinates;
import karuberu.core.util.Helper;
import karuberu.core.util.block.BlockSide;
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
			if (this.canMakeDoubleSlab(side, world.getBlockMetadata(x, y, z))) {
				boolean blockPlaced = this.tryToMakeDoubleSlab(itemStack, player, world, x, y, z);
				if (blockPlaced) {
					return true;
				}
			}
			Coordinates coords = Coordinates.getCoordsForBlockPlacement(x, y, z, side);
			boolean blockPlaced = this.tryToMakeDoubleSlab(itemStack, player, world, coords.x, coords.y, coords.z);
			if (blockPlaced) {
				return true;
			} else {
				return super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
			}
		}
	}
	
	private boolean canMakeDoubleSlab(int side, int metadata) {
		boolean isTopSlab = Helper.isTopSlab(metadata);
		return (side == BlockSide.BOTTOM.code && isTopSlab)
		|| (side == BlockSide.TOP.code && !isTopSlab);
	}
	
	private boolean tryToMakeDoubleSlab(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z) {		
		int blockId = world.getBlockId(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		int slabType = Helper.getSlabMetadata(metadata);
		if (this.slabMatches(blockId, metadata, itemStack.getItemDamage())) {
			boolean colliding = world.checkBlockCollision(this.doubleSlab.getCollisionBoundingBoxFromPool(world, x, y, z));
			if (colliding) {
				boolean blockSet = world.setBlock(x, y, z, this.doubleSlab.blockID, slabType, Helper.NOTIFY_AND_UPDATE_REMOTE);
				if (blockSet) {
					world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, this.doubleSlab.stepSound.getStepSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getPitch() * 0.8F);
					--itemStack.stackSize;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canPlaceItemBlockOnSide(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack itemStack) {
		int itemDamage = itemStack.getItemDamage();
		int originalX = x;
		int originalY = y;
		int originalZ = z;
		int blockId = world.getBlockId(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);

		if (this.canMakeDoubleSlab(side, metadata)
		&& this.slabMatches(blockId, metadata, itemDamage)) {
			return true;
		} else {
			Coordinates coords = Coordinates.getCoordsForBlockPlacement(x, y, z, side);
			blockId = coords.getBlockID(world);
			metadata = coords.getBlockMetadata(world);
			if (this.slabMatches(blockId, metadata, itemDamage)) {
				return true;
			} else {
				return super.canPlaceItemBlockOnSide(world, originalX, originalY, originalZ, side, player, itemStack);
			}
		}
	}
	
	private boolean slabMatches(int blockId, int blockMetadata, int itemDamage) {
		return blockId == this.singleSlab.blockID
		&& Helper.getSlabMetadata(blockMetadata) == itemDamage;
	}
}
