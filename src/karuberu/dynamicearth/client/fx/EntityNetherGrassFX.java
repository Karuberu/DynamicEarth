package karuberu.dynamicearth.client.fx;

import net.minecraft.client.particle.EntityLavaFX;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityNetherGrassFX extends EntityLavaFX {

	public EntityNetherGrassFX(World world, double x, double y, double z) {
		super(world, x, y, z);
		this.motionY = this.rand.nextDouble() * 0.08D + 0.05D;
	}

}
