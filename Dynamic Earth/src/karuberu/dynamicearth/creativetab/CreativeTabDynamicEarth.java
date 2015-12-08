package karuberu.dynamicearth.creativetab;

import karuberu.dynamicearth.DynamicEarth;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

public class CreativeTabDynamicEarth extends CreativeTabs {
	public static final CreativeTabs
		tabDynamicEarth = new CreativeTabDynamicEarth("Dynamic Earth");

	public CreativeTabDynamicEarth(String label) {
		super(label);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getTabIconItemIndex() {
		return DynamicEarth.includeMud ? DynamicEarth.mud.blockID : Block.dirt.blockID;
	}

	@Override
	public String getTranslatedTabLabel() {
		return this.getTabLabel();
	}
}
