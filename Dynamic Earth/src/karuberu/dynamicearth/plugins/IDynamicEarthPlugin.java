package karuberu.dynamicearth.plugins;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface IDynamicEarthPlugin {
	
	public String getName();
	
	public String getErrorReportRequestMessage();
	
	public boolean requiredModsAreLoaded();
	
	public void preInitialization(FMLPreInitializationEvent event);
	
	public void initialize() throws NoClassDefFoundError, NoSuchMethodError, Exception;
}
