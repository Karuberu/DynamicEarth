package karuberu.dynamicearth.api.fallingblock;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class EntityFallingBlock extends Entity {

	public EntityFallingBlock(World world) {
		super(world);
	}
	public EntityFallingBlock(World world, int x, int y, int z, int id, int metadata) {
		this(world);
	}

}
