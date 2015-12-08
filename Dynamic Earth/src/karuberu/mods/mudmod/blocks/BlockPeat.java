package karuberu.mods.mudmod.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.ITextureOverlay;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
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
import net.minecraftforge.common.ForgeDirection;

public class BlockPeat extends BlockMudMod implements ITextureOverlay, ITillable {
	
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
		wetPeat,
		dryPeat;
	
	public BlockPeat(int id) {
		super(id, Material.ground);
		this.setHardness(0.4F);
        this.setUnlocalizedName("peat");
		this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHydrateRadius(2);
        this.setTickRandomly(true);
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = this.texture = TextureManager.instance().getBlockTexture(Texture.PEAT);
		this.textureSide1 = TextureManager.instance().getBlockTexture(Texture.PEATSIDE1);
		this.textureSide2 = TextureManager.instance().getBlockTexture(Texture.PEATSIDE2);
		this.textureSide3 = TextureManager.instance().getBlockTexture(Texture.PEATSIDE3);
		this.textureSide4 = TextureManager.instance().getBlockTexture(Texture.PEATSIDE4);
		this.textureSide5 = TextureManager.instance().getBlockTexture(Texture.PEATSIDE5);
		this.textureSide6 = TextureManager.instance().getBlockTexture(Texture.PEATSIDE6);
		this.textureSide7 = TextureManager.instance().getBlockTexture(Texture.PEATSIDE7);
		this.textureOverlayTop = TextureManager.instance().getBlockTexture(Texture.PEATMOSSYOVERLAYTOP);
		this.textureOverlaySide = TextureManager.instance().getBlockTexture(Texture.PEATMOSSYOVERLAYSIDE);
		this.textureDry = TextureManager.instance().getBlockTexture(Texture.PEATDRY);
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int metadata = blockAccess.getBlockMetadata(x, y, z);
    	return this.getIcon(side, metadata);
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata) {
    	if (side == MCHelper.SIDE_TOP) {
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
			if (side != MCHelper.SIDE_TOP
			&& side != MCHelper.SIDE_BOTTOM) {
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
	    	if (topBlockID == MudMod.peatMoss.blockID
	    	&& BlockPeatMoss.isFullGrown(topBlockMeta)) {
				if (side == MCHelper.SIDE_TOP) {
					return this.textureOverlayTop;
				} else if (side != MCHelper.SIDE_BOTTOM) {
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
	public int getNumberOfPasses(int metadata) {
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
		return MudMod.overlayBlockRenderID;
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
    	if (MudMod.enableDeepPeat) {
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
    	if (MudMod.enableDeepPeat) {
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
		&& id != MudMod.peatMoss.blockID
		&& id != MudMod.peat.blockID) {
			world.setBlock(x, y, z, Block.dirt.blockID, 0, MCHelper.UPDATE_WITHOUT_NOTIFY_REMOTE);
		}
	}
	
	@Override
	public boolean onTilled(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
    	switch (metadata) {
    	case ZERO_EIGHTHS:
    		world.setBlock(x, y, z, Block.tilledField.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
    		return true;
    	case DRY: metadata = BlockMudModFarmland.PEAT_DRY; break;
    	case ONE_EIGHTH: metadata = BlockMudModFarmland.PEAT_1; break;
    	case TWO_EIGHTHS: metadata = BlockMudModFarmland.PEAT_2; break;
    	case THREE_EIGHTHS: metadata = BlockMudModFarmland.PEAT_3; break;
    	case FOUR_EIGHTHS: metadata = BlockMudModFarmland.PEAT_4; break;
    	case FIVE_EIGHTHS: metadata = BlockMudModFarmland.PEAT_5; break;
    	case SIX_EIGHTHS: metadata = BlockMudModFarmland.PEAT_6; break;
    	case SEVEN_EIGHTHS: metadata = BlockMudModFarmland.PEAT_7; break;
    	case WET: metadata = BlockMudModFarmland.PEAT_WET; break;
    	}
    	world.setBlock(x, y, z, MudMod.farmland.blockID, metadata, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
		return true;
	}
	
	@Override
	protected ItemStack getDryBlock(int metadata) {
		switch(metadata) {
		case WET: return dryPeat == null ? dryPeat = new ItemStack(MudMod.peat.blockID, 1, DRY) : dryPeat;
		default: return null;
		}
	}
	
	@Override
	protected ItemStack getWetBlock(int metadata) {
		switch(metadata) {
		case DRY: return wetPeat == null ? wetPeat = new ItemStack(MudMod.peat.blockID, 1, WET) : wetPeat;
		default: return null;
		}
	}
	
	protected void becomeDry(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch(metadata) {
		case WET:				metadata = DRY; break;
		default: return;
		}
		world.setBlock(x, y, z, MudMod.peat.blockID, metadata, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
	}
	
	protected void becomeWet(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch(metadata) {
		case DRY:				metadata = WET; break;
		default: return;
		}
		world.setBlock(x, y, z, MudMod.peat.blockID, metadata, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
	}
	
	@Override
    protected boolean isHydrated(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch (metadata) {
		default:
			int id = world.getBlockId(x, y + 1, z);
	    	return id == MudMod.peat.blockID
	    		|| id == MudMod.mud.blockID
	    		|| id == MudMod.peatMoss.blockID
	    		|| super.isHydrated(world, x, y, z);
		}
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
    	case DRY: return MudMod.peat.blockID;
    	default: return MudMod.peatClump.itemID;
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
        	blocksDropped.add(new ItemStack(MudMod.dirtClod, 4 - count));
        }
        return blocksDropped;
    }
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
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
