package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockStationary;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockMuddyStationary extends BlockStationary {

	protected BlockMuddyStationary(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setTickRandomly(true);
	}
	
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
	    return 0xbd9e82;
	}
	/**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
    	world.setBlock(x, y, z, Block.waterStill.blockID);
    }
}
