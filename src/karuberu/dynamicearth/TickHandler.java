package karuberu.dynamicearth;

import java.util.EnumSet;
import java.util.LinkedList;
import karuberu.core.util.Coordinates;
import karuberu.dynamicearth.blocks.BlockPermafrost;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class TickHandler implements ITickHandler {
		
	public static TickHandler
		instance = new TickHandler();
	private static int
		frostTickSpacing = 100;
	
	private int
		tick = 0;
	private LinkedList<LinkedList<Entity>>
		delayedSpawns = new LinkedList<LinkedList<Entity>>();
	private LinkedList<LinkedList<Coordinates>>
		scheduledPermafrost = new LinkedList<LinkedList<Coordinates>>();
	
	public static void register() {
		TickRegistry.registerTickHandler(instance, Side.SERVER);
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (this.delayedSpawns.size() > 0) {
			LinkedList<Entity> entityList = this.delayedSpawns.pop();
			if (entityList != null) {
				for (Entity entity : entityList) {
					entity.worldObj.spawnEntityInWorld(entity);
				}
			}
		}
		if (tick % frostTickSpacing == 0
		&& this.scheduledPermafrost.size() > 0) {
			LinkedList<Coordinates> coordList = this.scheduledPermafrost.pop();
			if (coordList != null) {
				for (Coordinates coords : coordList) {
					if (coords.getBlockID() == Block.dirt.blockID) {
						coords.setBlock(DynamicEarth.permafrost.blockID, BlockPermafrost.META_PERMAFROST);
					}
				}
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		tick++;
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return "DynamicEarthWorldTicks";
	}

	/**
	 * Schedules an entity to spawn after a specified delay in world ticks.
	 * @param entity : The entity to be spawned.
	 * @param tickDelay : The delay in world ticks. Max: 30
	 */
	public void scheduleEntitySpawn(Entity entity, int tickDelay) {
		if (entity == null || tickDelay <= 0) {
			return;
		} else if (tickDelay > 30) {
			tickDelay = 30;
		}
		int index = tickDelay - 1;
		LinkedList<Entity> entityList = null;
		if (index < this.delayedSpawns.size()) {
			entityList = this.delayedSpawns.get(index);
		}
		if (entityList == null) {
			entityList = new LinkedList<Entity>();
		}
		entityList.push(entity);
		while (this.delayedSpawns.size() <= index) {
			this.delayedSpawns.add(null);
		}
		this.delayedSpawns.set(index, entityList);
	}
	
	public void schedulePermafrostFreeze(World world, int x, int y, int z, int frostTicks) {
		if (world == null) {
			return;
		}
		Coordinates coords = new Coordinates(world, x, y, z);
		int index = frostTicks - 1;
		LinkedList<Coordinates> coordList = null;
		if (index < this.scheduledPermafrost.size()) {
			coordList = this.scheduledPermafrost.get(index);
		}
		if (coordList == null) {
			coordList = new LinkedList<Coordinates>();
		}
		coordList.push(coords);
		while (this.scheduledPermafrost.size() <= index) {
			this.scheduledPermafrost.add(null);
		}
		this.scheduledPermafrost.set(index, coordList);
	}
}
