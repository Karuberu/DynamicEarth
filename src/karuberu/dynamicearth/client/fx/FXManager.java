package karuberu.dynamicearth.client.fx;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public class FXManager {
	public static void spawnNetherGrassParticles(World world, int x, int y, int z, double yOffset) {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			if (world.rand.nextInt(100) == 0) {
				double posX = x + world.rand.nextDouble();
				double posY = y + yOffset;
				double posZ = z + world.rand.nextDouble();
	//			float loudness = 0.05F + world.rand.nextFloat() * 0.2F;
	//			float f2 = 0.8F + world.rand.nextFloat() * 0.8F;
				FXManager.doSpawnNetherGrassParticle(world, posX, posY, posZ);
	//			world.playSound(posX, posY + yOffset, posZ, "random.fizz", loudness, f2, false);
			}
		}
	}
	
	private static void doSpawnNetherGrassParticle(World world, double x, double y, double z) {
		EntityFX entityFX = new EntityNetherGrassFX(world, x, y, z);
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(entityFX);
	}
	
	public static void fizzleEffect(World world, int x, int y, int z, float yOffset, boolean spawnParticles) {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			double posX = x;
			double posY = (double)y + (double)yOffset;
			double posZ = z;
			float loudness = 0.5F;
			float pitch = 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F;
			FMLClientHandler.instance().getClient().sndManager.playSound("random.fizz", x, y + yOffset, z, loudness, pitch);
			if (spawnParticles) {
				for (int i = 0; i < 8; i++) {
					posX = x + world.rand.nextDouble();
					posZ = z + world.rand.nextDouble();
					FMLClientHandler.instance().getClient().renderGlobal.spawnParticle("smoke", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}
	
	public static void flameEffect(World world, int x, int y, int z, double yOffset, boolean spawnParticles) {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			world.playAuxSFX(1009, x, y, z, 0);
			if (spawnParticles) {
				double posX, posZ;
				double posY = y + yOffset;
				for (int i = 0; i < 8; i++) {
					posX = x + world.rand.nextDouble();
					posZ = z + world.rand.nextDouble();
					FXManager.doSpawnNetherGrassParticle(world, posX, posY, posZ);
				}
			}
		}
	}
}
