package karuberu.dynamicearth.plugins;

import karuberu.core.util.plugin.IPlugin;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class PluginBOP implements IPlugin {

	@Override
	public String getName() {
		return "Biomes O' Plenty Plugin";
	}

	@Override
	public String getErrorReportRequestMessage() {
		return PluginHandler.instance.getErrorReportRequestMessage();
	}

	@Override
	public boolean requiredModsAreLoaded() {
		return Loader.isModLoaded("BiomesOPlenty");
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {}

	@Override
	public void initialize() throws NoClassDefFoundError, NoSuchMethodError, Exception {
		Item mudball = biomesoplenty.api.Items.mudball.get();
		if (mudball != null) {
			OreDictionary.registerOre("mudBlob", mudball);
		}
	}
}
