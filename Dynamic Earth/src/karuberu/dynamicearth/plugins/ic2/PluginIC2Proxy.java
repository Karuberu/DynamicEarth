package karuberu.dynamicearth.plugins.ic2;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import karuberu.dynamicearth.plugins.IDynamicEarthPlugin;
import karuberu.dynamicearth.plugins.PluginHandler;

public class PluginIC2Proxy implements IDynamicEarthPlugin {

	@Override
	public String getName() {
		return "IC2 Plugin";
	}
	
	@Override
	public String getErrorReportRequestMessage() {
		return PluginHandler.pleaseNotify;
	}
	
	@Override
	public boolean requiredModsAreLoaded() {
		return Loader.isModLoaded("IC2");
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {}

	@Override
	public void initialize() throws NoClassDefFoundError, NoSuchMethodError, Exception {
		PluginIC2.initialize();
	}

}
