package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockAdobeWet extends BlockMudMod {
	
	public BlockAdobeWet(int i, int j) {
		super(i, j, Material.ground);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHydrateRadius(2, 1, 2);
	}
	
    @Override
    public String getTextureFile() {
    	return MudMod.terrainFile;
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int i, int j, int k, Random random) {
        if (!this.isWaterNearby(world, i, j, k)
        	&& (!this.isGettingRainedOn(world, i, j, k))
		) {
			world.setBlockWithNotify(i, j, k, MudMod.adobe.blockID);
        }
    }
}
