package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.mods.mudmod.MudMod;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

public class BlockMudDirt extends BlockMudMod {
	
	public BlockMudDirt(int id, int textureIndex, Material material) {
		this(id, textureIndex);
	}
	
    public BlockMudDirt(int id, int textureIndex) {
        super(id, textureIndex, Material.ground);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setTickRandomly(true);
        this.setHydrateRadius(1, 0, 1, 1);
    }
    
    @Override
    public int getWetBlock(int metadata) {
    	if (MudMod.mud != null) {
    		return MudMod.mud.blockID;
    	} else {
    		return -1;
    	}
    }
    
    @Override
    public boolean canHydrate(int metadata) {
    	return super.canHydrate(metadata) && !MudMod.restoreDirtOnChunkLoad;
    }
    
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
    	if (MudMod.includePermafrost && BlockPermafrost.canForm(world, x, y, z)) {
    		this.freeze(world, x, y, z);
    	}
    	super.updateTick(world, x, y, z, random);
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
