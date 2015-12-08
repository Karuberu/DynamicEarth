package karuberu.mods.mudmod.blocks;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.client.ITextureOverlay;
import karuberu.mods.mudmod.client.TextureManager;
import karuberu.mods.mudmod.client.TextureManager.Texture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class BlockSandySoil extends Block implements ITextureOverlay, ITillable, IGrassyBlock {
	public static final int
		DIRT = 0,
		GRASS = 1,
		MYCELIUM = 2;
	private Icon
		dirt,
		grassSide,
		myceliumSide,
		snowSide,
		grassSideOverlay;
	
	public BlockSandySoil(int id) {
		super(id, Material.ground);
		this.setHardness(0.4F);
		this.setStepSound(Block.soundGravelFootstep);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setTickRandomly(true);
	}
	
	@Override
    public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = this.dirt = TextureManager.instance().getBlockTexture(Texture.SANDYSOIL);
		this.grassSide = TextureManager.instance().getBlockTexture(Texture.SANDYGRASSSIDE);
		this.myceliumSide = TextureManager.instance().getBlockTexture(Texture.SANDYMYCELIUMSIDE);
		this.snowSide = TextureManager.instance().getBlockTexture(Texture.SANDYSNOWSIDE);
		this.grassSideOverlay = iconRegister.registerIcon("grass_side_overlay");
    }
    
    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
    	int metadata = blockAccess.getBlockMetadata(x, y, z);
    	if (metadata != DIRT
    	&& side != MCHelper.SIDE_BOTTOM
    	&& side != MCHelper.SIDE_TOP) {
        	Material material = blockAccess.getBlockMaterial(x, y + 1, z);
        	if (material == Material.snow || material == Material.craftedSnow) {
        		return this.snowSide;
        	}
    	}
    	return this.getIcon(side, metadata);
    }
    
    @Override
    public Icon getIcon(int side, int metadata) {
        switch (metadata) {
        case DIRT:
        	return dirt;
    	case GRASS:
    		switch (side) {
    		case MCHelper.SIDE_TOP: return Block.grass.getBlockTextureFromSide(side);
    		case MCHelper.SIDE_BOTTOM: return dirt;
    		default: return grassSide;
    		}
    	case MYCELIUM:
    		switch (side) {
    		case MCHelper.SIDE_TOP: return Block.mycelium.getBlockTextureFromSide(side);
    		case MCHelper.SIDE_BOTTOM: return dirt;
    		default: return myceliumSide;
    		}
        }
        return super.getIcon(side, metadata);
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int metadata, int side, int pass) {
		if (side == MCHelper.SIDE_TOP
		|| side == MCHelper.SIDE_BOTTOM
		|| pass != 1) {
			return null;
		} else {
        	Material material = blockAccess.getBlockMaterial(x, y + 1, z);
        	if (material != Material.snow && material != Material.craftedSnow) {
        		return this.grassSideOverlay;
        	}
        	return null;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getNumberOfPasses(int metadata) {
		switch(metadata) {
		case GRASS: return RenderBlocks.fancyGrass ? 1 : 0;
		default: return 0;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeTexture(IBlockAccess blockAccess, int x, int y, int z, int metadata, int side, int pass) {
		if (metadata == GRASS) {
			switch(pass) {
			case 0:
				if (side == MCHelper.SIDE_TOP) {
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
		if (metadata == GRASS) {
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
    	if (metadata == GRASS) {
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
    	if (metadata == GRASS) {
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
        if (metadata == MYCELIUM
        && random.nextInt(10) == 0) {
            world.spawnParticle("townaura", (double)((float)x + random.nextFloat()), (double)((float)y + 1.1F), (double)((float)z + random.nextFloat()), 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {
	    	int metadata = world.getBlockMetadata(x, y, z);
			if (metadata != DIRT) {
				if (world.getBlockLightValue(x, y + 1, z) < 4
				&& world.getBlockLightOpacity(x, y + 1, z) > 2) {
		            world.setBlock(x, y, z, MudMod.sandySoil.blockID, DIRT, MCHelper.NOTIFY_WITHOUT_UPDATE);
				}
			}
		}
		super.updateTick(world, x, y, z, random);
    }
    
    protected boolean isLightSufficient(World world, int x, int y, int z) {
    	return world.getBlockLightValue(x, y + 1, z) >= 4
        && world.getBlockLightOpacity(x, y + 1, z) <= 2;
    }
    
	@Override
	public void tryToGrow(World world, int x, int y, int z, EnumGrassType type) {
		if (this.getType(world, x, y, z) == EnumGrassType.DIRT
		&& this.isLightSufficient(world, x, y, z)) {
			switch (type) {
			case GRASS:
				world.setBlock(x, y, z, MudMod.sandySoil.blockID, GRASS, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
				break;
			case MYCELIUM:
				world.setBlock(x, y, z, MudMod.sandySoil.blockID, MYCELIUM, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
				break;
			default:
				return;
			}
		}
	}

	@Override
	public EnumGrassType getType(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
        switch (metadata) {
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
		switch (type) {
		case DIRT:
			return new ItemStack(MudMod.sandySoil, 1, DIRT);
		case GRASS:
			return new ItemStack(MudMod.sandySoil, 1, GRASS);
		case MYCELIUM:
			return new ItemStack(MudMod.sandySoil, 1, MYCELIUM);
		}
		return null;
	}
	
	@Override
	public boolean onTilled(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch (metadata) {
		case MYCELIUM:
			if (!MudMod.enableMyceliumTilling) {
				return false;
			}
		case DIRT:
		case GRASS:
			world.setBlock(x, y, z, MudMod.farmland.blockID, BlockMudModFarmland.SANDY_DRY, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch (metadata) {
		case DIRT:
		case GRASS:
			EnumPlantType type = plant.getPlantType(world, x, y + 1, z);
			if (type == EnumPlantType.Plains) {
				return true;
			} else if (type == EnumPlantType.Beach) {
                return (world.getBlockMaterial(x - 1, y, z    ) == Material.water ||
                        world.getBlockMaterial(x + 1, y, z    ) == Material.water ||
                        world.getBlockMaterial(x,     y, z - 1) == Material.water ||
                        world.getBlockMaterial(x,     y, z + 1) == Material.water);
			}
		}
		return super.canSustainPlant(world, x, y, z, direction, plant);
	}
	
	@Override
    public void onPlantGrow(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ) {
		world.setBlock(x, y, z, MudMod.sandySoil.blockID, DIRT, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
	}
	    
    @Override
    public boolean canSilkHarvest() {
    	return true;
    }
    
    @Override
    public int idDropped(int id, Random random, int damage) {
    	return MudMod.sandySoil.blockID;
    }
    
    @Override
    public int damageDropped(int damage) {
        return DIRT;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
		for (int i = 0; i < 3; ++i) {
            list.add(new ItemStack(blockId, 1, i));
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
