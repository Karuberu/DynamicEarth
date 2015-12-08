package karuberu.mods.mudmod.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.blocks.BlockMudMod.Rate;
import karuberu.mods.mudmod.client.ITextureOverlay;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;

public class BlockPeat extends BlockMudMod implements ITextureOverlay {
	
	public static final int
		META_FULL = 0,
		META_NONE = 1,
		META_1EIGHTH = 2,
		META_2EIGHTHS = 3,
		META_3EIGHTHS = 4,
		META_HALF = 5,
		META_5EIGHTHS = 6,
		META_6EIGHTHS = 7,
		META_7EIGHTHS = 8;
	private static final int
		texture = MudMod.BlockTexture.PEAT.ordinal(),
		textureSide1 = MudMod.BlockTexture.PEATSIDE1.ordinal(),
		textureSide2 = MudMod.BlockTexture.PEATSIDE2.ordinal(),
		textureSide3 = MudMod.BlockTexture.PEATSIDE3.ordinal(),
		textureSide4 = MudMod.BlockTexture.PEATSIDE4.ordinal(),
		textureSide5 = MudMod.BlockTexture.PEATSIDE5.ordinal(),
		textureSide6 = MudMod.BlockTexture.PEATSIDE6.ordinal(),
		textureSide7 = MudMod.BlockTexture.PEATSIDE7.ordinal(),
		textureOverlayTop = MudMod.BlockTexture.PEATMOSSYTOPOVERLAY.ordinal(),
		textureOverlaySide = MudMod.BlockTexture.PEATMOSSYSIDEOVERLAY.ordinal(),
		textureDirt = MudMod.BlockTexture.DIRT.ordinal();
	
	public BlockPeat(int id, int texture) {
		super(id, texture, Material.ground);
		this.setHardness(0.4F);
        this.setBlockName("peat");
		this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHydrateRadius(2);
        this.setTextureFile(MudMod.terrainFile);
	}

    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	if (MudMod.enableDeepPeat && metadata != META_NONE) {
    		return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x, y, z, x + 1, y + 1 - 0.125F, z + 1);
    	}
    	return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
    
    @Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        entity.motionX *= 0.4D;
        entity.motionZ *= 0.4D;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int metadata = blockAccess.getBlockMetadata(x, y, z);
    	return getBlockTextureFromSideAndMetadata(side, metadata);
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
    	if (side == MCHelper.SIDE_TOP) {
    		switch (metadata) {
    		case META_NONE: return Block.dirt.getBlockTextureFromSide(side);
    		default: return this.texture;
    		}
    	} else {
    		switch (metadata) {
    		case META_FULL: return this.texture;
    		default: return Block.dirt.getBlockTextureFromSide(side);
    		}
    	}
    }

	@Override
	@SideOnly(Side.CLIENT)
	public int getOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int side, int pass) {
		switch(pass) {
		case 1:
			if (side != MCHelper.SIDE_TOP
			&& side != MCHelper.SIDE_BOTTOM) {
				int metadata = blockAccess.getBlockMetadata(x, y, z);
	    		switch (metadata) {
	    		case META_1EIGHTH: return this.textureSide1;
	    		case META_2EIGHTHS: return this.textureSide2;
	    		case META_3EIGHTHS: return this.textureSide3;
	    		case META_HALF: return this.textureSide4;
	    		case META_5EIGHTHS: return this.textureSide5;
	    		case META_6EIGHTHS: return this.textureSide6;
	    		case META_7EIGHTHS: return this.textureSide7;
	    		default: return -1;
	    		}
			} else {
				return -1;
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
					return -1;
				}
	    	} else {
	    		return -1;
	    	}
		default: return -1;
		}
	}
	@Override
	@SideOnly(Side.CLIENT)
	public int getNumberOfPasses(int metadata) {
		return 2;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeTexture(IBlockAccess blockAccess, int x, int y, int z, int side, int pass) {
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
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
		int metadata = world.getBlockMetadata(x, y, z);
		int id = world.getBlockId(x, y + 1, z);
		if (metadata == META_NONE
		&& id != MudMod.peatMoss.blockID
		&& id != MudMod.peat.blockID) {
			world.setBlock(x, y, z, Block.dirt.blockID);
		}
	}
	
	@Override
	protected int getDryBlock(int metadata) {
		switch(metadata) {
		case META_FULL: return MudMod.peatDry.blockID;
		default: return -1;
		}
	}
	
	@Override
    protected boolean isHydrated(World world, int x, int y, int z) {
		int id = world.getBlockId(x, y+1, z);
    	return id == MudMod.peat.blockID
    		|| id == MudMod.mud.blockID
    		|| id == MudMod.peatMoss.blockID
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
    	case META_NONE: return Block.dirt.blockID;
    	default: return MudMod.peatClump.itemID;
    	}
    }
    
    @Override
    public int damageDropped(int metadata) {
    	return 0;
    }
    
    @Override
    public int quantityDropped(int metadata, int fortune, Random random) {
    	int min = 0, max = 4;
    	switch (metadata) {
    	case META_NONE: return 1;
    	case META_1EIGHTH: min = 0; max = 1; break;
    	case META_2EIGHTHS: min = 1; max = 1; break;
    	case META_3EIGHTHS: min = 1; max = 2; break;
    	case META_HALF: min = 2; max = 2; break;
    	case META_5EIGHTHS: min = 2; max = 3; break;
    	case META_6EIGHTHS: min = 3; max = 3; break;
    	case META_7EIGHTHS: min = 3; max = 4; break;
    	case META_FULL: min = 4; max = 4; break;
    	}
        return MathHelper.clamp_int((min + random.nextInt(1 + max - min)) + random.nextInt(fortune + 1), min, max);
    }
        
    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
		if (metadata == META_FULL) {
			return super.canSilkHarvest(world, player, x, y, z, metadata);
		} else {
			return false;
		}
	}
    
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> blocksDropped = new ArrayList<ItemStack>();
        int count = this.quantityDropped(metadata, fortune, world.rand);
        for(int i = 0; i < count; i++) {
            int id = this.idDropped(metadata, world.rand, 0);
            if (id > 0) {
                blocksDropped.add(new ItemStack(id, 1, this.damageDropped(metadata)));
            }
        }
        if (metadata != META_NONE) {
        	// Add mud blobs for however many peat clumps didn't drop.
            for(int i = 0; i < 4 - count; i++) {
            	blocksDropped.add(new ItemStack(MudMod.mudBlob));
            }
        }
        return blocksDropped;
    }
}
