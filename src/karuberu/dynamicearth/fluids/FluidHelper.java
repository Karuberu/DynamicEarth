package karuberu.dynamicearth.fluids;

import java.util.Hashtable;
import karuberu.core.util.FluidHelper.FluidReference;
import karuberu.core.util.client.ColorHelper;
import karuberu.dynamicearth.DynamicEarth;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Helper class whose primary function is to facilitate cleaner and more streamlined
 * code when dealing with fluids.
 * @author Karuberu
 */
public class FluidHelper {
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
						colorTable.put(fluidName, ColorHelper.getColor(color));
					} catch (NumberFormatException e) {
						DynamicEarth.logger.warning("Unable to parse liquid:color pair \"" + colorPair + "\". Skipping.");
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
