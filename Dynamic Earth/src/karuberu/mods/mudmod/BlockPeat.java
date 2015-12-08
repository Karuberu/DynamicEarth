package karuberu.mods.mudmod;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;

public class BlockPeat extends BlockGrass_mod {

	public BlockPeat(int par1) {
		super(par1);
	}

    @Override
    public String getTextureFile() {
    	return MudMod.terrainFile;
    }
    
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    @Override
    public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
        return MudMod.BlockTexture.PEAT.ordinal()+MudMod.IDOFFSET;
    }
    
    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    @Override
    public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return MudMod.BlockTexture.PEAT.ordinal()+MudMod.IDOFFSET;
    }
}
