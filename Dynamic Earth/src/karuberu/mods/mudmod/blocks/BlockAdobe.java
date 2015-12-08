package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.core.MCHelper;
import karuberu.core.event.INeighborBlockEventHandler;
import karuberu.core.event.NeighborBlockChangeEvent;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import karuberu.mods.mudmod.entity.EntityClayGolem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

public class BlockAdobe extends Block implements INeighborBlockEventHandler {

	public BlockAdobe(int id) {
		super(id, Material.rock);
        this.setHardness(1.5F);
        this.setResistance(5.0F);
        this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setUnlocalizedName("adobeDry");
	}
	
	@Override
	public void func_94332_a(IconRegister iconRegister) {
		this.field_94336_cN = TextureManager.instance().getBlockTexture(Texture.ADOBE);
	}
    
    @Override
	public int idDropped(int i, Random random, int j) {
        return MudMod.adobeDust.itemID;
    }
    
    @Override
	public int quantityDropped(Random random) {
        return 1 + random.nextInt(3);
    }
    
	@Override
	public void handleNeighborBlockChangeEvent(NeighborBlockChangeEvent event) {
		if (MudMod.includeClayGolems
		&& event.side == MCHelper.SIDE_TOP
		&& Block.blocksList[event.neighborBlockID] instanceof BlockPumpkin) {
			this.tryToSpawnClayGolem(event.world, event.x, event.y, event.z);
		}
	}
	
    protected boolean tryToSpawnClayGolem(World world, int x, int y, int z) {
    	int topBlockID = world.getBlockId(x, y + 1, z);
    	int bottomBlockID = world.getBlockId(x, y - 1, z);
    	if (Block.blocksList[topBlockID] instanceof BlockPumpkin
    	&& bottomBlockID == this.blockID) {
	    	boolean Xaligned = world.getBlockId(x - 1, y, z) == this.blockID && world.getBlockId(x + 1, y, z) == this.blockID;
	    	boolean Zaligned = world.getBlockId(x, y, z - 1) == this.blockID && world.getBlockId(x, y, z + 1) == this.blockID;
	    	if (Xaligned || Zaligned) {
	    		world.func_94571_i(x, y + 1, z);
	    		world.func_94571_i(x, y, z);
	    		world.func_94571_i(x, y - 1, z);
	    		if (Xaligned) {
	    			world.func_94571_i(x - 1, y, z);
	    			world.func_94571_i(x + 1, y, z);
	    		} else {
	    			world.func_94571_i(x, y, z - 1);
	    			world.func_94571_i(x, y, z + 1);
	    		}
                EntityClayGolem golem = new EntityClayGolem(world);
                golem.setLocationAndAngles((double)x + 0.5D, (double)y - 0.95D, (double)z + 0.5D, 0.0F, 0.0F);
                world.spawnEntityInWorld(golem);
                for (int i = 0; i < 120; ++i) {
                    world.spawnParticle("snowballpoof", (double)x + world.rand.nextDouble(), (double)(y - 2) + world.rand.nextDouble() * 3.9D, (double)z + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                }
	    	}
    	}
    	return false;
    }
}
