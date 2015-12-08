package karuberu.mods.mudmod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
	
	@SidedProxy(
		clientSide="karuberu.mods.mudmod.client.ClientProxy",
		serverSide="karuberu.mods.mudmod.CommonProxy"
	)
	public static CommonProxy proxy;
	
	public void registerNames() {}
	public void registerLocalizations() {}
	public void registerRenderInformation() {}
	public void registerLiquidIcons() {}
	public Minecraft getMinecraftClient() {
		return FMLClientHandler.instance().getClient();
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	  return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}


}
