package karuberu.mods.mudmod.blocks;

import java.util.List;

import karuberu.mods.mudmod.MudMod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMudBrickWall extends BlockWall {

	public BlockMudBrickWall(int id, Block block) {
		super(id, block);
        this.setTextureFile(MudMod.terrainFile);
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
    }
}
