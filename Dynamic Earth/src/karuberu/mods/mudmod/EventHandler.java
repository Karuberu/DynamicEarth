package karuberu.mods.mudmod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import karuberu.core.MCHelper;
import karuberu.core.event.BlockUpdateEvent;
import karuberu.core.event.CanPlantStayEvent;
import karuberu.core.event.INeighborBlockEventHandler;
import karuberu.core.event.NeighborBlockChangeEvent;
import karuberu.mods.mudmod.blocks.BlockMud;
import karuberu.mods.mudmod.blocks.BlockMudMod;
import karuberu.mods.mudmod.blocks.BlockPermafrost;
import karuberu.mods.mudmod.blocks.IGrassyBlock;
import karuberu.mods.mudmod.blocks.ISoil;
import karuberu.mods.mudmod.blocks.ITillable;
import karuberu.mods.mudmod.blocks.IGrassyBlock.EnumGrassType;
import karuberu.mods.mudmod.entity.EntityBomb;
import karuberu.mods.mudmod.entity.ai.EntityAIEatGrassyBlock;
import karuberu.mods.mudmod.items.ItemVase;
import karuberu.mods.mudmod.liquids.LiquidHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.liquids.LiquidDictionary;

public class EventHandler {
	public static EventHandler
		instance = new EventHandler();
	
	public static void register() {
		 MinecraftForge.EVENT_BUS.register(instance);
	}
	
	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void postTextureStitch(TextureStitchEvent.Post event) {
		CommonProxy.proxy.registerLiquidIcons();
	}
	
	@ForgeSubscribe
	public void itemTossed(ItemTossEvent itemTossEvent) {
		if (MudMod.includeBombs) {
			if (!itemTossEvent.player.worldObj.isRemote
			&& itemTossEvent.entityItem != null) {
				ItemStack itemStack = itemTossEvent.entityItem.getDataWatcher().getWatchableObjectItemStack(10);
				if (itemStack != null
				&& itemStack.getItem() == MudMod.bombLit
				&& itemStack.isItemDamaged()) {
					itemTossEvent.setCanceled(true);
					ItemStack droppedStack;
					if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("Stack Size")) {
						droppedStack = new ItemStack(MudMod.bomb, itemStack.getTagCompound().getByte("Stack Size") + 1);
					} else {
						droppedStack = new ItemStack(MudMod.bomb);
					}
					droppedStack.setTagCompound(itemStack.getTagCompound());
					itemTossEvent.player.dropPlayerItem(droppedStack);
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void itemDestroyed(PlayerDestroyItemEvent destroyItemEvent) {
		if (MudMod.includeBombs
		&& destroyItemEvent.original.getItem() == MudMod.bombLit) {
			if (!destroyItemEvent.entity.worldObj.isRemote) {
				destroyItemEvent.entity.worldObj.spawnEntityInWorld(new EntityBomb(destroyItemEvent.entity.worldObj, destroyItemEvent.entityPlayer, destroyItemEvent.original));
			}
		}
	}
	
	@ForgeSubscribe
	public void onPlayerEntityInteract(EntityInteractEvent entityInteractEvent) {
		if (MudMod.includeAdobe) {
			EntityPlayer player = entityInteractEvent.entityPlayer;
			ItemStack itemStack = player.getCurrentEquippedItem();
			if (itemStack != null) {
				Item item = itemStack.getItem();
				if (item == MudMod.vase
				&& entityInteractEvent.target instanceof EntityCow) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(MudMod.vase, 1, ItemVase.getDamage(LiquidDictionary.getLiquid(LiquidHandler.MILK, 1000))));
				} else if (item == MudMod.earthbowl
				&& entityInteractEvent.target instanceof EntityMooshroom) {
					ItemStack soup = new ItemStack(MudMod.earthbowlSoup);
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
	public void onEntityJoinWorld(EntityJoinWorldEvent entityJoinWorldEvent) {
		if (MudMod.includeDirtSlabs) {
			Entity entity = entityJoinWorldEvent.entity;
			if (entity instanceof EntitySheep) {
				((EntitySheep)entity).tasks.addTask(9, new EntityAIEatGrassyBlock((EntitySheep)entity));
			}
		}
	}
	
	@ForgeSubscribe
	public void onChunkLoaded(ChunkEvent.Load chunkLoadEvent) {
		if (MudMod.restoreDirtOnChunkLoad && !chunkLoadEvent.world.isRemote) {
			Chunk chunk = chunkLoadEvent.getChunk();
			for (int xi = 0; xi < 16; xi++) {
				for (int zi = 0; zi < 16; zi++) {
					for (int yi = 127; yi > 0; yi--) {
						int id = chunk.getBlockID(xi, yi, zi);
						if (id == MudMod.mud.blockID
						|| id == MudMod.permafrost.blockID
						|| id == MudMod.peat.blockID) {
							chunk.setBlockIDWithMetadata(xi, yi, zi, Block.dirt.blockID, 0);
						} else {
							Block block = Block.blocksList[id];
							if (block instanceof IGrassyBlock) {
								EnumGrassType type = ((IGrassyBlock)block).getType(chunkLoadEvent.world, xi, yi, zi);
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
	}
	
	@ForgeSubscribe
	public void onBlockUpdated(BlockUpdateEvent event) {
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		
		int blockId = world.getBlockId(x, y, z);
		if (!MudMod.restoreDirtOnChunkLoad) {
			if (blockId == Block.dirt.blockID) {
				if (BlockMudMod.willBlockHydrate(world, x, y, z, 1, 0, 1, 1)) {
					world.setBlock(x, y, z, MudMod.mud.blockID, BlockMud.NORMAL, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
				} else if (MudMod.includePermafrost
				&& BlockPermafrost.canForm(world, x, y, z)) {
					int metadata = world.getBlockMetadata(x, y, z);
					if (metadata >= 5) {
						world.setBlock(x, y, z, MudMod.permafrost.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
					} else {
						world.setBlockMetadataWithNotify(x, y, z, metadata + 1, MCHelper.DO_NOT_NOTIFY_OR_UPDATE);
					}
				}
			} else if (blockId == Block.grass.blockID
			|| blockId == Block.mycelium.blockID
			|| Block.blocksList[blockId] instanceof IGrassyBlock) {
				if (!world.isRemote) {
					IGrassyBlock block;
					if (blockId == Block.grass.blockID
					|| blockId == Block.mycelium.blockID) {
						block = IGrassyBlock.dirt;
					} else {
						block = (IGrassyBlock)Block.blocksList[blockId];
					}
					EnumGrassType type = block.getType(world, x, y, z);
					if (block.canSpread(world, x, y, z)) {
						for (int i = 0; i < 4; ++i) {
							int xi = x + world.rand.nextInt(3) - 1;
							int yi = y + world.rand.nextInt(5) - 3;
							int zi = z + world.rand.nextInt(3) - 1;
							int targetId = world.getBlockId(xi, yi, zi);
							IGrassyBlock targetBlock = null;
							if (Block.blocksList[targetId] instanceof IGrassyBlock) {
								targetBlock = (IGrassyBlock)Block.blocksList[targetId];
							} else if (targetId == Block.dirt.blockID
							&& blockId != Block.grass.blockID
							&& blockId != Block.mycelium.blockID) {
								targetBlock = IGrassyBlock.dirt;
							}
							if (targetBlock != null) {
								targetBlock.tryToGrow(world, xi, yi, zi, type);
							}
						}
					}
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onNeighborBlockChange(NeighborBlockChangeEvent event) {
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block instanceof INeighborBlockEventHandler) {
			((INeighborBlockEventHandler)block).handleNeighborBlockChangeEvent(event);
		}
		
		int blockID = world.getBlockId(x, y, z);
		if (MudMod.enableGrassBurning) {
			if (blockID == Block.grass.blockID
			|| blockID == Block.mycelium.blockID
			|| Block.blocksList[blockID] instanceof IGrassyBlock) {
				Material material = world.getBlockMaterial(x, y + 1, z);
				if (material == Material.fire || material == Material.lava) {
					if (blockID == Block.grass.blockID || blockID == Block.mycelium.blockID) {
						world.setBlock(x, y, z, Block.dirt.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
					} else {
						ItemStack itemStack = ((IGrassyBlock)Block.blocksList[blockID]).getBlockForType(world, x, y, z, EnumGrassType.DIRT);
						if (itemStack != null) {
							world.setBlock(x, y, z, itemStack.itemID, itemStack.getItemDamage(), MCHelper.NOTIFY_AND_UPDATE_REMOTE);
						}
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
		if (block != null) {
			if (MudMod.enableMyceliumTilling
			&& block.blockID == Block.mycelium.blockID) {
				world.setBlock(x, y, z, Block.tilledField.blockID);
				tilled = true;
			} else if (block instanceof ITillable
			&& ((ITillable)block).onTilled(world, x, y, z)) {
				tilled = true;
			}
			if (tilled) {
				event.entityPlayer.swingItem();
				event.current.attemptDamageItem(1, world.rand);
				block = Block.blocksList[world.getBlockId(x, y, z)];
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
			}
		}
	}
	
	@ForgeSubscribe
	public void onBonemeal(BonemealEvent event) {
		World world = event.world;
		int x = event.X;
		int y = event.Y;
		int z = event.Z;
		if (!world.isRemote) {
			Block block = Block.blocksList[world.getBlockId(x, y, z)];
			if (block instanceof IGrassyBlock
			&& ((IGrassyBlock)block).getType(world, x, y, z) == EnumGrassType.GRASS) {
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
	
	@ForgeSubscribe
	public void onCheckCanPlantStay(CanPlantStayEvent event) {
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		Block soil = Block.blocksList[world.getBlockId(x, y - 1, z)];
		if (soil instanceof ISoil) {
			event.setCanceled(!((ISoil)soil).willForcePlantToStay(world, x, y - 1, z, event.plant));
		} else {
			event.setCanceled(true);
		}
	}
}
