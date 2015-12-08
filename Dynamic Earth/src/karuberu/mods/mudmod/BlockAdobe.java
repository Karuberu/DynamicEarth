package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnchantmentHelper;
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
    
    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer entityPlayer, int i, int j, int k, int l)
    {
        entityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        entityPlayer.addExhaustion(0.025F);

        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(entityPlayer.inventory)) {
            ItemStack stack = this.createStackedBlock(l);
            if (stack != null) {
                this.dropBlockAsItem_do(world, i, j, k, stack);
            }
        } else {
        	super.harvestBlock(world, entityPlayer, i, j, k, l);
        }
    }
    public int idDropped(int i, Random random, int j) {
        return MudMod.adobeDust.shiftedIndex;
    }
    public int quantityDropped(Random random) {
        return 1 + random.nextInt(3);
    }
}
