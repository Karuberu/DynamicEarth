package karuberu.mods.mudmod;

import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
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

    public static boolean fallInstantly = false;
    private final int
    	meta_mud = 0,
    	meta_mudWet = 1,
    	meta_mudFalling = 2,
    	meta_mudFallingWet = 3;

    public BlockMud(int i, int j) {
		super(i, j, Material.ground);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockName("mud");
        this.setTickRandomly(true);
        this.setHydrateRadius(2, 4, 2);
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public String getTextureFile() {
    	return MudMod.terrainFile;
    }
    
    @Override
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
    
    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    	if (world.getBlockMetadata(x, y, z) == meta_mudWet) {    	
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
    	Item item = player.getCurrentEquippedItem().getItem();
    	if ((item == Item.field_82797_bK /* carrots */ || item == Item.field_82794_bL /* potatoes */)
    	&& world.isAirBlock(x, y + 1, z)) {
    		int plantID;
    		if (item == Item.field_82797_bK) {
    			plantID = Block.field_82513_cg.blockID;
    		} else {
    			plantID = Block.field_82514_ch.blockID;
    		}
            world.setBlockWithNotify(x, y + 1, z, plantID);
            player.getCurrentEquippedItem().stackSize--;
            return true;
        }
    	return false;
    }
    
    @Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
    	int plantID = plant.getPlantID(world, x, y + 1, z);
    	if (plantID == Block.reed.blockID
    	|| plantID == Block.sapling.blockID
    	|| plantID == Block.tallGrass.blockID
    	|| plantID == Block.field_82513_cg.blockID // carrots
    	|| plantID == Block.field_82514_ch.blockID // potatoes
    	) {
    		return true;
    	}
    	return super.canSustainPlant(world, x, y, z, direction, plant);
    }
    
    public boolean isSustainingPlant(World world, int x, int y, int z) {
    	Block block = Block.blocksList[world.getBlockId(x, y + 1, z)];
    	return block instanceof IPlantable ? block != null : false;
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
        	int metadata = world.getBlockMetadata(x, y, z);
        	if (metadata >= meta_mudWet && !this.isSustainingPlant(world, x, y, z)) {
            	this.tryToFall(world, x, y, z);
        	}
        	if (metadata <= meta_mudWet) {
	        	if (!this.isWaterNearby(world, x, y, z)
	        	&& !this.isGettingRainedOn(world, x, y, z)) {
	            	this.becomeDry(world, x, y, z);
	        	} else if (this.isMuddy(world, x, y, z)) {
	        		this.becomeWet(world, x, y, z);
	            }
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
    	if (metadata == meta_mud) {
	    	if (this.willMudslide(neighborID)) {
	    		this.becomeWet(world, x, y, z);
	    	}
    	} else if (metadata >= meta_mudWet){
    		this.initiateMudSlide(world, x, y, z);
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
        if (canFallBelow(world, x, y - 1, z) && y >= 0 && !this.isSustainingPlant(world, x, y, z)) {
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
        		}
        	}
			world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
        } else {
        	int metadata = world.getBlockMetadata(x, y, z);
        	if (metadata > this.meta_mudWet) {
    			world.setBlockAndMetadataWithUpdate(x, y, z, this.blockID, metadata-2, true);
        	}
        }
    }
    
    private boolean tryToFall(World world, int x, int y, int z) {
        if (canFallBelow(world, x, y - 1, z) && y >= 0) {
            byte var8 = 32;

            if (!fallInstantly && world.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8)) {
                if (!world.isRemote) {
                    EntityFallingSand fallingMud = new EntityFallingSand(world, x + 0.5F, y + 0.5F, z + 0.5F, this.blockID);
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
            return true;
        }
        return false;
    }
    
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
