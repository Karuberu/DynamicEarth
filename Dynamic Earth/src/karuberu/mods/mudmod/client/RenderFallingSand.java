package karuberu.mods.mudmod.client;

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
public class RenderFallingSand extends Render
{
    private RenderBlocks renderBlocks = new RenderBlocks();

    public RenderFallingSand()
    {
        this.shadowSize = 0.5F;
    }

    /**
     * The actual render method that is used in doRender
     */
    public void doRenderFallingSand(EntityFallingSand entityFallingSand, double x, double y, double z, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        Block block = Block.blocksList[entityFallingSand.blockID];
        World world = entityFallingSand.getWorld();
        GL11.glDisable(GL11.GL_LIGHTING);
        Tessellator tessellator;
        this.loadTexture(block.getTextureFile());

        if (block instanceof BlockAnvil && block.getRenderType() == 35) {
            this.renderBlocks.blockAccess = world;
            tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setTranslation((double)((float)(-MathHelper.floor_double(entityFallingSand.posX)) - 0.5F), (double)((float)(-MathHelper.floor_double(entityFallingSand.posY)) - 0.5F), (double)((float)(-MathHelper.floor_double(entityFallingSand.posZ)) - 0.5F));
            this.renderBlocks.renderBlockAnvilMetadata((BlockAnvil)block, MathHelper.floor_double(entityFallingSand.posX), MathHelper.floor_double(entityFallingSand.posY), MathHelper.floor_double(entityFallingSand.posZ), entityFallingSand.metadata);
            tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
        } else if (block.getRenderType() == 27) {
            this.renderBlocks.blockAccess = world;
            tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setTranslation((double)((float)(-MathHelper.floor_double(entityFallingSand.posX)) - 0.5F), (double)((float)(-MathHelper.floor_double(entityFallingSand.posY)) - 0.5F), (double)((float)(-MathHelper.floor_double(entityFallingSand.posZ)) - 0.5F));
            this.renderBlocks.renderBlockDragonEgg((BlockDragonEgg)block, MathHelper.floor_double(entityFallingSand.posX), MathHelper.floor_double(entityFallingSand.posY), MathHelper.floor_double(entityFallingSand.posZ));
            tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
        } else if (block != null) {
            this.renderBlocks.updateCustomBlockBounds(block);
            this.renderBlocks.renderBlockSandFalling(block, world, MathHelper.floor_double(entityFallingSand.posX), MathHelper.floor_double(entityFallingSand.posY), MathHelper.floor_double(entityFallingSand.posZ), entityFallingSand.metadata);
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.doRenderFallingSand((EntityFallingSand)par1Entity, par2, par4, par6, par8, par9);
    }
}
