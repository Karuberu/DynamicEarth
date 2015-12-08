package karuberu.dynamicearth;

import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class GameruleHelper {
	public static final String
		gameruleDoMudTick = "doMudTick";
	
	public static void initializeGamerules(World world) {
		GameRules gameRules = world.getGameRules();
		if (!gameRules.hasRule("doMudTick")) {
			gameRules.addGameRule("doMudTick", "true");
		}
	}
	
	public static boolean doMudTick(World world) {
		return world.getGameRules().getGameRuleBooleanValue(GameruleHelper.gameruleDoMudTick);
	}
	
	public static boolean mobGriefing(World world) {
		return world.getGameRules().getGameRuleBooleanValue("mobGriefing");
	}
	
	public static boolean doMobLoot(World world) {
		return world.getGameRules().getGameRuleBooleanValue("doMobLoot");
	}
	
	public static void setDoMudTick(World world, boolean bool) {
		world.getGameRules().setOrCreateGameRule(GameruleHelper.gameruleDoMudTick, String.valueOf(bool));
	}
}
