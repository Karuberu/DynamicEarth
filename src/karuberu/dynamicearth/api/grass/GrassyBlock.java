package karuberu.dynamicearth.api.grass;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GrassyBlock implements IGrassyBlock {
	final ItemStack
		dirtStack,
		grassStack,
		myceliumStack;
	
	public GrassyBlock(ItemStack dirtStack, ItemStack grassStack, ItemStack myceliumStack) {
		this.dirtStack = dirtStack;
		this.grassStack = grassStack;
		this.myceliumStack = myceliumStack;
	}
	
	@Override
	public boolean canSpread(World world, int x, int y, int z) {
		GrassType type = this.getType(world, x, y, z);
		if ((type == GrassType.GRASS || type == GrassType.MYCELIUM)
		&& world.getBlockLightValue(x, y + 1, z) >= 9
		&& world.getBlockLightOpacity(x, y + 1, z) <= 2) {
			return true;
		}
		return false;
	}
	
	@Override
	public void tryToGrow(World world, int x, int y, int z, GrassType type) {
		if (this.getType(world, x, y, z) == GrassType.DIRT
		&& world.getBlockLightValue(x, y + 1, z) >= 4
        && world.getBlockLightOpacity(x, y + 1, z) <= 2) {
			switch (type) {
			case GRASS:
				world.setBlock(x, y, z, this.grassStack.itemID, this.grassStack.getItemDamage(), 3);
				break;
			case MYCELIUM:
				world.setBlock(x, y, z, this.myceliumStack.itemID, this.myceliumStack.getItemDamage(), 3);
				break;
			default:
				return;
			}
		}
	}
	
	@Override
	public GrassType getType(World world, int x, int y, int z) {
		int blockId = world.getBlockId(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
        if (blockId == this.grassStack.itemID
        && metadata == this.grassStack.getItemDamage()) {
    		return GrassType.GRASS;
        } else if (blockId == this.myceliumStack.itemID
        && metadata == this.myceliumStack.getItemDamage()) {
    		return GrassType.MYCELIUM;
        } else if (blockId == this.dirtStack.itemID
        && metadata == this.dirtStack.getItemDamage()){
    		return GrassType.DIRT;
        } else {
        	return null;
        }
	}

	@Override
	public ItemStack getBlockForType(World world, int x, int y, int z, GrassType type) {
		switch (type) {
		case DIRT:
			return dirtStack;
		case GRASS:
			return grassStack;
		case MYCELIUM:
			return myceliumStack;
		}
		return null;
	}

	@Override
	public boolean willBurn(World world, int x, int y, int z) {
		GrassType type = this.getType(world, x, y, z); 
		return type == GrassType.GRASS || type == GrassType.MYCELIUM;
	}
}
