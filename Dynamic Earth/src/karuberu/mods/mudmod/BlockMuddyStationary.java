package karuberu.mods.mudmod;

import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.Block;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.BlockStationary;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockMuddyStationary extends BlockFluid {
	
	protected BlockMuddyStationary(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setHardness(100.0F);
		this.setLightOpacity(4);
		this.setBlockName("muddyWaterStill");
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return 0x75563e;
    }
	
    @Override
    public String getTextureFile() {
        return MudMod.terrainFile;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
    	super.updateTick(world, x, y, z, random);
//    	this.clarify(world, x, y, z);
    }
    
    public void clarify(World world, int x, int y, int z) {
		world.setBlockAndMetadataWithNotify(x, y, z, Block.waterStill.blockID, world.getBlockMetadata(x, y, z));
    }
}
