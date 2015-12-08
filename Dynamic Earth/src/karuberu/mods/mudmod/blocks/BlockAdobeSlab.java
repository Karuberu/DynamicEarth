package karuberu.mods.mudmod.blocks;

import java.util.List;
import java.util.Random;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.MudMod.BlockTexture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAdobeSlab extends BlockHalfSlab {

    public static final String[] slabType = new String[] {"adobe", "mudbrick"};

    public BlockAdobeSlab(int id, boolean par2) {
		super(id, par2, Material.rock);
		this.setHardness(1.5F);
		this.setResistance(5.0F);
		this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("adobeSlab");
        this.useNeighborBrightness[id] = true;
        this.setTextureFile(MudMod.terrainFile);
	}
    
    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        switch (metadata & 7) {
            case 0:
                return MudMod.BlockTexture.ADOBEDRY.ordinal();
            case 1:
                return MudMod.BlockTexture.MUDBRICK.ordinal();
            default:
                return 0;
        }
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return MudMod.adobeSingleSlab.blockID;
    }
    
    @Override
    protected ItemStack createStackedBlock(int i) {
        return new ItemStack(MudMod.adobeSingleSlab.blockID, 2, i & 7);
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
		if (blockId != MudMod.adobeDoubleSlab.blockID) {
            for (int i = 0; i < numSubBlocks; ++i) {
                list.add(new ItemStack(blockId, 1, i));
            }
        }
    }
	
    @SideOnly(Side.CLIENT)
    private static boolean isBlockSingleSlab(int blockId)
    {
        return blockId == MudMod.adobeSingleSlab.blockID;
    }
}
