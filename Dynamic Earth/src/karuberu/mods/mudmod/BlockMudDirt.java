package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockMudDirt extends BlockMudMod
{
    protected BlockMudDirt(int par1, int par2, Material material) {
        this(par1, par2);
    }
    
    protected BlockMudDirt(int par1, int par2) {
        super(par1, par2, Material.ground);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setTickRandomly(true);
        this.setHydrateRadius(2, 0, 4, 2);
    }
    
    @Override
    public boolean canHydrate(int metadata) {
    	return true;
    }
    
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
    	if (BlockPermafrost.canForm(world, x, y, z)) {
    		this.freeze(world, x, y, z);
    	}
    	super.updateTick(world, x, y, z, random);
    }
	
    @Override
    protected boolean willHydrate(World world, int x, int y, int z) {
    	if (this.canHydrate()) {
	    	if (this.isBeingHeated(world, x, y, z)) {
	    		return false;
	    	} else {
	    		int distance = this.getHydrationDistance(world, x, y, z);
	    		if (distance == 0 || distance == 1) {
		    		int rate = this.getHydrationRate(world, x, y, z);
		    		return rate <= 0 ? false : world.rand.nextInt(rate) == 0;
	    		}
	    	}
    	}
    	return false;
    }
    
    @Override
    protected void becomeWet(World world, int x, int y, int z) {
    	world.setBlockWithNotify(x, y, z, MudMod.mud.blockID);
    }
    
    protected void freeze(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
		if (metadata >= 5) {
			world.setBlockWithNotify(x, y, z, MudMod.permafrost.blockID);
    	} else {
    		world.setBlockAndMetadata(x, y, z, this.blockID, metadata+1);
    	}
	}
}
