package karuberu.dynamicearth.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import karuberu.core.util.Helper;
import karuberu.core.util.block.BlockSide;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.api.IVanillaReplaceable;
import karuberu.dynamicearth.api.grass.IGrassyBlock;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.client.fx.FXManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class BlockBurningSoil extends BlockDynamicEarth implements IVanillaReplaceable {
	
	private Icon
		dirt,
		grassSide,
		grassTop;
	public final int
		DIRT = 0,
		GRASS = 1;
	public static CreativeTabs
		creativeTab;
	private static ItemStack
		dirtStack,
		grassStack;
	
	public BlockBurningSoil(String unlocalizedName) {
		super(unlocalizedName, Material.ground);
		this.setHardness(0.4F);
		this.setStepSound(Block.soundGravelFootstep);
		this.setCreativeTab(creativeTab);
		this.setTickRandomly(true);
	}

	@Override
    public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = this.dirt = iconRegister.registerIcon("dirt");
		this.grassSide = iconRegister.registerIcon(BlockTexture.NETHERGRASSSIDE.getIconPath());
		this.grassTop = iconRegister.registerIcon(BlockTexture.NETHERGRASSTOP.getIconPath());
    }

    @Override
    public Icon getIcon(int side, int metadata) {
        switch (metadata) {
        case DIRT:
        	return dirt;
    	case GRASS:
    		switch (BlockSide.get(side)) {
    		case TOP: return grassTop;
    		case BOTTOM: return dirt;
    		default: return grassSide;
    		}
        }
        return dirt;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		super.randomDisplayTick(world, x, y, z, random);
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch (metadata) {
    	case GRASS:
    		FXManager.spawnNetherGrassParticles(world, x, y, z, 1.0D); break;
    	}
	}
	
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
    	int metadata = world.getBlockMetadata(x, y, z);
    	switch (metadata) {
    	case DIRT:
    		this.tryToGrow(world, x, y, z);
    		break;
    	case GRASS:
    		if (world.getBlockLightOpacity(x, y + 1, z) > 2) {
    			world.setBlockMetadataWithNotify(x, y, z, DIRT, Helper.NOTIFY_AND_UPDATE_REMOTE);
    		} else {
	    		this.lightFires(world, x, y, z);
	    		this.tryToSpread(world, x, y, z);
    		}
    		break;
    	}
    	this.applyHeatEffects(world, x, y, z);
    	super.updateTick(world, x, y, z, random);
    }
    
    private void tryToGrow(World world, int x, int y, int z) {
    	if (world.provider.isHellWorld
    	&& world.getBlockLightOpacity(x, y + 1, z) <= 2) {
    		boolean
    			hasNetherWart = false,
    			hasGrass = false,
    			hasMycelium = false;
    		int numLava = 0;
    		int xi, yi, zi;
     		for (int i = 0; i < 8; i++) {
    			xi = x + world.rand.nextInt(3) - 1;
    			yi = y + world.rand.nextInt(2) - 1;
    			zi = z + world.rand.nextInt(3) - 1;
       			if (world.getBlockMaterial(xi, yi, zi) == Material.lava) {
       				numLava++;
       			}
       		}
    		Block block;
     		for (int i = 0; i < numLava << 2; i++) {
    			xi = x + world.rand.nextInt(5) - 2;
    			yi = y - world.rand.nextInt(5) + 3;
    			zi = z + world.rand.nextInt(5) - 2;
    			block = Block.blocksList[world.getBlockId(xi, yi, zi)];
    			if (block != null) {
        			if (block instanceof IGrassyBlock) {
        				switch (((IGrassyBlock)block).getType(world, xi, yi, zi)) {
        				case GRASS: hasGrass = true; break;
        				case MYCELIUM: hasMycelium = true; break;
        				default:
        				}
        			} else if (block.blockID == Block.grass.blockID) {
        				hasGrass = true;
        			} else if (block.blockID == Block.mycelium.blockID) {
        				hasMycelium = true;
        			} else if (block.blockID == Block.netherStalk.blockID) {
        				hasNetherWart = true;
        			}
        		}
    		}
    		if (hasNetherWart && hasGrass && hasMycelium) {
    			world.setBlockMetadataWithNotify(x, y, z, GRASS, Helper.NOTIFY_AND_UPDATE_REMOTE);
    			if (world.isAirBlock(x, y + 1, z)) {
    	            FXManager.flameEffect(world, x, y, z, 1.2D, true);
    				world.setBlock(x, y + 1, z, Block.fire.blockID);
    			}
    		} else if (numLava > 0 && world.isAirBlock(x, y + 1, z)) {
    			FXManager.fizzleEffect(world, x, y, z, 1.2F, true);
    		}
    	}
    }

	private void lightFires(World world, int x, int y, int z) {
		int xi, yi, zi;
		for (int i = 0; i < 15; i++) {
			xi = x + world.rand.nextInt(3) - 1;
			yi = y + world.rand.nextInt(5) - 3;
			zi = z + world.rand.nextInt(3) - 1;
			if (world.isAirBlock(xi, yi, zi)) {
				if (world.getBlockId(xi, yi - 1, zi) != this.blockID
				|| world.rand.nextInt(50) == 0) {
					world.setBlock(xi, yi, zi, Block.fire.blockID);
				}
			}
		}
	}
	
	private void tryToSpread(World world, int x, int y, int z) {
		if (!DynamicEarth.restoreDirtOnChunkLoad
		&& world.getBlockLightOpacity(x, y + 1, z) <= 2) {
			int xi, yi, zi;
			Block targetBlock;
			for (int i = 0; i < 4; ++i) {
				xi = x + world.rand.nextInt(3) - 1;
				yi = y + world.rand.nextInt(5) - 3;
				zi = z + world.rand.nextInt(3) - 1;
				targetBlock = Block.blocksList[world.getBlockId(xi, yi, zi)];
				if (targetBlock != null
				&& (targetBlock.blockID == Block.dirt.blockID
				|| targetBlock.blockID == DynamicEarth.burningSoil.blockID
				|| targetBlock instanceof IGrassyBlock)
				&& world.getBlockLightOpacity(xi, yi + 1, zi) <= 2) {
					world.setBlock(xi, yi, zi, this.blockID, GRASS, Helper.NOTIFY_AND_UPDATE_REMOTE);
				}
			}
		}
	}
	
	private void applyHeatEffects(World world, int x, int y, int z) {
    	this.meltSnow(world, x, y, z);
    	if (BlockDynamicEarthWet.getHydrationDistanceWithinRadius(world, x, y, z, 1, 1, 1, 1) != -1) {
    		FXManager.fizzleEffect(world, x, y + 1, z, 0.0F, true);
    	}
	}
	
	private void meltSnow(World world, int x, int y, int z) {
		if (world.getBlockId(x, y + 1, z) == Block.snow.blockID) {
			world.setBlockToAir(x, y + 1, z);
			FXManager.fizzleEffect(world, x, y + 1, z, 0.0F, true);
		}
	}

	@Override
	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		if (plant.getPlantType(world, x, y + 1, z) == EnumPlantType.Nether) {
			return true;
		}
		return false;
	}
	
	@Override
	public ItemStack getVanillaBlockReplacement(Chunk chunk, int x, int y, int z) {
    	int metadata = chunk.getBlockMetadata(x, y, z);
    	if (metadata == GRASS) {
    		return grassStack == null ? grassStack = new ItemStack(Block.grass) : grassStack;
    	} else {
    		return dirtStack == null ? dirtStack = new ItemStack(Block.dirt) : dirtStack;
    	}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
		list.add(new ItemStack(blockId, 1, DIRT));
		list.add(new ItemStack(blockId, 1, GRASS));        
    }
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		if (metadata == GRASS) {
			items.add(new ItemStack(Item.blazePowder, 1));
			if (world.rand.nextInt(8 - fortune * 3) == 0) {
				items.add(new ItemStack(Item.blazePowder, 1));
			}
			items.add(new ItemStack(Block.dirt.blockID, 1, 0));
		} else {
			items.add(new ItemStack(DynamicEarth.burningSoil, 1, DIRT));
		}
		return items;
	}
}
