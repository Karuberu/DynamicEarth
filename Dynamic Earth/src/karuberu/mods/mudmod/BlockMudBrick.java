package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityIronGolem;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockMudBrick extends Block {

	public BlockMudBrick(int i, int j) {
		super(i, j, Material.rock);
		this.setHardness(1.0F);
		this.setResistance(3.0F);
		this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("blockMudBrick");
	}
	
    @Override
    public String getTextureFile() {
    	return MudMod.terrainFile;
    }
}
