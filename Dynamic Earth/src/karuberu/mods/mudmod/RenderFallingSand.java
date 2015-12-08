package karuberu.mods.mudmod;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.BlockAnvil;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderFallingSand extends Render
{
    private RenderBlocks renderBlocks = new RenderBlocks();

    public RenderFallingSand() {
        this.shadowSize = 0.5F;
    }

    /**
     * The actual render method that is used in doRender
     */
    public void doRenderFallingSand(EntityFallingSand fallingMud, double par2, double par4, double par6, float par8, float par9) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glDisable(GL11.GL_LIGHTING);
        Block block = Block.blocksList[fallingMud.blockID];
        World world = fallingMud.getWorld();
        this.loadTexture(block.getTextureFile());

        if (block instanceof BlockAnvil && block.getRenderType() == 35) {
            this.renderBlocks.blockAccess = world;
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setTranslation((double)((float)(-MathHelper.floor_double(fallingMud.posX)) - 0.5F), (double)((float)(-MathHelper.floor_double(fallingMud.posY)) - 0.5F), (double)((float)(-MathHelper.floor_double(fallingMud.posZ)) - 0.5F));
            this.renderBlocks.func_85096_a((BlockAnvil)block, MathHelper.floor_double(fallingMud.posX), MathHelper.floor_double(fallingMud.posY), MathHelper.floor_double(fallingMud.posZ), fallingMud.field_70285_b);
            tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
        } else if (block != null) {
            this.renderBlocks.func_83018_a(block);
            this.renderBlocks.func_78588_a(block, world, MathHelper.floor_double(fallingMud.posX), MathHelper.floor_double(fallingMud.posY), MathHelper.floor_double(fallingMud.posZ), fallingMud.field_70285_b);
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
