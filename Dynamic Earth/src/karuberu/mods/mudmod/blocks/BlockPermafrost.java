package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.core.MCHelper;
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

	public BlockPermafrost(int id,int texture) {
		super(id, texture, Material.rock);
		this.setHardness(1.5F);
		this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("permafrost");
        this.setTickRandomly(true);
        this.slipperiness = 0.93F;
        this.setTextureFile(MudMod.terrainFile);
	}
	
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
	    if (BlockPermafrost.willMelt(world, x, y, z)) {
	    	world.setBlockAndMetadataWithNotify(x, y, z, Block.dirt.blockID, 0);
	    }
    }
    
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
    	return true;
    }
    
    public static boolean canForm(World world, int x, int y, int z) {
    	if (!MudMod.restoreDirtOnChunkLoad
    	&& !BlockPermafrost.willMelt(world, x, y, z)) {
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
    
    public static boolean willMelt(World world, int x, int y, int z) {
    	return world.getBlockLightValue(x, y + 1, z) > maximumLightLevel
    		   && world.getBlockId(x, y + 1, z) != Block.snow.blockID;
    }
}
