package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockSand;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class BlockMud extends BlockMudMod {

    private static final int
    	META_NORMAL = 0,
    	META_WET = 1,
    	META_FALLING = 2,
    	META_FALLINGWET = 3;

    public BlockMud(int i, int j) {
		super(i, j, Material.ground, Block.dirt.blockID, -1);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockName("mud");
        this.setTextureFile(MudMod.terrainFile);
        this.setHydrateRadius(2, 1, 4, 2);

	}
    
    @Override
	@SideOnly(Side.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
	    switch(metadata) {
	    case META_NORMAL:
	    case META_FALLING: return MudMod.BlockTexture.MUD.ordinal();
	    case META_WET:
	    case META_FALLINGWET: return MudMod.BlockTexture.MUDWET.ordinal();
	    default: return MudMod.BlockTexture.MUD.ordinal();
	    }
    }
    
    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    	if (world.getBlockMetadata(x, y, z) == META_WET) {    	
            float var5 = 0.125F;
            return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x, y, z, x + 1, y + 1 - var5, z + 1);
    	}
    	return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x, y, z, x + 1, (y + 1), z + 1);
    }

    @Override
    public int tickRate() {
    	return 1;
    }
        
    @Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
    	int plantID = plant.getPlantID(world, x, y + 1, z);
    	if (plantID == Block.reed.blockID
    	|| plantID == Block.sapling.blockID
    	|| plantID == Block.tallGrass.blockID
    	) {
    		return true;
    	}
    	return super.canSustainPlant(world, x, y, z, direction, plant);
    }
    
    @Override
    protected boolean canHydrate(int metadata) {
    	if (metadata == META_NORMAL) {
        	return true;
    	}
    	return false;
    }
    
    @Override
    protected boolean canSpread(int metadata) {
    	return false;
    }
    
    public boolean isRooted(World world, int x, int y, int z) {
    	Block block = Block.blocksList[world.getBlockId(x, y + 1, z)];
    	return block instanceof IPlantable ? block != null : false;
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
        	int metadata = world.getBlockMetadata(x, y, z);
        	switch (metadata) {
        	case META_NORMAL:
	        	super.updateTick(world, x, y, z, random);
	        	break;
        	case META_WET:
        		super.updateTick(world, x, y, z, random);
        	case META_FALLING:
        	case META_FALLINGWET:
        		this.tryToFall(world, x, y, z);
        		break;
        	}
    	}
    }
        
    @Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        entity.motionX *= 0.4D;
        entity.motionZ *= 0.4D;
    }
    
//    @Override
//    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
//    	Material material = world.getBlockMaterial(x, y + 1, z);
//    	int metadata = world.getBlockMetadata(x, y + 1, z);
//    	if (material == Material.water) {
//    		if (metadata == 0) {
//    			world.setBlockAndMetadataWithNotify(x, y + 1, z, MudMod.muddyWaterStill.blockID, metadata);
//    		} else {
//    			world.setBlockAndMetadataWithNotify(x, y + 1, z, MudMod.muddyWaterMoving.blockID, metadata);
//    		}
//    	}
//    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch (metadata) {
    	case META_NORMAL:
	    	if (this.blockWillCauseMudslide(neighborID)) {
	    		this.becomeWet(world, x, y, z);
	    	}
	    	break;
    	case META_WET:
    	case META_FALLING:
    	case META_FALLINGWET:
    		this.initiateMudSlide(world, x, y, z);
	    }
    	super.onNeighborBlockChange(world, x, y, z, neighborID);
    }
    
    /**
     * returns true if there is water above the block (within hydration range) and
     * there is only water, air, and/or mud between them. Also returns true if this block
     * is horizontally adjacent to water (including diagonally).
     */
    protected boolean isMuddy(World world, int x, int y, int z) {
        for (int xi = x - 1; xi <= x + 1; ++xi) {
            for (int zi = z - 1; zi <= z + 1; ++zi) {
                if (world.getBlockMaterial(xi, y, zi) == Material.water) {
                    return true;
                }
            }
        }
    	Material material;
    	for (int yi = y + 1; yi <= y + this.hydrateRadiusYDown; ++yi) {
    		material = world.getBlockMaterial(x, yi, z);
    		if (material == Material.water) {
    			return true;
    		} else if (material != Material.air && world.getBlockId(x, yi, z) != MudMod.mud.blockID) {
    			return false;
    		}
    	}
        return false;
    }
    
    @Override
    protected boolean willHydrate(World world, int x, int y, int z) {
    	if (this.isGettingRainedOn(world, x, y, z)) {
    		return true;
    	} else {
        	return super.willHydrate(world, x, y, z) && this.isMuddy(world, x, y, z);
    	}
    }
    
    /**
     * returns true if the block given will trigger a mudslide (currently just water)
     */
    private boolean blockWillCauseMudslide(int blockID) {
    	Block block = Block.blocksList[blockID];
    	if (block != null) {
		    Material neighborMaterial = block.blockMaterial;
		    if (neighborMaterial == Material.water) {
	       		return true;
		    }
    	}
       	return false;
    }
    
    /**
     * Changes the block's metadata and attempts to start a mudslide
     */
    @Override
    protected void becomeWet(World world, int x, int y, int z) {
		world.setBlockAndMetadataWithUpdate(x, y, z, this.blockID, META_WET, true);
		this.initiateMudSlide(world, x, y, z);
    }
    
    @Override
    protected void becomeDry(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	if (metadata == META_WET) {
    		world.setBlockAndMetadataWithUpdate(x, y, z, this.blockID, META_NORMAL, true);
       	} else {
       		super.becomeDry(world, x, y, z);
    	}
    }
    
    private void initiateMudSlide(World world, int x, int y, int z) {
        if (this.willFall(world, x, y, z)) {
        	// if the block will fall, trigger surrounding blocks to mudslide as well,
        	// then schedule a block update to actually fall.
        	int[][] surroundingBlocks = new int[][] {
        		{x-1, y, z},
        		{x+1, y, z},
        		{x, y-1, z},
        		{x, y+1, z},
        		{x, y, z-1},
        		{x, y, z+1}
        	};
        	for (int i = 0; i < surroundingBlocks.length; i++) {
        		int xi = surroundingBlocks[i][0],
        			yi = surroundingBlocks[i][1],
        			zi = surroundingBlocks[i][2];
            	if (world.getBlockId(xi, yi, zi) == this.blockID) {
                	int metadata = world.getBlockMetadata(x, y, z);
            		switch(metadata) {
            		case META_NORMAL: metadata = META_FALLING; break;
            		case META_WET: metadata = META_FALLINGWET; break;
            		}
        			world.setBlockAndMetadata(xi, yi, zi, this.blockID, metadata);
        		}
        	}
			world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
        } else {
        	// if the block can't fall, change its metadata back to a stable one.
        	int metadata = world.getBlockMetadata(x, y, z);
        	if (metadata == META_FALLING || metadata == META_FALLINGWET) {
        		switch(metadata) {
        		case META_FALLING: metadata = META_NORMAL; break;
        		case META_FALLINGWET: metadata = META_WET; break;
        		}
    			world.setBlockAndMetadataWithUpdate(x, y, z, this.blockID, metadata, true);
        	}
        }
    }
    
    private boolean tryToFall(World world, int x, int y, int z) {
        if (this.willFall(world, x, y, z)) {
            byte var8 = 32;

            if (!BlockSand.fallInstantly && world.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8)) {
                if (!world.isRemote) {
                    EntityFallingSand fallingMud = new EntityFallingSand(world, x + 0.5F, y + 0.5F, z + 0.5F, this.blockID);
                    world.spawnEntityInWorld(fallingMud);
                }
            } else {
                world.setBlockWithNotify(x, y, z, 0);
                while (this.willFall(world, x, y, z)) {
                    --y;
                } if (y > 0) {
                    world.setBlockWithNotify(x, y, z, this.blockID);
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean willFall(World world, int x, int y, int z) {
    	return BlockSand.canFallBelow(world, x, y - 1, z) && y >= 0 && !this.isRooted(world, x, y, z);
    }
    
    @Override
    public int idDropped(int i, Random random, int j) {
        return MudMod.mudBlob.shiftedIndex;
    }
    @Override
    public int quantityDropped(Random random) {
    	return 4;
    }
}
