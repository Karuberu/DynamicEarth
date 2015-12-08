package karuberu.core.event;

import karuberu.core.MCHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class EventFactory {
		
	public static boolean onBlockUpdate(World world, int x, int y, int z, boolean randomTick) {
		BlockUpdateEvent event = new BlockUpdateEvent(world, x, y, z, randomTick);
		MinecraftForge.EVENT_BUS.post(event);
		return event.isCanceled();
	}
}
