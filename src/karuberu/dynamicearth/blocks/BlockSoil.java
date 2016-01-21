package karuberu.dynamicearth.blocks;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.core.util.Helper;
import karuberu.core.util.block.BlockSide;
import karuberu.core.util.client.fx.FXHelper;
import karuberu.core.util.client.render.ITextureOverlay;
import karuberu.core.util.client.render.RenderLayeredBlock;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.api.ISoil;
import karuberu.dynamicearth.api.ITillable;
import karuberu.dynamicearth.api.grass.IGrassyBlock;
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

public abstract class BlockSoil extends BlockDynamicEarthWet implements ITextureOverlay, ITillable, ISoil, IGrassyBlock {
	public final int
		DIRT,
		GRASS,
		MYCELIUM;
	protected Icon
		textureDirt,
		textureGrassSide,
		textureGrassTop,
		textureMyceliumSide,
		textureMyceliumTop,
		textureSnowSide,
		textureGrassSideOverlay;
	protected ItemStack
		dirtStack,
		grassStack,
		myceliumStack,
		mudStack,
		farmlandStack;
    public static CreativeTabs
		creativeTab = CreativeTabs.tabBlock;
    
	public BlockSoil(String unlocalizedName) {
		this(unlocalizedName, 0, 1, 2);
	}
	
    public BlockSoil(String unlocalizedName, int dirtMeta, int grassMeta, int myceliumMeta) {
		super(unlocalizedName, Material.ground);
		this.DIRT = dirtMeta;
		this.GRASS = grassMeta;
		this.MYCELIUM = myceliumMeta;
		this.setHardness(0.4F);
		this.setStepSound(Block.soundGravelFootstep);
		this.setCreativeTab(creativeTab);
		this.setTickRandomly(true);
	}
	
	protected void initializeItemStacks() {
		this.dirtStack = new ItemStack(this.blockID, 1, this.DIRT);
		this.grassStack = new ItemStack(this.blockID, 1, this.GRASS);
		this.myceliumStack = new ItemStack(this.blockID, 1, this.MYCELIUM);
		this.mudStack = new ItemStack(DynamicEarth.mud, 1, DynamicEarth.mud.NORMAL);
		this.farmlandStack = new ItemStack(Block.tilledField, 1, 0);
	}
	
	@Override
    public void registerIcons(IconRegister iconRegister) {
		this.textureDirt = iconRegister.registerIcon("dirt");
		this.textureGrassSide = iconRegister.registerIcon("grass_side");
		this.textureMyceliumSide = iconRegister.registerIcon("mycelium_side");
		this.textureSnowSide = iconRegister.registerIcon("grass_snow_side");
        this.textureGrassSideOverlay = iconRegister.registerIcon("grass_side_overlay");
        this.textureGrassTop = iconRegister.registerIcon("grass_top");
        this.textureMyceliumTop = iconRegister.registerIcon("mycelium_top");
    }
    
    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
    	int metadata = blockAccess.getBlockMetadata(x, y, z);
    	if (this.isType(blockAccess, x, y, z, GrassType.GRASS, GrassType.MYCELIUM)
    	&& side != BlockSide.BOTTOM.code
    	&& side != BlockSide.TOP.code) {
        	Material material = blockAccess.getBlockMaterial(x, y + 1, z);
        	if (material == Material.snow || material == Material.craftedSnow) {
        		return this.getSnowSideIcon(blockAccess, x, y, z, side);
        	}
    	}
    	return this.getIcon(side, metadata);
    }
    
	@SideOnly(Side.CLIENT)
    protected Icon getSnowSideIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
    	return this.textureSnowSide;
    }
    
    @Override
    public Icon getIcon(int side, int metadata) {
        if (metadata == this.DIRT) {
        	return textureDirt;
        } else if (metadata == this.GRASS) {
    		switch (BlockSide.get(side)) {
    		case TOP: return textureGrassTop;
    		case BOTTOM: return textureDirt;
    		default: return textureGrassSide;
    		}
        } else if (metadata == this.MYCELIUM) {
    		switch (BlockSide.get(side)) {
    		case TOP: return textureMyceliumTop;
    		case BOTTOM: return textureDirt;
    		default: return textureMyceliumSide;
    		}
        }
        return textureDirt;
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int metadata, int side, int pass) {
		if (side == BlockSide.TOP.code
		|| side == BlockSide.BOTTOM.code
		|| pass != 1) {
			return null;
		} else if (this.isType(blockAccess, x, y, z, GrassType.GRASS)) {
        	Material material = blockAccess.getBlockMaterial(x, y + 1, z);
        	if (material != Material.snow && material != Material.craftedSnow) {
        		return this.textureGrassSideOverlay;
        	} else {
        		return null;
        	}
		} else {
			return null;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getNumberOfAdditionalRenderPasses(int metadata) {
		if (metadata == this.GRASS) {
			return RenderBlocks.fancyGrass ? 1 : 0;
		} else {
			return 0;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeTexture(IBlockAccess blockAccess, int x, int y, int z, int metadata, int side, int pass) {
		if (this.isType(blockAccess, x, y, z, GrassType.GRASS)) {
			switch(pass) {
			case 0:
				if (side == BlockSide.TOP.code) {
					return true;
				}
				return false;
			case 1:
				if (side != BlockSide.TOP.code
				&& side != BlockSide.BOTTOM.code) {
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
		if (metadata == this.GRASS
		&& side == BlockSide.TOP.code) {
			return true;
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return RenderLayeredBlock.renderID;
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
    	if (this.isType(blockAccess, x, y, z, GrassType.GRASS)) {
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
    	if (metadata == this.GRASS) {
    		return this.getBlockColor();
    	} else {
    		return super.getBlockColor();
    	}
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        super.randomDisplayTick(world, x, y, z, random);
        if (this.isType(world, x, y, z, GrassType.MYCELIUM)
        && random.nextInt(10) == 0) {
        	FXHelper.spawnMyceliumParticles(world, x, y, z);
        }
    }
    
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {
	    	int metadata = world.getBlockMetadata(x, y, z);
			if (metadata != DIRT) {
				this.tryToBecomeDirt(world, x, y, z);
			}
		}
		super.updateTick(world, x, y, z, random);
    }
    
    protected void tryToBecomeDirt(World world, int x, int y, int z) {
		if (world.getBlockLightValue(x, y + 1, z) < 4
		&& world.getBlockLightOpacity(x, y + 1, z) > 2) {
	    	ItemStack itemStack = this.getBlockForType(world, x, y, z, GrassType.DIRT);
	    	if (itemStack != null) {
	    		world.setBlock(x, y, z, itemStack.itemID, itemStack.getItemDamage(), Helper.NOTIFY_WITHOUT_UPDATE);
	    	}
		}
	}
    
    @Override
    public ItemStack getWetBlock(int metadata) {
    	if (!DynamicEarth.includeMud) {
    		return null;
    	}
    	if (this.mudStack == null) {
    		this.initializeItemStacks();
    	}
    	if (metadata == this.DIRT) {
    		return this.mudStack;
    	} else {
    		return null;
    	}
    }
    
	@Override
	public boolean canSpread(World world, int x, int y, int z) {
		GrassType type = this.getType(world, x, y, z);
		if ((type == GrassType.GRASS || type == GrassType.MYCELIUM)
		&& world.getBlockLightValue(x, y + 1, z) >= 9
		&& world.getBlockLightOpacity(x, y + 1, z) <= 2) {
			return true;
		}
		return false;
	}
	
	@Override
	public void tryToGrow(World world, int x, int y, int z, GrassType type) {
		if (this.getType(world, x, y, z) == GrassType.DIRT
		&& world.getBlockLightValue(x, y + 1, z) >= 4
        && world.getBlockLightOpacity(x, y + 1, z) <= 2) {
			ItemStack itemStack;
			switch (type) {
			case GRASS:
				itemStack = this.getBlockForType(world, x, y, z, GrassType.GRASS);
				world.setBlock(x, y, z, itemStack.itemID, itemStack.getItemDamage(), Helper.NOTIFY_AND_UPDATE_REMOTE);
				break;
			case MYCELIUM:
				itemStack = this.getBlockForType(world, x, y, z, GrassType.MYCELIUM);
				world.setBlock(x, y, z, itemStack.itemID, itemStack.getItemDamage(), Helper.NOTIFY_AND_UPDATE_REMOTE);
				break;
			default:
				return;
			}
		}
	}

	@Override
	public GrassType getType(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
    	if (metadata == this.DIRT) {
        	return GrassType.DIRT;
    	} else if (metadata == this.GRASS) {
    		return GrassType.GRASS;
    	} else if (metadata == this.MYCELIUM) {
    		return GrassType.MYCELIUM;
    	} else {
    		return null;
        }
	}
	
	@Override
	public final ItemStack getBlockForType(World world, int x, int y, int z, GrassType type) {
		return this.getBlockForType((IBlockAccess)world, x, y, z, type);
	}
	
	public ItemStack getBlockForType(IBlockAccess blockAccess, int x, int y, int z, GrassType type) {
		if (this.dirtStack == null
		|| this.grassStack == null
		|| this.myceliumStack == null) {
			this.initializeItemStacks();
		}
		switch (type) {
		case DIRT:
			return this.dirtStack;
		case GRASS:
			return this.grassStack;
		case MYCELIUM:
			return this.myceliumStack;
		}
		return null;
	}
	
	protected boolean isType(IBlockAccess blockAccess, int x, int y, int z, GrassType... types) {
		int id = blockAccess.getBlockId(x, y, z);
		int metadata = blockAccess.getBlockMetadata(x, y, z);
		for (GrassType type : types) {
			ItemStack stack = this.getBlockForType(blockAccess, x, y, z, type);
			if (stack != null
			&& stack.itemID == id
			&& stack.getItemDamage() == metadata) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean willBurn(World world, int x, int y, int z) {
		GrassType type = this.getType(world, x, y, z); 
		return type == GrassType.GRASS || type == GrassType.MYCELIUM;
	}

	@Override
	public boolean onTilled(World world, int x, int y, int z) {
		if (this.isType(world, x, y, z, GrassType.MYCELIUM)) {
			if (!DynamicEarth.enableMyceliumTilling) {
				return false;
			}
		} else if (this.isType(world, x, y, z, GrassType.DIRT, GrassType.GRASS)) {
			ItemStack itemStack = this.getFarmlandBlock(world, x, y, z);
			world.setBlock(x, y, z, itemStack.itemID, itemStack.getItemDamage(), Helper.NOTIFY_AND_UPDATE_REMOTE);
			return true;
		}
		return false;
	}
	
	protected ItemStack getFarmlandBlock(World world, int x, int y, int z) {
		if (this.farmlandStack == null) {
			this.initializeItemStacks();
		}
		return this.farmlandStack;
	}
	
	@Override
	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		if (this.isType(world, x, y, z, GrassType.DIRT, GrassType.GRASS)) {
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
		if (this.isType(world, x, y, z, GrassType.MYCELIUM)
		&& plant.getPlantType(world, x, y + 1, z) == EnumPlantType.Cave) {
			return true;
		}
		return false;
	}
	
	@Override
    public void onPlantGrow(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ) {
		ItemStack itemStack = this.getBlockForType(world, x, y, z, GrassType.DIRT);
		if (itemStack != null) {
			world.setBlock(x, y, z, itemStack.itemID, itemStack.getItemDamage(), Helper.NOTIFY_AND_UPDATE_REMOTE);
		}
	}
	    
    @Override
    public boolean canSilkHarvest() {
    	return true;
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
    	return this.blockID;
    }
    
    @Override
    public int damageDropped(int par1) {
        return DIRT;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
		if (this.dirtStack == null
		|| this.grassStack == null
		|| this.myceliumStack == null) {
			this.initializeItemStacks();
		}
		list.add(this.dirtStack.copy());
		list.add(this.grassStack.copy());
		list.add(this.myceliumStack.copy());
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
