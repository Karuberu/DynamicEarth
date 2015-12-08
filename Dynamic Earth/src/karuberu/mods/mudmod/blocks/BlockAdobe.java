package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.entity.EntityClayGolem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

public class BlockAdobe extends Block {

	public BlockAdobe(int i, int j) {
		super(i, j, Material.rock);
        this.setHardness(1.5F);
        this.setResistance(5.0F);
        this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockName("adobeDry");
        this.setTextureFile(MudMod.terrainFile);
	}
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
    	if (MudMod.includeClayGolems
    	&& (neighborBlockID == Block.pumpkin.blockID
    	|| neighborBlockID == Block.pumpkinLantern.blockID)) {
    		this.tryToSpawnGolem(world, x, y, z);
    	}
    }
    
    private boolean tryToSpawnGolem(World world, int x, int y, int z) {
    	int topBlockID = world.getBlockId(x, y + 1, z);
    	int bottomBlockID = world.getBlockId(x, y - 1, z);
    	if ((Block.blocksList[topBlockID] instanceof BlockPumpkin)
    	&& (bottomBlockID == this.blockID)) {
	    	boolean Xaligned = world.getBlockId(x - 1, y, z) == this.blockID && world.getBlockId(x + 1, y, z) == this.blockID;
	    	boolean Zaligned = world.getBlockId(x, y, z - 1) == this.blockID && world.getBlockId(x, y, z + 1) == this.blockID;
	    	if (Xaligned || Zaligned) {
	    		world.setBlock(x, y + 1, z, 0);
	    		world.setBlock(x, y, z, 0);
	    		world.setBlock(x, y - 1, z, 0);
	    		if (Xaligned) {
	    			world.setBlock(x - 1, y, z, 0);
	    			world.setBlock(x + 1, y, z, 0);
	    		} else {
	    			world.setBlock(x, y, z - 1, 0);
	    			world.setBlock(x, y, z + 1, 0);
	    		}
                EntityClayGolem golem = new EntityClayGolem(world);
                golem.setLocationAndAngles((double)x + 0.5D, (double)y - 0.95D, (double)z + 0.5D, 0.0F, 0.0F);
                world.spawnEntityInWorld(golem);
                for (int i = 0; i < 120; ++i) {
                    world.spawnParticle("snowballpoof", (double)x + world.rand.nextDouble(), (double)(y - 2) + world.rand.nextDouble() * 3.9D, (double)z + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                }
	    		world.notifyBlockChange(x, y + 1, z, 0);
	    		world.notifyBlockChange(x, y, z, 0);
	    		world.notifyBlockChange(x, y - 1, z, 0);
	    		if (Xaligned) {
	    			world.notifyBlockChange(x - 1, y, z, 0);
	    			world.notifyBlockChange(x + 1, y, z, 0);
	    		} else {
	    			world.notifyBlockChange(x, y, z - 1, 0);
	    			world.notifyBlockChange(x, y, z + 1, 0);
	    		}
	    	}
    	}
    	return false;
    }
    
    @Override
	public int idDropped(int i, Random random, int j) {
        return MudMod.adobeDust.shiftedIndex;
    }
    
    @Override
	public int quantityDropped(Random random) {
        return 1 + random.nextInt(3);
    }
}
