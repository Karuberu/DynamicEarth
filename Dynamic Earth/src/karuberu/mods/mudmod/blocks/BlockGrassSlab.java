package karuberu.mods.mudmod.blocks;

import java.util.List;
import java.util.Random;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.Reference;
import karuberu.mods.mudmod.client.ITextureOverlay;
import karuberu.mods.mudmod.client.ICamouflageBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGrassSlab extends BlockHalfSlab implements ITextureOverlay {

    public static final String[]
    	slabType = new String[] {"grass", "mycelium"};
    public static final int
    	GRASS = 0,
    	MYCELIUM = 1;
    private static final int
    	TEXTURE_GRASS = MudMod.BlockTexture.GRASS.ordinal(),
    	TEXTURE_DIRT = MudMod.BlockTexture.DIRT.ordinal(),
    	TEXTURE_GRASS_SIDE = MudMod.BlockTexture.GRASSSLABSIDE.ordinal(),
    	TEXTURE_GRASS_SIDE_OVERLAY = MudMod.BlockTexture.GRASSSLABSIDEOVERLAY.ordinal(),
    	TEXTURE_SNOW = MudMod.BlockTexture.SNOW.ordinal(),
    	TEXTURE_SNOWY_SIDE = MudMod.BlockTexture.GRASSSLABSNOWSIDE.ordinal(),
    	TEXTURE_MYCELIUM = MudMod.BlockTexture.MYCELIUM.ordinal(),
    	TEXTURE_MYCELIUM_SIDE = MudMod.BlockTexture.MYCELIUMSLABSIDE.ordinal();

    public BlockGrassSlab(int id, boolean isDoubleSlab) {
		super(id, isDoubleSlab, Material.grass);
		this.setHardness(0.6F);
		this.setStepSound(Block.soundGrassFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setTickRandomly(true);
        this.useNeighborBrightness[id] = true;
        this.setTextureFile(MudMod.terrainFile);
	}
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	if (this.isOpaqueCube()) {
    		int metadata = world.getBlockMetadata(x, y, z);
    		switch (metadata & 7) {
    		case GRASS:
    			world.setBlock(x, y, z, Block.grass.blockID);
    			break;
    		case MYCELIUM:
    			world.setBlock(x, y, z, Block.mycelium.blockID);
    			break;
    		}
    	}
    }

    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (world.getBlockLightValue(x, y + 1, z) < 4
    	&& world.getBlockLightOpacity(x, y + 1, z) > 2) {
    		int metadata = world.getBlockMetadata(x, y, z);
            world.setBlockAndMetadataWithNotify(x, y, z, MudMod.dirtSlab.blockID, metadata - (metadata & 7) + BlockDirtSlab.DIRT);
		} else {
			this.tryToSpread(world, x, y, z, random);
		}
    }
	
    private void tryToSpread(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
        	if (world.getBlockLightValue(x, y + 1, z) >= 9 && world.getBlockLightOpacity(x, y + 1, z) <= 2) {
                for (int i = 0; i < 4; i++) {
                    int xi = x + random.nextInt(3) - 1,
                    	yi = y + random.nextInt(5) - 3,
                    	zi = z + random.nextInt(3) - 1;
                    int blockId = world.getBlockId(xi, yi, zi);
                    if (world.getBlockLightValue(xi, yi + 1, zi) >= 4
                    && world.getBlockLightOpacity(xi, yi + 1, zi) <= 2) {
                    	int metadata = world.getBlockMetadata(x, y, z);
                    	switch (metadata & 7) {
                    	case GRASS:
                    		if (blockId == Block.dirt.blockID) {
	                    		world.setBlockWithNotify(xi, yi, zi, Block.grass.blockID);
                    		} else if (blockId == MudMod.dirtSlab.blockID) {
	                    		world.setBlockAndMetadataWithNotify(xi, yi, zi, this.blockID, metadata - BlockDirtSlab.DIRT + GRASS);
                    		}
                    		break;
                    	case MYCELIUM:
                    		if (blockId == Block.dirt.blockID) {
                        		world.setBlockWithNotify(xi, yi, zi, Block.mycelium.blockID);
                    		} else if (blockId == MudMod.dirtSlab.blockID) {
                    			world.setBlockAndMetadataWithNotify(xi, yi, zi, this.blockID, metadata - BlockDirtSlab.DIRT + MYCELIUM);
                    		}
                    		break;
                    	}
                    }
                }
            }
        }
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
    	int metadata = blockAccess.getBlockMetadata(x, y, z);
    	if ((metadata & 7) == GRASS
    	&& side != Reference.SIDE_BOTTOM) {
    		switch (metadata & 8) {
    		case Reference.BOTTOM_SLAB:
    			if (this.isSnowAdjacent(blockAccess, x, y, z)) {
    				if (side == Reference.SIDE_TOP) {
    					return TEXTURE_SNOW;
    				} else {
    					return TEXTURE_SNOWY_SIDE;
    				}
    			}
    		case Reference.TOP_SLAB:
    			if (side != Reference.SIDE_TOP) {
		        	Material material = blockAccess.getBlockMaterial(x, y + 1, z);
		        	if (material == Material.snow || material == Material.craftedSnow) {
		        		return TEXTURE_SNOWY_SIDE;
		        	}
	        	}
	        }
    	}
    	return getBlockTextureFromSideAndMetadata(side, metadata);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
        switch (metadata & 7) {
    	case GRASS:
    		switch (side) {
    		case Reference.SIDE_TOP: return TEXTURE_GRASS;
    		case Reference.SIDE_BOTTOM: return TEXTURE_DIRT;
    		default: return TEXTURE_GRASS_SIDE;
    		}
    	case MYCELIUM:
    		switch (side) {
    		case Reference.SIDE_TOP: return TEXTURE_MYCELIUM;
    		case Reference.SIDE_BOTTOM: return TEXTURE_DIRT;
    		default: return TEXTURE_MYCELIUM_SIDE;
    		}
        }
        return TEXTURE_DIRT;
    }
    
    private boolean isSnowAdjacent(IBlockAccess blockAccess, int x, int y, int z) {
    	if (!MudMod.showSnowyBottomSlabs) {
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
	public int getOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		if (side == Reference.SIDE_TOP
		|| side == Reference.SIDE_BOTTOM) {
			return -1;
		} else {
			int metadata = blockAccess.getBlockMetadata(x, y, z);
			switch (metadata & 7) {
			case GRASS:
				switch (metadata & 8) {
				case Reference.BOTTOM_SLAB:
					if (!this.isSnowAdjacent(blockAccess, x, y, z)) {
						return TEXTURE_GRASS_SIDE_OVERLAY;
					}
					return -1;
				case Reference.TOP_SLAB:
		        	Material material = blockAccess.getBlockMaterial(x, y + 1, z);
		        	if (material != Material.snow && material != Material.craftedSnow) {
		        		return TEXTURE_GRASS_SIDE_OVERLAY;
		        	}
		        	return -1;
				}
			default: return -1;
			}
		}
	}
	@Override
	@SideOnly(Side.CLIENT)
	public boolean doTextureOverlay(int metadata) {
		switch(metadata & 7) {
		case GRASS: return RenderBlocks.fancyGrass;
		default: return false;
		}
	}
	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeBaseTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int metadata = blockAccess.getBlockMetadata(x, y, z);
		if ((metadata & 7) == GRASS) {
			switch(side) {
			case Reference.SIDE_TOP:
				if ((metadata & 8) == Reference.BOTTOM_SLAB
				&& this.isSnowAdjacent(blockAccess, x, y, z)) {
					return false;
				}
				return true;
			default: return false;
			}
		}
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeInventoryBaseTexture(int side, int metadata) {
		if ((metadata & 7) == GRASS) {
			switch(side) {
			case Reference.SIDE_TOP: return true;
			default: return false;
			}
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return MudMod.overlayBlockRenderID;
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
    	int metadata = blockAccess.getBlockMetadata(x, y, z);
    	if ((metadata & 7) == GRASS) {
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
    	if ((metadata & 7) == GRASS) {
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
        if ((metadata & 7) == MYCELIUM
        && random.nextInt(10) == 0) {
        	float yAdjustment = 0;
        	if ((metadata & 8) == 0) { yAdjustment = -0.5F; }
            world.spawnParticle("townaura", (double)((float)x + random.nextFloat()), (double)((float)y + 1.1F + yAdjustment), (double)((float)z + random.nextFloat()), 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public boolean canSilkHarvest(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
    	return true;
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
    	return MudMod.dirtSlab.blockID;
    }
    
    @Override
    protected ItemStack createStackedBlock(int metadata) {
        return new ItemStack(MudMod.grassSlab.blockID, 1, metadata & 7);
    }
    
	@Override
	public String getFullSlabName(int metadata) {
        if (metadata < 0 || metadata >= slabType.length) {
            metadata = 0;
        }
        return super.getBlockName() + "." + slabType[metadata];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
        if (blockId != MudMod.grassDoubleSlab.blockID) {
            int numSubBlocks = this.slabType.length;
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
}
