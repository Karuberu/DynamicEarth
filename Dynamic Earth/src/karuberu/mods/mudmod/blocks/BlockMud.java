package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.MudMod.BlockTexture;
import karuberu.mods.mudmod.entity.EntityFallingBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMud extends BlockMudMod implements IFallingBlock {

    private static final int
    	NORMAL = 0,
    	WET = 1,
    	FALLING = 2,
    	FALLINGWET = 3;

    public BlockMud(int i, int j) {
		super(i, j, Material.ground);
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
	    case WET:
	    case FALLINGWET: return MudMod.BlockTexture.MUDWET.ordinal();
	    default: return MudMod.BlockTexture.MUD.ordinal();
	    }
    }
    
    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    	if (MudMod.enableDeepMud && world.getBlockMetadata(x, y, z) == WET) {
            return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x, y, z, x + 1, y + 1 - 0.125F, z + 1);
    	}
    	return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x, y, z, x + 1, y + 1, z + 1);
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
    protected int getDryBlock(int metadata) {
    	return Block.dirt.blockID;
    }
    
    @Override
    protected boolean canHydrate(int metadata) {
    	switch(metadata) {
    	case NORMAL: return true;
    	default: return false;
    	}
    }
    
    @Override
    protected boolean canSpread(int metadata) {
    	if (MudMod.restoreDirtOnChunkLoad) {
    		return false;
    	} else {
    		return true;
    	}
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
        	case NORMAL:
	        	super.updateTick(world, x, y, z, random);
	        	break;
        	case WET:
        		super.updateTick(world, x, y, z, random);
        	case FALLING:
        	case FALLINGWET:
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
        
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch (metadata) {
    	case NORMAL:
	    	if (this.blockWillCauseMudslide(neighborID)) {
	    		this.becomeWet(world, x, y, z);
	    	}
	    	break;
    	case WET:
    	case FALLING:
    	case FALLINGWET:
    		this.initiateMudSlide(world, x, y, z);
	    }
    	Material material = world.getBlockMaterial(x, y + 1, z);
    	if (material == Material.fire || material == Material.lava) {
	    	this.becomeDry(world, x, y, z);
	    }
    	super.onNeighborBlockChange(world, x, y, z, neighborID);
    }
    
    /**
     * returns true if there is water above the block (within hydration range) and
     * there is only water, air, and/or mud between them. Also returns true if this block
     * is horizontally adjacent to water (including diagonally).
     */
    protected boolean isMuddy(World world, int x, int y, int z) {
        for (int xi = x - 1; xi <= x + 1; xi++) {
            for (int zi = z - 1; zi <= z + 1; zi++) {
                if (world.getBlockMaterial(xi, y, zi) == Material.water) {
                    return true;
                }
            }
        }
    	Material material;
    	for (int yi = y + 1; yi <= y + this.hydrateRadiusYDown; yi++) {
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
	public int getDryRate(World world, int x, int y, int z) {
    	int rate = super.getDryRate(world, x, y, z);
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
    		int id = world.getBlockId(xi, yi, zi);
    		if (id == MudMod.mud.blockID
    		|| id == MudMod.adobeWet.blockID
    		|| id == MudMod.peat.blockID) {
    			return rate++;
    		}
    	}
    	return rate;
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
		world.setBlockAndMetadataWithUpdate(x, y, z, this.blockID, WET, true);
		this.initiateMudSlide(world, x, y, z);
    }
    
    @Override
    protected void becomeDry(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch(metadata) {
    	case WET:
    		world.setBlockAndMetadataWithUpdate(x, y, z, this.blockID, NORMAL, true);
    		break;
    	default:
       		super.becomeDry(world, x, y, z);
    	}
    }
    
    private void initiateMudSlide(World world, int x, int y, int z) {
        if (this.canFall(world, x, y, z)) {
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
            		case NORMAL: metadata = FALLING; break;
            		case WET: metadata = FALLINGWET; break;
            		}
        			world.setBlockAndMetadata(xi, yi, zi, this.blockID, metadata);
        		}
        	}
			world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
        } else {
        	// if the block can't fall, change its metadata back to a stable one.
        	int metadata = world.getBlockMetadata(x, y, z);
        	if (metadata == FALLING || metadata == FALLINGWET) {
        		switch(metadata) {
        		case FALLING: metadata = NORMAL; break;
        		case FALLINGWET: metadata = WET; break;
        		}
    			world.setBlockAndMetadataWithUpdate(x, y, z, this.blockID, metadata, true);
        	}
        }
    }
    
    @Override
    public boolean tryToFall(World world, int x, int y, int z) {
        if (this.canFall(world, x, y, z)) {
            byte radius = 32;

            if (BlockFalling.doSpawnEntity()
            && world.checkChunksExist(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius)) {
                if (!world.isRemote) {
                    EntityFallingBlock fallingBlock = new EntityFallingBlock(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.blockID, world.getBlockMetadata(x, y, z));
                    world.spawnEntityInWorld(fallingBlock);
                }
            } else {
                world.setBlockWithNotify(x, y, z, 0);
                while (this.canFall(world, x, y, z)) {
                    --y;
                } if (y > 0) {
                    world.setBlockWithNotify(x, y, z, this.blockID);
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean canFall(World world, int x, int y, int z) {
    	return BlockFalling.canFallBelow(world, x, y - 1, z)
    		&& y >= 0
    		&& !this.isRooted(world, x, y, z);
    }
    
	@Override
	public void onStartFalling(EntityFallingBlock entityFallingBlock) {}

	@Override
	public void onFinishFalling(World world, int x, int y, int z, int metadata) {}

	@Override
	public boolean isDamagedByFall() {
		return false;
	}

	@Override
	public int getMetaForFall(int fallTime, int metadata, Random random) {
		return metadata;
	}

	@Override
	public int getFallDamage() {
		return 0;
	}
	
	@Override
	public int getMaxFallDamage() {
		return 0;
	}
	
	@Override
	public DamageSource getDamageSource() {
		return DamageSource.fallingBlock;
	}
    
    @Override
    public int idDropped(int i, Random random, int j) {
        return MudMod.mudBlob.itemID;
    }
    
    @Override
    public int quantityDropped(Random random) {
    	return 4;
    }
}
