package karuberu.dynamicearth.blocks;

import java.util.Random;
import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockPermafrost extends Block {
	public static final int
		META_PERMAFROST = 0,
		META_NORMAL = 0,
		META_CHILLED = 10,
		META_FREEZING = 11,
		META_FROZEN = 12;
	public static int
		maximumLightLevel = 11;
	public static float
		maximumTemperature = 0.15F;
    public static CreativeTabs
		creativeTab = CreativeTabs.tabBlock;
    
	public BlockPermafrost(int id) {
		super(id, Material.rock);
		this.setHardness(1.5F);
		this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(creativeTab);
		this.setUnlocalizedName("permafrost");
        this.setTickRandomly(true);
        this.slipperiness = 0.93F;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(BlockTexture.PERMAFROST.getIconPath());
	}
	
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
	    if (BlockPermafrost.willThaw(world, x, y, z)) {
	    	world.setBlock(x, y, z, Block.dirt.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
	    }
    }
    
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
    	return true;
    }
    
    public static boolean canForm(World world, int x, int y, int z) {
    	if (!DynamicEarth.restoreDirtOnChunkLoad
    	&& !BlockPermafrost.willThaw(world, x, y, z)) {
            if (world.getBiomeGenForCoords(x, z).getFloatTemperature() <= maximumTemperature) {
            	return true;
            } else {
	        	int[][] surroundingBlocks = MCHelper.getSurroundingBlocks(x, y, z);
	        	for (int i = 0; i < surroundingBlocks.length; i++) {
	        		int xi = surroundingBlocks[i][0],
	        			yi = surroundingBlocks[i][1],
	        			zi = surroundingBlocks[i][2];
	        		Material material = world.getBlockMaterial(xi, yi, zi);
	        		if (material == Material.ice
	        		|| material == Material.craftedSnow) {
	        			return true;
	        		}
	        	}
    		}
    	}
    	return false;
    }
    
    public static void tryToForm(World world, int x, int y, int z) {
    	if (world.getBlockId(x, y, z) == Block.dirt.blockID) {
        	if (BlockPermafrost.canForm(world, x, y, z)) {
            	int metadata = world.getBlockMetadata(x, y, z);
            	if (metadata == META_FROZEN) {
            		world.setBlock(x, y, z, DynamicEarth.permafrost.blockID, META_PERMAFROST, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
            	} else {
	        		switch (metadata) {
	        		case META_NORMAL: metadata = META_CHILLED; break;
	        		case META_CHILLED: metadata = META_FREEZING; break;
	        		case META_FREEZING: metadata = META_FROZEN; break;
	        		default: metadata = META_CHILLED; break;
	        		}
        			world.setBlockMetadataWithNotify(x, y, z, metadata, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
        		}
        	}
    	}
    }
    
    public static boolean willThaw(World world, int x, int y, int z) {
    	return world.getBlockLightValue(x, y + 1, z) > maximumLightLevel
    		   && world.getBlockId(x, y + 1, z) != Block.snow.blockID;
    }
}
