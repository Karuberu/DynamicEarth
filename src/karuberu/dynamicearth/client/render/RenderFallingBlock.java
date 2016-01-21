package karuberu.dynamicearth.client.render;

import karuberu.core.util.block.BlockSide;
import karuberu.core.util.client.render.ITextureOverlay;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.entity.EntityFallingBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderFallingBlock extends Render {
	private RenderBlocks renderBlocks = new RenderBlocks();

	public RenderFallingBlock() {
		this.shadowSize = 0.5F;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.locationBlocksTexture;
	}
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float par8, float par9) {
		this.doRenderFallingBlock((EntityFallingBlock)entity, x, y, z, par8, par9);
	}
	
	public void doRenderFallingBlock(EntityFallingBlock entityFallingBlock, double x, double y, double z, float par8, float par9) {
		if (entityFallingBlock.blockID == 0) {
			DynamicEarth.logger.severe("Falling block with id " + entityFallingBlock.blockID + " spawned at (" + (int)entityFallingBlock.posX + "," + (int)entityFallingBlock.posY + "," + (int)entityFallingBlock.posZ + ")!");
			entityFallingBlock.blockID = Block.sand.blockID;
		}
		Block block = Block.blocksList[entityFallingBlock.blockID];
		World world = entityFallingBlock.getWorld();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		this.bindEntityTexture(entityFallingBlock);
		GL11.glDisable(GL11.GL_LIGHTING);
		Tessellator tessellator;

		if (block instanceof BlockAnvil && block.getRenderType() == 35) {
			this.renderBlocks.blockAccess = world;
			tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setTranslation((-MathHelper.floor_double(entityFallingBlock.posX)) - 0.5F, (-MathHelper.floor_double(entityFallingBlock.posY)) - 0.5F, (-MathHelper.floor_double(entityFallingBlock.posZ)) - 0.5F);
			this.renderBlocks.renderBlockAnvilMetadata((BlockAnvil)block, MathHelper.floor_double(entityFallingBlock.posX), MathHelper.floor_double(entityFallingBlock.posY), MathHelper.floor_double(entityFallingBlock.posZ), entityFallingBlock.metadata);
			tessellator.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();
		} else if (block.getRenderType() == 27) {
			this.renderBlocks.blockAccess = world;
			tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setTranslation((-MathHelper.floor_double(entityFallingBlock.posX)) - 0.5F, (-MathHelper.floor_double(entityFallingBlock.posY)) - 0.5F, (-MathHelper.floor_double(entityFallingBlock.posZ)) - 0.5F);
			this.renderBlocks.renderBlockDragonEgg((BlockDragonEgg)block, MathHelper.floor_double(entityFallingBlock.posX), MathHelper.floor_double(entityFallingBlock.posY), MathHelper.floor_double(entityFallingBlock.posZ));
			tessellator.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();
		} else if (block != null) {
			this.renderFallingOverlayBlock(block, world, MathHelper.floor_double(entityFallingBlock.posX), MathHelper.floor_double(entityFallingBlock.posY), MathHelper.floor_double(entityFallingBlock.posZ), entityFallingBlock.metadata);
		}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
	
	protected void renderFallingOverlayBlock(Block block, World world, int x, int y, int z, int metadata) {
		Tessellator tessellator = Tessellator.instance;
		float sideColors[] = new float[] {
			0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F
		};
		
		ITextureOverlay textureOverlay;
		if (block instanceof ITextureOverlay) {
			textureOverlay = (ITextureOverlay)block;
		} else {
			textureOverlay = ITextureOverlay.normalBlock;
		}

//		int colorMultiplier = block.colorMultiplier(world, x, y, z);
		int colorMultiplier = block.getBlockColor();
		float r = (colorMultiplier >> 16 & 255) / 255.0F;
		float g = (colorMultiplier >> 8 & 255) / 255.0F;
		float b = (colorMultiplier & 255) / 255.0F;
		double pos = -0.5D;
		Icon texture;
		int numberOfPasses = 1 + textureOverlay.getNumberOfAdditionalRenderPasses(metadata);

		this.renderBlocks.setRenderBoundsFromBlock(block);
		for (int pass = 0; pass < numberOfPasses; pass++) {
			tessellator.startDrawingQuads();
			tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			for (int side = 0; side < 6; side++) {
				float sideColor = sideColors[side];
				if (textureOverlay.willColorizeInventoryBaseTexture(metadata, side)
				|| (block.blockID == Block.grass.blockID && side == BlockSide.TOP.code)) {
					tessellator.setColorOpaque_F(sideColor * r, sideColor * g, sideColor * b);
				} else {
					tessellator.setColorOpaque_F(sideColor, sideColor, sideColor);
				}
				texture = this.renderBlocks.getBlockIconFromSideAndMetadata(block, side, metadata);
				switch (BlockSide.get(side)) {
				case BOTTOM:
					this.renderBlocks.renderFaceYNeg(block, pos, pos, pos, texture);	break;
				case TOP:
					this.renderBlocks.renderFaceYPos(block, pos, pos, pos, texture);	break;
				case EAST:
					this.renderBlocks.renderFaceZNeg(block, pos, pos, pos, texture);	break;
				case WEST:
					this.renderBlocks.renderFaceZPos(block, pos, pos, pos, texture);	break;
				case NORTH:
					this.renderBlocks.renderFaceXNeg(block, pos, pos, pos, texture);	break;
				case SOUTH:
					this.renderBlocks.renderFaceXPos(block, pos, pos, pos, texture);	break;
				default:
					break;
				}
			}
			tessellator.draw();
		}
	}
}
