package karuberu.mods.mudmod.items;

import karuberu.mods.mudmod.entity.EntityMudball;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public class BehaviorMudballDispense extends BehaviorProjectileDispense {
    protected IProjectile getProjectileEntity(World world, IPosition position) {
        return new EntityMudball(world, position.getX(), position.getY(), position.getZ());
    }
}
