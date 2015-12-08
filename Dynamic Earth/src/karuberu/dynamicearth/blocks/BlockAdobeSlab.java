package karuberu.dynamicearth.blocks;

import java.util.List;
import java.util.Random;

import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAdobeSlab extends BlockHalfSlab {
	
	public static final int
		ADOBE = 0,
		MUDBRICK = 1;
    public static final String[]
    	slabType = new String[] {"adobe", "mudbrick"};

    public BlockAdobeSlab(int id, boolean par2) {
		super(id, par2, Material.rock);
		this.setHardness(1.5F);
		this.setResistance(5.0F);
		this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName("adobeSlab");
        this.useNeighborBrightness[id] = true;
	}
    
	@Override
	public void registerIcons(IconRegister iconRegister) {}

    @Override
    public Icon getIcon(int side, int metadata) {
        switch (MCHelper.getSlabMetadata(metadata)) {
        case 0:
            return DynamicEarth.adobe.getBlockTextureFromSide(side);
        case 1:
            return DynamicEarth.blockMudBrick.getBlockTextureFromSide(side);
        default:
        	return super.getIcon(side, metadata);
        }
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return DynamicEarth.adobeSingleSlab.blockID;
    }
    
    @Override
    protected ItemStack createStackedBlock(int i) {
        return new ItemStack(DynamicEarth.adobeSingleSlab.blockID, 2, i & 7);
    }
    
	@Override
	public String getFullSlabName(int metadata) {
        if (metadata < 0 || metadata >= slabType.length) {
            metadata = 0;
        }
        return super.getUnlocalizedName() + "." + slabType[metadata];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
		int numSubBlocks = this.slabType.length;
		if (blockId != DynamicEarth.adobeDoubleSlab.blockID) {
            for (int i = 0; i < numSubBlocks; ++i) {
                list.add(new ItemStack(blockId, 1, i));
            }
        }
    }
	
    @SideOnly(Side.CLIENT)
    private static boolean isBlockSingleSlab(int blockId) {
        return blockId == DynamicEarth.adobeSingleSlab.blockID;
    }
}
