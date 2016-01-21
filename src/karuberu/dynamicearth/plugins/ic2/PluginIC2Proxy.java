package karuberu.dynamicearth.plugins.ic2;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import karuberu.core.util.plugin.IPlugin;
import karuberu.dynamicearth.plugins.PluginHandler;

public class PluginIC2Proxy implements IPlugin {

	@Override
	public String getName() {
		return "IC2 Plugin";
	}
	
	@Override
	public String getErrorReportRequestMessage() {
		return PluginHandler.instance.getErrorReportRequestMessage();
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
