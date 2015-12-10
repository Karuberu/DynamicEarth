package karuberu.dynamicearth.plugins.nei;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import karuberu.dynamicearth.plugins.IDynamicEarthPlugin;
import karuberu.dynamicearth.plugins.PluginHandler;

public class PluginNEIProxy implements IDynamicEarthPlugin {

	@Override
	public String getName() {
		return "NEI Plugin";
	}

	@Override
	public String getErrorReportRequestMessage() {
		return PluginHandler.pleaseNotify;
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
