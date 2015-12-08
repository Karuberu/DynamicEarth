package karuberu.mods.mudmod.client;

import karuberu.mods.mudmod.entity.EntityFallingBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderFallingBlock extends Render
{
    private RenderBlocks renderBlocks = new RenderBlocks();

    public RenderFallingBlock() {
        this.shadowSize = 0.5F;
    }

    public void doRenderFallingBlock(EntityFallingBlock entityFallingBlock, double x, double y, double z, float par8, float par9) {
    	if (entityFallingBlock.blockID == 0) {
    		System.err.println("Falling block with id " + entityFallingBlock.blockID + " spawned at (" + (int)entityFallingBlock.posX + "," + (int)entityFallingBlock.posY + "," + (int)entityFallingBlock.posZ + ")!");
    		entityFallingBlock.blockID = Block.sand.blockID;
    	}
    	Block block = Block.blocksList[entityFallingBlock.blockID];
        World world = entityFallingBlock.getWorld();
    	GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        this.loadTexture("/terrain.png");
        GL11.glDisable(GL11.GL_LIGHTING);
        Tessellator tessellator;

        if (block instanceof BlockAnvil && block.getRenderType() == 35) {
            this.renderBlocks.blockAccess = world;
            tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setTranslation((double)((float)(-MathHelper.floor_double(entityFallingBlock.posX)) - 0.5F), (double)((float)(-MathHelper.floor_double(entityFallingBlock.posY)) - 0.5F), (double)((float)(-MathHelper.floor_double(entityFallingBlock.posZ)) - 0.5F));
            this.renderBlocks.renderBlockAnvilMetadata((BlockAnvil)block, MathHelper.floor_double(entityFallingBlock.posX), MathHelper.floor_double(entityFallingBlock.posY), MathHelper.floor_double(entityFallingBlock.posZ), entityFallingBlock.metadata);
            tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
        } else if (block.getRenderType() == 27) {
            this.renderBlocks.blockAccess = world;
            tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setTranslation((double)((float)(-MathHelper.floor_double(entityFallingBlock.posX)) - 0.5F), (double)((float)(-MathHelper.floor_double(entityFallingBlock.posY)) - 0.5F), (double)((float)(-MathHelper.floor_double(entityFallingBlock.posZ)) - 0.5F));
            this.renderBlocks.renderBlockDragonEgg((BlockDragonEgg)block, MathHelper.floor_double(entityFallingBlock.posX), MathHelper.floor_double(entityFallingBlock.posY), MathHelper.floor_double(entityFallingBlock.posZ));
            tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
        } else if (block != null) {
            this.renderBlocks.setRenderBoundsFromBlock(block);
            this.renderBlocks.renderBlockSandFalling(block, world, MathHelper.floor_double(entityFallingBlock.posX), MathHelper.floor_double(entityFallingBlock.posY), MathHelper.floor_double(entityFallingBlock.posZ), entityFallingBlock.metadata);
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double x, double y, double z, float par8, float par9) {
        this.doRenderFallingBlock((EntityFallingBlock)entity, x, y, z, par8, par9);
    }
}
