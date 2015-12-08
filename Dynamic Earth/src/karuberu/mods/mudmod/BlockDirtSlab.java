package karuberu.mods.mudmod;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.Block;
import net.minecraft.src.BlockHalfSlab;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;

public class BlockDirtSlab extends BlockHalfSlab {

    public static final String[] slabType = new String[] {"dirt"};

    public BlockDirtSlab(int par1, boolean par2) {
		super(par1, par2, Material.ground);
		this.setHardness(0.5F);
		this.setStepSound(soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("dirtSlab");
        this.setLightOpacity(0);
	}
    
    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        switch (metadata & 7) {
        	case 0: return 2;
            default:
                return 2;
        }
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
    	return MudMod.dirtSlab.blockID;
    }
    
    @Override
    protected ItemStack createStackedBlock(int i) {
        return new ItemStack(MudMod.dirtSlab.blockID, 2, i & 7);
    }
    
	@Override
	public String getFullSlabName(int metadata) {
        if (metadata < 0 || metadata >= slabType.length) {
            metadata = 0;
        }
        return super.getBlockName() + "." + slabType[metadata];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
		int numSubBlocks = this.slabType.length;
        for (int i = 0; i < numSubBlocks; ++i) {
            list.add(new ItemStack(blockId, 1, i));
        }
    }
	
    @SideOnly(Side.CLIENT)
    private static boolean isBlockSingleSlab(int blockId) {
        return blockId == MudMod.dirtSlab.blockID;
    }
}
