package karuberu.mods.mudmod.client;

import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract interface ICamouflageBlock {

	@SideOnly(Side.CLIENT)
	public int getDonorBlockID(IBlockAccess blockAccess, int x, int y, int z);
}
