package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityIronGolem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.StatList;
import net.minecraft.src.World;

public class BlockAdobe extends Block {

	public BlockAdobe(int i, int j) {
		super(i, j, Material.rock);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
    @Override
    public String getTextureFile() {
    	return MudMod.terrainFile;
    }
    
    @Override
	public int idDropped(int i, Random random, int j) {
        return MudMod.adobeDust.shiftedIndex;
    }
    @Override
	public int quantityDropped(Random random) {
        return 1 + random.nextInt(3);
    }
}
