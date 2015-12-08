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
import net.minecraft.block.BlockMushroom;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

public class BlockFertileSoil extends BlockMudMod implements ITextureOverlay, ITillable, ISoil, IGrassyBlock {
	public static final int
		SOIL = 0,
		GRASS = 1,
		MYCELIUM = 2;
	private Icon
		soil,
		grassSide,
		myceliumSide,
		snowSide,
		grassSideOverlay;
	private static ItemStack
		fertileMud;
	
	public BlockFertileSoil(int id) {
		super(id, Material.ground);
		this.setHardness(0.4F);
		this.setStepSound(Block.soundGravelFootstep);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setTickRandomly(true);
		this.setHydrateRadius(1, 0, 1, 1);
	}
	
	@Override
    public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = this.soil = TextureManager.instance().getBlockTexture(Texture.FERTILESOIL);
		this.grassSide = TextureManager.instance().getBlockTexture(Texture.FERTILEGRASSSIDE);
		this.myceliumSide = TextureManager.instance().getBlockTexture(Texture.FERTILEMYCELIUMSIDE);
		this.snowSide = TextureManager.instance().getBlockTexture(Texture.FERTILESNOWSIDE);
        this.grassSideOverlay = iconRegister.registerIcon("grass_side_overlay");
    }
    
    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
    	int metadata = blockAccess.getBlockMetadata(x, y, z);
    	if (metadata != SOIL
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
        case SOIL:
        	return soil;
    	case GRASS:
    		switch (side) {
    		case MCHelper.SIDE_TOP: return Block.grass.getBlockTextureFromSide(side);
    		case MCHelper.SIDE_BOTTOM: return soil;
    		default: return grassSide;
    		}
    	case MYCELIUM:
    		switch (side) {
    		case MCHelper.SIDE_TOP: return Block.mycelium.getBlockTextureFromSide(side);
    		case MCHelper.SIDE_BOTTOM: return soil;
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
			if (metadata != SOIL) {
				if (world.getBlockLightValue(x, y + 1, z) < 4
				&& world.getBlockLightOpacity(x, y + 1, z) > 2) {
		            world.setBlock(x, y, z, MudMod.fertileSoil.blockID, SOIL, MCHelper.NOTIFY_WITHOUT_UPDATE);
				} else {
					this.growPlants(world, x, y, z, random);
				}
			}
		}
		super.updateTick(world, x, y, z, random);
    }
    
    @Override
    protected ItemStack getWetBlock(int metadata) {
    	switch (metadata) {
    	case SOIL: return fertileMud == null ? fertileMud = new ItemStack(MudMod.mud.blockID, 1, BlockMud.FERTILE) : fertileMud;
    	default: return null;
    	}
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
				world.setBlock(x, y, z, MudMod.fertileSoil.blockID, GRASS, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
				break;
			case MYCELIUM:
				world.setBlock(x, y, z, MudMod.fertileSoil.blockID, MYCELIUM, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
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
			return new ItemStack(MudMod.fertileSoil, 1, SOIL);
		case GRASS:
			return new ItemStack(MudMod.fertileSoil, 1, GRASS);
		case MYCELIUM:
			return new ItemStack(MudMod.fertileSoil, 1, MYCELIUM);
		}
		return null;
	}
	
	protected void growPlants(World world, int x, int y, int z, Random random) {
		int metadata = world.getBlockMetadata(x, y, z);
		Block topBlock = Block.blocksList[world.getBlockId(x, y + 1, z)];
		if (topBlock instanceof IPlantable) {
			topBlock.updateTick(world, x, y + 1, z, random);
		}
		switch(metadata) {
		case GRASS:
			if (world.isAirBlock(x, y + 1, z)
			&& random.nextInt(30) == 0) {
				if (random.nextInt(10) == 0) {
					ForgeHooks.plantGrass(world, x, y + 1, z);
				} else {
					world.setBlock(x, y + 1, z, Block.tallGrass.blockID, 1, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
				}
			}
			break;
		case MYCELIUM:
			if (world.isAirBlock(x, y + 1, z)) {
				if (random.nextInt(30) == 0) {
					if (random.nextInt(3) == 0) {
						world.setBlock(x, y + 1, z, Block.mushroomRed.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
					} else {
						world.setBlock(x, y + 1, z, Block.mushroomBrown.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
					}
				}
			} else {
				if (random.nextInt(50) == 0) {
					if (topBlock instanceof BlockMushroom) {
						world.setBlock(x, y, z, Block.mycelium.blockID, 0, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
						if (!((BlockMushroom)topBlock).fertilizeMushroom(world, x, y + 1, z, random)) {
							world.setBlock(x, y, z, MudMod.fertileSoil.blockID, MYCELIUM, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
						} else {
							world.setBlock(x, y, z, Block.dirt.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean onTilled(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		switch (metadata) {
		case MYCELIUM:
			if (!MudMod.enableMyceliumTilling) {
				return false;
			}
		case SOIL:
		case GRASS:
			world.setBlock(x, y, z, MudMod.farmland.blockID, BlockMudModFarmland.SOIL_DRY, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		plant.getPlantID(world, x, y + 1, z);
		int metadata = world.getBlockMetadata(x, y, z);
		switch (metadata) {
		case SOIL:
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
	public boolean willForcePlantToStay(World world, int x, int y, int z, IPlantable plant) {
		plant.getPlantID(world, x, y + 1, z);
		int metadata = world.getBlockMetadata(x, y, z);
		switch (metadata) {
		case MYCELIUM:
			if (plant.getPlantType(world, x, y + 1, z) == EnumPlantType.Cave) {
				return true;
			}
		}
		return false;
	}
	
	@Override
    public void onPlantGrow(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ) {
		world.setBlock(x, y, z, Block.dirt.blockID);
	}
    
    @Override
    public boolean canSilkHarvest() {
    	return true;
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
    	return MudMod.fertileSoil.blockID;
    }
    
    @Override
    public int damageDropped(int par1) {
        return SOIL;
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
