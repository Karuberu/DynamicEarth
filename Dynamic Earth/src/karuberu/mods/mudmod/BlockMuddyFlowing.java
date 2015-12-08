package karuberu.mods.mudmod;

import net.minecraft.src.BlockFlowing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;

public class BlockMuddyFlowing extends BlockFlowing {

	protected BlockMuddyFlowing(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setHardness(100.0F);
		this.setLightOpacity(3);
		this.setBlockName("muddyWaterFlowing");
	}
	
//    @Override
//    public String getTextureFile() {
//        return MudMod.terrainFile;
//    }
}
