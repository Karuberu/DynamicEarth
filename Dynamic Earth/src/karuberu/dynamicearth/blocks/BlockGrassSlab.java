package karuberu.dynamicearth.blocks;

import java.util.List;
import java.util.Random;

import karuberu.core.MCHelper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.ITextureOverlay;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGrassSlab extends BlockHalfSlab implements ITextureOverlay, IGrassyBlock, ISoil {

    public static final String[]
    	slabType = new String[] {"grass", "mycelium"};
    public static final int
    	GRASS = 0,
    	MYCELIUM = 1;
    private Icon
    	textureGrassSide,
    	textureMyceliumSide,
    	textureSnowySide,
    	textureOverlayGrassSide;
    public static CreativeTabs
		creativeTab = CreativeTabs.tabBlock;

    public BlockGrassSlab(int id, boolean isDoubleSlab) {
		super(id, isDoubleSlab, Material.grass);
		this.setHardness(0.6F);
		this.setStepSound(Block.soundGrassFootstep);
        this.setCreativeTab(creativeTab);
        this.setTickRandomly(true);
        Block.useNeighborBrightness[id] = true;
	}
    
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.textureGrassSide = iconRegister.registerIcon(BlockTexture.GRASSSLAB.getIconPath());
		this.textureMyceliumSide = iconRegister.registerIcon(BlockTexture.MYCELIUMSLAB.getIconPath());
		this.textureSnowySide = iconRegister.registerIcon(BlockTexture.GRASSSLABSNOWY.getIconPath());
		this.textureOverlayGrassSide = iconRegister.registerIcon(BlockTexture.GRASSSLABOVERLAY.getIconPath());
	}
	
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	if (this.isOpaqueCube()) {
    		int metadata = world.getBlockMetadata(x, y, z);
    		switch (MCHelper.getSlabMetadata(metadata)) {
    		case GRASS:
    			world.setBlock(x, y, z, Block.grass.blockID, 0, MCHelper.UPDATE_WITHOUT_NOTIFY_REMOTE);
    			break;
    		case MYCELIUM:
    			world.setBlock(x, y, z, Block.mycelium.blockID, 0, MCHelper.UPDATE_WITHOUT_NOTIFY_REMOTE);
    			break;
    		}
    	}
    }

    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (world.getBlockLightValue(x, y + 1, z) < 4
    	&& world.getBlockLightOpacity(x, y + 1, z) > 2) {
    		int metadata = world.getBlockMetadata(x, y, z);
            world.setBlock(x, y, z, DynamicEarth.dirtSlab.blockID, MCHelper.convertSlabMetadata(metadata, BlockDirtSlab.DIRT), MCHelper.NOTIFY_WITHOUT_UPDATE);
		}
    }
	
	@Override
	public boolean canSpread(World world, int x, int y, int z) {
		EnumGrassType type = this.getType(world, x, y, z);
		if ((type == EnumGrassType.GRASS || type == EnumGrassType.MYCELIUM)
		&& world.getBlockLightValue(x, y + 1, z) >= 9) {
			return true;
		}
		return false;
	}
	
	@Override
	public void tryToGrow(World world, int x, int y, int z, EnumGrassType type) {}

	@Override
	public EnumGrassType getType(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
        switch (MCHelper.getSlabMetadata(metadata)) {
    	case GRASS:
    		return EnumGrassType.GRASS;
    	case MYCELIUM:
    		return EnumGrassType.MYCELIUM;
    	default:
    		return EnumGrassType.DIRT;
        }
	}
	
	@Override
	public ItemStack getBlockForType(World world, int x, int y, int z, EnumGrassType type) {
		return ((IGrassyBlock)DynamicEarth.dirtSlab).getBlockForType(world, x, y, z, type);
	}
	
	@Override
	public boolean willForcePlantToStay(World world, int x, int y, int z, IPlantable plant) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (MCHelper.isTopSlab(metadata)) {
			switch (MCHelper.getSlabMetadata(metadata)) {
			case MYCELIUM:
				if (plant.getPlantType(world, x, y + 1, z) == EnumPlantType.Cave) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isLightSufficient(World world, int x, int y, int z) {
		return world.getBlockLightValue(x, y + 1, z) >= 4
        && world.getBlockLightOpacity(x, y + 1, z) <= 2;
	}
	
	@Override
	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		return DynamicEarth.dirtSlab.canSustainPlant(world, x, y, z, direction, plant);
	}
	    
    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
    	int metadata = blockAccess.getBlockMetadata(x, y, z);
    	if (side != MCHelper.SIDE_BOTTOM) {
    		if (MCHelper.isBottomSlab(metadata)) {
    			if (this.isSnowAdjacent(blockAccess, x, y, z)) {
    				if (side == MCHelper.SIDE_TOP) {
    					return Block.snow.getBlockTextureFromSide(side);
    				} else {
    					return this.textureSnowySide;
    				}
    			}
    		} else {
    			if (side != MCHelper.SIDE_TOP) {
		        	Material material = blockAccess.getBlockMaterial(x, y + 1, z);
		        	if (material == Material.snow || material == Material.craftedSnow) {
		        		return this.textureSnowySide;
		        	}
	        	}
	        }
    	}
    	return this.getIcon(side, metadata);
    }
    
    @Override
    public Icon getIcon(int side, int metadata) {
        switch (MCHelper.getSlabMetadata(metadata)) {
    	case GRASS:
    		switch (side) {
    		case MCHelper.SIDE_TOP: return Block.grass.getBlockTextureFromSide(side);
    		case MCHelper.SIDE_BOTTOM: return Block.dirt.getBlockTextureFromSide(side);
    		default: return this.textureGrassSide;
    		}
    	case MYCELIUM:
    		switch (side) {
    		case MCHelper.SIDE_TOP: return Block.mycelium.getBlockTextureFromSide(side);
    		case MCHelper.SIDE_BOTTOM: return Block.dirt.getBlockTextureFromSide(side);
    		default: return this.textureMyceliumSide;
    		}
        }
        return super.getIcon(side, metadata);
    }
    
    private boolean isSnowAdjacent(IBlockAccess blockAccess, int x, int y, int z) {
    	if (!DynamicEarth.showSnowyBottomSlabs) {
    		return false;
    	}
    	final int[][] surroundingBlocks = new int[][] {
    		{x-1, y, z},
    		{x+1, y, z},
    		{x, y, z-1},
    		{x, y, z+1}
		};
    	Material material;
		for (int i = 0; i < surroundingBlocks.length; i++) {
			material = blockAccess.getBlockMaterial(surroundingBlocks[i][0], y, surroundingBlocks[i][2]);
        	if (material == Material.snow || material == Material.craftedSnow) {
        		return true;
        	}
		}
		return false;
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int metadata, int side, int pass) {
		if (side == MCHelper.SIDE_TOP
		|| side == MCHelper.SIDE_BOTTOM
		|| pass != 1) {
			return null;
		} else {
			switch (MCHelper.getSlabMetadata(metadata)) {
			case GRASS:
				if (MCHelper.isBottomSlab(metadata)) {
					if (!this.isSnowAdjacent(blockAccess, x, y, z)) {
						return this.textureOverlayGrassSide;
					}
					return null;
				} else {
		        	Material material = blockAccess.getBlockMaterial(x, y + 1, z);
		        	if (material != Material.snow && material != Material.craftedSnow) {
		        		return this.textureOverlayGrassSide;
		        	}
		        	return null;
				}
			default: return null;
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getNumberOfPasses(int metadata) {
		switch(MCHelper.getSlabMetadata(metadata)) {
		case GRASS: return RenderBlocks.fancyGrass ? 1 : 0;
		default: return 0;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeTexture(IBlockAccess blockAccess, int x, int y, int z, int metadata, int side, int pass) {
		if (MCHelper.getSlabMetadata(metadata) == GRASS) {
			switch(pass) {
			case 0:
				if (side == MCHelper.SIDE_TOP) {
					if (MCHelper.isBottomSlab(metadata)
					&& this.isSnowAdjacent(blockAccess, x, y, z)) {
						return false;
					}
					return true;
				}
				return false;
			case 1:
				if (side != MCHelper.SIDE_TOP
				&& side != MCHelper.SIDE_BOTTOM) {
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeInventoryBaseTexture(int side, int metadata) {
		if (MCHelper.getSlabMetadata(metadata) == GRASS) {
			switch(side) {
			case MCHelper.SIDE_TOP: return true;
			default: return false;
			}
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return DynamicEarth.overlayBlockRenderID;
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
    	int metadata = blockAccess.getBlockMetadata(x, y, z);
    	if (MCHelper.getSlabMetadata(metadata) == GRASS) {
	    	int r = 0;
	        int g = 0;
	        int b = 0;
	        for (int zi = -1; zi <= 1; ++zi) {
	            for (int xi = -1; xi <= 1; ++xi) {
	                int biomeGrassColor = blockAccess.getBiomeGenForCoords(x + xi, z + zi).getBiomeGrassColor();
	                r += (biomeGrassColor & 0xFF0000) >> 16;
	                g += (biomeGrassColor & 0x00FF00) >> 8;
	                b += (biomeGrassColor & 0x0000FF);
	            }
	        }
	        return (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
    	} else {
    		return super.colorMultiplier(blockAccess, x, y, z);
    	}
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockColor() {
        double temperature = 0.5D;
        double humidity = 1.0D;
        return ColorizerGrass.getGrassColor(temperature, humidity);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(int metadata) {
    	if (MCHelper.getSlabMetadata(metadata) == GRASS) {
    		return this.getBlockColor();
    	} else {
    		return super.getBlockColor();
    	}
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        super.randomDisplayTick(world, x, y, z, random);
        int metadata = world.getBlockMetadata(x, y, z);
        if (MCHelper.getSlabMetadata(metadata) == MYCELIUM
        && random.nextInt(10) == 0) {
        	float yAdjustment = 0;
        	if (MCHelper.isBottomSlab(metadata)) { yAdjustment = -0.5F; }
            world.spawnParticle("townaura", (double)((float)x + random.nextFloat()), (double)((float)y + 1.1F + yAdjustment), (double)((float)z + random.nextFloat()), 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public boolean canSilkHarvest(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
    	return true;
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
    	return DynamicEarth.dirtSlab.blockID;
    }
    
    @Override
    public int damageDropped(int par1) {
        return BlockDirtSlab.DIRT;
    }
    
    @Override
    protected ItemStack createStackedBlock(int metadata) {
        return new ItemStack(DynamicEarth.grassSlab.blockID, 1, metadata & 7);
    }
    
	@Override
	public String getFullSlabName(int metadata) {
        if (metadata < 0 || metadata >= slabType.length) {
            metadata = 0;
        }
        return super.getUnlocalizedName() + "." + slabType[metadata];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
        if (blockId != DynamicEarth.grassDoubleSlab.blockID) {
            int numSubBlocks = BlockGrassSlab.slabType.length;
            for (int i = 0; i < numSubBlocks; ++i) {
                list.add(new ItemStack(blockId, 1, i));
            }
        }
    }
	
    @SideOnly(Side.CLIENT)
    @Override
    public int idPicked(World world, int x, int y, int z) {
    	return this.blockID;
    }
    
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(this.idPicked(world, x, y, z), 1, world.getBlockMetadata(x, y, z));
	}
}
