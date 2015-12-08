package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockDirt_mod extends BlockMudMod
{
    protected BlockDirt_mod(int par1, int par2)
    {
        super(par1, par2, Material.ground);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setTickRandomly(true);
        this.setHydrateRadius(2, 4, 2);
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (this.isWaterNearby(world, x, y, z)) {
			this.becomeWet(world, x, y, z);
        }
    }
    @Override
    public void fillWithRain(World world, int x, int y, int z) {
		this.becomeWet(world, x, y, z);
    }
    
    public void becomeWet(World world, int x, int y, int z) {
    	world.setBlockWithNotify(x, y, z, MudMod.mud.blockID);
    }
}
