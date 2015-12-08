package karuberu.mods.mudmod.client;

import karuberu.mods.mudmod.CommonProxy;
import karuberu.mods.mudmod.MudMod;
import karuberu.mods.mudmod.blocks.BlockFertileSoil;
import karuberu.mods.mudmod.blocks.BlockGrassSlab;
import karuberu.mods.mudmod.blocks.BlockMud;
import karuberu.mods.mudmod.blocks.BlockPeat;
import karuberu.mods.mudmod.blocks.BlockSandySoil;
import karuberu.mods.mudmod.entity.EntityBomb;
import karuberu.mods.mudmod.entity.EntityFallingBlock;
import karuberu.mods.mudmod.entity.EntityMudball;
import karuberu.mods.mudmod.liquids.LiquidHandler;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidDictionary;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerNames() {
        LanguageRegistry.addName(new ItemStack(MudMod.mud, 1, BlockMud.NORMAL), "Mud");
        LanguageRegistry.addName(new ItemStack(MudMod.mud, 1, BlockMud.WET), "Wet Mud");
        LanguageRegistry.addName(new ItemStack(MudMod.mud, 1, BlockMud.FERTILE), "Rich Mud");
        LanguageRegistry.addName(new ItemStack(MudMod.mud, 1, BlockMud.FERTILE_WET), "Rich Wet Mud");
        LanguageRegistry.addName(MudMod.mudBlob, "Mud Blob");
        LanguageRegistry.addName(MudMod.dirtClod, "Dirt Clod");
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
	        LanguageRegistry.addName(MudMod.earthbowlRaw, "Unfired Bowl");
	        LanguageRegistry.addName(MudMod.earthbowl, "Earthenware Bowl");
	        LanguageRegistry.addName(MudMod.earthbowlSoup, "Mushroom Stew");
	        LanguageRegistry.addName(MudMod.liquidMilk, "Milk");
	        LanguageRegistry.addName(MudMod.liquidSoup, "Mushroom Stew");
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
	        LanguageRegistry.addName(new ItemStack(MudMod.grassSlab, 1, BlockGrassSlab.GRASS), "Grass Slab");
	        LanguageRegistry.addName(new ItemStack(MudMod.grassSlab, 1, BlockGrassSlab.MYCELIUM), "Mycelium Slab");
        }
        if (MudMod.includePeat) {
	        LanguageRegistry.addName(MudMod.peatMoss, "Peat Moss");
	        LanguageRegistry.addName(new ItemStack(MudMod.peat, 1, BlockPeat.WET), "Peat");
	        LanguageRegistry.addName(new ItemStack(MudMod.peat, 1, BlockPeat.DRY), "Dried Peat");
	        LanguageRegistry.addName(MudMod.peatClump, "Wet Peat Clump");
	        LanguageRegistry.addName(MudMod.peatBrick, "Peat Brick");
	        LanguageRegistry.addName(MudMod.peatMossSpecimen, "Peat Moss Specimen");
        }
        if (MudMod.includeFertileSoil) {
        	LanguageRegistry.addName(new ItemStack(MudMod.fertileSoil, 1, BlockFertileSoil.SOIL), "Rich Soil");
        	LanguageRegistry.addName(new ItemStack(MudMod.fertileSoil, 1, BlockFertileSoil.GRASS), "Fertile Grass");
        	LanguageRegistry.addName(new ItemStack(MudMod.fertileSoil, 1, BlockFertileSoil.MYCELIUM), "Fertile Mycelium");
        }
        if (MudMod.includeSandySoil) {
        	LanguageRegistry.addName(new ItemStack(MudMod.sandySoil, 1, BlockSandySoil.DIRT), "Sandy Soil");
        	LanguageRegistry.addName(new ItemStack(MudMod.sandySoil, 1, BlockSandySoil.GRASS), "Dry Grass");
        	LanguageRegistry.addName(new ItemStack(MudMod.sandySoil, 1, BlockSandySoil.MYCELIUM), "Dry Mycelium");
        }
	}
	
	@Override
	public void registerLocalizations() {
		if (MudMod.includeClayGolems) {
			LanguageRegistry.instance().addStringLocalization("entity.karuberu-mudMod.clayGolem.name", "en_US", "Adobe Golem");
		}
		if (MudMod.includeBombs) {
			LanguageRegistry.instance().addStringLocalization("entity.karuberu-mudMod.bomb.name", "en_US", "Earthenware Hand-bomb");
		}
	}
	
	@Override
	public void registerRenderInformation() {
 		RenderingRegistry.registerEntityRenderingHandler(EntityMudball.class, new RenderSnowball(MudMod.mudBlob, 0));
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlock.class, new RenderFallingBlock());
		if (MudMod.includeAdobe) {
			if (MudMod.includeBombs) {
				RenderingRegistry.registerEntityRenderingHandler(EntityBomb.class, new RenderSnowball(MudMod.bombLit, 0));
			}
		}
		if (MudMod.includeDirtSlabs || MudMod.includePeat) {
			MudMod.overlayBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(new RenderBlockWithOverlay());
		}
		if (MudMod.includePeat) {
			MudMod.peatMossRenderID = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(new RenderPeatMoss());
		}
	}
	
	@Override
	public void registerLiquidIcons() {
		if (MudMod.includeAdobe) {
			LiquidDictionary.getCanonicalLiquid(LiquidHandler.MILK).setRenderingIcon(MudMod.liquidMilk.getIconFromDamage(0)).setTextureSheet("/gui/items.png");
			LiquidDictionary.getCanonicalLiquid(LiquidHandler.SOUP).setRenderingIcon(MudMod.liquidSoup.getIconFromDamage(0)).setTextureSheet("/gui/items.png");
		}
	}
}
