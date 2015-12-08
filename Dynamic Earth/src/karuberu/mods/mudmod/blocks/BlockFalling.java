package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.mods.mudmod.entity.EntityFallingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockFalling extends Block implements IFallingBlock {
	
	public BlockFalling(int id, Material material) {
		super(id, material);
	}
	
	public BlockFalling(int id, int textureIndex, Material material) {
		super(id, textureIndex, material);
	}
	
	@Override
	public boolean tryToFall(World world, int x, int y, int z) {
        if (this.canFallBelow(world, x, y, z)) {
            byte radius = 32;

            if (this.doSpawnEntity()
            && world.checkChunksExist(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius)) {
                if (!world.isRemote) {
                    EntityFallingBlock fallingBlock = new EntityFallingBlock(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.blockID, world.getBlockMetadata(x, y, z));
                    world.spawnEntityInWorld(fallingBlock);
                }
            } else {
                world.setBlockWithNotify(x, y, z, 0);
                while (this.canFallBelow(world, x, y, z)) {
                    --y;
                } if (y > 0) {
                    world.setBlockWithNotify(x, y, z, this.blockID);
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
	public boolean isDamagedByFall() {
		return false;
	}

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
}
