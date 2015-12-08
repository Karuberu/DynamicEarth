package karuberu.dynamicearth.blocks;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.ITextureOverlay;
import karuberu.dynamicearth.client.TextureManager;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class BlockDynamicFarmland extends BlockDynamicEarth implements ITextureOverlay {
	public final static int
		MUD = 0,
		PEAT_DRY = 1,
		PEAT_WET = 2,
		SOIL_DRY = 3,
		SOIL_WET = 4,
		SANDY_DRY = 5,
		PEAT_1 = 6,
		PEAT_2 = PEAT_1 + 1,
		PEAT_3 = PEAT_2 + 1,
		PEAT_4 = PEAT_3 + 1,
		PEAT_5 = PEAT_4 + 1,
		PEAT_6 = PEAT_5 + 1,
		PEAT_7 = PEAT_6 + 1;
	private Icon
		textureFarmlandDry,
		textureFarmlandWet;
	private static ItemStack
		dirt,
		mud,
		peatDry,
		peatWet,
		peat1,
		peat2,
		peat3,
		peat4,
		peat5,
		peat6,
		peat7,
		peat8,
		soil,
		sandy,
		farmlandPeatDry,
		farmlandPeatWet,
		farmlandSoilDry,
		farmlandSoilWet;

	public BlockDynamicFarmland(int id) {
		super(id, Material.ground);
		this.setHydrateRadius(4, 0, 4);
		this.setSimpleHydration(true);
		this.setStepSound(Block.soundGravelFootstep);
		this.setHardness(0.4F);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        this.setLightOpacity(255);
		this.setTickRandomly(true);
		this.useNeighborBrightness[id] = true;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.textureFarmlandDry = iconRegister.registerIcon(BlockTexture.PEATFARMLAND.getIconPath());
		this.textureFarmlandWet = iconRegister.registerIcon(BlockTexture.PEATFARMLANDWET.getIconPath());
	}

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata) {
    	if (side == MCHelper.SIDE_TOP) {
    		switch (metadata) {
    		case SANDY_DRY:
    		case SOIL_DRY: return Block.tilledField.getIcon(side, 0);
    		case MUD:
    		case SOIL_WET: return Block.tilledField.getIcon(side, 1);
    		case PEAT_1:
    		case PEAT_2:
    		case PEAT_3:
    		case PEAT_4:
    		case PEAT_5:
    		case PEAT_6:
    		case PEAT_7:
    		case PEAT_WET: return this.textureFarmlandWet;
    		case PEAT_DRY: return this.textureFarmlandDry;
    		default: return Block.dirt.getBlockTextureFromSide(side);
    		}
    	} else {
    		ItemStack itemStack = BlockDynamicFarmland.getAssociatedBlock(metadata);
    		Block block = Block.blocksList[itemStack.itemID];
    		return block == null ? Block.dirt.getIcon(side, metadata) : block.getIcon(side, itemStack.getItemDamage());
    	}
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int metadata, int side, int pass) {
		ItemStack itemStack = BlockDynamicFarmland.getAssociatedBlock(blockAccess.getBlockMetadata(x, y, z));
		Block block = Block.blocksList[itemStack.itemID];
		if (block instanceof ITextureOverlay) {
			return ((ITextureOverlay)block).getOverlayTexture(blockAccess, x, y, z, itemStack.getItemDamage(), side, pass);
		} else {
			return null;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeTexture(IBlockAccess blockAccess, int x, int y, int z, int metadata, int side, int pass) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getNumberOfPasses(int metadata) {
		switch (metadata) {
		case PEAT_1:
		case PEAT_2:
		case PEAT_3:
		case PEAT_4:
		case PEAT_5:
		case PEAT_6:
		case PEAT_7: return 1;
		default: return 0;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeInventoryBaseTexture(int side, int metadata) {
		return false;
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
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return DynamicEarth.overlayBlockRenderID;
	}
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    	return AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 1 - 0.125F, z + 1);
    }
	
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	int fertility = BlockDynamicFarmland.getFertility(metadata);
    	if (fertility > 0) {
	    	Block block = Block.blocksList[world.getBlockId(x, y + 1, z)];
	    	if (block instanceof IPlantable) {
	    		for (int i = 0; i < fertility; i++) {
	    			block.updateTick(world, x, y + 1, z, random);
	    		}
	    	}
    	}
    	if (metadata == MUD) {
    		world.setBlock(x, y, z, Block.tilledField.blockID, 1, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
    	} else {
    		super.updateTick(world, x, y, z, random);
    	}
    }
    
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        super.onNeighborBlockChange(world, x, y, z, blockId);
        Material material = world.getBlockMaterial(x, y + 1, z);
        if (material.isSolid()) {
    		this.trample(world, x, y, z);
        } else if (DynamicEarth.includePeat
        && world.getBlockId(x, y + 1, z) == DynamicEarth.peatMoss.blockID
        && BlockPeatMoss.isFullGrown(world.getBlockMetadata(x, y + 1, z))) {
        	this.trample(world, x, y, z);
        }
    }
    
    @Override
    public void onFallenUpon(World world, int x, int y, int z, Entity entity, float height) {
        if (!world.isRemote
        && world.rand.nextFloat() < height - 0.5F) {
            if (entity instanceof EntityPlayer
            || world.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
            	entity.posY += 0.125F;
        		this.trample(world, x, y, z);
            }
        }
    }
    
    private void trample(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	ItemStack itemStack = BlockDynamicFarmland.getAssociatedBlock(metadata);
   		world.setBlock(x, y, z, itemStack.itemID, itemStack.getItemDamage(), MCHelper.NOTIFY_AND_UPDATE_REMOTE);
    }

    @Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch(metadata) {
		case MUD:
			if (plant.getPlantType(world, x, y, z) == EnumPlantType.Beach) {
				return true;
			}
		default:
	    	if (plant.getPlantType(world, x, y, z) == EnumPlantType.Crop
	    	|| plant.getPlantID(world, x, y, z) == Block.carrot.blockID
	    	|| plant.getPlantID(world, x, y, z) == Block.potato.blockID) {
	    		return true;
	    	}
		}
    	return false;
    }
    
    @Override
    public boolean isFertile(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch (metadata) {
    	case MUD:
		case PEAT_1:
		case PEAT_2:
		case PEAT_3:
		case PEAT_4:
		case PEAT_5:
		case PEAT_6:
		case PEAT_7:
    	case PEAT_WET:
    	case SOIL_WET: return true;
    	default: return false;
    	}
    }
    
	@Override
	protected ItemStack getDryBlock(int metadata) {
		if (DynamicEarth.includePeat
		&& metadata == PEAT_WET) {
			return farmlandPeatDry == null ? farmlandPeatDry = new ItemStack(DynamicEarth.farmland.blockID, 1, PEAT_DRY) : farmlandPeatDry;
		}
		if (DynamicEarth.includeFertileSoil
		&& metadata == SOIL_WET) {
			return farmlandSoilDry == null ? farmlandSoilDry = new ItemStack(DynamicEarth.farmland.blockID, 1, SOIL_DRY) : farmlandSoilDry;
		}
		return getAssociatedBlock(metadata);
	}
	
	@Override
	protected ItemStack getWetBlock(int metadata) {
		if (DynamicEarth.includePeat
		&& metadata == PEAT_DRY) {
			return farmlandPeatWet == null ? farmlandPeatWet = new ItemStack(DynamicEarth.farmland.blockID, 1, PEAT_WET) : farmlandPeatWet;
		}
		if (DynamicEarth.includeFertileSoil
		&& metadata == SOIL_DRY) {
			return farmlandSoilWet == null ? farmlandSoilWet = new ItemStack(DynamicEarth.farmland.blockID, 1, SOIL_WET) : farmlandSoilWet;
		}
		return null;
	}
	        
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ItemStack itemStack = BlockDynamicFarmland.getAssociatedBlock(metadata);
        Block block = Block.blocksList[itemStack.itemID];
        if (block != null) {
        	return block.getBlockDropped(world, x, y, z, itemStack.getItemDamage(), fortune);
        }
        return new ArrayList<ItemStack>();
    }
    
    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
		return false;
	}
    
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
    	switch (metadata) {
		case PEAT_1:
		case PEAT_2:
		case PEAT_3:
		case PEAT_4:
		case PEAT_5:
		case PEAT_6:
		case PEAT_7: return BlockDynamicFarmland.getAssociatedBlock(PEAT_WET);
    	}
		return BlockDynamicFarmland.getAssociatedBlock(metadata);
	}
	    
    private static int getFertility(int metadata) {
    	switch (metadata) {
    	case SOIL_DRY: return 1;
    	case SOIL_WET: return 2;
    	default: return 0;
        }
    }
    
    private static ItemStack getAssociatedBlock(int metadata) {
    	if (metadata == MUD) {
    		return mud == null ? mud = new ItemStack(DynamicEarth.mud.blockID, 1, BlockMud.NORMAL) : mud;
    	}
    	if (DynamicEarth.includePeat) {
    		switch (metadata) {
	    	case PEAT_DRY: return peatDry == null ? peatDry = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.DRY) : peatDry;
			case PEAT_WET: return peatWet == null ? peatWet = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.WET) : peatWet;
			case PEAT_1: return peat1 == null ? peat1 = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.ONE_EIGHTH) : peat1;
			case PEAT_2: return peat2 == null ? peat2 = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.TWO_EIGHTHS) : peat2;
			case PEAT_3: return peat3 == null ? peat3 = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.THREE_EIGHTHS) : peat3;
			case PEAT_4: return peat4 == null ? peat4 = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.FOUR_EIGHTHS) : peat4;
			case PEAT_5: return peat5 == null ? peat5 = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.FIVE_EIGHTHS) : peat5;
			case PEAT_6: return peat6 == null ? peat6 = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.SIX_EIGHTHS) : peat6;
			case PEAT_7: return peat7 == null ? peat7 = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.SEVEN_EIGHTHS) : peat7;
    		}
    	}
    	if (DynamicEarth.includeFertileSoil
    	&& (metadata == SOIL_DRY || metadata == SOIL_WET)) {
    		return soil == null ? soil = new ItemStack(DynamicEarth.fertileSoil.blockID, 1, BlockFertileSoil.SOIL) : soil;
    	}
    	if (DynamicEarth.includeSandySoil
    	&& metadata == SANDY_DRY) {
    		return sandy == null ? sandy = new ItemStack(DynamicEarth.sandySoil.blockID, 1, BlockSandySoil.DIRT) : sandy;
    	}
    	return dirt == null ? dirt = new ItemStack(Block.dirt.blockID, 1, 0) : dirt;
    }
}
