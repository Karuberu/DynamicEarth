package karuberu.dynamicearth;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.SidedProxy;

public class CommonProxy {
	
	@SidedProxy(
		clientSide="karuberu.dynamicearth.client.ClientProxy",
		serverSide="karuberu.dynamicearth.CommonProxy"
	)
	public static CommonProxy proxy;
	
	public void registerNames() {}
	public void registerLocalizations() {}
	public void registerRenderInformation() {}
	public void registerLiquidIcons() {}
	
	public Minecraft getMinecraftClient() {
		return null;
	}
}
