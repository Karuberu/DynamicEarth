package karuberu.mods.mudmod.client;

import org.lwjgl.opengl.GL11;

import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderBlockWithOverlay implements ISimpleBlockRenderingHandler {

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		if (block instanceof ITextureOverlay == false) {
			return;
		}
		ITextureOverlay textureOverlay = (ITextureOverlay)block;
		float color = 1.0F;
		Tessellator tessellator = Tessellator.instance;
        int renderColor = block.getRenderColor(metadata);
        int renderType;
        float r = (float)(renderColor >> 16 & 255) / 255.0F;
        float g = (float)(renderColor >> 8 & 255) / 255.0F;
        float b = (float)(renderColor & 255) / 255.0F;
        float normalX = 0.0F;
        float normalY = 0.0F;
        float normalZ = 0.0F;
        int blockTexture;
           
        renderer.setRenderBoundsFromBlock(block);
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        for (int side = 0; side < 6; side++) {
        	blockTexture = block.getBlockTextureFromSideAndMetadata(side, metadata);
        	switch (side) {
        	case Reference.SIDE_BOTTOM:
        		normalX = 0.0F;
        		normalY = -1.0F;
        		normalZ = 0.0F;
        		break;
        	case Reference.SIDE_TOP:
        		normalX = 0.0F;
        		normalY = 1.0F;
        		normalZ = 0.0F;
        		break;
        	case Reference.SIDE_EAST:
        		normalX = 0.0F;
        		normalY = 0.0F;
        		normalZ = -1.0F;
        		break;
        	case Reference.SIDE_WEST:
        		normalX = 0.0F;
        		normalY = 0.0F;
        		normalZ = 1.0F;
        		break;
        	case Reference.SIDE_NORTH:
        		normalX = -1.0F;
        		normalY = 0.0F;
        		normalZ = 0.0F;
        		break;
        	case Reference.SIDE_SOUTH:
        		normalX = 1.0F;
        		normalY = 0.0F;
        		normalZ = 0.0F;
        		break;
        	}
            if (renderer.useInventoryTint && textureOverlay.willColorizeInventoryBaseTexture(side, metadata)) {
                GL11.glColor4f(r * color, g * color, b * color, 1.0F);
            } else {
                GL11.glColor4f(color, color, color, 1.0F);
            }
            tessellator.startDrawingQuads();
            tessellator.setNormal(normalX, normalY, normalZ);
            switch (side) {
        	case Reference.SIDE_BOTTOM:
        		renderer.renderBottomFace(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case Reference.SIDE_TOP:
        		renderer.renderTopFace(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case Reference.SIDE_EAST:
        		renderer.renderEastFace(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case Reference.SIDE_WEST:
        		renderer.renderWestFace(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case Reference.SIDE_NORTH:
        		renderer.renderNorthFace(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case Reference.SIDE_SOUTH:
        		renderer.renderSouthFace(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
            }
            tessellator.draw();
        }
        
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (block instanceof ITextureOverlay == false) {
			return renderer.renderStandardBlock(block, x, y, z);
		}
        int colorMultiplier = block.colorMultiplier(blockAccess, x, y, z);
        float r = (float)(colorMultiplier >> 16 & 255) / 255.0F;
        float g = (float)(colorMultiplier >> 8 & 255) / 255.0F;
        float b = (float)(colorMultiplier & 255) / 255.0F;
        
        if (EntityRenderer.anaglyphEnable) {
            float anaglyphR = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float anaglyphG = (r * 30.0F + g * 70.0F) / 100.0F;
            float anaglyphB = (r * 30.0F + b * 70.0F) / 100.0F;
            r = anaglyphR;
            g = anaglyphG;
            b = anaglyphB;
        }
        if (Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[block.blockID] == 0) {
        	return this.renderBlockWithAmbientOcclusion(blockAccess, x, y, z, block, modelId, renderer, r, g, b);
        } else {
        	return this.renderBlockWithColorMultiplier(blockAccess, x, y, z, block, modelId, renderer, r, g, b);
        }
    }
	
	private boolean renderBlockWithColorMultiplier(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer, float r, float g, float b) {
		assert block instanceof ITextureOverlay;
		
		ITextureOverlay textureOverlay = (ITextureOverlay)block;
		renderer.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean blockRendered = false;
        int blockMetadata = blockAccess.getBlockMetadata(x, y, z);
        int mixedBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        int checkX = x;
        int checkY = y;
        int checkZ = z;
        boolean brightnessCheck = true;
        float sideColor = 0.0F;
        int blockTexture;
        int overlayTexture;
        
        for(int side = 0; side < 6; side++) {
    		blockTexture = block.getBlockTexture(blockAccess, x, y, z, side);
    		overlayTexture = textureOverlay.getOverlayTexture(blockAccess, x, y, z, side);
        	switch(side) {
        	case Reference.SIDE_BOTTOM:
        		checkX = x;
        		checkY = y - 1;
        		checkZ = z;
        		brightnessCheck = renderer.renderMinY > 0.0D;
        		sideColor = 0.5F;
        		break;
        	case Reference.SIDE_TOP:
        		checkX = x;
        		checkY = y + 1;
        		checkZ = z;
        		brightnessCheck = renderer.renderMaxY < 1.0D;
        		sideColor = 1.0F;
        		break;
        	case Reference.SIDE_EAST:
        		checkX = x;
        		checkY = y;
        		checkZ = z - 1;
        		brightnessCheck = renderer.renderMinZ > 0.0D;
        		sideColor = 0.8F;
        		break;
        	case Reference.SIDE_WEST:
        		checkX = x;
        		checkY = y;
        		checkZ = z + 1;
        		brightnessCheck = renderer.renderMaxZ < 1.0D;
        		sideColor = 0.8F;
        		break;
        	case Reference.SIDE_NORTH:
        		checkX = x - 1;
        		checkY = y;
        		checkZ = z;
        		brightnessCheck = renderer.renderMinX > 0.0D;
        		sideColor = 0.6F;
        		break;
        	case Reference.SIDE_SOUTH:
        		checkX = x + 1;
        		checkY = y;
        		checkZ = z;
        		brightnessCheck = renderer.renderMaxX < 1.0D;
        		sideColor = 0.6F;
        		break;
        	}
            if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, checkX, checkY, checkZ, side)) {
            	if (brightnessCheck) {
            		tessellator.setBrightness(mixedBrightness);
            	} else {
            		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, checkX, checkY, checkZ));
            	}
            	if (textureOverlay.willColorizeBaseTexture(blockAccess, x, y, z, side)) {
            		tessellator.setColorOpaque_F(r * sideColor, g * sideColor, b * sideColor);
            	} else {
            		tessellator.setColorOpaque_F(sideColor, sideColor, sideColor);
            	}
            	switch(side) {
            	case Reference.SIDE_BOTTOM:
            		renderer.renderBottomFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	case Reference.SIDE_TOP:
            		renderer.renderTopFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	case Reference.SIDE_EAST:
            		renderer.renderEastFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	case Reference.SIDE_WEST:
            		renderer.renderWestFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	case Reference.SIDE_NORTH:
            		renderer.renderNorthFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	case Reference.SIDE_SOUTH:
            		renderer.renderSouthFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	}
                if (textureOverlay.doTextureOverlay(blockMetadata) && overlayTexture > -1) {
                	switch(side) {
                	case Reference.SIDE_BOTTOM:
                		renderer.renderBottomFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	case Reference.SIDE_TOP:
                		renderer.renderTopFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	case Reference.SIDE_EAST:
                		renderer.renderEastFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	case Reference.SIDE_WEST:
                		renderer.renderWestFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	case Reference.SIDE_NORTH:
                		renderer.renderNorthFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	case Reference.SIDE_SOUTH:
                		renderer.renderSouthFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	}
    				tessellator.setColorOpaque_F(r * sideColor, g * sideColor, b * sideColor);
    	        }
                blockRendered = true;
            }
        }
        return blockRendered;
	}
	
	private boolean renderBlockWithAmbientOcclusion(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer, float r, float g, float b) {
		assert block instanceof ITextureOverlay;
		
		ITextureOverlay textureOverlay = (ITextureOverlay)block;
        int blockMetadata = blockAccess.getBlockMetadata(x, y, z);
        boolean doTextureOverlay;
        int blockTexture;
        int overlayTexture;
        int side;

		renderer.enableAO = true;
        boolean blockRendered = false;
        float lightValueTopLeft = renderer.lightValueOwn;
        float lightValueBottomLeft = renderer.lightValueOwn;
        float lightValueBottomRight = renderer.lightValueOwn;
        float lightValueTopRight = renderer.lightValueOwn;
        renderer.lightValueOwn = block.getAmbientOcclusionLightValue(blockAccess, x, y, z);
        renderer.aoLightValueXNeg = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z);
        renderer.aoLightValueYNeg = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z);
        renderer.aoLightValueZNeg = block.getAmbientOcclusionLightValue(blockAccess, x, y, z - 1);
        renderer.aoLightValueXPos = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z);
        renderer.aoLightValueYPos = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z);
        renderer.aoLightValueZPos = block.getAmbientOcclusionLightValue(blockAccess, x, y, z + 1);
        int mixedBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        int mixedBrightnessMinX = mixedBrightness;
        int mixedBrightnessMinY = mixedBrightness;
        int mixedBrightnessMinZ = mixedBrightness;
        int mixedBrightnessMaxX = mixedBrightness;
        int mixedBrightnessMaxY = mixedBrightness;
        int mixedBrightnessMaxZ = mixedBrightness;

        if (renderer.renderMinY <= 0.0D) {
            mixedBrightnessMinY = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
        }
        if (renderer.renderMaxY >= 1.0D) {
            mixedBrightnessMaxY = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
        }
        if (renderer.renderMinX <= 0.0D) {
            mixedBrightnessMinX = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
        }
        if (renderer.renderMaxX >= 1.0D) {
            mixedBrightnessMaxX = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
        }
        if (renderer.renderMinZ <= 0.0D) {
            mixedBrightnessMinZ = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
        }
        if (renderer.renderMaxZ >= 1.0D) {
            mixedBrightnessMaxZ = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
        }

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(0x0F000F);
        int id;
        renderer.aoGrassXYZPPC = Block.canBlockGrass[id = blockAccess.getBlockId(x + 1, y + 1, z)] || Block.blocksList[id] instanceof ITextureOverlay;
        renderer.aoGrassXYZPNC = Block.canBlockGrass[id = blockAccess.getBlockId(x + 1, y - 1, z)] || Block.blocksList[id] instanceof ITextureOverlay;
        renderer.aoGrassXYZPCP = Block.canBlockGrass[id = blockAccess.getBlockId(x + 1, y, z + 1)] || Block.blocksList[id] instanceof ITextureOverlay;
        renderer.aoGrassXYZPCN = Block.canBlockGrass[id = blockAccess.getBlockId(x + 1, y, z - 1)] || Block.blocksList[id] instanceof ITextureOverlay;
        renderer.aoGrassXYZNPC = Block.canBlockGrass[id = blockAccess.getBlockId(x - 1, y + 1, z)] || Block.blocksList[id] instanceof ITextureOverlay;
        renderer.aoGrassXYZNNC = Block.canBlockGrass[id = blockAccess.getBlockId(x - 1, y - 1, z)] || Block.blocksList[id] instanceof ITextureOverlay;
        renderer.aoGrassXYZNCN = Block.canBlockGrass[id = blockAccess.getBlockId(x - 1, y, z - 1)] || Block.blocksList[id] instanceof ITextureOverlay;
        renderer.aoGrassXYZNCP = Block.canBlockGrass[id = blockAccess.getBlockId(x - 1, y, z + 1)] || Block.blocksList[id] instanceof ITextureOverlay;
        renderer.aoGrassXYZCPP = Block.canBlockGrass[id = blockAccess.getBlockId(x, y + 1, z + 1)] || Block.blocksList[id] instanceof ITextureOverlay;
        renderer.aoGrassXYZCPN = Block.canBlockGrass[id = blockAccess.getBlockId(x, y + 1, z - 1)] || Block.blocksList[id] instanceof ITextureOverlay;
        renderer.aoGrassXYZCNP = Block.canBlockGrass[id = blockAccess.getBlockId(x, y - 1, z + 1)] || Block.blocksList[id] instanceof ITextureOverlay;
        renderer.aoGrassXYZCNN = Block.canBlockGrass[id = blockAccess.getBlockId(x, y - 1, z - 1)] || Block.blocksList[id] instanceof ITextureOverlay;

        int checkX = x;
        int checkY = y;
        int checkZ = z;
        float sideColor = 0.0F;
    	float lightValue = 0.0F;
    	int brightness = 0;
    	boolean willColorizeBaseTexture = false;
        for (side = 0; side < 6; side++) {
        	switch(side) {
        	case Reference.SIDE_BOTTOM:
        		checkX = x; checkY = y - 1; checkZ = z;
        		sideColor = 0.5F;
        		break;
        	case Reference.SIDE_TOP:
        		checkX = x; checkY = y + 1; checkZ = z;
        		sideColor = 1.0F;
        		break;
        	case Reference.SIDE_EAST:
        		checkX = x; checkY = y; checkZ = z - 1;
        		sideColor = 0.8F;
        		break;
        	case Reference.SIDE_WEST:
        		checkX = x; checkY = y; checkZ = z + 1;
        		sideColor = 0.8F;
        		break;
        	case Reference.SIDE_NORTH:
        		checkX = x - 1; checkY = y; checkZ = z;
        		sideColor = 0.6F;
        		break;
        	case Reference.SIDE_SOUTH:
        		checkX = x + 1; checkY = y; checkZ = z;
        		sideColor = 0.6F;
        		break;
        	}
            if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, checkX, checkY, checkZ, side)) {
                if (renderer.aoType > 0) {
                	switch (side) {
                	case Reference.SIDE_BOTTOM:
                        if (renderer.renderMinY <= 0.0D) { --y; }
                        break;
                	case Reference.SIDE_TOP:
                        if (renderer.renderMaxY >= 1.0D) { ++y; }
                        break;
                	case Reference.SIDE_EAST:
                        if (renderer.renderMinZ <= 0.0D) { --z; }
                        break;
                	case Reference.SIDE_WEST:
                        if (renderer.renderMaxZ >= 1.0D) { ++z; }
                        break;
                	case Reference.SIDE_NORTH:
                        if (renderer.renderMinX <= 0.0D) { --x; }
                        break;
                	case Reference.SIDE_SOUTH:
                        if (renderer.renderMaxX >= 1.0D) { ++x; }
                        break;
                	}
                	switch (side) {
                	case Reference.SIDE_BOTTOM:
                        renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
                        renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
                        renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
                        renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
                        renderer.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z);
                        renderer.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(blockAccess, x, y, z - 1);
                        renderer.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(blockAccess, x, y, z + 1);
                        renderer.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z);
                        break;
                	case Reference.SIDE_TOP:
                        renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
                        renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
                        renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
                        renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
                        renderer.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z);
                        renderer.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z);
                        renderer.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(blockAccess, x, y, z - 1);
                        renderer.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(blockAccess, x, y, z + 1);
                        break;
    				case Reference.SIDE_EAST:
    					renderer.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z);
    					renderer.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z);
    					renderer.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z);
    					renderer.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z);
    					renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
    					renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
    					renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
    					renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
    					break;
    				case Reference.SIDE_WEST:
    					renderer.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z);
    					renderer.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z);
    					renderer.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z);
    					renderer.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z);
    					renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
    					renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
    					renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
    					renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
    					break;
    				case Reference.SIDE_NORTH:
    					renderer.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z);
    					renderer.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(blockAccess, x, y, z - 1);
    					renderer.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(blockAccess, x, y, z + 1);
    					renderer.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z);
    					renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
    					renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
    					renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
    					renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
    					break;
    				case Reference.SIDE_SOUTH:
    					renderer.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z);
    					renderer.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(blockAccess, x, y, z - 1);
    					renderer.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(blockAccess, x, y, z + 1);
    					renderer.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z);
    					renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
    					renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
    					renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
    					renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
    					break;
    				}
                	switch (side) {
                	case Reference.SIDE_BOTTOM:
                        if (!renderer.aoGrassXYZCNN && !renderer.aoGrassXYZNNC) {
                            renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
                            renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
                        } else {
                            renderer.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z - 1);
                            renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
                        }
                        if (!renderer.aoGrassXYZCNP && !renderer.aoGrassXYZNNC) {
                            renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
                            renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
                        } else {
                            renderer.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z + 1);
                            renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
                        }
                        if (!renderer.aoGrassXYZCNN && !renderer.aoGrassXYZPNC) {
                            renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
                            renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
                        } else {
                            renderer.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z - 1);
                            renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
                        }
                        if (!renderer.aoGrassXYZCNP && !renderer.aoGrassXYZPNC) {
                            renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
                            renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
                        } else {
                            renderer.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z + 1);
                            renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
                        }
                        break;
                	case Reference.SIDE_TOP:
                        if (!renderer.aoGrassXYZCPN && !renderer.aoGrassXYZNPC) {
                            renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
                            renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
                        } else {
                            renderer.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z - 1);
                            renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
                        }
                        if (!renderer.aoGrassXYZCPN && !renderer.aoGrassXYZPPC) {
                            renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
                            renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
                        } else {
                            renderer.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z - 1);
                            renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
                        }
                        if (!renderer.aoGrassXYZCPP && !renderer.aoGrassXYZNPC) {
                            renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
                            renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
                        } else {
                            renderer.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z + 1);
                            renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
                        }
                        if (!renderer.aoGrassXYZCPP && !renderer.aoGrassXYZPPC) {
                            renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
                            renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
                        } else {
                            renderer.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z + 1);
                            renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
                        }
                        break;
                	case Reference.SIDE_EAST:
    					if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZCNN) {
    						renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
    						renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
    					} else {
    						renderer.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y - 1, z);
    						renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
    					}
    					if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZCPN) {
    						renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
    						renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
    					} else {
    						renderer.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y + 1, z);
    						renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
    					}
    					if (!renderer.aoGrassXYZPCN && !renderer.aoGrassXYZCNN) {
    						renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
    						renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
    					} else {
    						renderer.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y - 1, z);
    						renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
    					}
    					if (!renderer.aoGrassXYZPCN && !renderer.aoGrassXYZCPN) {
    						renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
    						renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
    					} else {
    						renderer.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y + 1, z);
    						renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
    					}
    					break;
                	case Reference.SIDE_WEST:
    					if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZCNP) {
    						renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
    						renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
    					} else {
    						renderer.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y - 1, z);
    						renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
    					}
    					if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZCPP) {
    						renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
    						renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
    					} else {
    						renderer.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y + 1, z);
    						renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
    					}
    					if (!renderer.aoGrassXYZPCP && !renderer.aoGrassXYZCNP) {
    						renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
    						renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
    					} else {
    						renderer.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y - 1, z);
    						renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
    					}
    					if (!renderer.aoGrassXYZPCP && !renderer.aoGrassXYZCPP) {
    						renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
    						renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
    					} else {
    						renderer.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y + 1, z);
    						renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
    					}
                		break;
                	case Reference.SIDE_NORTH:
    					if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZNNC) {
    						renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
    						renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
    					} else {
    						renderer.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z - 1);
    						renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
    					}
    					if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZNNC) {
    						renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
    						renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
    					} else {
    						renderer.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z + 1);
    						renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
    					}
    					if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZNPC) {
    						renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
    						renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
    					} else {
    						renderer.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z - 1);
    						renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
    					}
    					if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZNPC) {
    						renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
    						renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
    					} else {
    						renderer.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z + 1);
    						renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
    					}
                		break;
                	case Reference.SIDE_SOUTH:
    					if (!renderer.aoGrassXYZPNC && !renderer.aoGrassXYZPCN) {
    						renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
    						renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
    					} else {
    						renderer.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z - 1);
    						renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
    					}
    					if (!renderer.aoGrassXYZPNC && !renderer.aoGrassXYZPCP) {
    						renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
    						renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
    					} else {
    						renderer.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z + 1);
    						renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
    					}
    					if (!renderer.aoGrassXYZPPC && !renderer.aoGrassXYZPCN) {
    						renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
    						renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
    					} else {
    						renderer.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z - 1);
    						renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
    					}
    					if (!renderer.aoGrassXYZPPC && !renderer.aoGrassXYZPCP) {
    						renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
    						renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
    					} else {
    						renderer.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z + 1);
    						renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
    					}
    					break;
                	}
                	switch (side) {
                	case Reference.SIDE_BOTTOM:
                        if (renderer.renderMinY <= 0.0D) { ++y; }
                        break;
                	case Reference.SIDE_TOP:
                        if (renderer.renderMaxY >= 1.0D) { --y; }
                        break;
                	case Reference.SIDE_EAST:
                        if (renderer.renderMinZ <= 0.0D) { ++z; }
                        break;
                	case Reference.SIDE_WEST:
                        if (renderer.renderMaxZ >= 1.0D) { --z; }
                        break;
                	case Reference.SIDE_NORTH:
                        if (renderer.renderMinX <= 0.0D) { ++x; }
                        break;
                	case Reference.SIDE_SOUTH:
                        if (renderer.renderMaxX >= 1.0D) { --x; }
                        break;
                	}
                	switch (side) {
                	case Reference.SIDE_BOTTOM:
                        lightValueTopLeft = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + renderer.aoLightValueYNeg) / 4.0F;
                        lightValueTopRight = (renderer.aoLightValueScratchYZNP + renderer.aoLightValueYNeg + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
                        lightValueBottomRight = (renderer.aoLightValueYNeg + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
                        lightValueBottomLeft = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueYNeg + renderer.aoLightValueScratchYZNN) / 4.0F;
                        renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, mixedBrightnessMinY);
                        renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, mixedBrightnessMinY);
                        renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, mixedBrightnessMinY);
                        renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, mixedBrightnessMinY);
                        break;
                	case Reference.SIDE_TOP:
                        lightValueTopRight = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + renderer.aoLightValueYPos) / 4.0F;
                        lightValueTopLeft = (renderer.aoLightValueScratchYZPP + renderer.aoLightValueYPos + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
                        lightValueBottomLeft = (renderer.aoLightValueYPos + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
                        lightValueBottomRight = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueYPos + renderer.aoLightValueScratchYZPN) / 4.0F;
                        renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, mixedBrightnessMaxY);
                        renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, mixedBrightnessMaxY);
                        renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, mixedBrightnessMaxY);
                        renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, mixedBrightnessMaxY);
                        break;
    				case Reference.SIDE_EAST:
    					lightValueTopLeft = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueZNeg + renderer.aoLightValueScratchYZPN) / 4.0F;
    					lightValueBottomLeft = (renderer.aoLightValueZNeg + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
    					lightValueBottomRight = (renderer.aoLightValueScratchYZNN + renderer.aoLightValueZNeg + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
    					lightValueTopRight = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + renderer.aoLightValueZNeg) / 4.0F;
    					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, mixedBrightnessMinZ);
    					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, mixedBrightnessMinZ);
    					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, mixedBrightnessMinZ);
    					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, mixedBrightnessMinZ);
    					break;
    				case Reference.SIDE_WEST:
    					lightValueTopLeft = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueZPos + renderer.aoLightValueScratchYZPP) / 4.0F;
    					lightValueTopRight = (renderer.aoLightValueZPos + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
    					lightValueBottomRight = (renderer.aoLightValueScratchYZNP + renderer.aoLightValueZPos + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
    					lightValueBottomLeft = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + renderer.aoLightValueZPos) / 4.0F;
    					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, mixedBrightnessMaxZ);
    					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, mixedBrightnessMaxZ);
    					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, mixedBrightnessMaxZ);
    					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, mixedBrightnessMaxZ);
    					break;
    				case Reference.SIDE_NORTH:
    					lightValueTopRight = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueXNeg + renderer.aoLightValueScratchXZNP) / 4.0F;
    					lightValueTopLeft = (renderer.aoLightValueXNeg + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
    					lightValueBottomLeft = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueXNeg + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
    					lightValueBottomRight = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueXNeg) / 4.0F;
    					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, mixedBrightnessMinX);
    					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, mixedBrightnessMinX);
    					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, mixedBrightnessMinX);
    					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, mixedBrightnessMinX);
    					break;
    				case Reference.SIDE_SOUTH:
    					lightValueTopLeft = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueXPos + renderer.aoLightValueScratchXZPP) / 4.0F;
    					lightValueTopRight = (renderer.aoLightValueXPos + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
    					lightValueBottomRight = (renderer.aoLightValueScratchXZPN + renderer.aoLightValueXPos + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
    					lightValueBottomLeft = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueXPos) / 4.0F;
    					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, mixedBrightnessMaxX);
    					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, mixedBrightnessMaxX);
    					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, mixedBrightnessMaxX);
    					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, mixedBrightnessMaxX);
    					break;
    				}
                } else {
                	switch (side) {
                	case Reference.SIDE_BOTTOM:
                		lightValue = renderer.aoLightValueYNeg;
                		brightness = renderer.aoBrightnessXYNN;
                		break;
                	case Reference.SIDE_TOP:
                		lightValue = renderer.aoLightValueYPos;
                		brightness = mixedBrightnessMaxY;
                		break;
                	case Reference.SIDE_EAST:
                		lightValue = renderer.aoLightValueZNeg;
                		brightness = mixedBrightnessMinZ;
                		break;
                	case Reference.SIDE_WEST:
                		lightValue = renderer.aoLightValueZPos;
                		brightness = mixedBrightnessMaxZ;
                		break;
                	case Reference.SIDE_NORTH:
                		lightValue = renderer.aoLightValueXNeg;
                		brightness = mixedBrightnessMinX;
                		break;
                	case Reference.SIDE_SOUTH:
                		lightValue = renderer.aoLightValueXPos;
                		brightness = mixedBrightnessMaxX;
                		break;
                	}
                    lightValueTopRight = lightValue;
                    lightValueBottomRight = lightValue;
                    lightValueBottomLeft = lightValue;
                    lightValueTopLeft = lightValue;
                    renderer.brightnessTopLeft = brightness;
                    renderer.brightnessBottomLeft = brightness;
                    renderer.brightnessBottomRight = brightness;
                    renderer.brightnessTopRight = brightness;
                }

                willColorizeBaseTexture = textureOverlay.willColorizeBaseTexture(blockAccess, x, y, z, side);
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = (willColorizeBaseTexture ? r : 1.0F) * sideColor;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = (willColorizeBaseTexture ? g : 1.0F) * sideColor;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = (willColorizeBaseTexture ? b : 1.0F) * sideColor;
                renderer.colorRedTopLeft *= lightValueTopLeft;
                renderer.colorGreenTopLeft *= lightValueTopLeft;
                renderer.colorBlueTopLeft *= lightValueTopLeft;
                renderer.colorRedBottomLeft *= lightValueBottomLeft;
                renderer.colorGreenBottomLeft *= lightValueBottomLeft;
                renderer.colorBlueBottomLeft *= lightValueBottomLeft;
                renderer.colorRedBottomRight *= lightValueBottomRight;
                renderer.colorGreenBottomRight *= lightValueBottomRight;
                renderer.colorBlueBottomRight *= lightValueBottomRight;
                renderer.colorRedTopRight *= lightValueTopRight;
                renderer.colorGreenTopRight *= lightValueTopRight;
                renderer.colorBlueTopRight *= lightValueTopRight;
                String textureFile = block.getTextureFile();
                if (block instanceof ICamouflageBlock) {
                	Block donorBlock = Block.blocksList[((ICamouflageBlock)block).getDonorBlockID(blockAccess, x, y, z)];
                	ForgeHooksClient.bindTexture(donorBlock.getTextureFile(), 0);
                	blockTexture = donorBlock.getBlockTexture(blockAccess, x, y, z, side);
                } else {
                	blockTexture = block.getBlockTexture(blockAccess, x, y, z, side);
                }
            	switch(side) {
            	case Reference.SIDE_BOTTOM:
            		renderer.renderBottomFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	case Reference.SIDE_TOP:
            		renderer.renderTopFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	case Reference.SIDE_EAST:
            		renderer.renderEastFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	case Reference.SIDE_WEST:
            		renderer.renderWestFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	case Reference.SIDE_NORTH:
            		renderer.renderNorthFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	case Reference.SIDE_SOUTH:
            		renderer.renderSouthFace(block, (double)x, (double)y, (double)z, blockTexture); break;
            	}
            	overlayTexture = textureOverlay.getOverlayTexture(blockAccess, x, y, z, side);
                if (textureOverlay.doTextureOverlay(blockMetadata) && overlayTexture > -1) {
                    if (block instanceof ICamouflageBlock) {
                    	System.out.println(textureFile);
                     	ForgeHooksClient.bindTexture(textureFile, 0);
                    }
                    renderer.colorRedTopLeft *= r;
                    renderer.colorRedBottomLeft *= r;
                    renderer.colorRedBottomRight *= r;
                    renderer.colorRedTopRight *= r;
                    renderer.colorGreenTopLeft *= g;
                    renderer.colorGreenBottomLeft *= g;
                    renderer.colorGreenBottomRight *= g;
                    renderer.colorGreenTopRight *= g;
                    renderer.colorBlueTopLeft *= b;
                    renderer.colorBlueBottomLeft *= b;
                    renderer.colorBlueBottomRight *= b;
                    renderer.colorBlueTopRight *= b;
                	switch(side) {
                	case Reference.SIDE_BOTTOM:
                		renderer.renderBottomFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	case Reference.SIDE_TOP:
                		renderer.renderTopFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	case Reference.SIDE_EAST:
                		renderer.renderEastFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	case Reference.SIDE_WEST:
                		renderer.renderWestFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	case Reference.SIDE_NORTH:
                		renderer.renderNorthFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	case Reference.SIDE_SOUTH:
                		renderer.renderSouthFace(block, (double)x, (double)y, (double)z, overlayTexture); break;
                	}
                }
                blockRendered = true;
            }
        }
       
        renderer.enableAO = false;
        return blockRendered;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderId() {
		return MudMod.overlayBlockRenderID;
	}

}
