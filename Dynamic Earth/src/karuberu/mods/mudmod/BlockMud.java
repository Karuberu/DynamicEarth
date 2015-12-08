package karuberu.mods.mudmod;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityTracker;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.StatList;
import net.minecraft.src.World;

public class BlockMud extends BlockMudMod {

    public static boolean fallInstantly = false;
    private final int
    	meta_mud = 0,
    	meta_mudWet = 1,
    	meta_mudFalling = 2,
    	meta_mudFallingWet = 3;

    public BlockMud(int i, int j) {
		super(i, j, Material.ground);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHydrateRadius(2, 4, 2);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public String getTextureFile() {
    	return MudMod.terrainFile;
    }
    
    @SideOnly(Side.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
	    switch(metadata) {
	    case meta_mud:
	    case meta_mudFalling: return MudMod.BlockTexture.MUD.ordinal();
	    case meta_mudWet:
	    case meta_mudFallingWet: return MudMod.BlockTexture.MUDWET.ordinal();
	    default: return MudMod.BlockTexture.MUD.ordinal();
	    }
    }

//    @Override
//    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
//    	if (world.getBlockMaterial(x, y+1, z) == Material.water) {
//    		world.setBlock(x, y+1, z, mod_MudMod.muddyWaterStill.blockID);
//    	}
//    }
    
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    	if (world.getBlockMetadata(x, y, z) >= meta_mudWet) {    	
            float var5 = 0.125F;
            return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)x, (double)y, (double)z, (double)(x + 1), (double)((float)(y + 1) - var5), (double)(z + 1));
    	}
    	return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)x, (double)y, (double)z, (double)(x + 1), (double)((float)(y + 1)), (double)(z + 1));
    }
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        entity.motionX *= 0.4D;
        entity.motionZ *= 0.4D;
    }
    
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	if (metadata == meta_mud) {
	    	if (this.willMudslide(neighborID)) {
	    		this.becomeWet(world, x, y, z);
	    	}
    	} else if (metadata >= meta_mudWet){
    		this.initiateMudSlide(world, x, y, z);
    	}
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
        	int metadata = world.getBlockMetadata(x, y, z);
        	if (metadata > meta_mudWet) {
        		this.initiateMudSlide(world, x, y, z);
        	}
        	if (!this.isWaterNearby(world, x, y, z)
        	&& !this.isGettingRainedOn(world, x, y, z)) {
            	this.becomeDry(world, x, y, z);
        	} else if (this.isMuddy(world, x, y, z)) {
        		this.becomeWet(world, x, y, z);
            }
        }
    }
    @Override
    public void fillWithRain(World world, int x, int y, int z) {
    	this.becomeWet(world, x, y, z);
    }
    /**
     * returns true if there is water above the block (within hydration range) and
     * there is only water, air, and/or mud between them. Also returns true if this block
     * is horizontally adjacent to water (including diagonally).
     */
    protected boolean isMuddy(World world, int i, int j, int k) {
        for (int x = i - 1; x <= i + 1; ++x) {
            for (int z = k - 1; z <= k + 1; ++z) {
                if (world.getBlockMaterial(x, j, z) == Material.water) {
                    return true;
                }
            }
        }
    	Material material;
    	for (int y = j + 1; y <= j + hydrateRadiusY; ++y) {
    		material = world.getBlockMaterial(i, y, k);
    		if (material == Material.water) {
    			return true;
    		} else if (material != Material.air && world.getBlockId(i, y, k) != MudMod.mud.blockID) {
    			return false;
    		}
    	}
        return false;
    }
    /**
     * returns true if the block given will trigger a mudslide (currently just water)
     */
    private boolean willMudslide(int neighborBlockId) {
    	Block block = Block.blocksList[neighborBlockId];
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
    private void becomeWet(World world, int x, int y, int z) {
		world.setBlockAndMetadataWithUpdate(x, y, z, this.blockID, this.meta_mudWet, true);
		this.initiateMudSlide(world, x, y, z);
    }
    private void becomeDry(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	if (metadata == meta_mudWet) {
    		world.setBlockAndMetadataWithUpdate(x, y, z, this.blockID, this.meta_mud, true);
       	} else {
       		world.setBlock(x, y, z, Block.dirt.blockID);
    	}
    }
    private void initiateMudSlide(World world, int x, int y, int z) {
        if (canFallBelow(world, x, y - 1, z) && y >= 0) {
        	int[][] coords = new int[][] {
        		{x-1, y, z},
        		{x+1, y, z},
        		{x, y-1, z},
        		{x, y+1, z},
        		{x, y, z-1},
        		{x, y, z+1}
        	};
        	for (int i = 0; i < coords.length; i++) {
        		int xi = coords[i][0],
        			yi = coords[i][1],
        			zi = coords[i][2];
            	if (world.getBlockId(xi, yi, zi) == this.blockID) {
        			world.setBlockAndMetadata(xi, yi, zi, this.blockID, world.getBlockMetadata(xi, yi, zi)+2);
        			world.scheduleBlockUpdate(xi, yi, zi, this.blockID, this.tickRate());
        		}
        	}
        	this.tryToFall(world, x, y, z);
        } else {
        	int metadata = world.getBlockMetadata(x, y, z);
        	if (metadata > this.meta_mudWet) {
    			world.setBlockAndMetadataWithUpdate(x, y, z, this.blockID, metadata-2, true);
        	}
        }
    }
    /**
     * If there is space to fall below will start this block falling
     */
    private void tryToFall(World world, int x, int y, int z) {
        if (canFallBelow(world, x, y - 1, z) && y >= 0) {
            byte var8 = 32;

            if (!fallInstantly && world.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8)) {
                if (!world.isRemote) {
                    EntityFallingSand fallingMud = new EntityFallingSand(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.blockID);
                    world.spawnEntityInWorld(fallingMud);
                }
            } else {
                world.setBlockWithNotify(x, y, z, 0);
                while (canFallBelow(world, x, y - 1, z) && y > 0) {
                    --y;
                } if (y > 0) {
                    world.setBlockWithNotify(x, y, z, this.blockID);
                }
            }
        }
    }
    
    /**
     * Checks to see if the sand can fall into the block below it
     */
    public static boolean canFallBelow(World par0World, int par1, int par2, int par3) {
        int var4 = par0World.getBlockId(par1, par2, par3);

        if (var4 == 0) {
            return true;
        } else if (var4 == Block.fire.blockID) {
            return true;
        } else {
            Material var5 = Block.blocksList[var4].blockMaterial;
            return var5 == Material.water ? true : var5 == Material.lava;
        }
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
