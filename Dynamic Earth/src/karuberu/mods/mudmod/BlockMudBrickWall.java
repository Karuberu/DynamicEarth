package karuberu.mods.mudmod;

import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFence;
import net.minecraft.src.BlockWall;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;

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
