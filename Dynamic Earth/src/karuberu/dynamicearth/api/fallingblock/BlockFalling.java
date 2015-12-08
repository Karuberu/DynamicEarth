package karuberu.dynamicearth.api.fallingblock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import karuberu.dynamicearth.api.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class BlockFalling extends Block implements IFallingBlock {
	
	public BlockFalling(int id, Material material) {
		super(id, material);
	}
	
	@Override
	public boolean canSpawnFromBlock(World world, int x, int y, int z) {
		return world.getBlockId(x, y, z) == this.blockID;
	}

	/**
	 * Checks to see if the block can fall into the space below it.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@Override
	public boolean canFallBelow(World world, int x, int y, int z) {
		return BlockFalling.blockCanFallBelow(world, x, y, z);
	}
		
	@Override
	public boolean tryToFall(World world, int x, int y, int z) {
		if (BlockFalling.blockCanFallBelow(world, x, y, z)) {
			this.spawnFallingBlock(world, x, y, z, this.blockID, world.getBlockMetadata(x, y, z));
			return true;
		}
        return false;
	}
	
	public void spawnFallingBlock(World world, int x, int y, int z, int blockID, int metadata) {
		Entity entity = null;
		if (Reference.fallingBlockEntityClass != null) {
			Constructor constructor;
			try {
				constructor = Reference.fallingBlockEntityClass.getConstructor(World.class, int.class, int.class, int.class, int.class, int.class);
				entity = (Entity)constructor.newInstance(world, x, y, z, blockID, metadata);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (entity == null) {
			entity = new EntityFallingSand(world, x, y, z, blockID, metadata);
		}
		byte radius = 32;
        if (BlockFalling.doSpawnEntity()
        && world.checkChunksExist(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius)) {
            if (!world.isRemote) {
            	world.spawnEntityInWorld(entity);
            }
        } else {
    		IFallingBlock fallingBlock = Block.blocksList[blockID] instanceof IFallingBlock ? (IFallingBlock)Block.blocksList[blockID] : null;
            world.setBlockToAir(x, y, z);
            while (fallingBlock != null ? fallingBlock.canFallBelow(world, x, y, z) : BlockFalling.blockCanFallBelow(world, x, y, z)) {
                --y;
            } if (y > 0) {
                world.setBlock(x, y, z, blockID, metadata, 1 & 2);
            }
        }
	}

	@Override
	public void onStartFalling(Entity entity, World world, int x, int y, int z, int metadata) {}

	@Override
	public void onFallTick(Entity entity, World world, int x, int y, int z, int metadata, int fallTime) {}

	@Override
	public void onFinishFalling(Entity entity, World world, int x, int y, int z, int metadata) {}

	@Override
	public int getMetaForFall(int fallTime, int metadata, Random random) {
		return metadata;
	}

	@Override
	public float getFallDamage() {
		return 0.0F;
	}

	@Override
	public int getMaxFallDamage() {
		return 0;
	}

	@Override
	public DamageSource getDamageSource() {
		return DamageSource.fallingBlock;
	}

	@Override
	public ArrayList<ItemStack> getItemsDropped(World world, int x, int y, int z, int fallTime, int metadata, Random random) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(new ItemStack(this.blockID, 1, this.damageDropped(metadata)));
		return list;
	}
	
	public static boolean doSpawnEntity() {
		return !BlockSand.fallInstantly;
	}
	
	public static boolean blockCanFallBelow(World world, int x, int y, int z) {
		return BlockSand.canFallBelow(world, x, y - 1, z);
	}
}
