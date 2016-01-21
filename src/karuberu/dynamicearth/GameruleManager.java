package karuberu.dynamicearth;

import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class GameruleManager {
	public static final String
		gameruleDoMudTick = "doMudTick";
	
	public static void initializeGamerules(World world) {
		GameRules gameRules = world.getGameRules();
		if (!gameRules.hasRule("doMudTick")) {
			gameRules.addGameRule("doMudTick", "true");
		}
	}
	
	public static boolean doMudTick(World world) {
		return world.getGameRules().getGameRuleBooleanValue(GameruleManager.gameruleDoMudTick);
	}
	
	public static void setDoMudTick(World world, boolean bool) {
		world.getGameRules().setOrCreateGameRule(GameruleManager.gameruleDoMudTick, String.valueOf(bool));
	}
}
