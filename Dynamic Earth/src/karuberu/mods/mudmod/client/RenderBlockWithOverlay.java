package karuberu.mods.mudmod.client;

import org.lwjgl.opengl.GL11;

import karuberu.core.MCHelper;
import karuberu.mods.mudmod.MudMod;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
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
        float r = (float)(renderColor >> 16 & 255) / 255.0F;
        float g = (float)(renderColor >> 8 & 255) / 255.0F;
        float b = (float)(renderColor & 255) / 255.0F;
        float normalX = 0.0F;
        float normalY = 0.0F;
        float normalZ = 0.0F;
        Icon blockTexture;
           
        renderer.setRenderBoundsFromBlock(block);
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        for (int side = 0; side < 6; side++) {
        	blockTexture = block.getIcon(side, metadata);
        	switch (side) {
        	case MCHelper.SIDE_BOTTOM:
        		normalX = 0.0F;
        		normalY = -1.0F;
        		normalZ = 0.0F;
        		break;
        	case MCHelper.SIDE_TOP:
        		normalX = 0.0F;
        		normalY = 1.0F;
        		normalZ = 0.0F;
        		break;
        	case MCHelper.SIDE_EAST:
        		normalX = 0.0F;
        		normalY = 0.0F;
        		normalZ = -1.0F;
        		break;
        	case MCHelper.SIDE_WEST:
        		normalX = 0.0F;
        		normalY = 0.0F;
        		normalZ = 1.0F;
        		break;
        	case MCHelper.SIDE_NORTH:
        		normalX = -1.0F;
        		normalY = 0.0F;
        		normalZ = 0.0F;
        		break;
        	case MCHelper.SIDE_SOUTH:
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
        	case MCHelper.SIDE_BOTTOM:
        		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case MCHelper.SIDE_TOP:
        		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case MCHelper.SIDE_EAST:
        		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case MCHelper.SIDE_WEST:
        		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case MCHelper.SIDE_NORTH:
        		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case MCHelper.SIDE_SOUTH:
        		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
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
        int mixedBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        int checkX = x;
        int checkY = y;
        int checkZ = z;
        boolean brightnessCheck = true;
        float sideColor = 0.0F;    
        
        Icon texture;
        for(int side = 0; side < 6; side++) {
        	switch(side) {
        	case MCHelper.SIDE_BOTTOM:
        		checkX = x;
        		checkY = y - 1;
        		checkZ = z;
        		brightnessCheck = renderer.renderMinY > 0.0D;
        		sideColor = 0.5F;
        		break;
        	case MCHelper.SIDE_TOP:
        		checkX = x;
        		checkY = y + 1;
        		checkZ = z;
        		brightnessCheck = renderer.renderMaxY < 1.0D;
        		sideColor = 1.0F;
        		break;
        	case MCHelper.SIDE_EAST:
        		checkX = x;
        		checkY = y;
        		checkZ = z - 1;
        		brightnessCheck = renderer.renderMinZ > 0.0D;
        		sideColor = 0.8F;
        		break;
        	case MCHelper.SIDE_WEST:
        		checkX = x;
        		checkY = y;
        		checkZ = z + 1;
        		brightnessCheck = renderer.renderMaxZ < 1.0D;
        		sideColor = 0.8F;
        		break;
        	case MCHelper.SIDE_NORTH:
        		checkX = x - 1;
        		checkY = y;
        		checkZ = z;
        		brightnessCheck = renderer.renderMinX > 0.0D;
        		sideColor = 0.6F;
        		break;
        	case MCHelper.SIDE_SOUTH:
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
        		texture = block.getBlockTexture(blockAccess, x, y, z, side);
        		int numberOfPasses = textureOverlay.getNumberOfPasses(blockAccess.getBlockMetadata(x, y, z));
        		for (int pass = 0; pass < numberOfPasses; pass++) {
        			int metadata = blockAccess.getBlockMetadata(x, y, z);
        			if (pass > 0) {
    		            texture = textureOverlay.getOverlayTexture(blockAccess, x, y, z, metadata, side, pass);        				
        			}
        			if (texture != null) {
	                	if (textureOverlay.willColorizeTexture(blockAccess, x, y, z, metadata, side, pass)) {
	        				tessellator.setColorOpaque_F(r * sideColor, g * sideColor, b * sideColor);	            		
	                	} else {
	        				tessellator.setColorOpaque_F(sideColor, sideColor, sideColor);	            		
	                	}
		            	switch(side) {
		            	case MCHelper.SIDE_BOTTOM:
		            		renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, texture); break;
		            	case MCHelper.SIDE_TOP:
		            		renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, texture); break;
		            	case MCHelper.SIDE_EAST:
		            		renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, texture); break;
		            	case MCHelper.SIDE_WEST:
		            		renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, texture); break;
		            	case MCHelper.SIDE_NORTH:
		            		renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, texture); break;
		            	case MCHelper.SIDE_SOUTH:
		            		renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, texture); break;
		            	}
		                blockRendered = true;
        			}
	            }
    		}
        }
        return blockRendered;
	}
	
	private boolean renderBlockWithAmbientOcclusion(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer, float r, float g, float b) {
		assert block instanceof ITextureOverlay;
		
		ITextureOverlay textureOverlay = (ITextureOverlay)block;
		Icon texture;
		int side;
		
		renderer.enableAO = true;
		boolean blockRendered = false;
		float lightValueTopLeft = 0.0F;
		float lightValueBottomLeft = 0.0F;
		float lightValueBottomRight = 0.0F;
		float lightValueTopRight = 0.0F;
		int mixedBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(0xF000F);

		boolean blockGrassSide1;
		boolean blockGrassSide2;
		boolean blockGrassSide3;
		boolean blockGrassSide4;
		float adjustedAOLightValue;
		int adjustedMixedBrightness;

		int checkX = x;
		int checkY = y;
		int checkZ = z;
		float sideColor = 0.0F;
		boolean willColorizeTexture = false;
		for (side = 0; side < 6; side++) {
			boolean renderingAtBounds = false;
			switch(side) {
			case MCHelper.SIDE_BOTTOM:
				checkX = x; checkY = y - 1; checkZ = z;
				sideColor = 0.5F;
				break;
			case MCHelper.SIDE_TOP:
				checkX = x; checkY = y + 1; checkZ = z;
				sideColor = 1.0F;
				break;
			case MCHelper.SIDE_EAST:
				checkX = x; checkY = y; checkZ = z - 1;
				sideColor = 0.8F;
				break;
			case MCHelper.SIDE_WEST:
				checkX = x; checkY = y; checkZ = z + 1;
				sideColor = 0.8F;
				break;
			case MCHelper.SIDE_NORTH:
				checkX = x - 1; checkY = y; checkZ = z;
				sideColor = 0.6F;
				break;
			case MCHelper.SIDE_SOUTH:
				checkX = x + 1; checkY = y; checkZ = z;
				sideColor = 0.6F;
				break;
			}
			if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, checkX, checkY, checkZ, side)) {
				switch (side) {
				case MCHelper.SIDE_BOTTOM:
					if (renderer.renderMinY <= 0.0D) {
						renderingAtBounds = true;
						--y;
					}
					break;
				case MCHelper.SIDE_TOP:
					if (renderer.renderMaxY >= 1.0D) {
						renderingAtBounds = true;
						++y;
					}
					break;
				case MCHelper.SIDE_EAST:
					if (renderer.renderMinZ <= 0.0D) {
						renderingAtBounds = true;
						--z;
					}
					break;
				case MCHelper.SIDE_WEST:
					if (renderer.renderMaxZ >= 1.0D) {
						renderingAtBounds = true;
						++z;
					}
					break;
				case MCHelper.SIDE_NORTH:
					if (renderer.renderMinX <= 0.0D) {
						renderingAtBounds = true;
						--x;
					}
					break;
				case MCHelper.SIDE_SOUTH:
					if (renderer.renderMaxX >= 1.0D) {
						renderingAtBounds = true;
						++x;
					}
					break;
				}
	
				switch (side) {
				case MCHelper.SIDE_BOTTOM:
					renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
					renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
					renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
					renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
					renderer.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z);
					renderer.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(blockAccess, x, y, z - 1);
					renderer.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(blockAccess, x, y, z + 1);
					renderer.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z);
					blockGrassSide2 = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y - 1, z)];
					blockGrassSide1 = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y - 1, z)];
					blockGrassSide4 = Block.canBlockGrass[blockAccess.getBlockId(x, y - 1, z + 1)];
					blockGrassSide3 = Block.canBlockGrass[blockAccess.getBlockId(x, y - 1, z - 1)];
					if (!blockGrassSide3 && !blockGrassSide1) {
						renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
						renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
					} else {
						renderer.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z - 1);
						renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
					}
					if (!blockGrassSide4 && !blockGrassSide1) {
						renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
						renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
					} else {
						renderer.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z + 1);
						renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
					}
					if (!blockGrassSide3 && !blockGrassSide2) {
						renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
						renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
					} else {
						renderer.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z - 1);
						renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
					}
					if (!blockGrassSide4 && !blockGrassSide2) {
						renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
						renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
					} else {
						renderer.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z + 1);
						renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
					}
					break;
				case MCHelper.SIDE_TOP:
					renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
					renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
					renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
					renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
					renderer.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z);
					renderer.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z);
					renderer.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(blockAccess, x, y, z - 1);
					renderer.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(blockAccess, x, y, z + 1);
					blockGrassSide2 = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y + 1, z)];
					blockGrassSide1 = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y + 1, z)];
					blockGrassSide4 = Block.canBlockGrass[blockAccess.getBlockId(x, y + 1, z + 1)];
					blockGrassSide3 = Block.canBlockGrass[blockAccess.getBlockId(x, y + 1, z - 1)];
					if (!blockGrassSide3 && !blockGrassSide1) {
						renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
						renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
					} else {
						renderer.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z - 1);
						renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
					}
					if (!blockGrassSide3 && !blockGrassSide2) {
						renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
						renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
					} else {
						renderer.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z - 1);
						renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
					}
					if (!blockGrassSide4 && !blockGrassSide1) {
						renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
						renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
					} else {
						renderer.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z + 1);
						renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
					}
					if (!blockGrassSide4 && !blockGrassSide2) {
						renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
						renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
					} else {
						renderer.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z + 1);
						renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
					}
					break;
				case MCHelper.SIDE_EAST:
					renderer.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z);
					renderer.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z);
					renderer.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z);
					renderer.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z);
					renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
					renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
					renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
					renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
					blockGrassSide2 = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y, z - 1)];
					blockGrassSide1 = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y, z - 1)];
					blockGrassSide4 = Block.canBlockGrass[blockAccess.getBlockId(x, y + 1, z - 1)];
					blockGrassSide3 = Block.canBlockGrass[blockAccess.getBlockId(x, y - 1, z - 1)];
					if (!blockGrassSide1 && !blockGrassSide3) {
						renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
						renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
					} else {
						renderer.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y - 1, z);
						renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
					}
					if (!blockGrassSide1 && !blockGrassSide4) {
						renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
						renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
					} else {
						renderer.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y + 1, z);
						renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
					}
					if (!blockGrassSide2 && !blockGrassSide3) {
						renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
						renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
					} else {
						renderer.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y - 1, z);
						renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
					}
					if (!blockGrassSide2 && !blockGrassSide4) {
						renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
						renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
					} else {
						renderer.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y + 1, z);
						renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
					}
					break;
				case MCHelper.SIDE_WEST:
					renderer.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y, z);
					renderer.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y, z);
					renderer.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z);
					renderer.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z);
					renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
					renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
					renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
					renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
					blockGrassSide2 = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y, z + 1)];
					blockGrassSide1 = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y, z + 1)];
					blockGrassSide4 = Block.canBlockGrass[blockAccess.getBlockId(x, y + 1, z + 1)];
					blockGrassSide3 = Block.canBlockGrass[blockAccess.getBlockId(x, y - 1, z + 1)];
					if (!blockGrassSide1 && !blockGrassSide3) {
						renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
						renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
					} else {
						renderer.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y - 1, z);
						renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
					}
					if (!blockGrassSide1 && !blockGrassSide4) {
						renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
						renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
					} else {
						renderer.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(blockAccess, x - 1, y + 1, z);
						renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
					}
					if (!blockGrassSide2 && !blockGrassSide3) {
						renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
						renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
					} else {
						renderer.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y - 1, z);
						renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
					}
					if (!blockGrassSide2 && !blockGrassSide4) {
						renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
						renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
					} else {
						renderer.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(blockAccess, x + 1, y + 1, z);
						renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
					}
					break;
				case MCHelper.SIDE_NORTH:
					renderer.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z);
					renderer.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(blockAccess, x, y, z - 1);
					renderer.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(blockAccess, x, y, z + 1);
					renderer.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z);
					renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
					renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
					renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
					renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
					blockGrassSide2 = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y + 1, z)];
					blockGrassSide1 = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y - 1, z)];
					blockGrassSide4 = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y, z - 1)];
					blockGrassSide3 = Block.canBlockGrass[blockAccess.getBlockId(x - 1, y, z + 1)];
					if (!blockGrassSide4 && !blockGrassSide1) {
						renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
						renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
					} else {
						renderer.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z - 1);
						renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
					}
					if (!blockGrassSide3 && !blockGrassSide1)
					{
						renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
						renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
					} else {
						renderer.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z + 1);
						renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
					}
					if (!blockGrassSide4 && !blockGrassSide2) {
						renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
						renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
					} else {
						renderer.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z - 1);
						renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
					}
					if (!blockGrassSide3 && !blockGrassSide2) {
						renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
						renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
					} else {
						renderer.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z + 1);
						renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
					}
					break;
				case MCHelper.SIDE_SOUTH:
					renderer.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z);
					renderer.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(blockAccess, x, y, z - 1);
					renderer.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(blockAccess, x, y, z + 1);
					renderer.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z);
					renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
					renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
					renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
					renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
					blockGrassSide2 = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y + 1, z)];
					blockGrassSide1 = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y - 1, z)];
					blockGrassSide4 = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y, z + 1)];
					blockGrassSide3 = Block.canBlockGrass[blockAccess.getBlockId(x + 1, y, z - 1)];
					if (!blockGrassSide1 && !blockGrassSide3) {
						renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
						renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
					} else {
						renderer.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z - 1);
						renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
					}
					if (!blockGrassSide1 && !blockGrassSide4) {
						renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
						renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
					} else {
						renderer.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(blockAccess, x, y - 1, z + 1);
						renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
					}
					if (!blockGrassSide2 && !blockGrassSide3) {
						renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
						renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
					} else {
						renderer.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z - 1);
						renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
					}
					if (!blockGrassSide2 && !blockGrassSide4) {
						renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
						renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
					} else {
						renderer.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(blockAccess, x, y + 1, z + 1);
						renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
					}
					break;
				}
	
				if (renderingAtBounds) {
					switch (side) {
					case MCHelper.SIDE_BOTTOM: ++y; break;
					case MCHelper.SIDE_TOP: --y; break;
					case MCHelper.SIDE_EAST: ++z; break;
					case MCHelper.SIDE_WEST: --z; break;
					case MCHelper.SIDE_NORTH: ++x; break;
					case MCHelper.SIDE_SOUTH: --x; break;
					}
				}
				adjustedMixedBrightness = mixedBrightness;
				if (renderingAtBounds || !blockAccess.isBlockOpaqueCube(checkX, checkY, checkZ)) {
					adjustedMixedBrightness = block.getMixedBrightnessForBlock(blockAccess, checkX, checkY, checkZ);
				}
				adjustedAOLightValue = block.getAmbientOcclusionLightValue(blockAccess, checkX, checkY, checkZ);
				
				switch(side) {
				case (MCHelper.SIDE_BOTTOM):
					lightValueTopLeft = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + adjustedAOLightValue) / 4.0F;
					lightValueTopRight = (renderer.aoLightValueScratchYZNP + adjustedAOLightValue + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
					lightValueBottomRight = (adjustedAOLightValue + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
					lightValueBottomLeft = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + adjustedAOLightValue + renderer.aoLightValueScratchYZNN) / 4.0F;
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, adjustedMixedBrightness);
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, adjustedMixedBrightness);
					break;
				case (MCHelper.SIDE_TOP):
					lightValueTopRight = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + adjustedAOLightValue) / 4.0F;
					lightValueTopLeft = (renderer.aoLightValueScratchYZPP + adjustedAOLightValue + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
					lightValueBottomLeft = (adjustedAOLightValue + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
					lightValueBottomRight = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + adjustedAOLightValue + renderer.aoLightValueScratchYZPN) / 4.0F;
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, adjustedMixedBrightness);
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, adjustedMixedBrightness);
					break;
				case (MCHelper.SIDE_EAST):
					lightValueTopLeft = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + adjustedAOLightValue + renderer.aoLightValueScratchYZPN) / 4.0F;
					lightValueBottomLeft = (adjustedAOLightValue + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
					lightValueBottomRight = (renderer.aoLightValueScratchYZNN + adjustedAOLightValue + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
					lightValueTopRight = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + adjustedAOLightValue) / 4.0F;
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, adjustedMixedBrightness);
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, adjustedMixedBrightness);
					break;
				case (MCHelper.SIDE_WEST):	
					lightValueTopLeft = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + adjustedAOLightValue + renderer.aoLightValueScratchYZPP) / 4.0F;
					lightValueTopRight = (adjustedAOLightValue + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
					lightValueBottomRight = (renderer.aoLightValueScratchYZNP + adjustedAOLightValue + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
					lightValueBottomLeft = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + adjustedAOLightValue) / 4.0F;
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, adjustedMixedBrightness);
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, adjustedMixedBrightness);
					break;
				case (MCHelper.SIDE_NORTH):
					lightValueTopRight = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + adjustedAOLightValue + renderer.aoLightValueScratchXZNP) / 4.0F;
					lightValueTopLeft = (adjustedAOLightValue + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
					lightValueBottomLeft = (renderer.aoLightValueScratchXZNN + adjustedAOLightValue + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
					lightValueBottomRight = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + adjustedAOLightValue) / 4.0F;
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, adjustedMixedBrightness);
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, adjustedMixedBrightness);
					break;
				case (MCHelper.SIDE_SOUTH):
					lightValueTopLeft = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + adjustedAOLightValue + renderer.aoLightValueScratchXZPP) / 4.0F;
					lightValueBottomLeft = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + adjustedAOLightValue) / 4.0F;
					lightValueBottomRight = (renderer.aoLightValueScratchXZPN + adjustedAOLightValue + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
					lightValueTopRight = (adjustedAOLightValue + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, adjustedMixedBrightness);
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, adjustedMixedBrightness);
					break;
				}

				texture = block.getBlockTexture(blockAccess, x, y, z, side);
				int numberOfPasses = textureOverlay.getNumberOfPasses(blockAccess.getBlockMetadata(x, y, z));
				for (int pass = 0; pass <= numberOfPasses; pass++) {
					int metadata = blockAccess.getBlockMetadata(x, y, z);
					if (pass > 0) {
						texture = textureOverlay.getOverlayTexture(blockAccess, x, y, z, metadata, side, pass);
					}
					if (texture != null) {
						willColorizeTexture = textureOverlay.willColorizeTexture(blockAccess, x, y, z, metadata, side, pass);
						renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = (willColorizeTexture ? r : 1.0F) * sideColor;
						renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = (willColorizeTexture ? g : 1.0F) * sideColor;
						renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = (willColorizeTexture ? b : 1.0F) * sideColor;
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
						switch(side) {
						case MCHelper.SIDE_BOTTOM:
							renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, texture); break;
						case MCHelper.SIDE_TOP:
							renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, texture); break;
						case MCHelper.SIDE_EAST:
							renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, texture); break;
						case MCHelper.SIDE_WEST:
							renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, texture); break;
						case MCHelper.SIDE_NORTH:
							renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, texture); break;
						case MCHelper.SIDE_SOUTH:
							renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, texture); break;
						}
						blockRendered = true;
					}
				}
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
