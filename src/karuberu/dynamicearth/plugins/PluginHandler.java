package karuberu.dynamicearth.plugins;

import karuberu.core.util.KaruberuLogger;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.plugins.ic2.PluginIC2Proxy;
import karuberu.dynamicearth.plugins.nei.PluginNEIProxy;

public class PluginHandler extends karuberu.core.util.plugin.PluginHandler {
	public static boolean
		useForestryPeat;
	public static PluginHandler
		instance = new PluginHandler();
    
	@Override
    public void addPlugins() {
		this.loadedPlugins.add(new PluginBOP());
		this.loadedPlugins.add(new PluginEnrichedGravel());
		this.loadedPlugins.add(new PluginForestry());
		this.loadedPlugins.add(new PluginIC2Proxy());
		this.loadedPlugins.add(new PluginNEIProxy());
		this.loadedPlugins.add(new PluginRailcraft());
		this.loadedPlugins.add(new PluginThaumcraft());
		this.loadedPlugins.add(new PluginThermalExpansion());
    }

	@Override
	protected KaruberuLogger getLogger() {
		return DynamicEarth.logger;
	}
}
