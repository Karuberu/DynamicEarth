package karuberu.dynamicearth.plugins;

import java.util.ArrayList;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.plugins.ic2.PluginIC2Proxy;
import karuberu.dynamicearth.plugins.nei.PluginNEIProxy;

public class PluginHandler {
	public static boolean
		useForestryPeat;
	public static final int
		GENERIC_ERROR = -1,
		MOD_NOT_FOUND = 1,
		ERROR_NOT_FOUND = 2;
    private static ArrayList<IDynamicEarthPlugin>
    	loadedPlugins = new ArrayList<IDynamicEarthPlugin>();
    static {
		loadedPlugins.add(new PluginBOP());
		loadedPlugins.add(new PluginEnrichedGravel());
		loadedPlugins.add(new PluginForestry());
		loadedPlugins.add(new PluginIC2Proxy());
		loadedPlugins.add(new PluginNEIProxy());
		loadedPlugins.add(new PluginRailcraft());
		loadedPlugins.add(new PluginThaumcraft());
		loadedPlugins.add(new PluginThermalExpansion());
	}
	public static final String
		pleaseNotify = "Please contact Karuberu in the Dynamic Earth forum" +
			"thread and notify him of this error (include the contents of" +
			"this log file using spoiler tags).";

	public static void handlePluginPreInitialization(FMLPreInitializationEvent event) {
	    for (IDynamicEarthPlugin plugin : loadedPlugins) {
			plugin.preInitialization(event);
	    }
	}
	
	public static void initializePlugins() {
	    for (IDynamicEarthPlugin plugin : loadedPlugins) {
	    	if (plugin.requiredModsAreLoaded()) {
	    		try {
					plugin.initialize();
				} catch (NoClassDefFoundError e) {
					PluginHandler.printNotification(plugin, ERROR_NOT_FOUND, null);
				} catch (NoSuchMethodError e) {
					PluginHandler.printNotification(plugin, ERROR_NOT_FOUND, null);
				} catch (Exception e) {
					PluginHandler.printNotification(plugin, GENERIC_ERROR, e);
				}
			} else {
				PluginHandler.printNotification(plugin, MOD_NOT_FOUND, null);
			}
	    }
	}
	
	public static void printNotification(IDynamicEarthPlugin plugin, int errorCode, Exception e) {
		switch (errorCode) {
		case MOD_NOT_FOUND:
			DynamicEarth.logger.info("Required mods for " + plugin.getName() + " were not found. Skipping integration.");
			break;
		case ERROR_NOT_FOUND:
			DynamicEarth.logger.warning("Required mods for " + plugin.getName() + " were detected, but it appears to be incompatible. Skipping integration. " + plugin.getErrorReportRequestMessage());
			break;
		case GENERIC_ERROR:
			DynamicEarth.logger.warning("Required mods for " + plugin.getName() + " were detected, but integration has failed. One or more mods may have changed and become incompatible. " + pleaseNotify);
			e.printStackTrace();
			break;
		}
	}
}
