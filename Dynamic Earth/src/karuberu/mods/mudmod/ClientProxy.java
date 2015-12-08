package karuberu.mods.mudmod;

import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.ModTextureAnimation;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod.Instance;

public class ClientProxy extends CommonProxy {
	public static void registerRenderInformation() {
		RenderingRegistry.registerEntityRenderingHandler(EntityMudball.class, new RenderMudball(MudMod.itemsFile, MudMod.ItemIcon.MUDBLOB.ordinal()));
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingSand.class, new RenderFallingSand());
//		FMLClientHandler.instance().getClient().renderEngine.registerTextureFX(new TextureMuddyWaterFX());
//		FMLClientHandler.instance().getClient().renderEngine.registerTextureFX(new TextureMuddyWaterFlowFX());
 	}
}
