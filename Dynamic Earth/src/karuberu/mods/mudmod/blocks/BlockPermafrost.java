package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.mods.mudmod.MudMod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class BlockPermafrost extends Block {
	public static int maximumLightLevel = 11;
	public static float maximumTemperature = 0.15F;

	public BlockPermafrost(int id, int j) {
		super(id, j, Material.rock);
		this.setHardness(1.5F);
		this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("permafrost");
        this.setTickRandomly(true);
        this.setTextureFile(MudMod.terrainFile);
        this.slipperiness = 0.93F;
	}
	
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
	    if (world.getFullBlockLightValue(x, y+1, z) > maximumLightLevel) {
	    	world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
	    }
    }
    
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
    	return true;
    }
    
    public static boolean canForm(World world, int x, int y, int z) {
    	if (world.getFullBlockLightValue(x, y+1, z) <= maximumLightLevel && !MudMod.restoreDirtOnChunkLoad) {
            if (world.getBiomeGenForCoords(x, z).getFloatTemperature() <= maximumTemperature) {
    			return true;
    		} else {
	        	int[][] surroundingBlocks = new int[][] {
	        		{x-1, y, z},
	        		{x+1, y, z},
	        		{x, y-1, z},
	        		{x, y+1, z},
	        		{x, y, z-1},
	        		{x, y, z+1}
	    		};
	        	for (int i = 0; i < surroundingBlocks.length; i++) {
	        		int xi = surroundingBlocks[i][0],
	        			yi = surroundingBlocks[i][1],
	        			zi = surroundingBlocks[i][2];
	        		Material material = world.getBlockMaterial(xi, yi, zi);
	        		if (material == Material.ice) {
	        			return true;
	        		}
	        	}
    		}
    	}
    	return false;
    }
}
