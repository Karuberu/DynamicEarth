package karuberu.mods.mudmod.client;

import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract interface ITextureOverlay {

	@SideOnly(Side.CLIENT)
	public int getOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int side);
	
	@SideOnly(Side.CLIENT)
	public boolean doTextureOverlay(int metadata);
	
	@SideOnly(Side.CLIENT)
	public boolean willColorizeBaseTexture(IBlockAccess blockAccess, int x, int y, int z, int side);

	@SideOnly(Side.CLIENT)
	public boolean willColorizeInventoryBaseTexture(int side, int metadata);
}
