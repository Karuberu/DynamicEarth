package karuberu.mods.mudmod;

import karuberu.mods.mudmod.entity.ai.EntityAIEatGrassSlab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.world.ChunkEvent;

public class EventHandler {
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
					itemTossEvent.player.dropPlayerItem(new ItemStack(MudMod.bomb));
				}
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
		                    chunk.setBlockID(xi, yi, zi, Block.dirt.blockID);
		                }
					}
				}
			}
		}
	}
}
