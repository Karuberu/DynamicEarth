package karuberu.mods.mudmod.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.blocks.BlockGrassSlab;

public class ItemGrassSlab extends ItemDirtSlab {

	public ItemGrassSlab(int id) {
		super(id);
		this.singleSlab = MudMod.grassSlab;
        this.doubleSlab = MudMod.grassDoubleSlab;
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
    		if (world.getBlockId(x, y, z) == MudMod.dirtSlab.blockID || slabMetadata != itemDamage) {
	        	switch (itemDamage) {
	        	case BlockGrassSlab.GRASS:
	        		blockPlaced = world.setBlockWithNotify(x, y, z, Block.grass.blockID);
	        		break;
	        	case BlockGrassSlab.MYCELIUM:
	        		blockPlaced = world.setBlockWithNotify(x, y, z, Block.mycelium.blockID);
	        		break;
	        	}
    		} else {
    			blockPlaced = world.setBlockAndMetadataWithNotify(x, y, z, this.doubleSlab.blockID, slabMetadata);
    		}
    	} else if (isTopSlab) {
    		if (world.getBlockId(x, y, z) == MudMod.dirtSlab.blockID) {
        		blockPlaced = world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
    		} else if (slabMetadata != itemDamage) {
	        	switch (slabMetadata & 7) {
	        	case BlockGrassSlab.GRASS:
	        		blockPlaced = world.setBlockWithNotify(x, y, z, Block.grass.blockID);
	        		break;
	        	case BlockGrassSlab.MYCELIUM:
	        		blockPlaced = world.setBlockWithNotify(x, y, z, Block.mycelium.blockID);
	        		break;
	        	}
    		} else {
    			blockPlaced = world.setBlockAndMetadataWithNotify(x, y, z, this.doubleSlab.blockID, slabMetadata);    			
    		}
    	}
    	return blockPlaced;
    }

	@Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		ItemStack dirtSlabs = new ItemStack(MudMod.dirtSlab, itemStack.stackSize);
		boolean success = player.inventory.addItemStackToInventory(dirtSlabs);
		if (!success) {
			player.dropPlayerItem(dirtSlabs);
		}
	}

}
