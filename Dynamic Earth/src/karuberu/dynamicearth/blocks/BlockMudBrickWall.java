package karuberu.dynamicearth.blocks;

import java.util.List;

import karuberu.dynamicearth.DynamicEarth;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMudBrickWall extends BlockWall {
    public static CreativeTabs
		creativeTab = CreativeTabs.tabBlock;
    
	public BlockMudBrickWall(int id, Block block) {
		super(id, block);
		this.setCreativeTab(creativeTab);
	}
    
    @Override
    @SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
    }
    
    @Override
    public Icon getIcon(int side, int metadata){
    	switch(metadata) {
    	case 0: return DynamicEarth.blockMudBrick.getBlockTextureFromSide(side);
    	}
    	return super.getIcon(side, metadata);
    }
}
