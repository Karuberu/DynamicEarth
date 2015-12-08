package karuberu.mods.mudmod.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.Reference;
import karuberu.mods.mudmod.client.ITextureOverlay;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockPeat extends BlockMudMod implements ITextureOverlay {
	
	public boolean doTextureOverlay = false;
	public final int
		TEXTURE_OVERLAY_TOP = MudMod.BlockTexture.PEATMOSSYTOPOVERLAY.ordinal(),
		TEXTURE_OVERLAY_SIDE = MudMod.BlockTexture.PEATMOSSYSIDEOVERLAY.ordinal();
	protected static final int
		META_FULL = 0,
		META_NONE = 1,
		META_1EIGHTH = 2,
		META_2EIGHTHS = 3,
		META_3EIGHTHS = 4,
		META_HALF = 5,
		META_5EIGHTHS = 6,
		META_6EIGHTHS = 7,
		META_7EIGHTHS = 8;
	
	public BlockPeat(int id, int textureIndex) {
		super(id, textureIndex, Material.ground);
		this.setHardness(0.4F);
        this.setBlockName("peat");
		this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setTextureFile(MudMod.terrainFile);
        this.setHydrateRadius(2);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int metadata = blockAccess.getBlockMetadata(x, y, z);
		if (side != Reference.SIDE_BOTTOM) {
	    	int topBlockID = blockAccess.getBlockId(x, y + 1, z);
	    	int topBlockMeta = blockAccess.getBlockMetadata(x, y + 1, z);
	    	if (topBlockID == MudMod.peatMoss.blockID && topBlockMeta > 3) {
	    		this.doTextureOverlay = true;
	    	} else {
	    		this.doTextureOverlay = false;
	    	}
		}
    	return getBlockTextureFromSideAndMetadata(side, metadata);
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
    	if (side == Reference.SIDE_TOP) {
    		switch (metadata) {
    		case META_NONE: return MudMod.BlockTexture.DIRT.ordinal();
    		default: return MudMod.BlockTexture.PEAT.ordinal();
    		}
    	} else if (side == Reference.SIDE_BOTTOM ) {
    		switch (metadata) {
    		case META_FULL: return MudMod.BlockTexture.PEAT.ordinal();
    		default: return MudMod.BlockTexture.DIRT.ordinal();
    		}
    	} else {
    		switch (metadata) {
    		case META_FULL: return MudMod.BlockTexture.PEAT.ordinal();
    		case META_NONE: return MudMod.BlockTexture.DIRT.ordinal();
    		case META_1EIGHTH: return MudMod.BlockTexture.PEATSIDE1.ordinal();
    		case META_2EIGHTHS: return MudMod.BlockTexture.PEATSIDE2.ordinal();
    		case META_3EIGHTHS: return MudMod.BlockTexture.PEATSIDE3.ordinal();
    		case META_HALF: return MudMod.BlockTexture.PEATSIDE4.ordinal();
    		case META_5EIGHTHS: return MudMod.BlockTexture.PEATSIDE5.ordinal();
    		case META_6EIGHTHS: return MudMod.BlockTexture.PEATSIDE6.ordinal();
    		case META_7EIGHTHS: return MudMod.BlockTexture.PEATSIDE7.ordinal();
    		default: return MudMod.BlockTexture.PEAT.ordinal();
    		}
    	}
    }

	@Override
	@SideOnly(Side.CLIENT)
	public int getOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		if (side == Reference.SIDE_TOP) {
			return TEXTURE_OVERLAY_TOP;
		} else if (side != Reference.SIDE_BOTTOM) {
			return TEXTURE_OVERLAY_SIDE;
		} else {
			return -1;
		}
	}
	@Override
	@SideOnly(Side.CLIENT)
	public boolean doTextureOverlay(int metadata) {
		return this.doTextureOverlay;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeBaseTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return willColorizeInventoryBaseTexture(side, blockAccess.getBlockMetadata(x, y, z));
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
			world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
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
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
    	switch(metadata) {
    	case META_NONE: return 0;
    	default: return 2;
    	}
    }
    
    @Override
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face) {
    	switch(metadata) {
    	case META_NONE: return 0;
    	default: return 10;
    	}
    }
    
    @Override
    public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side) {
    	switch(metadata) {
    	case META_NONE: return false;
    	default: return side == ForgeDirection.UP;
    	}
    }
    
    @Override
    public int idDropped(int metadata, Random random, int par3) {
    	switch (metadata) {
    	case META_NONE: return Block.dirt.blockID;
    	default: return MudMod.peatClump.shiftedIndex;
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
