package karuberu.mods.mudmod;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
	
	@SidedProxy(clientSide="karuberu.mods.mudmod.ClientProxy", serverSide="karuberu.mods.mudmod.CommonProxy")
	public static CommonProxy proxy;
	
	public void registerNames() {}
	public void registerLocalizations() {}
	public void registerRenderInformation() {}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	  return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

}
