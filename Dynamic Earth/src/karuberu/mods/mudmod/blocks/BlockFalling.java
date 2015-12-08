package karuberu.mods.mudmod.blocks;

import java.util.ArrayList;
import java.util.Random;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.entity.EntityFallingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockFalling extends Block implements IFallingBlock {
	
	public BlockFalling(int id, Material material) {
		super(id, material);
	}
	
	@Override
	public boolean tryToFall(World world, int x, int y, int z) {
        if (BlockFalling.canFallBelow(world, x, y, z)) {
            byte radius = 32;

            if (BlockFalling.doSpawnEntity()
            && world.checkChunksExist(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius)) {
                if (!world.isRemote) {
                    EntityFallingBlock fallingBlock = new EntityFallingBlock(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.blockID, world.getBlockMetadata(x, y, z));
                    world.spawnEntityInWorld(fallingBlock);
                }
            } else {
                world.setBlockToAir(x, y, z);
                while (BlockFalling.canFallBelow(world, x, y, z)) {
                    --y;
                } if (y > 0) {
                    world.setBlock(x, y, z, this.blockID, world.getBlockMetadata(x, y, z), MCHelper.NOTIFY_AND_UPDATE_REMOTE);
                }
            }
            return true;
        }
        return false;
	}

	@Override
	public void onStartFalling(EntityFallingBlock entityFallingBlock) { }

	@Override
	public void onFinishFalling(World world, int x, int y, int z, int metadata) { }

	@Override
	public int getMetaForFall(int fallTime, int metadata, Random random) {
		return metadata;
	}

	@Override
	public int getFallDamage() {
		return 0;
	}

	@Override
	public int getMaxFallDamage() {
		return 0;
	}

	@Override
	public DamageSource getDamageSource() {
		return DamageSource.fallingBlock;
	}
	
	/**
	 * Checks to see if the block can fall into the space below it.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static boolean canFallBelow(World world, int x, int y, int z) {
		return BlockSand.canFallBelow(world, x, y, z);
	}
	
	public static boolean doSpawnEntity() {
		return !BlockSand.fallInstantly;
	}

	@Override
	public ArrayList<ItemStack> getItemsDropped(World world, int x, int y, int z, int fallTime, int metadata, Random random) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(new ItemStack(this.blockID, 1, this.damageDropped(metadata)));
		return list;
	}
}
