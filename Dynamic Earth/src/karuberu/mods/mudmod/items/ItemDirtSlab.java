package karuberu.mods.mudmod.items;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.blocks.BlockGrassSlab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDirtSlab extends ItemBlock {
    protected boolean isDoubleSlab;
    protected BlockHalfSlab singleSlab;
    protected Block doubleSlab;
	
    public ItemDirtSlab(int id) {
		super(id);
		this.singleSlab = MudMod.dirtSlab;
        this.doubleSlab = MudMod.dirtDoubleSlab;
        if (this.itemID == this.doubleSlab.blockID - 256) {
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
        return Block.blocksList[this.itemID].getBlockTextureFromSideAndMetadata(MCHelper.SIDE_EAST, damageValue);
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
        } else if (itemStack.stackSize == 0 || !player.canPlayerEdit(x, y, z, side, itemStack)) {
            return false;
        } else {
            int slabID = world.getBlockId(x, y, z);
            int slabMetadata = world.getBlockMetadata(x, y, z);
            int slabType = slabMetadata & 7;
            boolean isTopSlab = (slabMetadata & 8) != 0;

            if ((side == MCHelper.SIDE_TOP && !isTopSlab || side == MCHelper.SIDE_BOTTOM && isTopSlab)
            && this.blockMatches(slabID, slabType)) {
                if (world.checkIfAABBIsClear(this.doubleSlab.getCollisionBoundingBoxFromPool(world, x, y, z))) {
                	if (this.tryFormDoubleSlab(itemStack, world, x, y, z, side)) {
	                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.doubleSlab.stepSound.getStepSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getPitch() * 0.8F);
	                    --itemStack.stackSize;
                	}
                }
                return true;
            } else {
                return this.makeDoubleSlab(itemStack, player, world, x, y, z, side) ? true : super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
            }
        }
    }
    
    protected boolean blockMatches(int blockId, int slabType) {
    	assert MudMod.dirtSlab != null;
    	assert MudMod.grassSlab != null;
    	
    	return blockId == MudMod.dirtSlab.blockID || blockId == MudMod.grassSlab.blockID;
    }
    
    protected boolean tryFormDoubleSlab(ItemStack itemStack, World world, int x, int y, int z, int side) {
    	int slabMetadata = world.getBlockMetadata(x, y, z);
        boolean isTopSlab = (slabMetadata & 8) != 0;
    	boolean blockPlaced = false;
    	if (isTopSlab && world.getBlockId(x, y, z) == MudMod.grassSlab.blockID) {
        	switch (slabMetadata & 7) {
        	case BlockGrassSlab.GRASS:
        		blockPlaced = world.setBlockAndMetadataWithNotify(x, y, z, Block.grass.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
        		break;
        	case BlockGrassSlab.MYCELIUM:
        		blockPlaced = world.setBlockAndMetadataWithNotify(x, y, z, Block.mycelium.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
        		break;
        	}
    	} else {
    		blockPlaced = world.setBlockAndMetadataWithNotify(x, y, z, Block.dirt.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
    	}
    	return blockPlaced;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canPlaceItemBlockOnSide(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack itemStack) {
        int originalX = x;
        int originalY = y;
        int originalZ = z;
        int slabID = world.getBlockId(x, y, z);
        int slabMetadata = world.getBlockMetadata(x, y, z);
        int slabType = slabMetadata & 7;
        boolean isTopSlab = (slabMetadata & 8) != 0;

        if ((side == 1 && !isTopSlab || side == 0 && isTopSlab)
        && this.blockMatches(slabID, slabType)) {
            return true;
        } else {
        	switch (side) {
        	case MCHelper.SIDE_BOTTOM: --y; break;
        	case MCHelper.SIDE_TOP: ++y; break;
        	case MCHelper.SIDE_EAST: --z; break;
        	case MCHelper.SIDE_WEST: ++z; break;
        	case MCHelper.SIDE_NORTH: --x; break;
        	case MCHelper.SIDE_SOUTH: ++x; break;
            }
            slabID = world.getBlockId(x, y, z);
            slabMetadata = world.getBlockMetadata(x, y, z);
            slabType = slabMetadata & 7;
            isTopSlab = (slabMetadata & 8) != 0;
            return this.blockMatches(slabID, slabType) ? true : super.canPlaceItemBlockOnSide(world, originalX, originalY, originalZ, side, player, itemStack);
        }
    }

    private boolean makeDoubleSlab(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side) {
    	switch (side) {
    	case MCHelper.SIDE_BOTTOM: --y; break;
    	case MCHelper.SIDE_TOP: ++y; break;
    	case MCHelper.SIDE_EAST: --z; break;
    	case MCHelper.SIDE_WEST: ++z; break;
    	case MCHelper.SIDE_NORTH: --x; break;
    	case MCHelper.SIDE_SOUTH: ++x; break;
        }
        int blockId = world.getBlockId(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        int slabType = metadata & 7;

        if (this.blockMatches(blockId, slabType)) {
            if (world.checkIfAABBIsClear(this.doubleSlab.getCollisionBoundingBoxFromPool(world, x, y, z))) {
            	if (this.tryFormDoubleSlab(itemStack, world, x, y, z, side)) {
                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.doubleSlab.stepSound.getStepSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getPitch() * 0.8F);
                    --itemStack.stackSize;
            	}
            }
            return true;
        } else {
            return false;
        }
    }
}
