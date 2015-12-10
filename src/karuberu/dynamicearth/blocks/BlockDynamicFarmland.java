package karuberu.dynamicearth.blocks;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import karuberu.core.util.Helper;
import karuberu.core.util.block.BlockSide;
import karuberu.core.util.client.render.ITextureOverlay;
import karuberu.core.util.client.render.RenderLayeredBlock;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.GameruleHelper;
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
		FERTILE_DRY = 3,
		FERTILE_WET = 4,
		SANDY_DRY = 5,
		GLOWING_DRY = 6,
		GLOWING_WET = 7,
		PEAT_1 = 9,
		PEAT_2 = PEAT_1 + 1,
		PEAT_3 = PEAT_2 + 1,
		PEAT_4 = PEAT_3 + 1,
		PEAT_5 = PEAT_4 + 1,
		PEAT_6 = PEAT_5 + 1,
		PEAT_7 = PEAT_6 + 1;
	protected Icon
		textureFarmlandDry,
		textureFarmlandWet,
		texturePeatFarmlandDry,
		texturePeatFarmlandWet;
	protected ItemStack
		dirtStack,
		mudStack,
		peatDryStack,
		peatWetStack,
		peat1Stack,
		peat2Stack,
		peat3Stack,
		peat4Stack,
		peat5Stack,
		peat6Stack,
		peat7Stack,
		fertileSoilStack,
		sandySoilStack,
		glowingSoilStack,
		farmlandPeatDryStack,
		farmlandPeatWetStack,
		farmlandFertileDryStack,
		farmlandFertileWetStack,
		farmlandGlowingDryStack,
		farmlandGlowingWetStack;

	public BlockDynamicFarmland(int id) {
		super(id, Material.ground);
		this.setHydrateRadius(4, 0, 4);
		this.setSimpleHydration(true);
		this.setStepSound(Block.soundGravelFootstep);
		this.setHardness(0.4F);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        this.setLightOpacity(255);
		this.setTickRandomly(true);
		this.setUnlocalizedName("dynamicFarmland");
		Block.useNeighborBrightness[id] = true;
	}
	
	protected void initializeItemStacks() {
    	this.dirtStack = new ItemStack(Block.dirt.blockID, 1, 0);
		if (DynamicEarth.includePeat) {
	    	this.farmlandPeatDryStack = new ItemStack(DynamicEarth.farmland.blockID, 1, PEAT_DRY);
	    	this.farmlandPeatWetStack = new ItemStack(DynamicEarth.farmland.blockID, 1, PEAT_WET);
		}
		if (DynamicEarth.includeFertileSoil) {
	    	this.fertileSoilStack = new ItemStack(DynamicEarth.fertileSoil.blockID, 1, DynamicEarth.fertileSoil.DIRT);
	    	this.farmlandFertileDryStack = new ItemStack(DynamicEarth.farmland.blockID, 1, FERTILE_DRY);
	    	this.farmlandFertileWetStack = new ItemStack(DynamicEarth.farmland.blockID, 1, FERTILE_WET);
		}
    	if (DynamicEarth.includeSandySoil) {
        	this.sandySoilStack = new ItemStack(DynamicEarth.sandySoil.blockID, 1, DynamicEarth.sandySoil.DIRT);
    	}
    	if (DynamicEarth.includeGlowingSoil) {
        	this.glowingSoilStack = new ItemStack(DynamicEarth.glowingSoil.blockID, 1, DynamicEarth.glowingSoil.DIRT);
        	this.farmlandGlowingDryStack = new ItemStack(DynamicEarth.farmland.blockID, 1, GLOWING_DRY);
        	this.farmlandGlowingWetStack = new ItemStack(DynamicEarth.farmland.blockID, 1, GLOWING_WET);
    	}
    	if (DynamicEarth.includeMud) {
        	this.mudStack = new ItemStack(DynamicEarth.mud.blockID, 1, DynamicEarth.mud.NORMAL);
    	}
    	if (DynamicEarth.includePeat) {
        	this.peatDryStack = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.DRY);
        	this.peatWetStack = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.WET);
        	this.peat1Stack = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.ONE_EIGHTH);
        	this.peat2Stack = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.TWO_EIGHTHS);
        	this.peat3Stack = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.THREE_EIGHTHS);
        	this.peat4Stack = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.FOUR_EIGHTHS);
        	this.peat5Stack = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.FIVE_EIGHTHS);
        	this.peat6Stack = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.SIX_EIGHTHS);
        	this.peat7Stack = new ItemStack(DynamicEarth.peat.blockID, 1, BlockPeat.SEVEN_EIGHTHS);
    	}
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.textureFarmlandDry = iconRegister.registerIcon("farmland_dry");
		this.textureFarmlandWet = iconRegister.registerIcon("farmland_wet");
		this.texturePeatFarmlandDry = iconRegister.registerIcon(BlockTexture.PEATFARMLAND.getIconPath());
		this.texturePeatFarmlandWet = iconRegister.registerIcon(BlockTexture.PEATFARMLANDWET.getIconPath());
	}

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata) {
    	if (side == BlockSide.TOP.code) {
    		switch (metadata) {
    		case SANDY_DRY:
    		case FERTILE_DRY:
    		case GLOWING_DRY: return this.textureFarmlandDry;
    		case MUD:
    		case FERTILE_WET:
    		case GLOWING_WET: return this.textureFarmlandWet;
    		case PEAT_1:
    		case PEAT_2:
    		case PEAT_3:
    		case PEAT_4:
    		case PEAT_5:
    		case PEAT_6:
    		case PEAT_7:
    		case PEAT_WET: return this.texturePeatFarmlandWet;
    		case PEAT_DRY: return this.texturePeatFarmlandDry;
    		default: return Block.dirt.getBlockTextureFromSide(side);
    		}
    	} else {
    		ItemStack itemStack = this.getAssociatedBlock(metadata);
    		Block block = Block.blocksList[itemStack.itemID];
    		return block == null ? Block.dirt.getIcon(side, 0) : block.getIcon(side, itemStack.getItemDamage());
    	}
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int metadata, int side, int pass) {
		ItemStack itemStack = this.getAssociatedBlock(blockAccess.getBlockMetadata(x, y, z));
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
	public int getNumberOfAdditionalRenderPasses(int metadata) {
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
		return RenderLayeredBlock.renderID;
	}
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    	return AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 1 - 0.125F, z + 1);
    }
	
	@Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        if (block != null && block != this) {
            return block.getLightValue(world, x, y, z);
        }
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == BlockDynamicFarmland.GLOWING_DRY
        || metadata == BlockDynamicFarmland.GLOWING_WET) {
        	return BlockGlowingSoil.LIGHT_LEVEL;
        }
        return super.getLightValue(world, x, y, z);
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
    		world.setBlock(x, y, z, Block.tilledField.blockID, 1, Helper.NOTIFY_AND_UPDATE_REMOTE);
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
            || GameruleHelper.mobGriefing(world)) {
            	entity.posY += 0.125F;
        		this.trample(world, x, y, z);
            }
        }
    }
    
    private void trample(World world, int x, int y, int z) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	ItemStack itemStack = this.getAssociatedBlock(metadata);
   		world.setBlock(x, y, z, itemStack.itemID, itemStack.getItemDamage(), Helper.NOTIFY_AND_UPDATE_REMOTE);
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
    	case FERTILE_WET:
    	case GLOWING_WET: return true;
    	default: return false;
    	}
    }
    
	@Override
	protected ItemStack getDryBlock(int metadata) {
		if (this.dirtStack == null) {
			this.initializeItemStacks();
		}
		if (DynamicEarth.includePeat
		&& metadata == PEAT_WET) {
			return farmlandPeatDryStack;
		}
		if (DynamicEarth.includeFertileSoil
		&& metadata == FERTILE_WET) {
			return farmlandFertileDryStack;
		}
		if (DynamicEarth.includeGlowingSoil
		&& metadata == GLOWING_WET) {
			return farmlandGlowingDryStack;
		}
		return this.getAssociatedBlock(metadata);
	}
	
	@Override
	protected ItemStack getWetBlock(int metadata) {
		if (this.dirtStack == null) {
			this.initializeItemStacks();
		}
		if (DynamicEarth.includePeat
		&& metadata == PEAT_DRY) {
			return farmlandPeatWetStack;
		}
		if (DynamicEarth.includeFertileSoil
		&& metadata == FERTILE_DRY) {
			return farmlandFertileWetStack;
		}
		if (DynamicEarth.includeGlowingSoil
		&& metadata == GLOWING_DRY) {
			return farmlandGlowingWetStack;
		}
		return null;
	}
	        
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
        ItemStack itemStack = this.getAssociatedBlock(metadata);
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
		case PEAT_7: return this.getAssociatedBlock(PEAT_WET);
    	}
		return this.getAssociatedBlock(metadata);
	}
	
    private static int getFertility(int metadata) {
    	switch (metadata) {
    	case FERTILE_DRY: return 1;
    	case FERTILE_WET: return 2;
    	default: return 0;
        }
    }
    
    protected ItemStack getAssociatedBlock(int metadata) {
		if (this.dirtStack == null) {
			this.initializeItemStacks();
		}
    	if (DynamicEarth.includeMud
    	&& metadata == MUD) {
    		return mudStack;
    	}
    	if (DynamicEarth.includePeat) {
    		switch (metadata) {
	    	case PEAT_DRY: return peatDryStack;
			case PEAT_WET: return peatWetStack;
			case PEAT_1: return peat1Stack;
			case PEAT_2: return peat2Stack;
			case PEAT_3: return peat3Stack;
			case PEAT_4: return peat4Stack;
			case PEAT_5: return peat5Stack;
			case PEAT_6: return peat6Stack;
			case PEAT_7: return peat7Stack;
    		}
    	}
    	if (DynamicEarth.includeFertileSoil
    	&& (metadata == FERTILE_DRY || metadata == FERTILE_WET)) {
    		return fertileSoilStack;
    	}
    	if (DynamicEarth.includeSandySoil
    	&& metadata == SANDY_DRY) {
    		return sandySoilStack;
    	}
    	if (DynamicEarth.includeGlowingSoil
    	&& (metadata == GLOWING_DRY || metadata == GLOWING_WET)) {
    		return glowingSoilStack;
    	}
    	return this.dirtStack;
    }
}
