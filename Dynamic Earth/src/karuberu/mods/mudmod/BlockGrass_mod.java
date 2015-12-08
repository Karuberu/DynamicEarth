package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.BlockGrass;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockGrass_mod extends BlockGrass {
	
	protected BlockGrass_mod(int par1) {
		super(par1);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            if (!this.isLightSufficient(world, x, y, z)) {
	        	this.decay(world, x, y, z);
            } else if (world.getBlockLightValue(x, y + 1, z) >= 9) {
                this.tryToSpread(world, x, y, z, random);
            }
        }
	}
    
    private boolean blockIsStandingWater(World world, int x, int y, int z) {
    	return world.getBlockMaterial(x, y, z) == Material.water && (world.getBlockMetadata(x, y, z) == 0 || world.getBlockMetadata(x, y, z) >= 8);
    }
    
    protected boolean isLightSufficient(World world, int x, int y, int z) {
    	return world.getBlockLightValue(x, y + 1, z) >= 4 && world.getBlockLightOpacity(x, y + 1, z) <= 2;
    }
    
    protected void tryToSpread(World world, int x, int y, int z, Random random) {
        for (int i = 0; i < 4; ++i) {
            int randX = x + random.nextInt(3) - 1;
            int randY = y + random.nextInt(5) - 3;
            int randZ = z + random.nextInt(3) - 1;
            int blockId = world.getBlockId(randX, randY, randZ);
            
			if (this.willSpreadToBlock(world, randX, randY, randZ, random)) {
				world.setBlockWithNotify(randX, randY, randZ, this.blockID);
			}
        }
    }
    
    protected void decay(World world, int x, int y, int z) {
    	if (world.getBiomeGenForCoords(x, z) == BiomeGenBase.swampland
    	&& blockIsStandingWater(world, x, y + 1, z)) {
    		world.setBlockWithNotify(x, y, z, MudMod.blockPeat.blockID);
    	} else {
    		world.setBlockWithNotify(x, y, z, this.getDecayedBlock());
    	}
    }
    
    protected int getDecayedBlock() {
    	return Block.dirt.blockID;
    }
    
    protected boolean willSpreadToBlock(World world, int x, int y, int z, Random random) {
    	int blockId = world.getBlockId(x, y, z);
    	if (blockId == this.getDecayedBlock()
    	|| (blockId == MudMod.mud.blockID && random.nextInt(4) == 0)) {
    		if (this.isLightSufficient(world, x, y, z)) {
    			return true;
    		} else {
    			return world.getBiomeGenForCoords(x, z) == BiomeGenBase.swampland
	    		&& world.getBlockLightValue(x, y + 1, z) >= 6
	    		&& world.getBlockLightOpacity(x, y + 1, z) <= 3;
    		}
    	}
    	return false;
    }
}
