package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockPeatDry extends BlockMudMod {

	public BlockPeatDry(int id) {
		super(id, Material.ground);
		this.setHardness(0.4F);
        this.setStepSound(Block.soundGravelFootstep);
        this.setUnlocalizedName("peatDry");
		this.setHydrateRadius(2);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setTickRandomly(true);
	}
	
	@Override
	public void func_94332_a(IconRegister iconRegister) {
		this.field_94336_cN = TextureManager.instance().getBlockTexture(Texture.PEATDRY);
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
