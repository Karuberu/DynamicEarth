package karuberu.dynamicearth.plugins.nei;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import karuberu.core.util.plugin.IPlugin;
import karuberu.dynamicearth.plugins.PluginHandler;

public class PluginNEIProxy implements IPlugin {

	@Override
	public String getName() {
		return "NEI Plugin";
	}

	@Override
	public String getErrorReportRequestMessage() {
		return PluginHandler.instance.getErrorReportRequestMessage();
	}

	@Override
	public boolean requiredModsAreLoaded() {
		return Loader.isModLoaded("NotEnoughItems");
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {}

	@Override
	public void initialize() throws NoClassDefFoundError, NoSuchMethodError, Exception {
		PluginNEI.initialize();
	}
	
}
