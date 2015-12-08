package karuberu.core.asm;

public class ObfHelper {
	public static boolean
		obfuscated = false;
	public static enum ObfName {
		Block					("net/minecraft/block/Block", "amq"),
		blocksList				("blocksList", "p"),
		updateTick				("updateTick", "b"),
		World					("net/minecraft/world/World", "yc"),
		WorldServer 			("net/minecraft/world/WorldServer", "in"),
		tickUpdates 			("tickUpdates", "a"),
		tickBlocksAndAmbiance	("tickBlocksAndAmbiance", "g"),
		NextTickListEntry 		("net/minecraft/world/NextTickListEntry", "yt"),
		xCoord 					("xCoord", "a"),
		yCoord 					("yCoord", "b"),
		zCoord 					("zCoord", "c"),
		ExtendedBlockStorage	("net/minecraft/world/chunk/storage/ExtendedBlockStorage", "aaa"),
		getYLocation			("getYLocation", "d");
		
		private final String normalName, obfuscatedName;
		private ObfName(String normalName, String obfuscatedName) {
			this.normalName = normalName;
			this.obfuscatedName = obfuscatedName;
		}
		@Override
		public String toString() {
			return obfuscated ? obfuscatedName : normalName;
		}
		public boolean equals(String string) {
			return this.toString().equals(string);
		}
		public boolean equalsIgnoreCase(String string) {
			return this.toString().equalsIgnoreCase(string);
		}
	}
}
