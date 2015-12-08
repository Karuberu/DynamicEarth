package karuberu.mods.mudmod;

import net.minecraft.src.BehaviorProjectileDispense;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.IPosition;
import net.minecraft.src.IProjectile;
import net.minecraft.src.World;

public class BehaviorMudballDispense extends BehaviorProjectileDispense {
    protected IProjectile getProjectileEntity(World world, IPosition position) {
        return new EntityMudball(world, position.getX(), position.getY(), position.getZ());
    }
}
