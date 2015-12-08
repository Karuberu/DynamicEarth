package karuberu.dynamicearth.client;

import karuberu.dynamicearth.CommonProxy;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockAdobeSlab;
import karuberu.dynamicearth.blocks.BlockDirtSlab;
import karuberu.dynamicearth.blocks.BlockFertileSoil;
import karuberu.dynamicearth.blocks.BlockGrassSlab;
import karuberu.dynamicearth.blocks.BlockMud;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.blocks.BlockSandySoil;
import karuberu.dynamicearth.client.render.RenderAdobeGolem;
import karuberu.dynamicearth.client.render.RenderBlockWithOverlay;
import karuberu.dynamicearth.client.render.RenderFallingBlock;
import karuberu.dynamicearth.client.render.RenderMudball;
import karuberu.dynamicearth.client.render.RenderPeatMoss;
import karuberu.dynamicearth.entity.EntityAdobeGolem;
import karuberu.dynamicearth.entity.EntityBomb;
import karuberu.dynamicearth.entity.EntityFallingBlock;
import karuberu.dynamicearth.entity.EntityMudball;
import karuberu.dynamicearth.fluids.FluidHandler;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerNames() {
        LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, BlockMud.NORMAL), "Mud");
        LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, BlockMud.WET), "Wet Mud");
        LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, BlockMud.FERTILE), "Rich Mud");
        LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, BlockMud.FERTILE_WET), "Rich Wet Mud");
        LanguageRegistry.addName(DynamicEarth.farmland, "Tilled Earth");
        LanguageRegistry.addName(DynamicEarth.mudBlob, "Mud Blob");
        LanguageRegistry.addName(DynamicEarth.dirtClod, "Dirt Clod");
        if (DynamicEarth.includeMudBrick) {
        	LanguageRegistry.addName(DynamicEarth.mudBrick, "Mud Brick");
            LanguageRegistry.addName(DynamicEarth.blockMudBrick, "Mud Brick");
            LanguageRegistry.addName(DynamicEarth.mudBrickStairs, "Mud Brick Stairs");
            LanguageRegistry.addName(DynamicEarth.mudBrickWall, "Mud Brick Wall");
        }
        if (DynamicEarth.includeAdobe) {
	        LanguageRegistry.addName(DynamicEarth.adobeWet, "Moist Adobe");
	        LanguageRegistry.addName(DynamicEarth.adobe, "Adobe");
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeSingleSlab, 1, BlockAdobeSlab.ADOBE), "Adobe Slab");
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeDoubleSlab, 1, BlockAdobeSlab.ADOBE), "Adobe Slab");
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeSingleSlab, 1, BlockAdobeSlab.MUDBRICK), "Mud Brick Slab");
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeDoubleSlab, 1, BlockAdobeSlab.MUDBRICK), "Mud Brick Slab");
		    LanguageRegistry.addName(DynamicEarth.adobeStairs, "Adobe Stairs");
	        LanguageRegistry.addName(DynamicEarth.adobeBlob, "Moist Adobe Blob");
	        LanguageRegistry.addName(DynamicEarth.adobeDust, "Adobe Dust");
	        LanguageRegistry.addName(DynamicEarth.vaseRaw, "Unfired Vase");
	        LanguageRegistry.addName(DynamicEarth.vase, "Vase");
	        LanguageRegistry.addName(DynamicEarth.earthbowlRaw, "Unfired Bowl");
	        LanguageRegistry.addName(DynamicEarth.earthbowl, "Earthenware Bowl");
	        LanguageRegistry.addName(DynamicEarth.earthbowlSoup, "Mushroom Stew");
	        LanguageRegistry.addName(DynamicEarth.liquidMilk, "Milk");
	        LanguageRegistry.addName(DynamicEarth.liquidSoup, "Mushroom Stew");
	        if (DynamicEarth.includeBombs) {
		        LanguageRegistry.addName(DynamicEarth.bomb, "Earthenware Hand-bomb");
		        LanguageRegistry.addName(DynamicEarth.bombLit, "Earthenware Hand-bomb");
	        }
        }
        if (DynamicEarth.includePermafrost) {
        	LanguageRegistry.addName(DynamicEarth.permafrost, "Permafrost");
        }
        if (DynamicEarth.includeDirtSlabs) {
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.dirtSlab, 1, BlockDirtSlab.DIRT), "Dirt Slab");
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.GRASS), "Grass Slab");
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.grassSlab, 1, BlockGrassSlab.MYCELIUM), "Mycelium Slab");
        }
        if (DynamicEarth.includePeat) {
	        LanguageRegistry.addName(DynamicEarth.peatMoss, "Peat Moss");
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.peat, 1, BlockPeat.WET), "Peat");
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.peat, 1, BlockPeat.DRY), "Dried Peat");
	        LanguageRegistry.addName(DynamicEarth.peatClump, "Wet Peat Clump");
	        LanguageRegistry.addName(DynamicEarth.peatBrick, "Peat Brick");
	        LanguageRegistry.addName(DynamicEarth.peatMossSpecimen, "Peat Moss Specimen");
        }
        if (DynamicEarth.includeFertileSoil) {
        	LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileSoil, 1, BlockFertileSoil.SOIL), "Rich Soil");
        	LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileSoil, 1, BlockFertileSoil.GRASS), "Fertile Grass");
        	LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileSoil, 1, BlockFertileSoil.MYCELIUM), "Fertile Mycelium");
        }
        if (DynamicEarth.includeSandySoil) {
        	LanguageRegistry.addName(new ItemStack(DynamicEarth.sandySoil, 1, BlockSandySoil.DIRT), "Sandy Soil");
        	LanguageRegistry.addName(new ItemStack(DynamicEarth.sandySoil, 1, BlockSandySoil.GRASS), "Dry Grass");
        	LanguageRegistry.addName(new ItemStack(DynamicEarth.sandySoil, 1, BlockSandySoil.MYCELIUM), "Dry Mycelium");
        }
	}
	
	@Override
	public void registerLocalizations() {
		if (DynamicEarth.includeAdobe) {
			LanguageRegistry.instance().addStringLocalization("fluid.soup", "en_US", "Mushroom Stew");
			LanguageRegistry.instance().addStringLocalization("fluid.milk", "en_US", "Milk");
		}
		if (DynamicEarth.includeAdobeGolems) {
			LanguageRegistry.instance().addStringLocalization("entity.DynamicEarth.clayGolem.name", "en_US", "Adobe Golem");
		}
		if (DynamicEarth.includeBombs) {
			LanguageRegistry.instance().addStringLocalization("entity.DynamicEarth.bomb.name", "en_US", "Earthenware Hand-bomb");
		}
	}
	
	@Override
	public void registerRenderInformation() {
 		RenderingRegistry.registerEntityRenderingHandler(EntityMudball.class, new RenderMudball(DynamicEarth.mudBlob, 0));
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlock.class, new RenderFallingBlock());
		if (DynamicEarth.includeAdobe) {
			if (DynamicEarth.includeAdobeGolems) {
				RenderingRegistry.registerEntityRenderingHandler(EntityAdobeGolem.class, new RenderAdobeGolem());
			}
			if (DynamicEarth.includeBombs) {
				RenderingRegistry.registerEntityRenderingHandler(EntityBomb.class, new RenderMudball(DynamicEarth.bombLit, 0));
			}
		}
		if (DynamicEarth.includeDirtSlabs || DynamicEarth.includePeat) {
			DynamicEarth.overlayBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(new RenderBlockWithOverlay());
		}
		if (DynamicEarth.includePeat) {
			DynamicEarth.peatMossRenderID = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(new RenderPeatMoss());
		}
	}
	
	@Override
	public void registerLiquidIcons() {
		if (DynamicEarth.includeAdobe) {
			if (FluidHandler.milk != null) {
				FluidHandler.milk.setIcons(DynamicEarth.liquidMilk.getIcon(0, 0));
			}
			if (FluidHandler.soup != null) {
				FluidHandler.soup.setIcons(DynamicEarth.liquidSoup.getIcon(0, 0));
			}
		}
	}
}
