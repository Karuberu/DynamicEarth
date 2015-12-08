package karuberu.dynamicearth.blocks;

import java.util.List;
import java.util.Random;

import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDirtSlab extends BlockHalfSlab implements IGrassyBlock {

    public static final String[]
    	slabType = new String[] {"dirt"};
    public static final int
    	DIRT = 0;

    public BlockDirtSlab(int id, boolean par2) {
		super(id, par2, Material.ground);
		this.setHardness(0.5F);
		this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setTickRandomly(true);
        this.useNeighborBrightness[id] = true;
	}
    
	@Override
	public void registerIcons(IconRegister iconRegister) {}

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
    	int metadata = blockAccess.getBlockMetadata(x, y, z);
    	return this.getIcon(side, metadata);
    }
    
    @Override
    public Icon getIcon(int side, int metadata) {
        switch (MCHelper.getSlabMetadata(metadata)) {
        case DIRT: return Block.dirt.getBlockTextureFromSide(side);
        default: return super.getIcon(side, metadata);
        }
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	if (this.isOpaqueCube()) {
        	int metadata = world.getBlockMetadata(x, y, z);
        	switch(MCHelper.getSlabMetadata(metadata)) {
        	case DIRT:
        		world.setBlock(x, y, z, Block.dirt.blockID, 0, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
        		break;
        	}
    	}
    }
    
    public boolean isLightSufficient(World world, int x, int y, int z) {
    	return world.getBlockLightValue(x, y + 1, z) >= 4
        && world.getBlockLightOpacity(x, y + 1, z) <= 2;
    }
	
	@Override
	public void tryToGrow(World world, int x, int y, int z, EnumGrassType type) {
		if (this.isLightSufficient(world, x, y, z)) {
			switch (type) {
			case GRASS:
				world.setBlock(x, y, z, DynamicEarth.grassSlab.blockID, MCHelper.convertSlabMetadata(world.getBlockMetadata(x, y, z), BlockGrassSlab.GRASS), MCHelper.NOTIFY_AND_UPDATE_REMOTE);
				break;
			case MYCELIUM:
				world.setBlock(x, y, z, DynamicEarth.grassSlab.blockID, MCHelper.convertSlabMetadata(world.getBlockMetadata(x, y, z), BlockGrassSlab.MYCELIUM), MCHelper.NOTIFY_AND_UPDATE_REMOTE);
				break;
			default:
				return;
			}
		}
	}
	
	@Override
	public ItemStack getBlockForType(World world, int x, int y, int z, EnumGrassType type) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch (type) {
		case DIRT:
			return new ItemStack(DynamicEarth.dirtSlab, 1, MCHelper.convertSlabMetadata(metadata, BlockDirtSlab.DIRT));
		case GRASS:
			return new ItemStack(DynamicEarth.grassSlab, 1, MCHelper.convertSlabMetadata(metadata, BlockGrassSlab.GRASS));
		case MYCELIUM:
			return new ItemStack(DynamicEarth.grassSlab, 1, MCHelper.convertSlabMetadata(metadata, BlockGrassSlab.MYCELIUM));
		}
		return null;
	}

	@Override
	public EnumGrassType getType(World world, int x, int y, int z) {
		return EnumGrassType.DIRT;
	}
	
	@Override
	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (MCHelper.isTopSlab(metadata)) {
			EnumPlantType type = plant.getPlantType(world, x, y + 1, z);
			if (type == EnumPlantType.Plains) {
				return true;
			} else if (type == EnumPlantType.Beach) {
                return (world.getBlockMaterial(x - 1, y, z    ) == Material.water ||
                        world.getBlockMaterial(x + 1, y, z    ) == Material.water ||
                        world.getBlockMaterial(x,     y, z - 1) == Material.water ||
                        world.getBlockMaterial(x,     y, z + 1) == Material.water);
			}
		}
		return super.canSustainPlant(world, x, y, z, direction, plant);
	}
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
    	return DynamicEarth.dirtSlab.blockID;
    }
    
    @Override
    protected ItemStack createStackedBlock(int metadata) {
        return new ItemStack(DynamicEarth.dirtSlab.blockID, 2, metadata & 7);
    }
    
	@Override
	public String getFullSlabName(int metadata) {
        if (metadata < 0 || metadata >= slabType.length) {
            metadata = 0;
        }
        return super.getUnlocalizedName() + "." + slabType[metadata];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
        if (blockId != DynamicEarth.dirtDoubleSlab.blockID) {
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
    
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(this.idPicked(world, x, y, z), 1, world.getBlockMetadata(x, y, z));
	}
}
