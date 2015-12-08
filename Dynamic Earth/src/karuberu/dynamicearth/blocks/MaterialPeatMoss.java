package karuberu.dynamicearth.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialPeatMoss extends Material {
	public static final MaterialPeatMoss
		material = new MaterialPeatMoss();

	public MaterialPeatMoss() {
		super(MapColor.foliageColor);
		this.setBurning();
		this.setReplaceable();
		this.setNoPushMobility();
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}
	
	@Override
	public boolean blocksMovement() {
		return false;
	}
}
