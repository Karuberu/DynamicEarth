package karuberu.dynamicearth.blocks;

import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public interface ITillable {
	public boolean onTilled(World world, int x, int y, int z);
}
