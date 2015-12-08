package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockFlowing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockMuddyFlowing extends BlockFlowing {

	protected BlockMuddyFlowing(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
	    return 0xbd9e82;
	}
	
	/**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
    	world.setBlock(x, y, z, Block.waterMoving.blockID);
    }
}
