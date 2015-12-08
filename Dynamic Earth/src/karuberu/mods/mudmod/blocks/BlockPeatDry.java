package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPeatDry extends BlockMudMod {

	public BlockPeatDry(int id, int textureIndex) {
		super(id, textureIndex, Material.ground);
		this.setHardness(0.4F);
        this.setStepSound(Block.soundGravelFootstep);
        this.setBlockName("peatDry");
        this.setTextureFile(MudMod.terrainFile);
		this.setHydrateRadius(2);
	}
	
	@Override
	protected int getWetBlock(int metadata) {
		return MudMod.peat.blockID;
	}
	
    @Override
    public int idDropped(int metadata, Random random, int par3) {
    	return MudMod.peatBrick.shiftedIndex;
    }
    
    @Override
    public int quantityDropped(int metadata, int fortune, Random random) {
    	return 4;
    }
}
