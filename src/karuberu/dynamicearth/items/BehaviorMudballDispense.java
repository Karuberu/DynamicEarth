package karuberu.dynamicearth.items;

import karuberu.dynamicearth.entity.EntityMudball;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public class BehaviorMudballDispense extends BehaviorProjectileDispense {
	@Override
	protected IProjectile getProjectileEntity(World world, IPosition position) {
		return new EntityMudball(world, position.getX(), position.getY(), position.getZ());
	}
}
