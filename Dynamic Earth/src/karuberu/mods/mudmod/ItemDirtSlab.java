package karuberu.mods.mudmod;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.BlockHalfSlab;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemSlab;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemDirtSlab extends ItemBlock {
    private final boolean isDoubleSlab;
    private final BlockHalfSlab singleSlab;
    private final Block doubleSlab;
	
    public ItemDirtSlab(int id) {
		super(id);
		this.singleSlab = MudMod.dirtSlab;
        this.doubleSlab = Block.dirt;
        if (this.shiftedIndex == this.doubleSlab.blockID - 256) {
            this.isDoubleSlab = true;
        } else  {
	        this.isDoubleSlab = false;
		}
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
	
    @Override
    @SideOnly(Side.CLIENT)
    public int getIconFromDamage(int damageValue) {
        return Block.blocksList[this.shiftedIndex].getBlockTextureFromSideAndMetadata(2, damageValue);
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getItemNameIS(ItemStack itemStack) {
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
                if (world.checkIfAABBIsClear(this.doubleSlab.getCollisionBoundingBoxFromPool(world, x, y, z)) && world.setBlockAndMetadataWithNotify(x, y, z, this.doubleSlab.blockID, slabType)) {
                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.doubleSlab.stepSound.getStepSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getPitch() * 0.8F);
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
            if (world.checkIfAABBIsClear(this.doubleSlab.getCollisionBoundingBoxFromPool(world, x, y, z)) && world.setBlockAndMetadataWithNotify(x, y, z, this.doubleSlab.blockID, slabType)) {
                world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.doubleSlab.stepSound.getStepSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getPitch() * 0.8F);
                --itemStack.stackSize;
            }
            return true;
        } else {
            return false;
        }
    }
}
