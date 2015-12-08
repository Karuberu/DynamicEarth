package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockPermafrost extends Block {

	public BlockPermafrost(int i, int j) {
		super(i, j, Material.rock);
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
	    if (world.getSavedLightValue(EnumSkyBlock.Block, x, y+1, z) > 11) {
	    	world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
	    }
    }
    
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
    	return true;
    }
    
    public static boolean canForm(World world, int x, int y, int z) {
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        float temperature = biome.getFloatTemperature();
        if (temperature <= 0.15F && world.getSavedLightValue(EnumSkyBlock.Block, x, y+1, z) <= 11) {
        	for (int i = 1; i <= 3; i++) {
        		if (world.canBlockSeeTheSky(x, y+i, z)) {
        			return true;
        		}
        	}
        	return false;
        } else {
        	return false;
        }
    }
}
