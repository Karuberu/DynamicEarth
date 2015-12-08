package karuberu.mods.mudmod.client;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;

public class FXManager {
	
	public static void potionSplash(World world, int x, int y, int z, int potionDamage) {
		if (!world.isRemote) {
			double posX = (double)x;
	        double posY = (double)y;
	        double posZ = (double)z;
	        boolean isSplash = ItemPotion.isSplash(potionDamage);
	        String particleName;
	        if (isSplash) {
	            particleName = "iconcrack_" + Item.potion.itemID;
	            for (int i = 0; i < 8; ++i) {
	            	ModLoader.getMinecraftInstance().renderGlobal.spawnParticle(particleName, posX, posY, posZ, world.rand.nextGaussian() * 0.15D, world.rand.nextDouble() * 0.2D, world.rand.nextGaussian() * 0.15D);
	            }
	        }
            
	        int color = Item.potion.getColorFromDamage(potionDamage);
	        float r = (float)(color >> 16 & 255) / 255.0F;
	        float g = (float)(color >> 8 & 255) / 255.0F;
	        float b = (float)(color >> 0 & 255) / 255.0F;
	        particleName = "spell";
	        if (Item.potion.isEffectInstant(potionDamage)) {
	            particleName = "instantSpell";
	        }
	        for (int i = 0; i < 100; i++) {
	            double d0 = world.rand.nextDouble() * 4.0D;
	            double d1 = world.rand.nextDouble() * Math.PI * 2.0D;
	            double velocityX = Math.cos(d1) * d0;
	            double velocityY = 0.01D + world.rand.nextDouble() * 0.5D;
	            double velocityZ = Math.sin(d1) * d0;
	            EntityFX entityfx = ModLoader.getMinecraftInstance().renderGlobal.doSpawnParticle(particleName, posX + velocityX * 0.1D, posY + 0.3D, posZ + velocityZ * 0.1D, velocityX, velocityY, velocityZ);
	            if (entityfx != null) {
	                float colorRand = 0.75F + world.rand.nextFloat() * 0.25F;
	                entityfx.setRBGColorF(r * colorRand, g * colorRand, b * colorRand);
	                entityfx.multiplyVelocity((float)d0);
	            }
	        }
	        if (isSplash) {
	        	ModLoader.getMinecraftInstance().renderGlobal.theWorld.playSound(posX + 0.5D, posY + 0.5D, posZ + 0.5D, "random.glass", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F, false);
	        }
		}
	}
}
