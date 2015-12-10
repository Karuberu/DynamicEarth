package karuberu.dynamicearth.api.mud;

import java.util.ArrayList;
import java.util.Random;
import karuberu.dynamicearth.api.Reference;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MudRegistry {
	private static class MudslideData {
		public final int blockID;
		public final int metadata;
		public final int minIntensity;
		public final float chance;
		public final boolean isMetaSensitive;
		
		public MudslideData(int blockID, int metadata, int minIntensity, float chance, boolean isMetaSensitive) {
			this.blockID = blockID;
			this.metadata = metadata;
			this.minIntensity = minIntensity;
			this.chance = chance;
			this.isMetaSensitive = isMetaSensitive;
		}
	}
	
	private static ArrayList<MudslideData>
		mudslideBlocks = new ArrayList<MudslideData>();
	
	public static void registerMudslideBlock(Block block, int minimumIntensity, float chance) {
		MudRegistry.registerMudslideBlock(block.blockID, minimumIntensity, chance);
	}
	public static void registerMudslideBlock(int blockID, int minimumIntensity, float chance) {
		if (blockID > 0 && blockID < Block.blocksList.length
		&& MudRegistry.findMudslideBlock(blockID, -1) == -1) {
			Reference.logger.fine("Registering block \"" + new ItemStack(blockID, 1, 0).getDisplayName() + "\" as a block affected by mudslides.");
			MudRegistry.mudslideBlocks.add(new MudslideData(blockID, -1, minimumIntensity, chance, false));
		}
	}
	
	public static void registerMudslideBlock(Block block, int metadata, int minimumIntensity, float chance) {
		MudRegistry.registerMudslideBlock(block.blockID, metadata, minimumIntensity, chance);
	}
	public static void registerMudslideBlock(int blockID, int metadata, int minimumIntensity, float chance) {
		if (blockID > 0 && blockID < Block.blocksList.length
		&& MudRegistry.findMudslideBlock(blockID, metadata) == -1) {
			Reference.logger.fine("Registering block \"" + new ItemStack(blockID, 1, metadata).getDisplayName() + "\" (metadata: " + metadata + ") as a block affected by mudslides.");
			MudRegistry.mudslideBlocks.add(new MudslideData(blockID, metadata, minimumIntensity, chance, true));
		}
	}
	
	public static void registerOreDictionaryItems(String tag, int minimumIntensity, float chance) {
		Reference.logger.fine("Registering all OreDictionary blocks with name \"" + tag + "\" as blocks affected by mudslides.");
		ArrayList<ItemStack> items = OreDictionary.getOres(tag);
    	for (ItemStack itemStack : items) {
    		MudRegistry.registerMudslideBlock(itemStack.itemID, itemStack.getItemDamage(), minimumIntensity, chance);
    	}
	}
	
	public static boolean canBlockMudslide(int blockID, int metadata) {
		return MudRegistry.findMudslideBlock(blockID, metadata) > -1;
	}
	
	public static boolean willBlockMudslide(int blockID, int metadata, int intensity, Random random) {
		int index = MudRegistry.findMudslideBlock(blockID, metadata);
		if (index > -1) {
			MudslideData data = MudRegistry.mudslideBlocks.get(index);
			if (intensity >= data.minIntensity
			&& random.nextFloat() < data.chance) {
				return true;
			}
		}
		return false;
	}
	
	private static int findMudslideBlock(int blockID, int metadata) {
		int numRegistered = MudRegistry.mudslideBlocks.size();
		for (int i = 0; i < numRegistered; i++) {
			MudslideData data = MudRegistry.mudslideBlocks.get(i);
			if (data.blockID == blockID
			&& (!data.isMetaSensitive || data.metadata == metadata)) {
				return i;
			}
		}
		return -1;
	}
}
