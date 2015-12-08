package karuberu.mods.mudmod;

import java.util.Random;

import karuberu.core.MCHelper;
import karuberu.core.event.BlockUpdateEvent;
import karuberu.core.event.INeighborBlockEventHandler;
import karuberu.core.event.NeighborBlockChangeEvent;
import karuberu.mods.mudmod.blocks.BlockAdobe;
import karuberu.mods.mudmod.blocks.BlockMud;
import karuberu.mods.mudmod.blocks.BlockMudMod;
import karuberu.mods.mudmod.blocks.BlockPeat;
import karuberu.mods.mudmod.blocks.BlockPeatMoss;
import karuberu.mods.mudmod.blocks.BlockPermafrost;
import karuberu.mods.mudmod.entity.EntityBomb;
import karuberu.mods.mudmod.entity.ai.EntityAIEatGrassSlab;
import karuberu.mods.mudmod.items.ItemBombLit;
import karuberu.mods.mudmod.world.WorldGenMudMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkEvent;

public class EventHandler {
	public static EventHandler
		instance = new EventHandler();
	
	public static void register() {
		 MinecraftForge.EVENT_BUS.register(instance);
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
					player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(MudMod.vaseMilk));
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
				((EntitySheep)entity).tasks.addTask(9, new EntityAIEatGrassSlab((EntitySheep)entity));
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
		                if (id == MudMod.mud.blockID || id == MudMod.permafrost.blockID) {
		                    chunk.setBlockIDWithMetadata(xi, yi, zi, Block.dirt.blockID, 0);
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
		
		int blockID = world.getBlockId(x, y, z);
		if (!MudMod.restoreDirtOnChunkLoad
		&& blockID == Block.dirt.blockID) {
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
		}
	}
	
	@ForgeSubscribe
	public void onNeighborBlockChange(NeighborBlockChangeEvent event) {
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		int neighborBlockID = event.neighborBlockID;
		int side = event.side;
		
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block instanceof INeighborBlockEventHandler) {
			((INeighborBlockEventHandler)block).handleNeighborBlockChangeEvent(event);
		}
		
		int blockID = world.getBlockId(x, y, z);
		if (MudMod.enableGrassBurning
		&& (blockID == Block.grass.blockID)) {
			Material material = world.getBlockMaterial(x, y + 1, z);
			if (material == Material.fire || material == Material.lava) {
				world.setBlock(x, y, z, Block.dirt.blockID, 0, MCHelper.NOTIFY_AND_UPDATE_REMOTE);
			}
		}
	}
}
