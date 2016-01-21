package karuberu.dynamicearth.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMudball extends Render {

	private Item item;
    private int damageValue;
      
    public RenderMudball(Item item, int damageValue) {
    	this.item = item;
    	this.damageValue = damageValue;
    }
    public RenderMudball(Item item) {
        this(item, 0);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float par8, float par9) {
        Icon icon = this.item.getIconFromDamage(damageValue);
        if (icon != null) {
	    	GL11.glPushMatrix();
	        GL11.glTranslatef((float)x, (float)y, (float)z);
	        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	        GL11.glScalef(0.5F, 0.5F, 0.5F);
	        this.bindEntityTexture(entity);
	        this.renderIcon(Tessellator.instance, icon);
	        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	        GL11.glPopMatrix();
        }
    }

    private void renderIcon(Tessellator tessellator, Icon icon) {
        float f = icon.getMinU();
        float f1 = icon.getMaxU();
        float f2 = icon.getMinV();
        float f3 = icon.getMaxV();
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, f, f3);
        tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, f1, f3);
        tessellator.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, f1, f2);
        tessellator.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, f, f2);
        tessellator.draw();
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.locationItemsTexture;
	}
}
