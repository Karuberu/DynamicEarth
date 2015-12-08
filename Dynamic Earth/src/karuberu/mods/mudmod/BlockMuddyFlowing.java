package karuberu.mods.mudmod;

import net.minecraft.src.BlockFlowing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;

public class BlockMuddyFlowing extends BlockFlowing {

	protected BlockMuddyFlowing(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
	    return 0xbd9e82;
	}
}
