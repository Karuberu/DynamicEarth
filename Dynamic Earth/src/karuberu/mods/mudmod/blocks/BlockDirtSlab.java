package karuberu.mods.mudmod.blocks;

import java.util.List;
import java.util.Random;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDirtSlab extends BlockHalfSlab {

    public static final String[]
    	slabType = new String[] {"dirt"};
    public static final int
    	DIRT = 0;
    private static final int
    	TEXTURE_DIRT = 2;

    public BlockDirtSlab(int id, boolean par2) {
		super(id, par2, Material.ground);
		this.setHardness(0.5F);
		this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setTickRandomly(true);
        this.useNeighborBrightness[id] = true;
	}
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	if (this.isOpaqueCube()) {
        	int metadata = world.getBlockMetadata(x, y, z);
        	switch(metadata & 7) {
        	case DIRT:
        		world.setBlock(x, y, z, Block.dirt.blockID);
        		break;
        	}
    	}
    }

    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch(metadata & 7) {
    	case DIRT:
    		this.tryToGrowGrass(world, x, y, z, random);
    		break;
    	}
    }
    
	protected void tryToGrowGrass(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
        	int metadata = world.getBlockMetadata(x, y, z);
        	if (world.getBlockLightValue(x, y + 1, z) >= 4 && world.getBlockLightOpacity(x, y + 1, z) <= 2) {
                for (int i = 0; i < 4; i++) {
                    int xi = x + random.nextInt(3) - 1,
                    	yi = y + random.nextInt(5) - 1,
                    	zi = z + random.nextInt(3) - 1;
                    int blockId = world.getBlockId(xi, yi, zi);
                    if (blockId == Block.grass.blockID
                    && world.getBlockLightValue(xi, yi + 1, zi) >= 9
                    && world.getBlockLightOpacity(xi, yi + 1, zi) <= 2) {
                		int slabType = metadata - DIRT;
                        world.setBlockAndMetadataWithNotify(x, y, z, MudMod.grassSlab.blockID, BlockGrassSlab.GRASS + slabType);
                    }
                }
            }
        }
    }
	
    @Override
    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
    	int metadata = blockAccess.getBlockMetadata(x, y, z);
    	return this.getBlockTextureFromSideAndMetadata(side, metadata);
    }
    
    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        switch (metadata & 7) {
        case DIRT: return TEXTURE_DIRT;
        default: return TEXTURE_DIRT;
        }
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
    	return MudMod.dirtSlab.blockID;
    }
    
    @Override
    protected ItemStack createStackedBlock(int metadata) {
        return new ItemStack(MudMod.dirtSlab.blockID, 2, metadata & 7);
    }
    
	@Override
	public String getFullSlabName(int metadata) {
        if (metadata < 0 || metadata >= slabType.length) {
            metadata = 0;
        }
        return super.getBlockName() + "." + slabType[metadata];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
        if (blockId != MudMod.dirtDoubleSlab.blockID) {
            int numSubBlocks = this.slabType.length;
            for (int i = 0; i < numSubBlocks; ++i) {
                list.add(new ItemStack(blockId, 1, i));
            }
        }
    }
	
    @SideOnly(Side.CLIENT)
    @Override
    public int idPicked(World world, int x, int y, int z) {
    	return this.blockID;
    }
}
