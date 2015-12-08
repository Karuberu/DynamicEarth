package karuberu.dynamicearth.blocks;

import java.util.Random;

import karuberu.core.MCHelper;
import karuberu.core.event.INeighborBlockEventHandler;
import karuberu.core.event.NeighborBlockChangeEvent;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.TextureManager;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.entity.EntityAdobeGolem;

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
        this.func_111022_d(BlockTexture.ADOBE.getIconPath());
	}
	    
    @Override
	public int idDropped(int i, Random random, int j) {
        return DynamicEarth.adobeDust.itemID;
    }
    
    @Override
	public int quantityDropped(Random random) {
        return 1 + random.nextInt(3);
    }
    
	@Override
	public void handleNeighborBlockChangeEvent(NeighborBlockChangeEvent event) {
		if (DynamicEarth.includeClayGolems
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
	    		world.setBlockToAir(x, y + 1, z);
	    		world.setBlockToAir(x, y, z);
	    		world.setBlockToAir(x, y - 1, z);
	    		if (Xaligned) {
	    			world.setBlockToAir(x - 1, y, z);
	    			world.setBlockToAir(x + 1, y, z);
	    		} else {
	    			world.setBlockToAir(x, y, z - 1);
	    			world.setBlockToAir(x, y, z + 1);
	    		}
                EntityAdobeGolem golem = new EntityAdobeGolem(world);
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
