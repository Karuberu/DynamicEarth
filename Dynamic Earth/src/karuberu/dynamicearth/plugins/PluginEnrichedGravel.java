package karuberu.dynamicearth.plugins;

import karuberu.dynamicearth.DynamicEarth;
import mods.KBIgravelore.api.CustomSifterDropAPI;
import mods.KBIgravelore.api.GravelDropAPI;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class PluginEnrichedGravel implements IDynamicEarthPlugin {

	@Override
	public String getName() {
		return "Enriched Gravel Plugin";
	}

	@Override
	public String getErrorReportRequestMessage() {
		return PluginHandler.pleaseNotify;
	}

	@Override
	public boolean requiredModsAreLoaded() {
		return Loader.isModLoaded("KBIgravelore");
	}

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {}

	@Override
	public void initialize() throws NoClassDefFoundError, NoSuchMethodError, Exception {
		CustomSifterDropAPI.setConstant(Block.dirt.blockID, 0, new ItemStack(DynamicEarth.dirtClod, 4));
    	if (DynamicEarth.includeMud) {
    		GravelDropAPI.addItemToList(0, new ItemStack(DynamicEarth.mudBlob));
    		CustomSifterDropAPI.setConstant(DynamicEarth.mud.blockID, DynamicEarth.mud.NORMAL, new ItemStack(DynamicEarth.mudBlob, 4));
    		CustomSifterDropAPI.setConstant(DynamicEarth.mud.blockID, DynamicEarth.mud.WET, new ItemStack(DynamicEarth.mudBlob, 4));
    		CustomSifterDropAPI.setConstant(DynamicEarth.fertileMud.blockID, DynamicEarth.fertileMud.NORMAL, new ItemStack(DynamicEarth.mudBlob, 4));
    		CustomSifterDropAPI.setConstant(DynamicEarth.fertileMud.blockID, DynamicEarth.fertileMud.WET, new ItemStack(DynamicEarth.mudBlob, 4));
    	}
    	if (DynamicEarth.includeAdobe) {
    		mods.KBIgravelore.api.GravelDropAPI.addItemToList(0, new ItemStack(DynamicEarth.adobeDust));
    	}
    	if (DynamicEarth.includeFertileSoil) {
			CustomSifterDropAPI.setConstant(DynamicEarth.fertileSoil.blockID, DynamicEarth.fertileSoil.DIRT, new ItemStack(DynamicEarth.dirtClod, 3));
			CustomSifterDropAPI.setConstant(DynamicEarth.fertileSoil.blockID, DynamicEarth.fertileSoil.DIRT, new ItemStack(DynamicEarth.peatClump, 1));
    	}
    }
}
