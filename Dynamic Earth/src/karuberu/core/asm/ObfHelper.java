package karuberu.core.asm;

public class ObfHelper {
	public static boolean
		obfuscated;
	public static enum ObfName {
		Block					("net/minecraft/block/Block", "aou"),
		blocksList				("blocksList", "aou"),
		World					("net/minecraft/world/World", "zv"),
		updateTick				("updateTick", "a"),
		setBlock				("setBlockAndMetadataWithNotify", "f"),
		notifyBlockChange		("notifyBlockChange", "d"),
		WorldServer 			("net/minecraft/world/WorldServer", "iz"),
		tickUpdates 			("tickUpdates", "a"),
		tickBlocksAndAmbiance	("tickBlocksAndAmbiance", "g"),
		NextTickListEntry 		("net/minecraft/world/NextTickListEntry", "aal"),
		xCoord 					("xCoord", "a"),
		yCoord 					("yCoord", "b"),
		zCoord 					("zCoord", "c"),
		ExtendedBlockStorage	("net/minecraft/world/chunk/storage/ExtendedBlockStorage", "abr"),
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
