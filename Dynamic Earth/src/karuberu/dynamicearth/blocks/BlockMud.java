package karuberu.dynamicearth.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import karuberu.core.event.INeighborBlockEventHandler;
import karuberu.core.event.NeighborBlockChangeEvent;
import karuberu.core.util.Coordinates;
import karuberu.core.util.Helper;
import karuberu.core.util.block.BlockSide;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.GameruleHelper;
import karuberu.dynamicearth.api.ITillable;
import karuberu.dynamicearth.api.fallingblock.BlockFalling;
import karuberu.dynamicearth.api.fallingblock.IFallingBlock;
import karuberu.dynamicearth.api.grass.IGrassyBlock;
import karuberu.dynamicearth.api.mud.IMudBlock;
import karuberu.dynamicearth.api.mud.MudRegistry;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.client.fx.FXManager;
import karuberu.dynamicearth.entity.EntityFallingBlock;
import karuberu.dynamicearth.items.ItemMudBlob;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMud extends BlockSoil implements IMudBlock, IGrassyBlock, IFallingBlock, INeighborBlockEventHandler, ITillable {
	public final int
		NORMAL = DIRT,
		WET = NORMAL + 8,
		WET_GRASS = GRASS + 8,
		WET_MYCELIUM = MYCELIUM + 8;
	private static final String
		TAG_MUDSLIDE_INTENSITY = "MudslideIntensity",
		TAG_MUDSLIDE_XSHIFT = "MudslideXShift",
		TAG_MUDSLIDE_ZSHIFT = "MudslideZShift";
	@SideOnly(Side.CLIENT)
	protected Icon
		textureMud,
		textureMudWet,
		textureWetGrassSide,
		textureWetMyceliumSide,
		textureWetSnowSide;
	protected ItemStack
		dryStack, 			// the item that mud will dry into
		dryGrassStack,		// the item that grassy mud will dry into
		dryMyceliumStack,	// the item that mycelium mud will dry into
		wetStack,			// the item that mud will hydrate into
		wetGrassStack,		// the item that grassy mud will hydrate into
		wetMyceliumStack;	// the item that mycelium mud will hydrate into
    public static CreativeTabs
		creativeTab = CreativeTabs.tabBlock;
	
	public BlockMud(int id) {
		super(id);
		this.setHardness(0.5F);
		this.setStepSound(Block.soundGravelFootstep);
		this.setCreativeTab(creativeTab);
		this.setUnlocalizedName("mud");
		this.setHydrateRadius(2, 1, 4, 2);
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		super.registerIcons(iconRegister);
		this.textureMud = this.textureDirt = iconRegister.registerIcon(BlockTexture.MUD.getIconPath());
		this.textureMudWet = iconRegister.registerIcon(BlockTexture.MUDWET.getIconPath());
		this.textureGrassSide = iconRegister.registerIcon(BlockTexture.MUDGRASSSIDE.getIconPath());
		this.textureSnowSide = iconRegister.registerIcon(BlockTexture.MUDSNOWSIDE.getIconPath());
		this.textureMyceliumSide = iconRegister.registerIcon(BlockTexture.MUDMYCELIUMSIDE.getIconPath());
		this.textureWetGrassSide = iconRegister.registerIcon(BlockTexture.MUDGRASSSIDEWET.getIconPath());
		this.textureWetSnowSide = iconRegister.registerIcon(BlockTexture.MUDSNOWSIDEWET.getIconPath());
		this.textureWetMyceliumSide = iconRegister.registerIcon(BlockTexture.MUDMYCELIUMSIDEWET.getIconPath());
	}
	
	@Override
	protected void initializeItemStacks() {
		super.initializeItemStacks();
		this.dryStack = new ItemStack(Block.dirt.blockID, 1, 0);
		this.dryGrassStack = new ItemStack(Block.grass.blockID, 1, 0);
		this.dryMyceliumStack = new ItemStack(Block.mycelium.blockID, 1, 0);
		this.wetStack = this.mudStack = new ItemStack(this.blockID, 1, this.WET);
		this.wetGrassStack = new ItemStack(this.blockID, 1, this.WET_GRASS);
		this.wetMyceliumStack = new ItemStack(this.blockID, 1, this.WET_MYCELIUM);
		this.farmlandStack = new ItemStack(DynamicEarth.farmland.blockID, 1, BlockDynamicFarmland.MUD);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		if (metadata == this.WET) {
			return this.textureMudWet;
		} else if (metadata == this.WET_GRASS) {
			if (side == BlockSide.BOTTOM.code) {
				return this.textureMudWet;
			} else if (side == BlockSide.TOP.code) {
				return this.textureGrassTop;
			} else {
				return this.textureWetGrassSide;
			}
		} else if (metadata == this.WET_MYCELIUM) {
			if (side == BlockSide.BOTTOM.code) {
				return this.textureMudWet;
			} else if (side == BlockSide.TOP.code) {
				return this.textureMyceliumTop;
			} else {
				return this.textureWetMyceliumSide;
			}
		}
		return super.getIcon(side, metadata);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    protected Icon getSnowSideIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
    	if (this.isWetBlock(blockAccess.getBlockMetadata(x, y, z))) {
    		return this.textureWetSnowSide;
    	} else {
    		return this.textureSnowSide;
    	}
    }
		
	@Override
	@SideOnly(Side.CLIENT)
	public int getNumberOfAdditionalRenderPasses(int metadata) {
		if (metadata == this.WET_GRASS) {
			return RenderBlocks.fancyGrass ? 1 : 0;
		} else {
			return super.getNumberOfAdditionalRenderPasses(metadata);
		}
	}
		
	@Override
	@SideOnly(Side.CLIENT)
	public boolean willColorizeInventoryBaseTexture(int side, int metadata) {
		if (metadata == this.WET_GRASS) {
			if (side == BlockSide.TOP.code) {
				return true;
			} else {
				return false;
			}
		}
		return super.willColorizeInventoryBaseTexture(side, metadata);
	}
	    
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(int metadata) {
    	if (metadata == this.WET_GRASS) {
    		return this.getBlockColor();
    	}
    	return super.getRenderColor(metadata);
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (DynamicEarth.enableDeepMud
		&& this.isWetBlock(metadata)) {
			return AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 1 - 0.125F, z + 1);
		}
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public int tickRate(World world) {
		return 5;
	}

	@Override
	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		int plantID = plant.getPlantID(world, x, y + 1, z);
		if (plantID == Block.reed.blockID
		|| plantID == Block.sapling.blockID
		|| plantID == Block.tallGrass.blockID
		|| plant.getPlantType(world, x, y + 1, z) == EnumPlantType.Plains
		|| plant instanceof BlockFlower
		) {
			return true;
		}
		return super.canSustainPlant(world, x, y, z, direction, plant);
	}
	
	@Override
	protected ItemStack getDryBlock(int metadata) {
		if (this.dryStack == null) {
			this.initializeItemStacks();
		}
		if (metadata == this.NORMAL) {
			return this.dryStack;
		} else if (metadata == this.GRASS) {
			return this.dryGrassStack;
		} else if (metadata == this.MYCELIUM) {
			return this.dryMyceliumStack;
		} else if (metadata == this.WET) {
			return this.dirtStack;
		} else if (metadata == this.WET_GRASS) {
			return this.grassStack;
		} else if (metadata == this.WET_MYCELIUM) {
			return this.myceliumStack;
		}
		return null;
	}
	
	@Override
	public ItemStack getWetBlock(int metadata) {
		if (this.wetStack == null) {
			this.initializeItemStacks();
		}
		if (metadata == this.NORMAL) {
			return this.wetStack;
		} else if (metadata == this.GRASS) {
			return this.wetGrassStack;
		} else if (metadata == this.MYCELIUM) {
			return this.wetMyceliumStack;
		}
		return null;
	}
	
	protected boolean isWetBlock(int metadata) {
		return metadata / 8 == 1;
	}
	
	protected int getRawMeta(int metadata) {
		return metadata % 8;
	}
	
	@Override
	protected boolean canSpreadHydration(int metadata) {
		if (DynamicEarth.restoreDirtOnChunkLoad) {
			return false;
		} else {
			return metadata == this.NORMAL || metadata == this.WET;
		}
	}
	
	public boolean isRooted(World world, int x, int y, int z) {
		Block block = Block.blocksList[world.getBlockId(x, y + 1, z)];
		if (block instanceof IPlantable) {
			return true;
		} else {
			return this.isType(world, x, y, z, GrassType.GRASS, GrassType.MYCELIUM);
		}
	}
	
	@Override
	public boolean canSpread(World world, int x, int y, int z) {
		if (this.isType(world, x, y, z, GrassType.GRASS, GrassType.MYCELIUM)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected ItemStack getBlockForSpread(World world, int x, int y, int z, int targetX, int targetY, int targetZ) {
		if (this.dirtStack == null) {
			this.initializeItemStacks();
		}
		Block target = Block.blocksList[world.getBlockId(targetX, targetY, targetZ)];
		if (target == null) {
			return null;
		} else if (target.blockID == Block.dirt.blockID) {
			return this.dirtStack;
		} else if (target.blockID == Block.grass.blockID) {
			return this.grassStack;
		} else if (target.blockID == Block.mycelium.blockID) {
			return this.myceliumStack;
		}
		return null;
	}

	@Override
	public void tryToGrow(World world, int x, int y, int z, GrassType type) {
		if (world.rand.nextInt(2) == 0) {
			super.tryToGrow(world, x, y, z, type);
		}
	}

	@Override
	public GrassType getType(World world, int x, int y, int z) {
		int metadata = this.getRawMeta(world.getBlockMetadata(x, y, z));
		if (metadata == this.NORMAL) {
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
	public ItemStack getBlockForType(IBlockAccess world, int x, int y, int z, GrassType type) {
		if (dirtStack == null) {
			this.initializeItemStacks();
		}
		int metadata = world.getBlockMetadata(x, y, z);
		switch (type) {
		case DIRT:
			if (this.isWetBlock(metadata)) {
				return this.wetStack;
			} else {
				return this.dirtStack;
			}
		case GRASS:
			if (this.isWetBlock(metadata)) {
				return this.wetGrassStack;
			} else {
				return this.grassStack;
			}
		case MYCELIUM:
			if (this.isWetBlock(metadata)) {
				return this.wetMyceliumStack;
			} else {
				return this.myceliumStack;
			}
		default:
			return null;
		}
	}

	@Override
	public boolean willBurn(World world, int x, int y, int z) {
		return false;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (world.isRemote) {
			return;
		}
		int metadata = world.getBlockMetadata(x, y, z);
		if (GameruleHelper.doMudTick(world)) {
			if (this.isWetBlock(metadata)) {
				super.updateTick(world, x, y, z, random);
				if (this.isHydrated(world, x, y, z)) {
					this.tryToMudslide(world, x, y, z);
				} else {
					this.tryToFall(world, x, y, z);
				}
			} else {
				super.updateTick(world, x, y, z, random);
			}
		}
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.motionX *= 0.7D;
		entity.motionZ *= 0.7D;
	}

	@Override
    public void onFallenUpon(World world, int x, int y, int z, Entity entity, float velocity) {
		if (world.rand.nextFloat() < velocity - 0.6F) {
			this.tryToTrample(world, x, y, z, entity);
		}
	}

	@Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		if (world.rand.nextInt(5) == 0) {
			this.tryToTrample(world, x, y, z, entity);
		}
	}
	
	protected void tryToTrample(World world, int x, int y, int z, Entity entity) {
		if (!world.isRemote
		&& this.isType(world, x, y, z, GrassType.MYCELIUM, GrassType.GRASS)) {
			if (!(entity instanceof EntityPlayer)
			&& !GameruleHelper.mobGriefing(world)) {
				return;
			}
			ItemStack itemStack = this.getBlockForType(world, x, y, z, GrassType.DIRT);
			if (itemStack != null) {
				world.setBlock(x, y, z, itemStack.itemID, itemStack.getItemDamage(), Helper.NOTIFY_AND_UPDATE_REMOTE);
			}
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (this.isWetBlock(metadata)) {
			this.tryToFall(world, x, y, z);
		}
		super.onNeighborBlockChange(world, x, y, z, neighborID);
	}
	
	@Override
	public void handleNeighborBlockChangeEvent(NeighborBlockChangeEvent event) {
		Block neighborBlock = Block.blocksList[event.neighborBlockID];
		if (neighborBlock != null) {
			if (neighborBlock.blockMaterial == Material.lava) {
				FXManager.fizzleEffect(event.world, event.x, event.y, event.z, 0.0F, false);
				this.becomeDry(event.world, event.x, event.y, event.z);
			} else if (event.side == BlockSide.TOP && neighborBlock.blockMaterial == Material.fire) {
				this.becomeDry(event.world, event.x, event.y, event.z);
			} else if (neighborBlock.blockMaterial == Material.water) {
				this.becomeWet(event.world, event.x, event.y, event.z);
			}
		}
	}
	
	@Override
	protected boolean isHydrated(World world, int x, int y, int z) {
		if (!this.isType(world, x, y, z, GrassType.DIRT)) {
			return this.getHydrationDistance(world, x, y, z) == 0;
		}
		int metadata = world.getBlockMetadata(x, y, z);
		if (this.isWetBlock(metadata)) {
			int hydrationDistance = this.getHydrationDistance(world, x, y, z);
			return hydrationDistance == 0 || (super.isHydrated(world, x, y, z) && this.isMuddy(world, x, y, z));
		} else {
			return super.isHydrated(world, x, y, z);
		}
	}
	
	/**
	 * Returns true if there is water above the block (within hydration range) and
	 * there is only water, air, or wet blocks between them. Also returns true if this block
	 * is horizontally adjacent to water (including diagonally).
	 */
	protected boolean isMuddy(World world, int x, int y, int z) {
		Material material;
		for (int xi = x - 1; xi <= x + 1; xi++) {
			for (int zi = z - 1; zi <= z + 1; zi++) {
				material = world.getBlockMaterial(xi, y, zi);
				if (material == Material.water) {
					return true;
				}
			}
		}
		int hydrationRange = this.getHydrationRange(world, x, y, z, BlockSide.BOTTOM.code);
		int metadata = world.getBlockMetadata(x, y, z);
		for (int yi = y + 1; yi <= y + hydrationRange; yi++) {
			material = world.getBlockMaterial(x, yi, z);
			if (material == Material.water) {
				return true;
			}
			if (!world.isAirBlock(x, yi, z)
			&& !BlockDynamicEarth.isBlockWet(world, x, yi, z)
			&& !this.isWetBlock(world, x, yi, z, metadata)) {
				return false;
			}
		}
		return false;
	}
	
	@Override
	protected boolean willHydrate(World world, int x, int y, int z) {
		int distance = this.getHydrationDistance(world, x, y, z);
		if (distance > 0) {
			return super.willHydrate(world, x, y, z) && this.isMuddy(world, x, y, z);
		} else if (distance == 0) {
			return super.willHydrate(world, x, y, z);
		}
		return false;
	}
	
	/**
	 * Changes the block's metadata and attempts to start a mudslide
	 */
	@Override
	protected void becomeWet(World world, int x, int y, int z) {
		super.becomeWet(world, x, y, z);
		this.tryToMudslide(world, x, y, z);
	}
	
	@Override
	protected Rate getDryRateForBiome(BiomeGenBase biome) {
		Rate rate = super.getDryRateForBiome(biome);
		return rate == Rate.NONE ? Rate.SLOW : rate;
	}
	
	@Override
	public boolean canFormMud(World world, int x, int y, int z) {
		return this.isHydrated(world, x, y, z);
	}

	@Override
	public void tryToFormMud(World world, int x, int y, int z) {
		BlockMud.tryToForm(world, x, y, z);
	}
	
	public void tryToMudslide(World world, int x, int y, int z) {
		this.tryToMudslide(world, x, y, z, 0);
	}
	
	@Override
	public void tryToMudslide(World world, int x, int y, int z, int intensity) {
		int metadata = world.getBlockMetadata(x, y, z);
		// Try to fall at the starting coordinates before trying anything fancy.
		if (this.doTryToMudslide(world, x, y, z, metadata, 0, 0, intensity)) {
			return;
		}
		// If fancy mudslides are disabled, go no further.
		if (!DynamicEarth.enableFancyMudslides) {
			return;
		}
		// Otherwise, check if the mudslide is intense or the block is loose (hydrated)
		if (intensity >= 5 || this.isHydrated(world, x, y, z)) {
			// If underwater mudslides are disabled and still water is adjacent, go no further.
			if (!DynamicEarth.enableUnderwaterMudslides) {
				DynamicEarth.logger.debugFiltered(y >= 85, "Checking for water blocks...");
				Coordinates[] adjacentBlocks = Coordinates.getForSides(x, y, z, BlockSide.TOP, BlockSide.NORTH, BlockSide.EAST, BlockSide.WEST, BlockSide.SOUTH);
				for (Coordinates coords : adjacentBlocks) {
					DynamicEarth.logger.debugFiltered(y >= 85, coords.getBlock(world));
					if (coords.getBlockID(world) == Block.waterStill.blockID) {
						DynamicEarth.logger.debugFiltered(y >= 85, "Water block found! Canceling mudslide!");						
						return;
					}
				}
			}
			// Otherwise, check for a valid block to slide to.
			Coordinates[] validBlocks = Coordinates.getForSides(x, y, z,
				BlockSide.EAST, BlockSide.WEST, BlockSide.NORTH, BlockSide.SOUTH
			);
			Coordinates coords;
			int numAttempts = validBlocks.length << 1;
			for (int i = 0; i < numAttempts; i++) {
				coords = validBlocks[world.rand.nextInt(validBlocks.length)];
				if (this.doTryToMudslide(world, coords.x, coords.y, coords.z, metadata, coords.x - x, coords.z - z, intensity)) {
					// Set the original starting block to air if the slide was successful.
					world.setBlockToAir(x, y, z);
					return;
				}
			}
		}
	}
	
	private boolean doTryToMudslide(World world, int x, int y, int z, int metadata, int xShift, int zShift, int intensity) {
		if ((world.getBlockId(x, y, z) == this.blockID || world.getBlockMaterial(x, y, z).isReplaceable())
		&& world.getEntitiesWithinAABB(EntityFallingBlock.class, this.getCollisionBoundingBoxFromPool(world, x, y, z)).isEmpty()
		&& this.canFallBelow(world, x, y, z)) {
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setInteger(TAG_MUDSLIDE_INTENSITY, intensity + 1);
			tagCompound.setInteger(TAG_MUDSLIDE_XSHIFT, xShift);
			tagCompound.setInteger(TAG_MUDSLIDE_ZSHIFT, zShift);
			this.doFall(world, x, y, z, metadata, tagCompound, this.tickRate(world));
			return true;
		}
		return false;
	}
	
	private void triggerAdjacentBlocksToMudslide(World world, int x, int y, int z, int intensity) {
		// If the intensity is low and the block isn't hydrated, stop sliding.
		if (!this.isHydrated(world, x, y, z) && intensity <= 0) {
			return;
		}
		// If the block is under another block or is sitting on a block that isn't air or a wet block, stop sliding.
		if (!world.isAirBlock(x, y + 1, z)
		|| !(world.isAirBlock(x, y - 1, z) || BlockDynamicEarth.isBlockWet(world, x, y - 1, z))) {
			return;
		}
		Coordinates[] surroundingBlocks = Coordinates.getSurroundingBlockCoords(x, y, z);
		for (Coordinates coords : surroundingBlocks) {
			x = coords.x;
			y = coords.y;
			z = coords.z;
			Block block = coords.getBlock(world);
			int metadata = coords.getBlockMetadata(world);
			if (block instanceof IMudBlock) {
				((IMudBlock)block).tryToMudslide(world, x, y, z, world.rand.nextInt(2) == 0 ? intensity++ : intensity);
			} else if (DynamicEarth.enableMoreDestructiveMudslides
			&& block != null
			&& block.getBlockHardness(world, x, y, z) > -1
			&& BlockFalling.blockCanFallBelow(world, x, y, z)
			&& MudRegistry.willBlockMudslide(block.blockID, metadata, intensity, world.rand)) {
				EntityFallingBlock.spawnFallingBlock(world, x, y, z, block.blockID, metadata, null, block.tickRate(world));
			}
		}
	}
		
	@Override
	public boolean canSpawnFromBlock(World world, int x, int y, int z) {
//		return world.getBlockId(x, y, z) == this.blockID
//		|| world.getBlockMaterial(x, y, z).isReplaceable();
		return world.getBlockId(x, y, z) == this.blockID;
	}
	
	@Override
	public boolean canFallBelow(World world, int x, int y, int z) {
		return y > 0
		&& (world.getBlockMaterial(x, y - 1, z).isReplaceable())
		&& !this.isRooted(world, x, y, z);
	}
	
	@Override
	public boolean tryToFall(World world, int x, int y, int z) {
		if (this.canFallBelow(world, x, y, z)) {
			this.tryToMudslide(world, x, y, z);
			return true;
		}
		return false;
	}
	
	private void doFall(World world, int x, int y, int z, int metadata, NBTTagCompound tagCompound, int delay) {
		world.setBlock(x, y, z, this.blockID, metadata, Helper.DO_NOT_NOTIFY_OR_UPDATE);
		EntityFallingBlock entityFallingBlock = new EntityFallingBlock(world, x, y, z, this.blockID, metadata);
		entityFallingBlock.setAdditionalTags(tagCompound);
		EntityFallingBlock.spawnFallingBlock(entityFallingBlock, delay);
	}
	
	@Override
	public void onStartFalling(Entity entity, World world, int x, int y, int z, int metadata) {
		if (!(entity instanceof EntityFallingBlock)) {
			return;
		}
		EntityFallingBlock entityFallingBlock = (EntityFallingBlock)entity;
		NBTTagCompound tagCompound = entityFallingBlock.getAdditionalTags();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
		}
		int intensity = !tagCompound.hasKey(TAG_MUDSLIDE_INTENSITY) ? 0 : tagCompound.getInteger(TAG_MUDSLIDE_INTENSITY);
		this.triggerAdjacentBlocksToMudslide(world, x, y, z, intensity);
		if (DynamicEarth.enableFancyMudslides) {
			int xShifted = x, zShifted = z;
			if (tagCompound.hasKey(TAG_MUDSLIDE_XSHIFT)) {
				xShifted += tagCompound.getInteger(TAG_MUDSLIDE_XSHIFT);
				tagCompound.removeTag(TAG_MUDSLIDE_XSHIFT);
			}
			if (tagCompound.hasKey(TAG_MUDSLIDE_ZSHIFT)) {
				zShifted += tagCompound.getInteger(TAG_MUDSLIDE_ZSHIFT);
				tagCompound.removeTag(TAG_MUDSLIDE_ZSHIFT);
			}
			if (xShifted != x || zShifted != z) {
				entityFallingBlock.setDead();
				if (this.canFallBelow(world, xShifted, y, zShifted)
				&& world.getEntitiesWithinAABB(EntityFallingBlock.class, AxisAlignedBB.getBoundingBox(xShifted, y, zShifted, xShifted + 1.0D, y + 1.0D, zShifted + 1.0D)).isEmpty()) {
					world.setBlock(xShifted, y, zShifted, entityFallingBlock.blockID, entityFallingBlock.metadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
					this.doFall(world, xShifted, y, zShifted, entityFallingBlock.metadata, tagCompound, this.tickRate(world) << 1);
				} else {
					world.setBlock(x, y, z, entityFallingBlock.blockID, entityFallingBlock.metadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
				}
			}
		}
	}
	
	@Override
	public void onFallTick(Entity entity, World world, int x, int y, int z, int metadata, int fallTime) {
		if (!(entity instanceof EntityFallingBlock)) {
			return;
		}
		EntityFallingBlock entityFallingBlock = (EntityFallingBlock)entity;
		if (fallTime % 6 == 0) {
			NBTTagCompound tagCompound = entityFallingBlock.getAdditionalTags();
			int intensity = tagCompound == null ? 0 : tagCompound.getInteger(TAG_MUDSLIDE_INTENSITY);
			if (intensity >= 5 || this.isHydrated(world, x, y, z)) {
				this.triggerAdjacentBlocksToMudslide(world, x, y, z, intensity);
			}
		}
	}
	
	@Override
	public void onFinishFalling(Entity entity, World world, int x, int y, int z, int metadata) {
		if (!(entity instanceof EntityFallingBlock)) {
			return;
		}
		EntityFallingBlock entityFallingBlock = (EntityFallingBlock)entity;
		if (!world.isAirBlock(x, y, z)) {
			Material material = world.getBlockMaterial(x, y, z);
			if (material.isReplaceable()) {
				world.setBlockToAir(x, y, z);
			} else if (!material.blocksMovement()) {
				Block block = Block.blocksList[world.getBlockId(x, y, z)];
				int meta = world.getBlockMetadata(x, y, z);
				world.setBlockToAir(x, y, z);
				block.dropBlockAsItem(world, x, y, z, block.blockID, meta);
			}
		}
		// Helps prevent floating blocks.
		world.setBlock(x, y, z, this.blockID, entityFallingBlock.metadata, Helper.NOTIFY_AND_UPDATE_REMOTE);
		entity.setDead();
		this.tryToMudslide(world, x, y, z);
	}

	@Override
	public ArrayList<ItemStack> getItemsDropped(World world, int x, int y, int z, int fallTime, int metadata, Random random) {
		ArrayList<ItemStack> drops = this.getBlockDropped(world, x, y, z, metadata, 0);
		if (DynamicEarth.enableMudslideBlockPreservation) {
			for (ItemStack drop : drops) {
				NBTTagCompound compound = drop.getTagCompound();
				if (compound == null) {
					compound = new NBTTagCompound();
				}
				compound.setBoolean(ItemMudBlob.TAG_SPECIAL, true);
				drop.setTagCompound(compound);
			}
		}
		return drops;
	}
	
	@Override
	public int getMetaForFall(int fallTime, int metadata, Random random) {
		return metadata;
	}

	@Override
	public float getFallDamage() {
		return 0.0F;
	}
	
	@Override
	public int getMaxFallDamage() {
		return 0;
	}
	
	@Override
	public DamageSource getDamageSource() {
		return DamageSource.fallingBlock;
	}
	
	@Override
	public int idDropped(int metadata, Random random, int fortune) {
		return DynamicEarth.mudBlob.itemID;
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 4;
	}
	
	@Override
	public int damageDropped(int metadata) {
		return ItemMudBlob.NORMAL;
	}
		
	@Override
    protected ItemStack createStackedBlock(int metadata) {
		return new ItemStack(this.blockID, 1, this.getRawMeta(metadata));
	}
    
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List list) {
		list.add(new ItemStack(blockId, 1, NORMAL));
		list.add(new ItemStack(blockId, 1, WET));
		list.add(new ItemStack(blockId, 1, GRASS));
		list.add(new ItemStack(blockId, 1, WET_GRASS));
		list.add(new ItemStack(blockId, 1, MYCELIUM));
		list.add(new ItemStack(blockId, 1, WET_MYCELIUM));
    }
	    
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(this.idPicked(world, x, y, z), 1, world.getBlockMetadata(x, y, z));
	}
		
	public static void tryToForm(World world, int x, int y, int z) {
		if (world.getBlockId(x, y, z) == Block.dirt.blockID
		&& BlockDynamicEarth.willBlockHydrate(world, x, y, z, 1, 0, 1, 1)) {
			world.setBlock(x, y, z, DynamicEarth.mud.blockID, DynamicEarth.mud.NORMAL, Helper.NOTIFY_AND_UPDATE_REMOTE);
		}
	}
}
