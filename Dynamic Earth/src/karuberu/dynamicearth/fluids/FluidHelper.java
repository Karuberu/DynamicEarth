package karuberu.dynamicearth.fluids;

import java.util.Hashtable;
import karuberu.dynamicearth.DELogger;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Helper class whose primary function is to facilitate cleaner and more streamlined
 * code when dealing with fluids.
 * @author Karuberu
 */
public class FluidHelper {
	/**
	 * Provides quick and standardized access to known fluids and their properties.
	 * @author Karuberu
	 */
	public static enum FluidReference {
		WATER("water", "7690DA", 0),
		MILK("milk", "FEFEFE"),
		SOUP("soup", "CC9978"),
		REDSTONE("redstone", "C40000"),
		ENDER("ender", "105E51"),
		GLOWSTONE("glowstone", "FECB03"),
		CREOSOTE("creosote", "676311"),
		STEAM("steam", "828282", 15),
		OIL("oil", "202020", 0),
		FUEL("fuel", "C1B920", 0),
		BIOMASS("biomass", "29B000"),
		ETHANOL("bioethanol", "FF7100"),
		SEEDOIL("seedoil", "FFFFA7"),
		HONEY("honey", "FFD801"),
		JUICE("juice", "3BC900"),
		ICE("ice", "53D9F1");
		
		public final String
			name, color;
		public int
			blockMeta = -1;
		
		private FluidReference(String name, String color) {
			this.name = name;
			this.color = color;
		}
		
		private FluidReference(String name, String color, int blockMeta) {
			this(name, color);
			this.blockMeta = blockMeta;
		}
		
		public Fluid getFluid() {
			return FluidRegistry.getFluid(name);
		}
		
		/**
		 * Returns a fluid stack of the fluid with the specified volume.
		 * @param amount
		 */
		public FluidStack getFluidStack(int amount) {
			return FluidRegistry.getFluidStack(name, amount);
		}
		
		/**
		 * Returns a fluid stack of the fluid with the standard bucket volume.
		 */
		public FluidStack getBucketVolumeStack() {
			return this.getFluidStack(BUCKET_VOLUME);
		}
		
		/**
		 * Returns a fluid stack of the fluid with the standard bowl volume.
		 */
		public FluidStack getBowlVolumeStack() {
			return this.getFluidStack(BOWL_VOLUME);
		}
		
		/**
		 * Returns a fluid stack of the fluid with the standard bottle volume.
		 */
		public FluidStack getBottleVolumeStack() {
			return this.getFluidStack(BOTTLE_VOLUME);
		}
		
		/**
		 * Returns the name and color of the liquid in the form suited to saving in
		 * the config file.
		 */
		public String toConfigString() {
			return this.name + ":" + this.color;
		}
		
		/**
		 * Returns a string array with all the defined fluids in this enum,
		 * each in the config format.
		 */
		public static String[] getConfig() {
			FluidReference[] list = FluidReference.values();
			String[] configList = new String[FluidReference.values().length];
			for (int i = 0; i < configList.length; i++) {
				configList[i] = list[i].toConfigString();
			}
			return configList;
		}
	}
	public static final int
		UNDEFINED_COLOR = 0xFFFFFF,
		BUCKET_VOLUME = FluidContainerRegistry.BUCKET_VOLUME,
		BOWL_VOLUME = 250,
		BOTTLE_VOLUME = 250;
	public static final int
		STILL_LAVA_META = 0,
		STILL_WATER_META = 0;
	private static Hashtable<String, Integer>
		colorTable = new Hashtable<String, Integer>();

	public static int getFluidColor(Fluid fluid) {
		if (fluid == null) {
			return UNDEFINED_COLOR;
		}
		String fluidName = fluid.getName().toLowerCase();
		if (colorTable.containsKey(fluidName)) {
			return colorTable.get(fluidName);
		} else {
			return fluid.getColor();
		}
	}
	public static int getFluidColor(FluidStack fluidStack) {
		if (fluidStack == null) {
			return UNDEFINED_COLOR;
		}
		return FluidHelper.getFluidColor(fluidStack.getFluid());
	}
	
	public static void setColorList(String[] colorList) {
		if (colorList == null) {
			return;
		}
		for (String colorPair : colorList) {
			String[] parts = colorPair.split(":");
			if (parts.length == 2) {
				String fluidName = parts[0].trim().toLowerCase();
				String color = parts[1].trim();
				if (color.length() == 6) {
					try {
						parts = new String[] {
							color.substring(0, 2),
							color.substring(2, 4),
							color.substring(4, 6)
						};
						colorTable.put(fluidName, 
							(Integer.parseInt(parts[0], 16) << 16) +
							(Integer.parseInt(parts[1], 16) << 8) +
							(Integer.parseInt(parts[2], 16))
						);
					} catch (NumberFormatException e) {
						DELogger.warning("Unable to parse liquid:color pair \"" + colorPair + "\". Skipping.");
					}
				}
			}
		}
	}
	
	/**
	 * Searches through known fluids and returns the metadata of the fluid when placed
	 * in block form or -1 if not found.
	 * @return
	 */
	public static int getFluidBlockMetadata(Fluid fluid) {
		if (fluid != null) {
			for (FluidReference reference : FluidReference.values()) {
				Fluid referenceFluid = reference.getFluid();
				if (referenceFluid != null
				&& fluid.getID() == referenceFluid.getID()) {
					return reference.blockMeta;
				}
			}
		}
		return -1;
	}
	
	public static int getFluidBlockID(Fluid fluid) {
		if (fluid.getID() == FluidReference.WATER.getFluid().getID()) {
			return fluid.getBlockID() - 1;
		}
		return fluid.getBlockID();
	}
}
