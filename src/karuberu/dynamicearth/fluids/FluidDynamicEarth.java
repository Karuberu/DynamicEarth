package karuberu.dynamicearth.fluids;

import net.minecraftforge.fluids.Fluid;

public class FluidDynamicEarth extends Fluid {

	private int color = -1;
	
	public FluidDynamicEarth(String fluidName) {
		super(fluidName);
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	@Override
	public int getColor() {
		return color >= 0 ? color : super.getColor();
	}
}
