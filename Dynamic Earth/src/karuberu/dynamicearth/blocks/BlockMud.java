package karuberu.dynamicearth.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import karuberu.core.MCHelper;
import karuberu.core.event.INeighborBlockEventHandler;
import karuberu.core.event.NeighborBlockChangeEvent;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockDynamicEarth.Rate;
import karuberu.dynamicearth.client.TextureManager;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.entity.EntityFallingBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMud extends BlockDynamicEarth implements IFallingBlock, INeighborBlockEventHandler, ITillable {

	public static final int
		NORMAL = 0,
		WET = 1,
		FALLING = 2,
		FALLING_WET = 3,
		FERTILE = 4,
		FERTILE_WET = 5,
		FALLING_FERTILE = 6,
		FALLING_FERTILE_WET = 7;
	@SideOnly(Side.CLIENT)
	private Icon
		textureMud,
		textureMudWet;
	private static ItemStack
		dirt,
		wetMud,
		dryMud,
		fertileSoil,
		fertileMud,
		fertileMudWet;
	
	public BlockMud(int id) {
		super(id, Material.ground);
		this.setHardness(0.5F);
		this.setStepSound(Block.soundGravelFootstep);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName("mud");
		this.setHydrateRadius(2, 1, 4, 2);
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.textureMud = iconRegister.registerIcon(BlockTexture.MUD.getIconPath());
		this.textureMudWet = iconRegister.registerIcon(BlockTexture.MUDWET.getIconPath());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		switch(metadata) {
		case WET:
		case FALLING_WET:
		case FERTILE_WET:
		case FALLING_FERTILE_WET: return this.textureMudWet;
		default: return textureMud;
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (DynamicEarth.enableDeepMud
		&& metadata == WET || metadata == FERTILE_WET) {
			return AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 1 - 0.125F, z + 1);
		}
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public int tickRate(World world) {
		return 1;
	}

	@Override
	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		int plantID = plant.getPlantID(world, x, y + 1, z);
		if (plantID == Block.reed.blockID
		|| plantID == Block.sapling.blockID
		|| plantID == Block.tallGrass.blockID
		) {
			return true;
		}
		return super.canSustainPlant(world, x, y, z, direction, plant);
	}
	
	@Override
	protected ItemStack getDryBlock(int metadata) {
		switch (metadata) {
		case NORMAL: return dirt == null ? dirt = new ItemStack(Block.dirt.blockID, 1, 0) : dirt;
		case WET: return dryMud == null ? dryMud = new ItemStack(DynamicEarth.mud.blockID, 1, NORMAL) : dryMud;
		case FERTILE: return fertileSoil == null ? fertileSoil = new ItemStack(DynamicEarth.fertileSoil.blockID, 1, BlockFertileSoil.SOIL) : fertileSoil;
		case FERTILE_WET: return fertileMud == null ? fertileMud = new ItemStack(DynamicEarth.mud.blockID, 1, FERTILE) : fertileMud;
		}
		return null;
	}
	
	@Override
	protected ItemStack getWetBlock(int metadata) {
		switch (metadata) {
		case NORMAL: return wetMud == null ? wetMud = new ItemStack(DynamicEarth.mud.blockID, 1, WET) : wetMud;
		case FERTILE: return fertileMudWet == null ? fertileMudWet = new ItemStack(DynamicEarth.mud.blockID, 1, FERTILE_WET) : fertileMudWet;
		}
		return null;
	}
		
	@Override
	protected boolean canSpread(int metadata) {
		if (DynamicEarth.restoreDirtOnChunkLoad) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	protected ItemStack getBlockForSpread(World world, int x, int y, int z, int targetX, int targetY, int targetZ) {
		int targetID = world.getBlockId(targetX, targetY, targetZ);
		if (targetID == Block.dirt.blockID) {
			return new ItemStack(DynamicEarth.mud.blockID, 1, NORMAL);
		} else if (targetID == DynamicEarth.fertileSoil.blockID) {
			return new ItemStack(DynamicEarth.mud.blockID, 1, FERTILE);
		}
		return null;
	}
	
	public boolean isRooted(World world, int x, int y, int z) {
		Block block = Block.blocksList[world.getBlockId(x, y + 1, z)];
		return block instanceof IPlantable ? block != null : false;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {
			int metadata = world.getBlockMetadata(x, y, z);
			if (metadata == FERTILE || metadata == FERTILE_WET) {
				Block topBlock = Block.blocksList[world.getBlockId(x, y + 1, z)];
				if (topBlock instanceof IPlantable) {
					topBlock.updateTick(world, x, y + 1, z, random);
					if (topBlock instanceof BlockReed) {
						topBlock.updateTick(world, x, y + 1, z, random);
					}
				}
			}
			switch (metadata) {
			case NORMAL:
			case FERTILE:
				super.updateTick(world, x, y, z, random);
				break;
			case WET:
			case FERTILE_WET:
				super.updateTick(world, x, y, z, random);
			case FALLING:
			case FALLING_WET:
			case FALLING_FERTILE:
			case FALLING_FERTILE_WET:
				this.tryToFall(world, x, y, z);
				break;
			}
		}
	}
	
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch(metadata) {
    	case FALLING:
    		world.setBlock(x, y, z, DynamicEarth.mud.blockID, NORMAL, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
    		break;
    	case FALLING_WET:
    		world.setBlock(x, y, z, DynamicEarth.mud.blockID, WET, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
    		break;
    	case FALLING_FERTILE:
    		world.setBlock(x, y, z, DynamicEarth.mud.blockID, FERTILE, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
    		break;
    	case FALLING_FERTILE_WET:
    		world.setBlock(x, y, z, DynamicEarth.mud.blockID, FERTILE_WET, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
    		break;
    	}
    }
		
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.motionX *= 0.7D;
		entity.motionZ *= 0.7D;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch (metadata) {
		case WET:
		case FERTILE_WET:
		case FALLING:
		case FALLING_WET:
		case FALLING_FERTILE:
		case FALLING_FERTILE_WET:
			this.initiateMudSlide(world, x, y, z);
		}
		super.onNeighborBlockChange(world, x, y, z, neighborID);
	}
	
	@Override
	public void handleNeighborBlockChangeEvent(NeighborBlockChangeEvent event) {
		Block neighborBlock = Block.blocksList[event.neighborBlockID];
		if (neighborBlock != null) {
			if (neighborBlock.blockMaterial == Material.lava
			|| (event.side == MCHelper.SIDE_TOP && neighborBlock.blockMaterial == Material.fire)) {
				this.becomeDry(event.world, event.x, event.y, event.z);
			} else if (neighborBlock.blockMaterial == Material.water) {
				this.becomeWet(event.world, event.x, event.y, event.z);
			}
		}
	}
	
	@Override
	public boolean onTilled(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch (metadata) {
		case FERTILE:
		case FERTILE_WET: metadata = BlockDynamicFarmland.SOIL_WET; break;
		default: metadata = BlockDynamicFarmland.MUD; break;
		}
		world.setBlock(x, y, z, DynamicEarth.farmland.blockID, metadata, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
		return true;
	}
	
	protected boolean isHydrated(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch (metadata) {
		case WET:
		case FERTILE_WET:
			int hydrationDistance = this.getHydrationDistance(world, x, y, z);
			return hydrationDistance == 0 || this.isMuddy(world, x, y, z);
		default:
			return super.isHydrated(world, x, y, z);
		}
	}
	
	/**
	 * Returns true if there is water above the block (within hydration range) and
	 * there is only water, air, or mud between them. Also returns true if this block
	 * is horizontally adjacent to water (including diagonally).
	 */
	protected boolean isMuddy(World world, int x, int y, int z) {
		Material material;
		for (int xi = x - 1; xi <= x + 1; xi++) {
			for (int zi = z - 1; zi <= z + 1; zi++) {
				material = world.getBlockMaterial(xi, y, zi);
				if (material == Material.water) {
					return true;
				}
			}
		}
		for (int yi = y + 1; yi <= y + this.hydrateRadiusYDown; yi++) {
			material = world.getBlockMaterial(x, yi, z);
			if (material == Material.water) {
				return true;
			}
			if (!world.isAirBlock(x, yi, z)
			&& world.getBlockId(x, yi, z) != DynamicEarth.mud.blockID) {
				return false;
			}
		}
		return false;
	}
	
	@Override
	protected boolean willHydrate(World world, int x, int y, int z) {
		if (this.isBlockGettingRainedOn(world, x, y, z)) {
			return true;
		} else {
			return super.willHydrate(world, x, y, z) && this.isMuddy(world, x, y, z);
		}
	}
	
	/**
	 * Changes the block's metadata and attempts to start a mudslide
	 */
	@Override
	protected void becomeWet(World world, int x, int y, int z) {
		super.becomeWet(world, x, y, z);
		this.initiateMudSlide(world, x, y, z);
	}	
	
	@Override
	protected Rate getDryRateForBiome(BiomeGenBase biome) {
		Rate rate = super.getDryRateForBiome(biome);
		return rate == Rate.NONE ? Rate.SLOW : rate;
	}
	
	private void initiateMudSlide(World world, int x, int y, int z) {
		if (this.canFall(world, x, y, z)) {
			// if the block will fall, trigger surrounding blocks to mudslide as well,
			// then schedule a block update to actually fall.
			int[][] surroundingBlocks = MCHelper.getSurroundingBlocks(x, y, z);
			for (int[] coordinates : surroundingBlocks) {
				int xi = coordinates[0],
					yi = coordinates[1],
					zi = coordinates[2];
				if (world.getBlockId(xi, yi, zi) == this.blockID) {
					int metadata = world.getBlockMetadata(x, y, z);
					switch(metadata) {
					case NORMAL: metadata = FALLING; break;
					case WET: metadata = FALLING_WET; break;
					case FERTILE: metadata = FALLING_FERTILE; break;
					case FERTILE_WET: metadata = FALLING_FERTILE_WET; break;
					}
					world.setBlock(xi, yi, zi, this.blockID, metadata, MCHelper.UPDATE_WITHOUT_NOTIFY_REMOTE);
				}
			}
			world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
		} else {
			// if the block can't fall, change its metadata back to a stable one.
			int metadata = world.getBlockMetadata(x, y, z);
			if (metadata == FALLING || metadata == FALLING_WET) {
				switch(metadata) {
				case FALLING: metadata = NORMAL; break;
				case FALLING_WET: metadata = WET; break;
				case FALLING_FERTILE: metadata = FERTILE; break;
				case FALLING_FERTILE_WET: metadata = FERTILE_WET; break;
				}
				world.setBlock(x, y, z, this.blockID, metadata, MCHelper.UPDATE_WITHOUT_NOTIFY_REMOTE);
			}
		}
	}
	
	@Override
	public boolean tryToFall(World world, int x, int y, int z) {
		if (this.canFall(world, x, y, z)) {
			byte radius = 32;
			if (BlockFalling.doSpawnEntity()
			&& world.checkChunksExist(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius)) {
				if (!world.isRemote) {
					EntityFallingBlock fallingBlock = new EntityFallingBlock(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.blockID, world.getBlockMetadata(x, y, z));
					world.spawnEntityInWorld(fallingBlock);
				}
			} else {
				world.setBlockToAir(x, y, z);
				while (this.canFall(world, x, y, z)) {
					--y;
				} if (y > 0) {
					world.setBlock(x, y, z, this.blockID, world.getBlockMetadata(x, y, z), MCHelper.NOTIFY_AND_UPDATE_REMOTE);
				}
			}
			return true;
		}
		return false;
	}
	
	private boolean canFall(World world, int x, int y, int z) {
		return BlockFalling.canFallBelow(world, x, y - 1, z)
		&& y >= 0
		&& !this.isRooted(world, x, y, z);
	}
	
	@Override
	public void onStartFalling(EntityFallingBlock entityFallingBlock) {}

	@Override
	public void onFinishFalling(World world, int x, int y, int z, int metadata) {}

	@Override
	public ArrayList<ItemStack> getItemsDropped(World world, int x, int y, int z, int fallTime, int metadata, Random random) {
		return this.getBlockDropped(world, x, y, z, metadata, 0);
	}
	
	@Override
	public int getMetaForFall(int fallTime, int metadata, Random random) {
		switch (metadata) {
		case FALLING: return NORMAL;
		case FALLING_WET: return WET;
		case FALLING_FERTILE: return FERTILE;
		case FALLING_FERTILE_WET: return FERTILE_WET;
		default: return NORMAL;
		}
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
	
	@Override
	public int idDropped(int i, Random random, int j) {
		return DynamicEarth.mudBlob.itemID;
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 4;
	}
	
    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
    	switch (metadata) {
    	case NORMAL:
    	case FERTILE:
			return super.canSilkHarvest(world, player, x, y, z, metadata);
		default:
			return false;
    	}
	}
    
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
		list.add(new ItemStack(blockId, 1, NORMAL));
		list.add(new ItemStack(blockId, 1, WET));
		list.add(new ItemStack(blockId, 1, FERTILE));
		list.add(new ItemStack(blockId, 1, FERTILE_WET));
    }
	
    @SideOnly(Side.CLIENT)
    @Override
    public int idPicked(World world, int x, int y, int z) {
    	return DynamicEarth.mud.blockID;
    }
    
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(this.idPicked(world, x, y, z), 1, world.getBlockMetadata(x, y, z));
	}
	
	/**
	 * returns true if the block given will trigger a mudslide (currently just water)
	 */
	public static boolean blockWillCauseMudslide(int blockID) {
		Block block = Block.blocksList[blockID];
		if (block != null) {
			Material neighborMaterial = block.blockMaterial;
			if (neighborMaterial == Material.water) {
		   		return true;
			}
		}
	   	return false;
	}
}
