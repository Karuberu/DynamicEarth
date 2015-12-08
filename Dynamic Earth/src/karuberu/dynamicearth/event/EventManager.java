package karuberu.dynamicearth.event;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import karuberu.core.event.BlockUpdateEvent;
import karuberu.core.event.CanPlantStayEvent;
import karuberu.core.event.EndermanGrabBlockEvent;
import karuberu.core.event.NeighborBlockChangeEvent;
import karuberu.core.util.Helper;
import karuberu.dynamicearth.CommonProxy;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.GameruleHelper;
import karuberu.dynamicearth.api.IItemStackHandler;
import karuberu.dynamicearth.api.ISoil;
import karuberu.dynamicearth.api.ITillable;
import karuberu.dynamicearth.api.Reference;
import karuberu.dynamicearth.api.grass.GrassyBlockRegistry;
import karuberu.dynamicearth.api.grass.IGrassyBlock;
import karuberu.dynamicearth.api.grass.IGrassyBlock.GrassType;
import karuberu.dynamicearth.api.IVanillaReplaceable;
import karuberu.dynamicearth.blocks.BlockMud;
import karuberu.dynamicearth.blocks.BlockPermafrost;
import karuberu.dynamicearth.entity.EntityBomb;
import karuberu.dynamicearth.entity.ai.EntityAIEatGrassyBlock;
import karuberu.dynamicearth.fluids.FluidHelper.FluidReference;
import karuberu.dynamicearth.items.ItemVase;
import karuberu.dynamicearth.world.WorldGenDynamicEarth;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public class EventManager {
	public static EventManager
		instance = new EventManager();
	
	public static void register() {
		 MinecraftForge.EVENT_BUS.register(instance);
	}
	
	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void postTextureStitch(TextureStitchEvent.Post event) {
		if (event.map.textureType == 1) {
			CommonProxy.proxy.registerLiquidIcons();
		}
	}
	
	@ForgeSubscribe
	public void preChunkPopulation(PopulateChunkEvent.Pre event) {
		WorldGenDynamicEarth.populateChunk(event.world, event.chunkProvider, event.chunkX, event.chunkZ, event.hasVillageGenerated, event.rand);
	}
		
	@ForgeSubscribe
	public void onWorldLoaded(WorldEvent.Load event) {
		if (event.world.isRemote) {
			return;
		}
		GameruleHelper.initializeGamerules(event.world);
	}
		
	@ForgeSubscribe
	public void onChunkLoaded(ChunkEvent.Load event) {
		if (event.world.isRemote) {
			return;
		}
		if (DynamicEarth.restoreDirtOnChunkLoad) {
			Chunk chunk = event.getChunk();
			Block block;
			for (int xi = 0; xi < 16; xi++) {
				for (int zi = 0; zi < 16; zi++) {
					for (int yi = 256; yi > 0; yi--) {
						block = Block.blocksList[chunk.getBlockID(xi, yi, zi)];
						if (block == null
						|| block.blockID < 256) {
							continue;
						} else if (block instanceof IVanillaReplaceable) {
							ItemStack itemStack = ((IVanillaReplaceable)block).getVanillaBlockReplacement(chunk, xi, yi, zi);
							if (itemStack != null) {
								chunk.setBlockIDWithMetadata(xi, yi, zi, itemStack.itemID, 0);
							} else {
								chunk.setBlockIDWithMetadata(xi, yi, zi, 0, 0);
							}
						} else if (block instanceof IGrassyBlock) {
							GrassType type = ((IGrassyBlock)block).getType(event.world, xi, yi, zi);
							switch (type) {
							case DIRT:
								chunk.setBlockIDWithMetadata(xi, yi, zi, Block.dirt.blockID, 0);								
								break;
							case GRASS:
								chunk.setBlockIDWithMetadata(xi, yi, zi, Block.grass.blockID, 0);								
								break;
							case MYCELIUM:
								chunk.setBlockIDWithMetadata(xi, yi, zi, Block.mycelium.blockID, 0);								
								break;
							}
						}
					}
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity.worldObj.isRemote) {
			return;
		}
		if (event.entity instanceof EntitySheep) {
			EntitySheep sheep = (EntitySheep)event.entity;
			sheep.tasks.addTask(5, new EntityAIEatGrassyBlock(sheep));
		}
	}
	
	@ForgeSubscribe
	public void onPlayerEntityInteract(EntityInteractEvent event) {
		if (DynamicEarth.includeAdobe) {
			EntityPlayer player = event.entityPlayer;
			ItemStack itemStack = player.getCurrentEquippedItem();
			if (itemStack != null) {
				if (itemStack.itemID == DynamicEarth.vase.itemID
				&& event.target instanceof EntityCow) {
					player.inventory.setInventorySlotContents(
						player.inventory.currentItem,
						ItemVase.getFilledVase(FluidReference.MILK.getBucketVolumeStack())
					);
				} else if (itemStack.itemID == DynamicEarth.earthbowl.itemID
				&& event.target instanceof EntityMooshroom) {
					ItemStack soup = new ItemStack(DynamicEarth.earthbowlSoup);
					if (itemStack.stackSize > 1) {
						if (player.inventory.addItemStackToInventory(soup)) {
							player.inventory.decrStackSize(player.inventory.currentItem, 1);
						}
					} else {
						player.inventory.setInventorySlotContents(player.inventory.currentItem, soup);
					}
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onEndermanGrabBlock(EndermanGrabBlockEvent event) {
		if (event.getResult() != Result.DEFAULT
		|| !GameruleHelper.mobGriefing(event.world)) {
			return;
		}
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block == null || block.hasTileEntity(world.getBlockMetadata(x, y, z))) {
			event.setResult(Result.DENY);
		} else if (block.blockID == DynamicEarth.mud.blockID
		|| block.blockID == DynamicEarth.permafrost.blockID
		|| block.blockID == DynamicEarth.peat.blockID
		|| block.blockID == DynamicEarth.burningSoil.blockID
		|| block instanceof IGrassyBlock) {
			event.setResult(Result.ALLOW);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void onEntityPlaySound(PlaySoundAtEntityEvent event) {
		if (event.entity == null || event.name == null) {
			return;
		}
		Entity entity = event.entity;
		World world = entity.worldObj;
		int x = MathHelper.floor_double(entity.posX);
		int y = MathHelper.floor_double(entity.posY - entity.height);
		int z = MathHelper.floor_double(entity.posZ);
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block != null && block.stepSound != null
		&& event.name.equals(block.stepSound.getStepSound())) {
			if (block.blockID == DynamicEarth.burningSoil.blockID
			&& world.getBlockMetadata(x, y, z) == DynamicEarth.burningSoil.GRASS) {
				event.name = Block.soundGrassFootstep.getStepSound();
			} else if (block instanceof IGrassyBlock) {
				GrassType type = ((IGrassyBlock)block).getType(world, x, y, z);
				if (type == GrassType.MYCELIUM || type == GrassType.GRASS) {
					event.name = Block.soundGrassFootstep.getStepSound();
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void onLivingEntityDeath(LivingDeathEvent event) {
		if (event.entityLiving == null
		|| event.entityLiving.worldObj.isRemote) {
			return;
		}
		World world = event.entityLiving.worldObj;
		if (DynamicEarth.enableEndermanBlockDrops
		&& GameruleHelper.doMobLoot(world)
		&& event.entityLiving instanceof EntityEnderman) {
			EntityEnderman enderman = (EntityEnderman)event.entityLiving;
			if (enderman.getCarried() != 0) {
				ItemStack carriedItem = new ItemStack(enderman.getCarried(), 1, enderman.getCarryingData());
				enderman.setCarried(0);
				enderman.setCarryingData(0);
				enderman.entityDropItem(carriedItem, 0);
			}
		}
	}
	
	@ForgeSubscribe
	public void itemTossed(ItemTossEvent event) {
		if (event.player.worldObj.isRemote
		&& event.entityItem == null) {
			return;
		}
		if (DynamicEarth.includeBombs) {
			ItemStack itemStack = event.entityItem.getDataWatcher().getWatchableObjectItemStack(10);
			if (itemStack != null
			&& itemStack.getItem() == DynamicEarth.bombLit) {
				event.setCanceled(true);
				ItemStack droppedStack;
				if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("Stack Size")) {
					droppedStack = new ItemStack(DynamicEarth.bomb, itemStack.getTagCompound().getByte("Stack Size") + 1);
				} else {
					droppedStack = new ItemStack(DynamicEarth.bomb);
				}
				droppedStack.setTagCompound(itemStack.getTagCompound());
				event.player.dropPlayerItem(droppedStack);
			}
		}
	}
	
	@ForgeSubscribe
	public void itemPickedUp(EntityItemPickupEvent event) {
		if (event.entity.worldObj.isRemote) {
			return;
		}
		ItemStack itemStack = event.item.getEntityItem();
		if (itemStack != null
		&& itemStack.getItem() instanceof IItemStackHandler) {
			IItemStackHandler handler = (IItemStackHandler)itemStack.getItem();
			World world = event.entity.worldObj;
			int x = MathHelper.floor_double(event.entity.posX);
			int y = MathHelper.floor_double(event.entity.posY);
			int z = MathHelper.floor_double(event.entity.posZ);
			boolean pickedUp = handler.onPickup(world, x, y, z, itemStack);
			if (!pickedUp) {
				event.setCanceled(true);
			}
		}
	}
	
	@ForgeSubscribe
	public void itemDestroyed(PlayerDestroyItemEvent event) {
		if (event.entity.worldObj.isRemote) {
			return;
		}
		if (DynamicEarth.includeBombs
		&& event.original.getItem() == DynamicEarth.bombLit) {
			World world = event.entity.worldObj;
			EntityPlayer player = event.entityPlayer;
			world.spawnEntityInWorld(new EntityBomb(world, player, event.original));
		}
	}
	
	@ForgeSubscribe
	public void itemExpired(ItemExpireEvent event) {
		ItemStack itemStack = event.entityItem.getEntityItem();
		if (itemStack != null
		&& itemStack.getItem() instanceof IItemStackHandler) {
			IItemStackHandler handler = (IItemStackHandler)itemStack.getItem();
			World world = event.entity.worldObj;
			int x = MathHelper.floor_double(event.entity.posX);
			int y = MathHelper.floor_double(event.entity.posY);
			int z = MathHelper.floor_double(event.entity.posZ);
			int extraTime = handler.onExpire(world, x, y, z, itemStack);
			if (extraTime > 0) {
				event.extraLife = extraTime;
				event.setCanceled(true);
			}
		}
	}
	
	@ForgeSubscribe
	public void onBlockUpdated(BlockUpdateEvent event) {
		if (event.world.isRemote) {
			return;
		}
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		IGrassyBlock grassyBlock = null;
		
		if (!DynamicEarth.restoreDirtOnChunkLoad
		|| DynamicEarth.enableBottomSlabGrassKilling) {
			grassyBlock = GrassyBlockRegistry.getGrassyBlock(world, x, y, z);
		}
		
		if (DynamicEarth.enableBottomSlabGrassKilling
		&& grassyBlock != null) {
			if (Block.blocksList[world.getBlockId(x, y + 1, z)] instanceof BlockHalfSlab
			&& Helper.isBottomSlab(world.getBlockMetadata(x, y + 1, z))) {
				ItemStack itemStack = grassyBlock.getBlockForType(world, x, y, z, GrassType.DIRT);
				if (itemStack != null) {
					world.setBlock(x, y, z, itemStack.itemID, itemStack.getItemDamage(), Helper.NOTIFY_AND_UPDATE_REMOTE);
				}
			}
		}
		
		if (!DynamicEarth.restoreDirtOnChunkLoad
		&& event.isRandomTick) {
			int blockId = world.getBlockId(x, y, z);
			if (blockId == Block.dirt.blockID) {
				if (DynamicEarth.includeMud) {
					BlockMud.tryToForm(world, x, y, z);
				}
				if (DynamicEarth.includePermafrost) {
					BlockPermafrost.tryToForm(world, x, y, z);
				}
			} else if (grassyBlock != null
			&& grassyBlock.canSpread(world, x, y, z)) {
				int xi, yi, zi;
				int targetBlockID;
				IGrassyBlock targetGrassyBlock;
				GrassType type = grassyBlock.getType(world, x, y, z);
				boolean isModdedBlock = GrassyBlockRegistry.isModdedBlock(world, x, y, z);
				for (int i = 0; i < 4; ++i) {
					xi = x + world.rand.nextInt(3) - 1;
					yi = y + world.rand.nextInt(5) - 3;
					zi = z + world.rand.nextInt(3) - 1;
					targetBlockID = world.getBlockId(xi, yi, zi);
					if (targetBlockID > 0
					&& (isModdedBlock || targetBlockID != Block.dirt.blockID)) {
						targetGrassyBlock = GrassyBlockRegistry.getGrassyBlock(targetBlockID, world.getBlockMetadata(xi, yi, zi));
						if (targetGrassyBlock != null) {
							targetGrassyBlock.tryToGrow(world, xi, yi, zi, type);
						}
					}
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onCanPlantStayCheck(CanPlantStayEvent event) {
		if (event.getResult() != Result.DEFAULT) {
			return;
		}
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		Block soil = Block.blocksList[world.getBlockId(x, y - 1, z)];
		if (soil instanceof ISoil
		&& ((ISoil)soil).willForcePlantToStay(world, x, y - 1, z, event.plant)) {
			event.setResult(Result.ALLOW);
		}
	}
	
	@ForgeSubscribe
	public void onNeighborBlockChange(NeighborBlockChangeEvent event) {
		if (event.getResult() == Result.DENY) {
			return;
		}
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		
		if (DynamicEarth.enableGrassBurning) {
			IGrassyBlock grassyBlock = GrassyBlockRegistry.getGrassyBlock(world, x, y, z);
			if (grassyBlock != null) {
				Material material = world.getBlockMaterial(x, y + 1, z);
				if ((material == Material.fire || material == Material.lava)
				&& grassyBlock.willBurn(world, x, y, z)) {
					ItemStack itemStack = grassyBlock.getBlockForType(world, x, y, z, GrassType.DIRT);
					if (itemStack != null) {
						world.setBlock(x, y, z, itemStack.itemID, itemStack.getItemDamage(), Helper.NOTIFY_AND_UPDATE_REMOTE);
					}
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onUseHoe(UseHoeEvent event) {
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		boolean tilled = false;
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block != null
		&& world.isAirBlock(x, y + 1, z)) {
			if (block.blockID == Block.mycelium.blockID) {
				tilled = Reference.mycelium.onTilled(world, x, y, z);
			} else if (block instanceof ITillable) {
				tilled = ((ITillable)block).onTilled(world, x, y, z);
			}
			if (tilled) {
				event.entityPlayer.swingItem();
				event.current.attemptDamageItem(1, world.rand);
				block = Block.blocksList[world.getBlockId(x, y, z)];
				if (block != null) {
					world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onBonemeal(BonemealEvent event) {
		if (!event.world.isRemote) {
			World world = event.world;
			int x = event.X;
			int y = event.Y;
			int z = event.Z;
			IGrassyBlock grassyBlock = GrassyBlockRegistry.getGrassyBlock(world, x, y, z);
			if (grassyBlock != null
			&& GrassyBlockRegistry.isModdedBlock(world, x, y, z)
			&& grassyBlock.getType(world, x, y, z) == GrassType.GRASS) {
				for (int i = 0; i < 128; i++) {
					int xi = x;
					int yi = y + 1;
					int zi = z;
					for (int j = 0; j < i / 16; j++) {
						xi += world.rand.nextInt(3) - 1;
						yi += (world.rand.nextInt(3) - 1) * world.rand.nextInt(3) / 2;
						zi += world.rand.nextInt(3) - 1;
						if (world.getBlockId(xi, yi - 1, zi) == Block.grass.blockID
						&& world.isBlockNormalCube(xi, yi, zi)) {
							break;
						}
					}
					if (world.isAirBlock(xi, yi, zi)) {
						if (world.rand.nextInt(10) != 0) {
							if (Block.tallGrass.canBlockStay(world, xi, yi, zi)) {
								world.setBlock(xi, yi, zi, Block.tallGrass.blockID, 1, 3);
							}
						} else {
							ForgeHooks.plantGrass(world, xi, yi, zi);
						}
					}
				}
				event.setResult(Result.ALLOW);
			}
		}
		event.setCanceled(false);
	}
}
