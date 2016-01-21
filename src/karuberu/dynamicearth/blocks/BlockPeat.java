package karuberu.dynamicearth.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import karuberu.core.util.Helper;
import karuberu.core.util.block.BlockSide;
import karuberu.core.util.client.render.ITextureOverlay;
import karuberu.core.util.client.render.RenderLayeredBlock;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.api.ITillable;
import karuberu.dynamicearth.api.IVanillaReplaceable;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeDirection;

public class BlockPeat extends BlockDynamicEarthWet implements ITextureOverlay, ITillable, IVanillaReplaceable {
	
	public static final int
		WET = 0,
		DRY = 1,
		ZERO_EIGHTHS = 2,
		ONE_EIGHTH = ZERO_EIGHTHS + 1,
		TWO_EIGHTHS = ONE_EIGHTH + 1,
		THREE_EIGHTHS = TWO_EIGHTHS + 1,
		FOUR_EIGHTHS = THREE_EIGHTHS + 1,
		FIVE_EIGHTHS = FOUR_EIGHTHS + 1,
		SIX_EIGHTHS = FIVE_EIGHTHS + 1,
		SEVEN_EIGHTHS = SIX_EIGHTHS + 1;
	@SideOnly(Side.CLIENT)
	private Icon
		texture,
		textureSide1,
		textureSide2,
		textureSide3,
		textureSide4,
		textureSide5,
		textureSide6,
		textureSide7,
		textureOverlayTop,
		textureOverlaySide,
		textureDry;
	private static ItemStack
		dirt,
		wetPeat,
		dryPeat;
    public static CreativeTabs
		creativeTab = CreativeTabs.tabBlock;
	
	public BlockPeat(String unlocalizedName) {
		super(unlocalizedName, Material.ground);
		this.setHardness(0.4F);
		this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(creativeTab);
        this.setHydrateRadius(2);
        this.setTickRandomly(true);
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.texture = iconRegister.registerIcon(BlockTexture.PEAT.getIconPath());
		this.textureSide1 = iconRegister.registerIcon(BlockTexture.PEATSIDE1.getIconPath());
		this.textureSide2 = iconRegister.registerIcon(BlockTexture.PEATSIDE2.getIconPath());
		this.textureSide3 = iconRegister.registerIcon(BlockTexture.PEATSIDE3.getIconPath());
		this.textureSide4 = iconRegister.registerIcon(BlockTexture.PEATSIDE4.getIconPath());
		this.textureSide5 = iconRegister.registerIcon(BlockTexture.PEATSIDE5.getIconPath());
		this.textureSide6 = iconRegister.registerIcon(BlockTexture.PEATSIDE6.getIconPath());
		this.textureSide7 = iconRegister.registerIcon(BlockTexture.PEATSIDE7.getIconPath());
		this.textureOverlayTop = iconRegister.registerIcon(BlockTexture.PEATMOSSYOVERLAYTOP.getIconPath());
		this.textureOverlaySide = iconRegister.registerIcon(BlockTexture.PEATMOSSYOVERLAYSIDE.getIconPath());
		this.textureDry = iconRegister.registerIcon(BlockTexture.PEATDRY.getIconPath());
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int metadata = blockAccess.getBlockMetadata(x, y, z);
    	return getIcon(side, metadata);
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata) {
    	if (side == BlockSide.TOP.code) {
    		switch (metadata) {
    		case ZERO_EIGHTHS: return Block.dirt.getBlockTextureFromSide(side);
    		case DRY: return this.textureDry;
    		default: return this.texture;
    		}
    	} else {
    		switch (metadata) {
    		case WET: return this.texture;
    		case DRY: return this.textureDry;
    		default: return Block.dirt.getBlockTextureFromSide(side);
    		}
    	}
    }

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int metadata, int side, int pass) {
		switch(pass) {
		case 1:
			if (side != BlockSide.TOP.code
			&& side != BlockSide.BOTTOM.code) {
	    		switch (metadata) {
	    		case ONE_EIGHTH: return this.textureSide1;
	    		case TWO_EIGHTHS: return this.textureSide2;
	    		case THREE_EIGHTHS: return this.textureSide3;
	    		case FOUR_EIGHTHS: return this.textureSide4;
	    		case FIVE_EIGHTHS: return this.textureSide5;
	    		case SIX_EIGHTHS: return this.textureSide6;
	    		case SEVEN_EIGHTHS: return this.textureSide7;
	    		default: return null;
	    		}
			} else {
				return null;
			}
		case 2:
	    	int topBlockID = blockAccess.getBlockId(x, y + 1, z);
	    	int topBlockMeta = blockAccess.getBlockMetadata(x, y + 1, z);
	    	if (topBlockID == DynamicEarth.peatMoss.blockID
	    	&& BlockPeatMoss.isFullGrown(topBlockMeta)) {
				if (side == BlockSide.TOP.code) {
					return this.textureOverlayTop;
				} else if (side != BlockSide.BOTTOM.code) {
					return this.textureOverlaySide;
				} else {
					return null;
				}
	    	} else {
	    		return null;
	    	}
		default: return null;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getNumberOfAdditionalRenderPasses(int metadata) {
		return 2;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeTexture(IBlockAccess blockAccess, int x, int y, int z, int metadata, int side, int pass) {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeInventoryBaseTexture(int side, int metadata) {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return RenderLayeredBlock.renderID;
	}
	
	@Override
    public boolean renderAsNormalBlock() {
        return false;
    }
	
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
		switch (metadata) {
		case DRY:
			return 2;
		default:
			return 0;
		}
    }
    
    @Override
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face) {
		switch (metadata) {
		case DRY:
			return 40;
		default:
			return 0;
		}
    }
    
    @Override
    public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side) {
		switch (metadata) {
		case DRY:
			return side == ForgeDirection.UP;
		default:
			return false;
		}
    }
    
    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	if (DynamicEarth.enableDeepPeat) {
    		switch (metadata) {
    		case ZERO_EIGHTHS:
    		case DRY:
    			break;
    		default:
        		return AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 1 - 0.125F, z + 1);
    		}
    	}
    	return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
	
    @Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	if (DynamicEarth.enableDeepPeat) {
    		switch (metadata) {
    		case ZERO_EIGHTHS:
    		case DRY:
    			break;
    		default:
		        entity.motionX *= 0.4D;
		        entity.motionZ *= 0.4D;
    		}
    	}
    }
    
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
		int metadata = world.getBlockMetadata(x, y, z);
		int id = world.getBlockId(x, y + 1, z);
		if (metadata == ZERO_EIGHTHS
		&& id != DynamicEarth.peatMoss.blockID
		&& id != DynamicEarth.peat.blockID) {
			world.setBlock(x, y, z, Block.dirt.blockID, 0, Helper.UPDATE_WITHOUT_NOTIFY_REMOTE);
		}
	}
	
	@Override
	public boolean onTilled(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
    	switch (metadata) {
    	case ZERO_EIGHTHS:
    		world.setBlock(x, y, z, Block.tilledField.blockID, 0, Helper.NOTIFY_AND_UPDATE_REMOTE);
    		return true;
    	case DRY: metadata = BlockDynamicFarmland.PEAT_DRY; break;
    	case ONE_EIGHTH: metadata = BlockDynamicFarmland.PEAT_1; break;
    	case TWO_EIGHTHS: metadata = BlockDynamicFarmland.PEAT_2; break;
    	case THREE_EIGHTHS: metadata = BlockDynamicFarmland.PEAT_3; break;
    	case FOUR_EIGHTHS: metadata = BlockDynamicFarmland.PEAT_4; break;
    	case FIVE_EIGHTHS: metadata = BlockDynamicFarmland.PEAT_5; break;
    	case SIX_EIGHTHS: metadata = BlockDynamicFarmland.PEAT_6; break;
    	case SEVEN_EIGHTHS: metadata = BlockDynamicFarmland.PEAT_7; break;
    	case WET: metadata = BlockDynamicFarmland.PEAT_WET; break;
    	}
    	world.setBlock(x, y, z, DynamicEarth.farmland.blockID, metadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
		return true;
	}
	
	@Override
	protected ItemStack getDryBlock(int metadata) {
		switch(metadata) {
		case WET: return dryPeat == null ? dryPeat = new ItemStack(DynamicEarth.peat.blockID, 1, DRY) : dryPeat;
		default: return null;
		}
	}
	
	@Override
	protected ItemStack getWetBlock(int metadata) {
		switch(metadata) {
		case DRY: return wetPeat == null ? wetPeat = new ItemStack(DynamicEarth.peat.blockID, 1, WET) : wetPeat;
		default: return null;
		}
	}
	
	@Override
	protected void becomeDry(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch(metadata) {
		case WET:
			metadata = DRY; break;
		default:
			return;
		}
		world.setBlock(x, y, z, DynamicEarth.peat.blockID, metadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
	}
	
	@Override
	protected void becomeWet(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch(metadata) {
		case DRY:
			metadata = WET; break;
		default:
			return;
		}
		world.setBlock(x, y, z, DynamicEarth.peat.blockID, metadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
	}
	
	@Override
    protected boolean isHydrated(World world, int x, int y, int z) {
		int id = world.getBlockId(x, y + 1, z);
    	return id == DynamicEarth.peat.blockID
    		|| (DynamicEarth.includeMud && id == DynamicEarth.mud.blockID)
    		|| id == DynamicEarth.peatMoss.blockID
    		|| super.isHydrated(world, x, y, z);
    }
	
	@Override
    protected Rate getDryRateForBiome(BiomeGenBase biome) {
    	float biomeHumidity = biome.rainfall;
    	if (biomeHumidity >= 0.9F) {
    		return Rate.NONE;
    	} else if (biomeHumidity >= 0.7F) {
    		return Rate.SLOW;
    	} else if (biomeHumidity >= 0.5F) {
    		return Rate.MEDIUM;
    	} else {
    		return Rate.QUICK;
    	}
    }
    
    @Override
    public int idDropped(int metadata, Random random, int par3) {
    	switch (metadata) {
    	case ZERO_EIGHTHS: return Block.dirt.blockID;
    	case DRY: return DynamicEarth.peat.blockID;
    	default: return DynamicEarth.peatClump.itemID;
    	}
    }
    
    @Override
    public int damageDropped(int metadata) {
    	switch (metadata) {
    	case DRY: return DRY;
    	default: return 0;
    	}
    }
    
    @Override
    public int quantityDropped(int metadata, int fortune, Random random) {
    	int min = 0, max = 4;
    	switch (metadata) {
    	case ZERO_EIGHTHS:
    	case DRY: return 1;
    	case ONE_EIGHTH: min = 0; max = 1; break;
    	case TWO_EIGHTHS: min = 1; max = 1; break;
    	case THREE_EIGHTHS: min = 1; max = 2; break;
    	case FOUR_EIGHTHS: min = 2; max = 2; break;
    	case FIVE_EIGHTHS: min = 2; max = 3; break;
    	case SIX_EIGHTHS: min = 3; max = 3; break;
    	case SEVEN_EIGHTHS: min = 3; max = 4; break;
    	case WET: min = 4; max = 4; break;
    	}
        return MathHelper.clamp_int((min + random.nextInt(1 + max - min)) + random.nextInt(fortune + 1), min, max);
    }
       
    @Override
    protected boolean canSilkHarvest() {
    	return true;
    }
    
    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
    	switch (metadata) {
    	case WET:
			return super.canSilkHarvest(world, player, x, y, z, metadata);
		default:
			return false;
    	}
	}
    
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> blocksDropped = new ArrayList<ItemStack>();
        int count = this.quantityDropped(metadata, fortune, world.rand);
        blocksDropped.add(new ItemStack(this.idDropped(metadata, world.rand, 0), count, this.damageDropped(metadata)));
        if (metadata != ZERO_EIGHTHS && metadata != DRY) {
        	blocksDropped.add(new ItemStack(DynamicEarth.dirtClod, 4 - count));
        }
        return blocksDropped;
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
		list.add(new ItemStack(blockId, 1, WET));
		list.add(new ItemStack(blockId, 1, DRY));
    }
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch (metadata) {
		case ZERO_EIGHTHS:
			return new ItemStack(Block.dirt);
		case WET:
		case DRY:
			return new ItemStack(this.idPicked(world, x, y, z), 1, metadata);
		default:
			return new ItemStack(this.idPicked(world, x, y, z), 1, WET);
		}
	}
	
	@Override
	public ItemStack getVanillaBlockReplacement(Chunk chunk, int x, int y, int z) {
		return dirt == null ? dirt = new ItemStack(Block.dirt.blockID, 1, 0) : dirt;
	}
	
	public static int getMetaForFractionFormed(int eighths) {
		switch (eighths) {
    	case 0: return ZERO_EIGHTHS;
    	case 1: return ONE_EIGHTH;
    	case 2: return TWO_EIGHTHS;
    	case 3: return THREE_EIGHTHS;
    	case 4: return FOUR_EIGHTHS;
    	case 5: return FIVE_EIGHTHS;
    	case 6: return SIX_EIGHTHS;
    	case 7: return SEVEN_EIGHTHS;
    	default:
    		if (eighths >= 8) {
    			return WET;
    		}
    		return ZERO_EIGHTHS;
		}
	}
	
	public static boolean isPartiallyFormed(int metadata) {
		switch (metadata) {
    	case ZERO_EIGHTHS:
    	case ONE_EIGHTH:
    	case TWO_EIGHTHS:
    	case THREE_EIGHTHS:
    	case FOUR_EIGHTHS:
    	case FIVE_EIGHTHS:
    	case SIX_EIGHTHS:
    	case SEVEN_EIGHTHS: return true;
    	default: return false;
		}
	}
}
