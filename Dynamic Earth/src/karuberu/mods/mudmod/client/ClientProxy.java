package karuberu.mods.mudmod.client;

import karuberu.mods.mudmod.CommonProxy;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.MudMod.ItemIcon;
import karuberu.mods.mudmod.entity.EntityBomb;
import karuberu.mods.mudmod.entity.EntityFallingBlock;
import karuberu.mods.mudmod.entity.EntityMudball;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerNames() {
        LanguageRegistry.addName(MudMod.mud, "Mud");
        LanguageRegistry.addName(MudMod.mudBlob, "Mud Blob");
        if (MudMod.includeMudBrick) {
        	LanguageRegistry.addName(MudMod.mudBrick, "Mud Brick");
            LanguageRegistry.addName(MudMod.blockMudBrick, "Mud Brick");
            LanguageRegistry.addName(MudMod.mudBrickStairs, "Mud Brick Stairs");
            LanguageRegistry.addName(MudMod.mudBrickWall, "Mud Brick Wall");
        }
        if (MudMod.includeAdobe) {
	        LanguageRegistry.addName(MudMod.adobeWet, "Moist Adobe");
	        LanguageRegistry.addName(MudMod.adobe, "Adobe");
	        LanguageRegistry.addName(new ItemStack(MudMod.adobeSingleSlab, 1, 0), "Adobe Slab");
	        LanguageRegistry.addName(new ItemStack(MudMod.adobeDoubleSlab, 1, 0), "Adobe Slab");
	        LanguageRegistry.addName(new ItemStack(MudMod.adobeSingleSlab, 1, 1), "Mud Brick Slab");
	        LanguageRegistry.addName(new ItemStack(MudMod.adobeDoubleSlab, 1, 1), "Mud Brick Slab");
		    LanguageRegistry.addName(MudMod.adobeStairs, "Adobe Stairs");
	        LanguageRegistry.addName(MudMod.adobeBlob, "Moist Adobe Blob");
	        LanguageRegistry.addName(MudMod.adobeDust, "Adobe Dust");
	        LanguageRegistry.addName(MudMod.vaseRaw, "Unfired Vase");
	        LanguageRegistry.addName(MudMod.vase, "Vase");
	        LanguageRegistry.addName(MudMod.vaseWater, "Water Vase");
	        LanguageRegistry.addName(MudMod.vaseMilk, "Milk Vase");
	        LanguageRegistry.addName(MudMod.earthbowlRaw, "Unfired Bowl");
	        LanguageRegistry.addName(MudMod.earthbowl, "Earthenware Bowl");
	        LanguageRegistry.addName(MudMod.earthbowlSoup, "Mushroom Stew");
	        if (MudMod.includeBombs) {
		        LanguageRegistry.addName(MudMod.bomb, "Earthenware Hand-bomb");
		        LanguageRegistry.addName(MudMod.bombLit, "Earthenware Hand-bomb");
	        }
        }
        if (MudMod.includePermafrost) {
        	LanguageRegistry.addName(MudMod.permafrost, "Permafrost");
        }
        if (MudMod.includeDirtSlabs) {
	        LanguageRegistry.addName(new ItemStack(MudMod.dirtSlab, 1, 0), "Dirt Slab");
	        LanguageRegistry.addName(new ItemStack(MudMod.grassSlab, 1, 0), "Grass Slab");
	        LanguageRegistry.addName(new ItemStack(MudMod.grassSlab, 1, 1), "Mycelium Slab");
        }
        if (MudMod.includePeat) {
	        LanguageRegistry.addName(MudMod.peatMoss, "Peat Moss");
	        LanguageRegistry.addName(MudMod.peat, "Peat");
	        LanguageRegistry.addName(MudMod.peatDry, "Dried Peat");
	        LanguageRegistry.addName(MudMod.peatClump, "Wet Peat Clump");
	        LanguageRegistry.addName(MudMod.peatBrick, "Peat Brick");
	        LanguageRegistry.addName(MudMod.peatMossSpecimen, "Peat Moss Specimen");
        }
	}
	
	@Override
	public void registerLocalizations() {
		if (MudMod.includeClayGolems) {
			LanguageRegistry.instance().addStringLocalization("entity.karuberu-mudMod.clayGolem.name", "en_US", "Clay Golem");
		}
		if (MudMod.includeBombs) {
			LanguageRegistry.instance().addStringLocalization("entity.karuberu-mudMod.bomb.name", "en_US", "Earthenware Hand-bomb");
		}
	}
	
	@Override
	public void registerRenderInformation() {
		MinecraftForgeClient.preloadTexture(MudMod.terrainFile);
        MinecraftForgeClient.preloadTexture(MudMod.itemsFile);
 		RenderingRegistry.registerEntityRenderingHandler(EntityMudball.class, new RenderMudball(MudMod.itemsFile, MudMod.ItemIcon.MUDBLOB.ordinal()));
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlock.class, new RenderFallingBlock());
		if (MudMod.includeBombs) {
			RenderingRegistry.registerEntityRenderingHandler(EntityBomb.class, new RenderMudball(MudMod.itemsFile, MudMod.ItemIcon.BOMB.ordinal()));
		}
		if (MudMod.includeDirtSlabs || MudMod.includePeat) {
			MudMod.overlayBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(new RenderBlockWithOverlay());
		}
	}
}
