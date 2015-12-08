package karuberu.dynamicearth.plugins;

import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class PluginBOP implements IDynamicEarthPlugin {

	@Override
	public String getName() {
		return "Biomes O' Plenty Plugin";
	}

	@Override
	public String getErrorReportRequestMessage() {
		return PluginHandler.pleaseNotify;
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
