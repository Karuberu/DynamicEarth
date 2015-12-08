package karuberu.mods.mudmod;

import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModTextureAnimation;
import net.minecraft.src.RenderEngine;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerNames() {
        LanguageRegistry.addName(MudMod.mud, "Mud");
        LanguageRegistry.addName(MudMod.mudBlob, "Mud Blob");
        LanguageRegistry.addName(MudMod.mudBrick, "Mud Brick");
        LanguageRegistry.addName(MudMod.adobeWet, "Moist Adobe");
        LanguageRegistry.addName(MudMod.adobe, "Adobe");
        LanguageRegistry.addName(MudMod.blockMudBrick, "Mud Brick");
        LanguageRegistry.addName(new ItemStack(MudMod.adobeSingleSlab, 1, 0), "Adobe Slab");
        LanguageRegistry.addName(new ItemStack(MudMod.adobeDoubleSlab, 1, 0), "Adobe Slab");
        LanguageRegistry.addName(new ItemStack(MudMod.adobeSingleSlab, 1, 1), "Mud Brick Slab");
        LanguageRegistry.addName(new ItemStack(MudMod.adobeDoubleSlab, 1, 1), "Mud Brick Slab");
        LanguageRegistry.addName(new ItemStack(MudMod.dirtSlab, 1, 0), "Dirt Slab");
        LanguageRegistry.addName(MudMod.adobeStairs, "Adobe Stairs");
        LanguageRegistry.addName(MudMod.mudBrickStairs, "Mud Brick Stairs");
        LanguageRegistry.addName(MudMod.mudBrickWall, "Mud Brick Wall");
        LanguageRegistry.addName(MudMod.permafrost, "Permafrost");
        LanguageRegistry.addName(MudMod.adobeBlob, "Moist Adobe Blob");
        LanguageRegistry.addName(MudMod.adobeDust, "Adobe Dust");
        LanguageRegistry.addName(MudMod.vaseRaw, "Unfired Vase");
        LanguageRegistry.addName(MudMod.vase, "Vase");
        LanguageRegistry.addName(MudMod.vaseWater, "Water Vase");
        LanguageRegistry.addName(MudMod.vaseMilk, "Milk Vase");
        LanguageRegistry.addName(MudMod.earthbowlRaw, "Unfired Bowl");
        LanguageRegistry.addName(MudMod.earthbowl, "Earthenware Bowl");
        LanguageRegistry.addName(MudMod.earthbowlSoup, "Mushroom Stew");
	}
	
	@Override
	public void registerLocalizations() {
        LanguageRegistry.instance().addStringLocalization("entity.karuberu-mudMod.clayGolem.name", "en_US", "Clay Golem");
	}
	
	@Override
	public void registerRenderInformation() {
		MinecraftForgeClient.preloadTexture(MudMod.terrainFile);
        MinecraftForgeClient.preloadTexture(MudMod.itemsFile);
		RenderingRegistry.registerEntityRenderingHandler(EntityMudball.class, new RenderMudball(MudMod.itemsFile, MudMod.ItemIcon.MUDBLOB.ordinal()));
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingSand.class, new RenderFallingSand());
 	}
}
