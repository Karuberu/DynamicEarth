package karuberu.mods.mudmod.blocks;

import java.util.Random;

import karuberu.core.MCHelper;
import karuberu.core.event.INeighborBlockEventHandler;
import karuberu.core.event.NeighborBlockChangeEvent;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import karuberu.mods.mudmod.entity.EntityFallingBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMud extends BlockMudMod implements IFallingBlock, INeighborBlockEventHandler {

    public static final int
    	NORMAL = 0,
    	WET = 1,
    	FALLING = 2,
    	FALLINGWET = 3;
    @SideOnly(Side.CLIENT)
    private Icon
    	textureMud,
    	textureMudWet;

    public BlockMud(int id) {
		super(id, Material.ground);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundGravelFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setUnlocalizedName("mud");
        this.setHydrateRadius(2, 1, 4, 2);
	}
    
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = this.textureMud = TextureManager.instance().getBlockTexture(Texture.MUD);
		this.textureMudWet = TextureManager.instance().getBlockTexture(Texture.MUDWET);
	}
    
    @Override
	@SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int side, int metadata) {
	    switch(metadata) {
	    case WET:
	    case FALLINGWET: return this.textureMudWet;
	    default: return textureMud;
	    }
    }
    
    @Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    	if (MudMod.enableDeepMud && world.getBlockMetadata(x, y, z) == WET) {
            return AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 1 - 0.25F, z + 1);
    	}
    	return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
    	
    @Override
    public int tickRate(World world) {
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
    protected boolean canDry(int metadata) {
    	return true;
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
        if (entity.posY > y + 0.5D) {
        	entity.posY -= 0.1D;
        }
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch (metadata) {
    	case WET:
    	case FALLING:
    	case FALLINGWET:
    		this.initiateMudSlide(world, x, y, z);
	    }
    	super.onNeighborBlockChange(world, x, y, z, neighborID);
    }
    
    @Override
    public void handleNeighborBlockChangeEvent(NeighborBlockChangeEvent event) {
    	Block neighborBlock = Block.blocksList[event.neighborBlockID];
    	if (neighborBlock != null) {
    		if (neighborBlock.blockMaterial == Material.lava
    		|| (event.side == MCHelper.SIDE_TOP && neighborBlock.blockMaterial == Material.fire)) {
    			this.becomeDry(event.world, event.x, event.y, event.z);
    		} else if (neighborBlock.blockMaterial == Material.water) {
    			this.becomeWet(event.world, event.x, event.y, event.z);
    		}
    	}
    }
    
	protected boolean isHydrated(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch (metadata) {
    	case WET:
    		int hydrationDistance = this.getHydrationDistance(world, x, y, z);
    		return hydrationDistance == 0 || this.isMuddy(world, x, y, z);
    	default:
    		return super.isHydrated(world, x, y, z);
    	}
	}
    
    /**
     * Returns true if there is water above the block (within hydration range) and
     * there is only water, air, or mud between them. Also returns true if this block
     * is horizontally adjacent to water (including diagonally).
     */
    protected boolean isMuddy(World world, int x, int y, int z) {
    	Material material;
        for (int xi = x - 1; xi <= x + 1; xi++) {
            for (int zi = z - 1; zi <= z + 1; zi++) {
            	material = world.getBlockMaterial(xi, y, zi);
                if (material == Material.water) {
                    return true;
                }
            }
        }
    	for (int yi = y + 1; yi <= y + this.hydrateRadiusYDown; yi++) {
    		material = world.getBlockMaterial(x, yi, z);
    		if (material == Material.water) {
    			return true;
    		}
    		if (!world.isAirBlock(x, yi, z)
    		&& world.getBlockId(x, yi, z) != MudMod.mud.blockID) {
    			return false;
    		}
    	}
        return false;
    }
    
    @Override
    protected boolean willHydrate(World world, int x, int y, int z) {
    	if (this.isBlockGettingRainedOn(world, x, y, z)) {
    		return true;
    	} else {
        	return super.willHydrate(world, x, y, z) && this.isMuddy(world, x, y, z);
    	}
    }
    
    /**
     * Changes the block's metadata and attempts to start a mudslide
     */
    @Override
    protected void becomeWet(World world, int x, int y, int z) {
		world.setBlock(x, y, z, this.blockID, WET, MCHelper.UPDATE_WITHOUT_NOTIFY_REMOTE);
		this.initiateMudSlide(world, x, y, z);
    }
    
    @Override
    protected void becomeDry(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch(metadata) {
    	case WET:
    		world.setBlock(x, y, z, this.blockID, NORMAL, MCHelper.UPDATE_WITHOUT_NOTIFY_REMOTE);
    		break;
    	default:
       		super.becomeDry(world, x, y, z);
    	}
    }
    
    private void initiateMudSlide(World world, int x, int y, int z) {
        if (this.canFall(world, x, y, z)) {
        	// if the block will fall, trigger surrounding blocks to mudslide as well,
        	// then schedule a block update to actually fall.
        	int[][] surroundingBlocks = MCHelper.getSurroundingBlocks(x, y, z);
        	for (int[] coordinates : surroundingBlocks) {
        		int xi = coordinates[0],
        			yi = coordinates[1],
        			zi = coordinates[2];
            	if (world.getBlockId(xi, yi, zi) == this.blockID) {
                	int metadata = world.getBlockMetadata(x, y, z);
            		switch(metadata) {
            		case NORMAL: metadata = FALLING; break;
            		case WET: metadata = FALLINGWET; break;
            		}
        			world.setBlock(xi, yi, zi, this.blockID, metadata, MCHelper.UPDATE_WITHOUT_NOTIFY_REMOTE);
        		}
        	}
			world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
        } else {
        	// if the block can't fall, change its metadata back to a stable one.
        	int metadata = world.getBlockMetadata(x, y, z);
        	if (metadata == FALLING || metadata == FALLINGWET) {
        		switch(metadata) {
        		case FALLING: metadata = NORMAL; break;
        		case FALLINGWET: metadata = WET; break;
        		}
    			world.setBlock(x, y, z, this.blockID, metadata, MCHelper.UPDATE_WITHOUT_NOTIFY_REMOTE);
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
                world.setBlockToAir(x, y, z);
                while (this.canFall(world, x, y, z)) {
                    --y;
                } if (y > 0) {
                    world.setBlock(x, y, z, this.blockID, world.getBlockMetadata(x, y, z), MCHelper.NOTIFY_AND_UPDATE_REMOTE);
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
    
    /**
     * returns true if the block given will trigger a mudslide (currently just water)
     */
    public static boolean blockWillCauseMudslide(int blockID) {
    	Block block = Block.blocksList[blockID];
    	if (block != null) {
		    Material neighborMaterial = block.blockMaterial;
		    if (neighborMaterial == Material.water) {
	       		return true;
		    }
    	}
       	return false;
    }
}
