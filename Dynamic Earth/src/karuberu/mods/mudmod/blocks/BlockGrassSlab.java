package karuberu.mods.mudmod.blocks;

import java.util.List;
import java.util.Random;

import karuberu.core.MCHelper;
import karuberu.core.event.INeighborBlockEventHandler;
import karuberu.core.event.NeighborBlockChangeEvent;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.ITextureOverlay;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGrassSlab extends BlockHalfSlab implements ITextureOverlay, INeighborBlockEventHandler {

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

    public BlockGrassSlab(int id, boolean isDoubleSlab) {
		super(id, isDoubleSlab, Material.grass);
		this.setHardness(0.6F);
		this.setStepSound(Block.soundGrassFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setTickRandomly(true);
        this.useNeighborBrightness[id] = true;
	}
    
	@Override
	public void func_94332_a(IconRegister iconRegister) {
		this.field_94336_cN = this.textureGrassSide = TextureManager.instance().getBlockTexture(Texture.GRASSSLAB);
		this.textureMyceliumSide = TextureManager.instance().getBlockTexture(Texture.MYCELIUMSLAB);
		this.textureSnowySide = TextureManager.instance().getBlockTexture(Texture.GRASSSLABSNOWY);
		this.textureOverlayGrassSide = TextureManager.instance().getBlockTexture(Texture.GRASSSLABOVERLAY);
	}
	
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
    	if (this.isOpaqueCube()) {
    		int metadata = world.getBlockMetadata(x, y, z);
    		switch (MCHelper.getSlabMetadata(metadata)) {
    		case GRASS:
    			world.setBlockAndMetadataWithNotify(x, y, z, Block.grass.blockID, 0, MCHelper.UPDATE_WITHOUT_NOTIFY_REMOTE);
    			break;
    		case MYCELIUM:
    			world.setBlockAndMetadataWithNotify(x, y, z, Block.mycelium.blockID, 0, MCHelper.UPDATE_WITHOUT_NOTIFY_REMOTE);
    			break;
    		}
    	}
    }

    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (world.getBlockLightValue(x, y + 1, z) < 4
    	&& world.getBlockLightOpacity(x, y + 1, z) > 2) {
    		int metadata = world.getBlockMetadata(x, y, z);
            world.setBlockAndMetadataWithNotify(x, y, z, MudMod.dirtSlab.blockID, MCHelper.convertSlabMetadata(metadata, BlockDirtSlab.DIRT), MCHelper.NOTIFY_WITHOUT_UPDATE);
		} else {
			this.tryToSpread(world, x, y, z, random);
		}
    }
    
	@Override
	public void handleNeighborBlockChangeEvent(NeighborBlockChangeEvent event) {
		int metadata = event.world.getBlockMetadata(event.x, event.y, event.z);
		if (MudMod.enableGrassBurning
		&& event.side == MCHelper.SIDE_TOP
		&& MCHelper.isTopSlab(metadata)) {
			Block block = Block.blocksList[event.neighborBlockID];
			if (block != null
			&& (block.blockMaterial == Material.fire || block.blockMaterial == Material.lava)) {
				event.world.setBlockAndMetadataWithNotify(event.x, event.y, event.z, MudMod.dirtSlab.blockID, MCHelper.getSlabType(metadata), MCHelper.NOTIFY_AND_UPDATE_REMOTE);
			}
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
                    	switch (MCHelper.getSlabMetadata(metadata)) {
                    	case GRASS:
                    		if (blockId == Block.dirt.blockID) {
	                    		world.setBlockAndMetadataWithNotify(xi, yi, zi, Block.grass.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
                    		} else if (blockId == MudMod.dirtSlab.blockID) {
	                    		world.setBlockAndMetadataWithNotify(xi, yi, zi, this.blockID, MCHelper.convertSlabMetadata(world.getBlockMetadata(xi, yi, zi), GRASS), MCHelper.NOTIFY_AND_UPDATE_REMOTE);
                    		}
                    		break;
                    	case MYCELIUM:
                    		if (blockId == Block.dirt.blockID) {
                        		world.setBlockAndMetadataWithNotify(xi, yi, zi, Block.mycelium.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
                    		} else if (blockId == MudMod.dirtSlab.blockID) {
                    			world.setBlockAndMetadataWithNotify(xi, yi, zi, this.blockID, MCHelper.convertSlabMetadata(world.getBlockMetadata(xi, yi, zi), MYCELIUM), MCHelper.NOTIFY_AND_UPDATE_REMOTE);
                    		}
                    		break;
                    	}
                    }
                }
            }
        }
	}
    
    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
    	int metadata = blockAccess.getBlockMetadata(x, y, z);
    	if (side != MCHelper.SIDE_BOTTOM) {
    		switch (MCHelper.getSlabType(metadata)) {
    		case MCHelper.BOTTOM_SLAB:
    			if (this.isSnowAdjacent(blockAccess, x, y, z)) {
    				if (side == MCHelper.SIDE_TOP) {
    					return Block.snow.getBlockTextureFromSide(side);
    				} else {
    					return this.textureSnowySide;
    				}
    			}
    		case MCHelper.TOP_SLAB:
    			if (side != MCHelper.SIDE_TOP) {
		        	Material material = blockAccess.getBlockMaterial(x, y + 1, z);
		        	if (material == Material.snow || material == Material.craftedSnow) {
		        		return this.textureSnowySide;
		        	}
	        	}
	        }
    	}
    	return this.getBlockTextureFromSideAndMetadata(side, metadata);
    }
    
    @Override
    public Icon getBlockTextureFromSideAndMetadata(int side, int metadata) {
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
        return super.getBlockTextureFromSideAndMetadata(side, metadata);
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
	public Icon getOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int side, int pass) {
		if (side == MCHelper.SIDE_TOP
		|| side == MCHelper.SIDE_BOTTOM
		|| pass != 1) {
			return null;
		} else {
			int metadata = blockAccess.getBlockMetadata(x, y, z);
			switch (metadata & 7) {
			case GRASS:
				switch (metadata & 8) {
				case MCHelper.BOTTOM_SLAB:
					if (!this.isSnowAdjacent(blockAccess, x, y, z)) {
						return this.textureOverlayGrassSide;
					}
					return null;
				case MCHelper.TOP_SLAB:
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
	public boolean willColorizeTexture(IBlockAccess blockAccess, int x, int y, int z, int side, int pass) {
		int metadata = blockAccess.getBlockMetadata(x, y, z);
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
		if ((metadata & 7) == GRASS) {
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
        return super.getUnlocalizedName() + "." + slabType[metadata];
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
