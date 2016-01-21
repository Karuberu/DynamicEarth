package karuberu.dynamicearth.blocks;

import java.util.Random;
import karuberu.core.util.Helper;
import karuberu.core.util.Coordinates;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.TickHandler;
import karuberu.dynamicearth.api.IVanillaReplaceable;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockPermafrost extends BlockDynamicEarth implements IVanillaReplaceable {
	public static final int
		META_PERMAFROST = 0;
	public static int
		maximumLightLevel = 11;
	public static float
		maximumTemperature = 0.15F;
    public static CreativeTabs
		creativeTab = CreativeTabs.tabBlock;
    private static ItemStack
    	dirt;
    
	public BlockPermafrost(String unlocalizedName) {
		super(unlocalizedName, Material.rock);
		this.setHardness(1.5F);
		this.setStepSound(Block.soundStoneFootstep);
        this.setCreativeTab(creativeTab);
        this.setTickRandomly(true);
        this.slipperiness = 0.93F;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(BlockTexture.PERMAFROST.getIconPath());
	}
	
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
	    if (BlockPermafrost.willThaw(world, x, y, z)) {
	    	world.setBlock(x, y, z, Block.dirt.blockID, 0, Helper.NOTIFY_AND_UPDATE_REMOTE);
	    }
    }
    
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
    	return true;
    }
    
	@Override
	public ItemStack getVanillaBlockReplacement(Chunk chunk, int x, int y, int z) {
		return dirt == null ? dirt = new ItemStack(Block.dirt.blockID, 1, 0) : dirt;
	}
    
    public static boolean canForm(World world, int x, int y, int z) {
    	if (!DynamicEarth.restoreDirtOnChunkLoad
    	&& !BlockPermafrost.willThaw(world, x, y, z)) {
            if (world.getBiomeGenForCoords(x, z).getFloatTemperature() <= maximumTemperature) {
            	return true;
            } else {
        		Coordinates[] surroundingBlocks = Coordinates.getSurroundingBlockCoords(x, y, z);
        		for (Coordinates coords : surroundingBlocks) {
	        		Material material = world.getBlockMaterial(coords.x, coords.y, coords.z);
	        		if (material == Material.ice
	        		|| material == Material.craftedSnow) {
	        			return true;
	        		}
	        	}
    		}
    	}
    	return false;
    }
    
    public static void tryToForm(World world, int x, int y, int z) {
    	if (world.getBlockId(x, y, z) == Block.dirt.blockID) {
        	if (BlockPermafrost.canForm(world, x, y, z)) {
            	TickHandler.instance.schedulePermafrostFreeze(world, x, y, z, 10);
        	}
    	}
    }
    
    public static boolean willThaw(World world, int x, int y, int z) {
    	return world.getBlockLightValue(x, y + 1, z) > maximumLightLevel
    		   && world.getBlockId(x, y + 1, z) != Block.snow.blockID;
    }
}
