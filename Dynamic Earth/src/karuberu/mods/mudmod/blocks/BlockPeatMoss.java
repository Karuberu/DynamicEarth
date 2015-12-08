package karuberu.mods.mudmod.blocks;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

public class BlockPeatMoss extends BlockMudMod {
	
	protected static int minimumLightLevel = 6;
	public static int tickRate = 20;
	public static final int
		GROWTHSTAGE_1 = 0,
		GROWTHSTAGE_2 = 1,
		GROWTHSTAGE_3 = 2,
		GROWTHSTAGE_4 = 3,
		GROWTHSTAGE_FULLGROWN = 4;

	public BlockPeatMoss(int id, int textureIndex) {
		super(id, textureIndex, MaterialPeatMoss.material);
        this.setBlockName("peatMoss");
		this.setStepSound(Block.soundGrassFootstep);
		this.setLightOpacity(0);
		this.setTickRandomly(true);
		this.setTextureFile(MudMod.terrainFile);
		this.setHydrateRadius(3, 2, 0, 3);
	}

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
    	int metadata = blockAccess.getBlockMetadata(x, y, z);
    	switch(metadata) {
    	case GROWTHSTAGE_1: this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, (1.0F / 32.0F), 0.6F); break;
    	case GROWTHSTAGE_2: this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, (1.0F / 32.0F), 0.7F); break;
    	case GROWTHSTAGE_3: this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, (1.0F / 32.0F), 0.9F); break;
    	case GROWTHSTAGE_4: this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, (1.0F / 16.0F), 1.0F); break;
    	default: this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, (1.0F / 8.0F), 1.0F); break;
    	}
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
    	if (side == Reference.SIDE_TOP) {
    		return true;
    	} else {
    		return super.shouldSideBeRendered(blockAccess, x, y, z, side);
    	}
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
    	return this.canBlockStay(world, x, y, z) && super.canPlaceBlockAt(world, x, y, z);
    }
    
    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        int soilID = world.getBlockId(x, y - 1, z);
        int metadata = world.getBlockMetadata(x, y, z);
        if (world.getFullBlockLightValue(x, y, z) >= this.minimumLightLevel
        && (this.soilIsValid(soilID, metadata))) {
        	return true;
        }
        return false;
    }
    
    public static boolean soilIsValid(int soilID, int metadata) {
    	switch(metadata) {
    	case GROWTHSTAGE_1:
    	case GROWTHSTAGE_2:
    	case GROWTHSTAGE_3:
    	case GROWTHSTAGE_4:
    		return soilID == Block.tilledField.blockID;
    	default:
	    	return soilID == Block.dirt.blockID
	    		|| soilID == Block.grass.blockID
	    		|| soilID == Block.tilledField.blockID
	    		|| soilID == MudMod.mud.blockID
	    		|| soilID == MudMod.peat.blockID;
    	}
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
        this.tryRemoveInvalidMoss(world, x, y, z);
    }

    private boolean tryRemoveInvalidMoss(World world, int x, int y, int z) {
        if (!this.canPlaceBlockAt(world, x, y, z)) {
            world.setBlockWithNotify(x, y, z, 0);
            this.dropBlockAsItem(world, x, y, z, 0, 0);
            return true;
        }
        return false;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        super.harvestBlock(world, player, x, y, z, metadata);
        world.setBlockWithNotify(x, y, z, 0);
    }
    
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        if (world.rand.nextInt(Math.max(0, 9 - metadata - fortune)) == 0) {
        	items.add(new ItemStack(MudMod.peatMossSpecimen));
        }
        return items;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
		int soilID = world.getBlockId(x, y - 1, z);
		int metadata = world.getBlockMetadata(x, y, z);
    	if (this.isLightSufficient(world, x, y, z)
    	&& this.isHydrated(world, x, y, z)) {
    		if (metadata == GROWTHSTAGE_FULLGROWN) {
	    		if (random.nextInt(3) == 0) {
		    		this.makePeat(world, x, y, z);
	    		}
	    		if (random.nextInt(3) == 0) {
		        	this.tryToSpread(world, x, y, z);
	    		}
    		} else {
    			this.grow(world, x, y, z);
    		}
    	} else {
            world.setBlockWithNotify(x, y, z, 0);
    	}
    }
    
    @Override
    public int tickRate() {
        return this.tickRate;
    }
    
    private boolean isLightSufficient(World world, int x, int y, int z) {
    	return world.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) >= minimumLightLevel
    	|| world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) >= minimumLightLevel;
    }
    
    protected void makePeat(World world, int x, int y, int z) {
		final int metadata = world.getBlockMetadata(x, y, z);
		int soilID = world.getBlockId(x, y - 1, z);
		int i = 1;
		while (soilIsValid(soilID, metadata)) {
    		if (soilID != MudMod.peat.blockID) {
        		world.setBlockAndMetadataWithNotify(x, y - i, z, MudMod.peat.blockID, BlockPeat.META_NONE);
        		return;
    		} else {
    			int soilMeta = world.getBlockMetadata(x, y - i, z);
    			switch (soilMeta) {
    			case BlockPeat.META_FULL: break;
    			case BlockPeat.META_7EIGHTHS:
    				world.setBlockAndMetadata(x, y - i, z, MudMod.peat.blockID, BlockPeat.META_FULL);
    				break;
    			default:
    				world.setBlockAndMetadata(x, y - i, z, MudMod.peat.blockID, soilMeta + 1);
    				break;
    			}
    		}
    		soilID = world.getBlockId(x, y - i, z);
    		i++;
		}
    }
    
    protected void grow(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch(metadata) {
    	case GROWTHSTAGE_1:
    	case GROWTHSTAGE_2:
    	case GROWTHSTAGE_3:
    	case GROWTHSTAGE_4:
    		world.setBlockAndMetadataWithNotify(x, y, z, MudMod.peatMoss.blockID, metadata + 1);    	
    		break;
    	case GROWTHSTAGE_FULLGROWN:
    		break;
    	default:
    		world.setBlockAndMetadataWithNotify(x, y, z, MudMod.peatMoss.blockID, GROWTHSTAGE_FULLGROWN);    		
    		break;
    	}
    }
    
    @Override
    protected int getDryBlock(int metadata) {
    	return 0;
    }
    
    @Override
    protected boolean isBeingHeated(World world, int x, int y, int z) {
    	return false;
    }
    
    @Override
    protected boolean canSpread(int metadata) {
    	return true;
    }
    
    @Override
    protected void spread(World world, int x, int y, int z, int n) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	for (int i = 0; i < n; ++i) {
        	int xi = x + world.rand.nextInt(3) - 1,
            	yi = y + world.rand.nextInt(3) - 1,
            	zi = z + world.rand.nextInt(3) - 1;
	    	if (world.getBlockId(xi, yi, zi) == this.getDryBlock(metadata)
	    	&& this.isHydrated(world, xi, yi, zi)
	    	&& this.canBlockStay(world, xi, yi, zi)) {
	    		world.setBlockWithNotify(xi, yi, zi, this.blockID);
	    		break;
	    	}
    	}
	}
    
    @Override
    public int idPicked(World world, int x, int y, int z) {
    	return MudMod.peatMossSpecimen.shiftedIndex;
    }
}
