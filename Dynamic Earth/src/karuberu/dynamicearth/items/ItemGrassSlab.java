package karuberu.dynamicearth.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockGrassSlab;

public class ItemGrassSlab extends ItemDirtSlab {

	public ItemGrassSlab(int id) {
		super(id);
		this.singleSlab = DynamicEarth.grassSlab;
        this.doubleSlab = DynamicEarth.grassDoubleSlab;
        if (this.itemID == this.doubleSlab.blockID - 256) {
            this.isDoubleSlab = true;
        } else  {
	        this.isDoubleSlab = false;
		}
	}
	
	@Override
    protected boolean tryFormDoubleSlab(ItemStack itemStack, World world, int x, int y, int z, int side) {
    	int slabMetadata = world.getBlockMetadata(x, y, z);
    	int itemDamage = itemStack.getItemDamage();
        boolean isTopSlab = (slabMetadata & 8) != 0;
    	boolean blockPlaced = false;
    	if (!isTopSlab) {
    		if (world.getBlockId(x, y, z) == DynamicEarth.dirtSlab.blockID || slabMetadata != itemDamage) {
	        	switch (itemDamage) {
	        	case BlockGrassSlab.GRASS:
	        		blockPlaced = world.setBlock(x, y, z, Block.grass.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
	        		break;
	        	case BlockGrassSlab.MYCELIUM:
	        		blockPlaced = world.setBlock(x, y, z, Block.mycelium.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
	        		break;
	        	}
    		} else {
    			blockPlaced = world.setBlock(x, y, z, this.doubleSlab.blockID, slabMetadata, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
    		}
    	} else if (isTopSlab) {
    		if (world.getBlockId(x, y, z) == DynamicEarth.dirtSlab.blockID) {
        		blockPlaced = world.setBlock(x, y, z, Block.dirt.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
    		} else if (slabMetadata != itemDamage) {
	        	switch (slabMetadata & 7) {
	        	case BlockGrassSlab.GRASS:
	        		blockPlaced = world.setBlock(x, y, z, Block.grass.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
	        		break;
	        	case BlockGrassSlab.MYCELIUM:
	        		blockPlaced = world.setBlock(x, y, z, Block.mycelium.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
	        		break;
	        	}
    		} else {
    			blockPlaced = world.setBlock(x, y, z, this.doubleSlab.blockID, slabMetadata, MCHelper.NOTIFY_AND_UPDATE_REMOTE);    			
    		}
    	}
    	return blockPlaced;
    }
}
