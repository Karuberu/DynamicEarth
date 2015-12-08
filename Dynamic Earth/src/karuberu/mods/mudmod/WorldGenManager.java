package karuberu.mods.mudmod;

import cpw.mods.fml.common.registry.GameRegistry;

public class WorldGenManager {
	
	public static boolean
		doGenerateMud,
		doGeneratePermafrost;

	public static void registerWorldGen() {
        if (doGeneratePermafrost) {
        	GameRegistry.registerWorldGenerator(new WorldGenPermafrost(MudMod.permafrost.blockID));
        }
        if (doGenerateMud) {
        	GameRegistry.registerWorldGenerator(new WorldGenMud(4, MudMod.mud.blockID));
        }
	}
}
