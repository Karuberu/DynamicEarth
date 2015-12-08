package karuberu.dynamicearth.blocks;

import java.util.Random;
import karuberu.core.util.Helper;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMushroom;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

public class BlockFertileSoil extends BlockSoil {
	
	public BlockFertileSoil(int id) {
		super(id);
		this.setUnlocalizedName("fertileSoil");
	}
	
	@Override
    public void registerIcons(IconRegister iconRegister) {
		this.textureDirt = iconRegister.registerIcon(BlockTexture.FERTILESOIL.getIconPath());
		this.textureGrassSide = iconRegister.registerIcon(BlockTexture.FERTILEGRASSSIDE.getIconPath());
		this.textureMyceliumSide = iconRegister.registerIcon(BlockTexture.FERTILEMYCELIUMSIDE.getIconPath());
		this.textureSnowSide = iconRegister.registerIcon(BlockTexture.FERTILESNOWSIDE.getIconPath());
		this.textureGrassSideOverlay = iconRegister.registerIcon("grass_side_overlay");
		this.textureGrassTop = iconRegister.registerIcon("grass_top");
		this.textureMyceliumTop = iconRegister.registerIcon("mycelium_top");
    }
	
	@Override
	protected void initializeItemStacks() {
		super.initializeItemStacks();
		this.mudStack = new ItemStack(DynamicEarth.fertileMud.blockID, 1, DynamicEarth.fertileMud.NORMAL);
		this.farmlandStack = new ItemStack(DynamicEarth.farmland.blockID, 1, BlockDynamicFarmland.FERTILE_DRY);
	}
        
    @Override
	public void updateTick(World world, int x, int y, int z, Random random) {
    	super.updateTick(world, x, y, z, random);
    	if (!world.isRemote) {
			this.growPlants(world, x, y, z, random);
		}
    }
    
	protected void growPlants(World world, int x, int y, int z, Random random) {
		Block topBlock = Block.blocksList[world.getBlockId(x, y + 1, z)];
		if (topBlock instanceof IPlantable) {
			topBlock.updateTick(world, x, y + 1, z, random);
		}
		if (this.isType(world, x, y, z, GrassType.GRASS)) {
			this.growGrassPlants(world, x, y, z, random);
		} else if (this.isType(world, x, y, z, GrassType.MYCELIUM)) {
			this.growMyceliumPlants(world, x, y, z, random);
		}
	}
	
	private void growGrassPlants(World world, int x, int y, int z, Random random) {
		if (world.isAirBlock(x, y + 1, z)
		&& random.nextInt(30) == 0) {
			if (random.nextInt(10) == 0) {
				ForgeHooks.plantGrass(world, x, y + 1, z);
			} else {
				world.setBlock(x, y + 1, z, Block.tallGrass.blockID, 1, Helper.NOTIFY_AND_UPDATE_REMOTE);
			}
		}
	}
	
	private void growMyceliumPlants(World world, int x, int y, int z, Random random) {
		Block topBlock = Block.blocksList[world.getBlockId(x, y + 1, z)];
		if (topBlock == null
		&& random.nextInt(30) == 0) {
			if (random.nextInt(3) == 0) {
				world.setBlock(x, y + 1, z, Block.mushroomRed.blockID, 0, Helper.NOTIFY_AND_UPDATE_REMOTE);
			} else {
				world.setBlock(x, y + 1, z, Block.mushroomBrown.blockID, 0, Helper.NOTIFY_AND_UPDATE_REMOTE);
			}
		} else if (random.nextInt(50) == 0
		&& topBlock instanceof BlockMushroom) {
			world.setBlock(x, y, z, Block.mycelium.blockID, 0, Helper.DO_NOT_NOTIFY_OR_UPDATE);
			boolean mushroomGrown = ((BlockMushroom)topBlock).fertilizeMushroom(world, x, y + 1, z, random);
			if (mushroomGrown) {
				this.onPlantGrow(world, x, y, z, x, y + 1, z);
			} else {
				world.setBlock(x, y, z, DynamicEarth.fertileSoil.blockID, MYCELIUM, Helper.DO_NOT_NOTIFY_OR_UPDATE);
			}
		}
	}

	@Override
    public void onPlantGrow(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ) {
		world.setBlock(x, y, z, Block.dirt.blockID);
	}
}
