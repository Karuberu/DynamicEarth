package karuberu.mods.mudmod;

import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockAdobeWet extends BlockMudMod {
	
	public BlockAdobeWet(int i, int j) {
		super(i, j, Material.ground, MudMod.adobe.blockID, -1);
		this.setHardness(0.5F);
		this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockName("adobeWet");
        this.setTickRandomly(true);
        this.setTextureFile(MudMod.terrainFile);
        this.setHydrateRadius(2, 1, 2);
	}
}
