package karuberu.mods.mudmod.blocks;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.core.KaruberuLogger;
import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

public class BlockPeatMoss extends BlockMudMod {
	
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

	public BlockPeatMoss(int id) {
		super(id, MaterialPeatMoss.material);
		this.setUnlocalizedName("peatMoss");
		this.setStepSound(Block.soundGrassFootstep);
		this.setLightOpacity(0);
		this.setHardness(0.1F);
		this.setTickRandomly(true);
		this.setHydrateRadius(hydrationRadius, 2, 0, hydrationRadius);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = TextureManager.instance().getBlockTexture(Texture.PEATMOSS);
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
		return MudMod.peatMossRenderID;
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
		if (world.getFullBlockLightValue(x, y, z) >= this.minimumLightLevel
		&& (this.soilIsValid(soilID, metadata))) {
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
			return soilID == Block.tilledField.blockID;
		default:
			return soilID == Block.dirt.blockID
				|| soilID == Block.grass.blockID
				|| soilID == Block.mycelium.blockID
				|| soilID == Block.tilledField.blockID
				|| soilID == MudMod.mud.blockID
				|| soilID == MudMod.peat.blockID
				|| soilID == MudMod.peatDry.blockID;
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
			world.setBlock(x, y, z, MudMod.peatMoss.blockID, GROWTHSTAGE_FULLGROWN, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
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
			items.add(new ItemStack(MudMod.peatMossSpecimen));
		}
		return items;
	}

	@Override
	public int idPicked(World world, int x, int y, int z) {
		return MudMod.peatMossSpecimen.itemID;
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
		int soilID = world.getBlockId(x, y - 1, z);
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
		return this.tickRate;
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
			if (soilID != MudMod.peat.blockID
			&& soilID != MudMod.peatDry.blockID) {
				world.setBlock(x, y - i, z, MudMod.peat.blockID, i == 1 ? BlockPeat.META_NONE : BlockPeat.META_1EIGHTH, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
				return;
			} else if (soilID == MudMod.peat.blockID) {
				int soilMeta = world.getBlockMetadata(x, y - i, z);
				switch (soilMeta) {
				case BlockPeat.META_FULL: break;
				case BlockPeat.META_7EIGHTHS:
					world.setBlock(x, y - i, z, MudMod.peat.blockID, BlockPeat.META_FULL, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
					return;
				default:
					world.setBlock(x, y - i, z, MudMod.peat.blockID, soilMeta + 1, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
					return;
				}
			}
			i++;
		}
	}
	
	protected void grow(World world, int x, int y, int z) {
		if (this.isLightSufficient(world, x, y, z)) {
			int metadata = world.getBlockMetadata(x, y, z);
			switch(metadata) {
			case GROWTHSTAGE_FULLGROWN:
				if (world.rand.nextInt(50) == 0) {
					int random = world.rand.nextInt(100);
					int newMetadata = GROWTHSTAGE_FULLGROWN;
					if (random > 95) {
						newMetadata = GROWTH_NONE;
					} else if (random > 45) {
						newMetadata = GROWTH_GRASS;
					} else if (random > 44) {
						newMetadata = GROWTH_FLOWER_RED;
					} else if (random > 43) {
						newMetadata = GROWTH_FLOWER_YELLOW;
					} else if (random > 40) {
						newMetadata = GROWTH_MUSHROOM_BROWN;
					} else if (random > 39) {
						newMetadata = GROWTH_MUSHROOM_RED;
					} else {
						break;
					}
					world.setBlockMetadataWithNotify(x, y, z, newMetadata, MCHelper.NOTIFY_AND_UPDATE_REMOTE);				
				}
				break;
			case GROWTHSTAGE_1:
			case GROWTHSTAGE_2:
			case GROWTHSTAGE_3:
			case GROWTHSTAGE_4:
				world.setBlockMetadataWithNotify(x, y, z, metadata + 1, MCHelper.NOTIFY_AND_UPDATE_REMOTE);		
				break;
			default:
				world.setBlockMetadataWithNotify(x, y, z, GROWTHSTAGE_FULLGROWN, MCHelper.NOTIFY_AND_UPDATE_REMOTE);			
				break;
			}
		}
	}
	
	@Override
	protected int getDryBlock(int metadata) {
		return 0;
	}
	
	@Override
	protected boolean isBeingHeated(World world, int x, int y, int z) {
		return false;
	}
	
	@Override
	protected boolean canSpread(int metadata) {
		return metadata == BlockPeatMoss.GROWTHSTAGE_FULLGROWN;
	}
	
	@Override
	protected void spread(World world, int x, int y, int z, int n) {
		int metadata = world.getBlockMetadata(x, y, z);
		for (int i = 0; i < n; ++i) {
			int xi = x + world.rand.nextInt(3) - 1,
				yi = y + world.rand.nextInt(2) - 1,
				zi = z + world.rand.nextInt(3) - 1;
			int soilID = world.getBlockId(xi, yi - 1, zi);
			int newMetadata = this.getMetadataForSpread(world, xi, yi, zi);
			if (newMetadata != -1
			&& this.isHydrated(world, xi, yi, zi)
			&& this.canBlockStay(world, xi, yi, zi, metadata)
			&& world.getBlockLightValue(xi, yi, zi) >= this.minimumLightLevel) {
				world.setBlock(xi, yi, zi, this.blockID, newMetadata, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
				break;
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
