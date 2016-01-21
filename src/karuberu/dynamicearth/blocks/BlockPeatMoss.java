package karuberu.dynamicearth.blocks;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.core.util.Helper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class BlockPeatMoss extends BlockDynamicEarthWet {
	
	public static int minimumLightLevel = 6;
	public static int tickRate = 20;
	public static int hydrationRadius = 3;
	public static final int
		GROWTHSTAGE_1 = 0,
		GROWTHSTAGE_2 = 1,
		GROWTHSTAGE_3 = 2,
		GROWTHSTAGE_4 = 3,
		GROWTHSTAGE_FULLGROWN = 4,
		GROWTH_GRASS = 5,
		GROWTH_FLOWER_RED = 6,
		GROWTH_FLOWER_YELLOW = 7,
		GROWTH_MUSHROOM_BROWN = 8,
		GROWTH_MUSHROOM_RED = 9,
		GROWTH_NONE = 10;

	public BlockPeatMoss(String unlocalizedName) {
		super(unlocalizedName, MaterialPeatMoss.material);
		this.setStepSound(Block.soundGrassFootstep);
		this.setLightOpacity(0);
		this.setHardness(0.1F);
		this.setTickRandomly(true);
		this.setHydrateRadius(hydrationRadius, 2, 0, hydrationRadius);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(BlockTexture.PEATMOSS.getIconPath());
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
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
		switch(metadata) {
		case GROWTHSTAGE_1: this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, (1.0F / 32.0F), 0.6F); break;
		case GROWTHSTAGE_2: this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, (1.0F / 32.0F), 0.7F); break;
		case GROWTHSTAGE_3: this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, (1.0F / 32.0F), 0.9F); break;
		case GROWTHSTAGE_4: this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, (1.0F / 16.0F), 1.0F); break;
		default: this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, (1.0F / 8.0F), 1.0F); break;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return DynamicEarth.peatMossRenderID;
	}
	   
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return this.canBlockStay(world, x, y, z) && super.canPlaceBlockAt(world, x, y, z);
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		return this.canBlockStay(world, x, y, z, metadata);
	}
	
	private boolean canBlockStay(World world, int x, int y, int z, int metadata) {
		int soilID = world.getBlockId(x, y - 1, z);
		if (world.getFullBlockLightValue(x, y, z) >= BlockPeatMoss.minimumLightLevel
		&& BlockPeatMoss.soilIsValid(soilID, metadata)) {
			return true;
		}
		return false;
	}
	
	public static boolean soilIsValid(int soilID, int metadata) {
		switch(metadata) {
		case GROWTHSTAGE_1:
		case GROWTHSTAGE_2:
		case GROWTHSTAGE_3:
		case GROWTHSTAGE_4:
			return soilID == Block.tilledField.blockID
			|| soilID == DynamicEarth.farmland.blockID;
		default:
			return soilID == Block.dirt.blockID
				|| soilID == Block.grass.blockID
				|| soilID == Block.mycelium.blockID
				|| soilID == Block.tilledField.blockID
				|| (DynamicEarth.includeMud && soilID == DynamicEarth.mud.blockID)
				|| soilID == DynamicEarth.peat.blockID
				|| soilID == DynamicEarth.farmland.blockID
				|| (DynamicEarth.includeFertileSoil && soilID == DynamicEarth.fertileSoil.blockID);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
		this.tryRemoveInvalidMoss(world, x, y, z);
	}

	private boolean tryRemoveInvalidMoss(World world, int x, int y, int z) {
		if (!this.canPlaceBlockAt(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, 0, 0);
			world.setBlockToAir(x, y, z);
			return true;
		}
		return false;
	}

	@Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if (this.hasPlantGrowth(world, x, y, z)) {
			if (!player.capabilities.isCreativeMode) {
				this.getPlantGrowth(world, x, y, z).harvestBlock(world, player, x, y, z, this.getPlantGrowthMetadata(world, x, y, z));
			}
			world.setBlock(x, y, z, DynamicEarth.peatMoss.blockID, GROWTHSTAGE_FULLGROWN, Helper.NOTIFY_AND_UPDATE_REMOTE);
			return false;
		} else {
			return super.removeBlockByPlayer(world, player, x, y, z);
		}
    }

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int metadata) {
		super.harvestBlock(world, player, x, y, z, metadata);
		world.setBlockToAir(x, y, z);
	}
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		if (this.hasPlantGrowth(world, x, y, z)) {
			items.addAll(this.getPlantGrowth(world, x, y, z).getBlockDropped(world, x, y, z, this.getPlantGrowthMetadata(world, x, y, z), fortune));
		}
		if (world.rand.nextInt(Math.max(1, 10 - metadata - fortune)) == 0) {
			items.add(new ItemStack(DynamicEarth.peatMossSpecimen));
		}
		return items;
	}

	@Override
	public int idPicked(World world, int x, int y, int z) {
		return DynamicEarth.peatMossSpecimen.itemID;
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		if (this.hasPlantGrowth(world, x, y, z)) {
			return new ItemStack(this.getPlantGrowth(world, x, y, z), 1, this.getPlantGrowthMetadata(world, x, y, z));
		}
		return super.getPickBlock(target, world, x, y, z);
	}
	
	public boolean hasPlantGrowth(IBlockAccess blockAccess, int x, int y, int z, int metadata) {
		switch (metadata) {
		case GROWTH_GRASS:
		case GROWTH_FLOWER_RED:
		case GROWTH_FLOWER_YELLOW:
		case GROWTH_MUSHROOM_BROWN:
		case GROWTH_MUSHROOM_RED: return true;
		default: return false;
		}
	}
	
	public boolean hasPlantGrowth(IBlockAccess blockAccess, int x, int y, int z) {
		return this.hasPlantGrowth(blockAccess, x, y, z, blockAccess.getBlockMetadata(x, y, z));
	}

	public Block getPlantGrowth(IBlockAccess blockAccess, int x, int y, int z, int metadata) {
		switch (metadata) {
		case GROWTH_GRASS: return Block.tallGrass;
		case GROWTH_FLOWER_RED: return Block.plantRed;
		case GROWTH_FLOWER_YELLOW: return Block.plantYellow;
		case GROWTH_MUSHROOM_BROWN: return Block.mushroomBrown;
		case GROWTH_MUSHROOM_RED: return Block.mushroomRed;
		default: return null;
		}
	}
	public Block getPlantGrowth(IBlockAccess blockAccess, int x, int y, int z) {
		return this.getPlantGrowth(blockAccess, x, y, z, blockAccess.getBlockMetadata(x, y, z));
	}
	
	public int getPlantGrowthMetadata(IBlockAccess blockAccess, int x, int y, int z, int metadata) {
		switch (metadata) {
		case GROWTH_GRASS: return 1;
		case GROWTH_FLOWER_RED: return 0;
		case GROWTH_FLOWER_YELLOW: return 0;
		case GROWTH_MUSHROOM_BROWN: return 0;
		case GROWTH_MUSHROOM_RED: return 0;
		default: return -1;
		}
	}
	public int getPlantGrowthMetadata(IBlockAccess blockAccess, int x, int y, int z) {
		return this.getPlantGrowthMetadata(blockAccess, x, y, z, blockAccess.getBlockMetadata(x, y, z));
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (this.isLightSufficient(world, x, y, z)
		&& this.isHydrated(world, x, y, z)) {
			if (!BlockPeatMoss.isFullGrown(metadata)) {
				this.grow(world, x, y, z);			
			} else {
				if (metadata == GROWTHSTAGE_FULLGROWN) {
					this.grow(world, x, y, z);
				}
				if (random.nextInt(3) == 0) {
					this.makePeat(world, x, y, z);
				}
				this.tryToSpread(world, x, y, z);
			}
		} else {
			world.setBlockToAir(x, y, z);
		}
	}
	
	@Override
	public int tickRate(World world) {
		return BlockPeatMoss.tickRate;
	}
	
	private boolean isLightSufficient(World world, int x, int y, int z) {
		return world.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) >= minimumLightLevel
		|| world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) >= minimumLightLevel;
	}
	
	protected void makePeat(World world, int x, int y, int z) {
		final int metadata = world.getBlockMetadata(x, y, z);
		int soilID;
		int i = 1;
		while (soilIsValid(soilID = world.getBlockId(x, y - i, z), metadata)) {
			if (soilID != DynamicEarth.peat.blockID) {
				world.setBlock(x, y - i, z, DynamicEarth.peat.blockID, i == 1 ? BlockPeat.ZERO_EIGHTHS : BlockPeat.ONE_EIGHTH, Helper.NOTIFY_AND_UPDATE_REMOTE);
				return;
			} else if (soilID == DynamicEarth.peat.blockID) {
				int soilMeta = world.getBlockMetadata(x, y - i, z);
				switch (soilMeta) {
				case BlockPeat.WET:
				case BlockPeat.DRY:
					break;
				case BlockPeat.SEVEN_EIGHTHS:
					world.setBlock(x, y - i, z, DynamicEarth.peat.blockID, BlockPeat.WET, Helper.NOTIFY_AND_UPDATE_REMOTE);
					return;
				default:
					if (BlockPeat.isPartiallyFormed(soilMeta)) {
						world.setBlock(x, y - i, z, DynamicEarth.peat.blockID, soilMeta + 1, Helper.NOTIFY_AND_UPDATE_REMOTE);
						return;
					}
				}
			}
			i++;
		}
	}
	
	protected void grow(World world, int x, int y, int z) {
		if (this.isLightSufficient(world, x, y, z)) {
			int metadata = world.getBlockMetadata(x, y, z);
			BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
			float humidity = biome.rainfall;
			switch (metadata) {
			case GROWTHSTAGE_FULLGROWN:
				if (world.rand.nextInt(50) == 0) {
					int random = world.rand.nextInt(100);
					int newMetadata = GROWTHSTAGE_FULLGROWN;
					if (random >= 95) {
						newMetadata = GROWTH_NONE;
					} else if (random >= 45) {
						newMetadata = GROWTH_GRASS;
					} else if (random >= 44
					&& humidity < 0.9F) {
						newMetadata = GROWTH_FLOWER_RED;
					} else if (random >= 43
					&& humidity < 0.9F) {
						newMetadata = GROWTH_FLOWER_YELLOW;
					} else if (random >= 40
					&& humidity >= 0.5F) {
						newMetadata = GROWTH_MUSHROOM_BROWN;
					} else if (random >= 39
					&& humidity >= 0.5F) {
						newMetadata = GROWTH_MUSHROOM_RED;
					} else {
						break;
					}
					world.setBlockMetadataWithNotify(x, y, z, newMetadata, Helper.NOTIFY_AND_UPDATE_REMOTE);				
				}
				break;
			case GROWTHSTAGE_1:
			case GROWTHSTAGE_2:
			case GROWTHSTAGE_3:
			case GROWTHSTAGE_4:
				world.setBlockMetadataWithNotify(x, y, z, metadata + 1, Helper.NOTIFY_AND_UPDATE_REMOTE);		
				break;
			default:
				world.setBlockMetadataWithNotify(x, y, z, GROWTHSTAGE_FULLGROWN, Helper.NOTIFY_AND_UPDATE_REMOTE);			
				break;
			}
		}
	}
	
	@Override
	protected ItemStack getDryBlock(int metadata) {
		return new ItemStack(0, 1, 0);
	}
	
	@Override
	protected boolean isBeingHeated(World world, int x, int y, int z) {
		return false;
	}
	
	@Override
	protected boolean canSpreadHydration(int metadata) {
		return metadata == BlockPeatMoss.GROWTHSTAGE_FULLGROWN;
	}
	
	@Override
	protected void spread(World world, int x, int y, int z, int n) {
		for (int i = 0; i < n; ++i) {
			int xi = x + world.rand.nextInt(3) - 1,
				yi = y + world.rand.nextInt(2) - 1,
				zi = z + world.rand.nextInt(3) - 1;
			int newMetadata = BlockPeatMoss.getMetadataForSpread(world, xi, yi, zi);
			if (newMetadata != -1
			&& this.getHydrationDistance(world, xi, yi, zi) > 0
			&& this.canBlockStay(world, xi, yi, zi, newMetadata)
			&& world.getBlockLightValue(xi, yi, zi) >= BlockPeatMoss.minimumLightLevel) {
				world.setBlock(xi, yi, zi, this.blockID, newMetadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
			}
		}
	}
	
	public static boolean canSpreadToBlock(World world, int x, int y, int z) {
		return getMetadataForSpread(world, x, y, z) != -1;
	}
	
	public static int getMetadataForSpread(World world, int x, int y, int z) {
		int blockID = world.getBlockId(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		if (blockID == 0) {
			return GROWTHSTAGE_FULLGROWN;
		} else if (blockID == Block.tallGrass.blockID && metadata == 1) {
			return GROWTH_GRASS;
		} else if (blockID == Block.plantRed.blockID && metadata == 0) {
			return GROWTH_FLOWER_RED;
		} else if (blockID == Block.plantYellow.blockID && metadata == 0) {
			return GROWTH_FLOWER_YELLOW;
		} else if (blockID == Block.mushroomBrown.blockID && metadata == 0) {
			return GROWTH_MUSHROOM_BROWN;
		} else if (blockID == Block.mushroomRed.blockID && metadata == 0) {
			return GROWTH_MUSHROOM_RED;
		} else {
			return -1;
		}
	}
	
	public static boolean isFullGrown(int metadata) {
		switch (metadata) {
		case GROWTHSTAGE_1:
		case GROWTHSTAGE_2:
		case GROWTHSTAGE_3:
		case GROWTHSTAGE_4:
			return false;
		default:
			return true;
		}
	}
}
