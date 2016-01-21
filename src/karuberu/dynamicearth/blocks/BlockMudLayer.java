package karuberu.dynamicearth.blocks;

import java.util.Random;
import karuberu.core.util.Helper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.api.IVanillaReplaceable;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.items.ItemMudBlob;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockMudLayer extends BlockDynamicEarthWet implements IVanillaReplaceable {

	public BlockMudLayer(String unlocalizedName) {
		super(unlocalizedName, Material.ground);
		this.setStepSound(Block.soundGravelFootstep);
		this.setLightOpacity(0);
		this.setHardness(0.2F);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setHydrateRadius(3, 0, 0, 3);
		this.setUseNeighborBrightness(true);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(BlockTexture.MUD.getIconPath());
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
        float height = 0.125F * metadata;
        return AxisAlignedBB.getAABBPool().getAABB(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + height, z + this.maxZ);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.motionX *= 0.7D;
		entity.motionZ *= 0.7D;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		int metadata = blockAccess.getBlockMetadata(x, y, z);
		float height;
		if (metadata >= 8) {
			super.setBlockBoundsBasedOnState(blockAccess, x, y, z);
			return;
		} else {
			height = 0.125F * (1 + metadata);
		}
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, height, 1.0F);
	}
	
	@Override
	protected boolean canDry(int metadata) {
		return true;
	}
	
	@Override
	protected void becomeDry(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (metadata >= 7) {
			world.setBlock(x, y, z, Block.dirt.blockID, 0, Helper.NOTIFY_AND_UPDATE_REMOTE);
		} else if (metadata == 3) {
			world.setBlock(x, y, z, DynamicEarth.dirtSlab.blockID, BlockDirtSlab.DIRT, Helper.NOTIFY_AND_UPDATE_REMOTE);
		} else if (metadata == 0) {
			world.setBlockToAir(x, y, z);
		} else {
			world.setBlockMetadataWithNotify(x, y, z, metadata - 1, Helper.NOTIFY_AND_UPDATE_REMOTE);
		}
	}
		   
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {     
        Block block = Block.blocksList[world.getBlockId(x, y - 1, z)];
        if (block == null) {
        	return false;
        }
        if (block.blockID == DynamicEarth.mudLayer.blockID
        && world.getBlockMetadata(x, y - 1, z) >= 7) {
        	return true;
        }
        if (!block.isLeaves(world, x, y - 1, z)
        && !block.isOpaqueCube()) {
        	return false;
        }
        return world.getBlockMaterial(x, y - 1, z).blocksMovement();
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		return this.canBlockStay(world, x, y, z, metadata);
	}
	
	private boolean canBlockStay(World world, int x, int y, int z, int metadata) {
		if (world.isBlockOpaqueCube(x, y - 1, z)) {
			return true;
		}
		return false;
	}
	
	@Override
    public void onBlockAdded(World world, int x, int y, int z) {
		if (world.getBlockMetadata(x, y, z) >= 7) {
			world.setBlock(x, y, z, DynamicEarth.mud.blockID, DynamicEarth.mud.NORMAL, Helper.NOTIFY_AND_UPDATE_REMOTE);
		}
	}
    
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		super.updateTick(world, x, y, z, random);
		if (world.getBlockMetadata(x, y, z) >= 7) {
			world.setBlock(x, y, z, DynamicEarth.mud.blockID, DynamicEarth.mud.NORMAL, Helper.NOTIFY_AND_UPDATE_REMOTE);
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
		this.tryRemoveInvalidMud(world, x, y, z);
	}

	private boolean tryRemoveInvalidMud(World world, int x, int y, int z) {
		if (!this.canPlaceBlockAt(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, 0, 0);
			world.setBlockToAir(x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public int idDropped(int metadata, Random random, int fortune) {
		return DynamicEarth.mudBlob.itemID;
	}
	
	@Override
	public int quantityDropped(int metadata, int fortune, Random random) {
		return MathHelper.clamp_int((metadata + 1) / 2, 1, 4);
	}
	
	@Override
	public int damageDropped(int metadata) {
		return ItemMudBlob.NORMAL;
	}
	
    @Override
    protected boolean canSilkHarvest() {
    	return false;
    }
    
	@Override
	public int idPicked(World world, int x, int y, int z) {
		return DynamicEarth.mudBlob.itemID;
	}

	@Override
	public ItemStack getVanillaBlockReplacement(Chunk chunk, int x, int y, int z) {
		if (chunk.getBlockMetadata(x, y, z) >= 7) {
			return new ItemStack(Block.dirt);
		}
		return null;
	}
}
