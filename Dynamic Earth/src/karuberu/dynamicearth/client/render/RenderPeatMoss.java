package karuberu.dynamicearth.client.render;

import org.lwjgl.opengl.GL11;

import karuberu.core.util.block.BlockSide;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockPeatMoss;
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

public class RenderPeatMoss implements ISimpleBlockRenderingHandler {

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		float color = 1.0F;
		Tessellator tessellator = Tessellator.instance;
//        int renderColor = block.getRenderColor(metadata);
//        int renderType;
//        float r = (float)(renderColor >> 16 & 255) / 255.0F;
//        float g = (float)(renderColor >> 8 & 255) / 255.0F;
//        float b = (float)(renderColor & 255) / 255.0F;
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
        	switch (BlockSide.get(side)) {
        	case BOTTOM:
        		normalX = 0.0F;
        		normalY = -1.0F;
        		normalZ = 0.0F;
        		break;
        	case TOP:
        		normalX = 0.0F;
        		normalY = 1.0F;
        		normalZ = 0.0F;
        		break;
        	case EAST:
        		normalX = 0.0F;
        		normalY = 0.0F;
        		normalZ = -1.0F;
        		break;
        	case WEST:
        		normalX = 0.0F;
        		normalY = 0.0F;
        		normalZ = 1.0F;
        		break;
        	case NORTH:
        		normalX = -1.0F;
        		normalY = 0.0F;
        		normalZ = 0.0F;
        		break;
        	case SOUTH:
        		normalX = 1.0F;
        		normalY = 0.0F;
        		normalZ = 0.0F;
        		break;
			default:
				break;
        	}
            GL11.glColor4f(color, color, color, 1.0F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(normalX, normalY, normalZ);
            switch (BlockSide.get(side)) {
        	case BOTTOM:
        		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case TOP:
        		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case EAST:
        		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case WEST:
        		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case NORTH:
        		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
        	case SOUTH:
        		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, blockTexture); break;
			default:
				break;
            }
            tessellator.draw();
        }
        
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
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
		renderer.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean blockRendered = false;
//        int blockMetadata = blockAccess.getBlockMetadata(x, y, z);
        int mixedBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        int checkX = x;
        int checkY = y;
        int checkZ = z;
        boolean brightnessCheck = true;
        float sideColor = 0.0F;    
        
        Icon texture;
        for(int side = 0; side < 6; side++) {
        	switch (BlockSide.get(side)) {
        	case BOTTOM:
        		checkX = x;
        		checkY = y - 1;
        		checkZ = z;
        		brightnessCheck = renderer.renderMinY > 0.0D;
        		sideColor = 0.5F;
        		break;
        	case TOP:
        		checkX = x;
        		checkY = y + 1;
        		checkZ = z;
        		brightnessCheck = renderer.renderMaxY < 1.0D;
        		sideColor = 1.0F;
        		break;
        	case EAST:
        		checkX = x;
        		checkY = y;
        		checkZ = z - 1;
        		brightnessCheck = renderer.renderMinZ > 0.0D;
        		sideColor = 0.8F;
        		break;
        	case WEST:
        		checkX = x;
        		checkY = y;
        		checkZ = z + 1;
        		brightnessCheck = renderer.renderMaxZ < 1.0D;
        		sideColor = 0.8F;
        		break;
        	case NORTH:
        		checkX = x - 1;
        		checkY = y;
        		checkZ = z;
        		brightnessCheck = renderer.renderMinX > 0.0D;
        		sideColor = 0.6F;
        		break;
        	case SOUTH:
        		checkX = x + 1;
        		checkY = y;
        		checkZ = z;
        		brightnessCheck = renderer.renderMaxX < 1.0D;
        		sideColor = 0.6F;
        		break;
			default:
				break;
        	}
            if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, checkX, checkY, checkZ, side)) {
            	if (brightnessCheck) {
            		tessellator.setBrightness(mixedBrightness);
            	} else {
            		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, checkX, checkY, checkZ));
            	}
        		texture = block.getBlockTexture(blockAccess, x, y, z, side);
    			if (texture != null) {
    				tessellator.setColorOpaque_F(sideColor, sideColor, sideColor);
	            	switch (BlockSide.get(side)) {
	            	case BOTTOM:
	            		renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, texture); break;
	            	case TOP:
	            		renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, texture); break;
	            	case EAST:
	            		renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, texture); break;
	            	case WEST:
	            		renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, texture); break;
	            	case NORTH:
	            		renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, texture); break;
	            	case SOUTH:
	            		renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, texture); break;
					default:
						break;
	            	}
	                blockRendered = true;
    			}
    		}
        }
        return blockRendered;
	}
	
	private boolean renderBlockWithAmbientOcclusion(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer, float r, float g, float b) {
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
//		float lightValue = 0.0F;
//		int brightness = 0;
		boolean willColorizeTexture = false;
		for (side = 0; side < 6; side++) {
			boolean renderingAtBounds = false;
			switch (BlockSide.get(side)) {
			case BOTTOM:
				checkX = x; checkY = y - 1; checkZ = z;
				sideColor = 0.5F;
				break;
			case TOP:
				checkX = x; checkY = y + 1; checkZ = z;
				sideColor = 1.0F;
				break;
			case EAST:
				checkX = x; checkY = y; checkZ = z - 1;
				sideColor = 0.8F;
				break;
			case WEST:
				checkX = x; checkY = y; checkZ = z + 1;
				sideColor = 0.8F;
				break;
			case NORTH:
				checkX = x - 1; checkY = y; checkZ = z;
				sideColor = 0.6F;
				break;
			case SOUTH:
				checkX = x + 1; checkY = y; checkZ = z;
				sideColor = 0.6F;
				break;
			default:
				break;
			}
			if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, checkX, checkY, checkZ, side)) {
				switch (BlockSide.get(side)) {
				case BOTTOM:
					if (renderer.renderMinY <= 0.0D) {
						renderingAtBounds = true;
						--y;
					}
					break;
				case TOP:
					if (renderer.renderMaxY >= 1.0D) {
						renderingAtBounds = true;
						++y;
					}
					break;
				case EAST:
					if (renderer.renderMinZ <= 0.0D) {
						renderingAtBounds = true;
						--z;
					}
					break;
				case WEST:
					if (renderer.renderMaxZ >= 1.0D) {
						renderingAtBounds = true;
						++z;
					}
					break;
				case NORTH:
					if (renderer.renderMinX <= 0.0D) {
						renderingAtBounds = true;
						--x;
					}
					break;
				case SOUTH:
					if (renderer.renderMaxX >= 1.0D) {
						renderingAtBounds = true;
						++x;
					}
					break;
				default:
					break;
				}
	
				switch (BlockSide.get(side)) {
				case BOTTOM:
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
				case TOP:
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
				case EAST:
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
				case WEST:
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
				case NORTH:
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
				case SOUTH:
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
				default:
					break;
				}
	
				if (renderingAtBounds) {
					switch (BlockSide.get(side)) {
					case BOTTOM: ++y; break;
					case TOP: --y; break;
					case EAST: ++z; break;
					case WEST: --z; break;
					case NORTH: ++x; break;
					case SOUTH: --x; break;
					default: break;
					}
				}
				adjustedMixedBrightness = mixedBrightness;
				if (renderingAtBounds || !blockAccess.isBlockOpaqueCube(checkX, checkY, checkZ)) {
					adjustedMixedBrightness = block.getMixedBrightnessForBlock(blockAccess, checkX, checkY, checkZ);
				}
				adjustedAOLightValue = block.getAmbientOcclusionLightValue(blockAccess, checkX, checkY, checkZ);
				
				switch (BlockSide.get(side)) {
				case BOTTOM:
					lightValueTopLeft = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + adjustedAOLightValue) / 4.0F;
					lightValueTopRight = (renderer.aoLightValueScratchYZNP + adjustedAOLightValue + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
					lightValueBottomRight = (adjustedAOLightValue + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
					lightValueBottomLeft = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + adjustedAOLightValue + renderer.aoLightValueScratchYZNN) / 4.0F;
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, adjustedMixedBrightness);
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, adjustedMixedBrightness);
					break;
				case TOP:
					lightValueTopRight = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + adjustedAOLightValue) / 4.0F;
					lightValueTopLeft = (renderer.aoLightValueScratchYZPP + adjustedAOLightValue + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
					lightValueBottomLeft = (adjustedAOLightValue + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
					lightValueBottomRight = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + adjustedAOLightValue + renderer.aoLightValueScratchYZPN) / 4.0F;
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, adjustedMixedBrightness);
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, adjustedMixedBrightness);
					break;
				case EAST:
					lightValueTopLeft = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + adjustedAOLightValue + renderer.aoLightValueScratchYZPN) / 4.0F;
					lightValueBottomLeft = (adjustedAOLightValue + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
					lightValueBottomRight = (renderer.aoLightValueScratchYZNN + adjustedAOLightValue + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
					lightValueTopRight = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + adjustedAOLightValue) / 4.0F;
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, adjustedMixedBrightness);
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, adjustedMixedBrightness);
					break;
				case WEST:	
					lightValueTopLeft = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + adjustedAOLightValue + renderer.aoLightValueScratchYZPP) / 4.0F;
					lightValueTopRight = (adjustedAOLightValue + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
					lightValueBottomRight = (renderer.aoLightValueScratchYZNP + adjustedAOLightValue + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
					lightValueBottomLeft = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + adjustedAOLightValue) / 4.0F;
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, adjustedMixedBrightness);
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, adjustedMixedBrightness);
					break;
				case NORTH:
					lightValueTopRight = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + adjustedAOLightValue + renderer.aoLightValueScratchXZNP) / 4.0F;
					lightValueTopLeft = (adjustedAOLightValue + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
					lightValueBottomLeft = (renderer.aoLightValueScratchXZNN + adjustedAOLightValue + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
					lightValueBottomRight = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + adjustedAOLightValue) / 4.0F;
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, adjustedMixedBrightness);
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, adjustedMixedBrightness);
					break;
				case SOUTH:
					lightValueTopLeft = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + adjustedAOLightValue + renderer.aoLightValueScratchXZPP) / 4.0F;
					lightValueBottomLeft = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + adjustedAOLightValue) / 4.0F;
					lightValueBottomRight = (renderer.aoLightValueScratchXZPN + adjustedAOLightValue + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
					lightValueTopRight = (adjustedAOLightValue + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
					renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, adjustedMixedBrightness);
					renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, adjustedMixedBrightness);
					renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, adjustedMixedBrightness);
					renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, adjustedMixedBrightness);
					break;
				default:
					break;
				}

				texture = block.getBlockTexture(blockAccess, x, y, z, side);
				if (texture != null) {
					willColorizeTexture = false;
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
					switch (BlockSide.get(side)) {
					case BOTTOM:
						renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, texture); break;
					case TOP:
						renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, texture); break;
					case EAST:
						renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, texture); break;
					case WEST:
						renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, texture); break;
					case NORTH:
						renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, texture); break;
					case SOUTH:
						renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, texture); break;
					default:
						break;
					}
					blockRendered = true;
				}
			}
		}
		if (block instanceof BlockPeatMoss && ((BlockPeatMoss)block).hasPlantGrowth(blockAccess, x, y, z)) {
			Block plantGrowth = ((BlockPeatMoss)block).getPlantGrowth(blockAccess, x, y, z);
			int plantGrowthMetadata = ((BlockPeatMoss)block).getPlantGrowthMetadata(blockAccess, x, y, z);
			tessellator.setBrightness(plantGrowth.getMixedBrightnessForBlock(blockAccess, x, y, z));
			float f = 1.0F;
			int colorMultiplier = plantGrowth.colorMultiplier(blockAccess, x, y, z);
			r = (float)(colorMultiplier >> 16 & 0xFF) / 255.0F;
			g = (float)(colorMultiplier >> 8 & 0xFF) / 255.0F;
			b = (float)(colorMultiplier & 0xFF) / 255.0F;

			if (EntityRenderer.anaglyphEnable) {
				float f4 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
				float f5 = (r * 30.0F + g * 70.0F) / 100.0F;
				float f6 = (r * 30.0F + b * 70.0F) / 100.0F;
				r = f4;
				g = f5;
				b = f6;
			}
			tessellator.setColorOpaque_F(f * r, f * g, f * b);
			double posX = (double)x;
			double posY = (double)y;
			double posZ = (double)z;
			if (plantGrowth == Block.tallGrass) {
				long i1 = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
				i1 = i1 * i1 * 42317861L + i1 * 11L;
				posX += ((double)((float)(i1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
				posY += ((double)((float)(i1 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
				posZ += ((double)((float)(i1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
			}
			renderer.drawCrossedSquares(plantGrowth, plantGrowthMetadata, posX, posY, posZ, 1.0F);
			blockRendered = true;
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
		return DynamicEarth.peatMossRenderID;
	}

}
