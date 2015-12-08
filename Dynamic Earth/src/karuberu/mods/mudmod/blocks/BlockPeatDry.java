package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockPeatDry extends BlockMudMod {

	public BlockPeatDry(int id, int textureIndex) {
		super(id, textureIndex, Material.ground);
		this.setHardness(0.4F);
        this.setStepSound(Block.soundGravelFootstep);
        this.setBlockName("peatDry");
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setTextureFile(MudMod.terrainFile);
		this.setHydrateRadius(2);
	}
	
	@Override
	protected int getWetBlock(int metadata) {
		return MudMod.peat.blockID;
	}
	
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
    	return 2;
    }
    
    @Override
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face) {
    	return 40;
    }
    
    @Override
    public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side) {
    	return side == ForgeDirection.UP;
    }
}
