package karuberu.dynamicearth.client.render;

import karuberu.dynamicearth.client.TextureManager;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class RenderAdobeGolem extends RenderIronGolem {

	@Override
    protected ResourceLocation getIronGolemTextures(EntityIronGolem par1EntityIronGolem) {
        return TextureManager.clayGolemTexture;
    }
}
