package karuberu.dynamicearth.client;

import karuberu.dynamicearth.CommonProxy;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockFertileSoil;
import karuberu.dynamicearth.blocks.BlockGrassSlab;
import karuberu.dynamicearth.blocks.BlockMud;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.blocks.BlockSandySoil;
import karuberu.dynamicearth.client.TextureManager.BlockTexture;
import karuberu.dynamicearth.entity.EntityBomb;
import karuberu.dynamicearth.entity.EntityFallingBlock;
import karuberu.dynamicearth.entity.EntityMudball;
import karuberu.dynamicearth.fluids.FluidHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerNames() {
        LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, BlockMud.NORMAL), "Mud");
        LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, BlockMud.WET), "Wet Mud");
        LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, BlockMud.FERTILE), "Rich Mud");
        LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, BlockMud.FERTILE_WET), "Rich Wet Mud");
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
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeSingleSlab, 1, 0), "Adobe Slab");
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeDoubleSlab, 1, 0), "Adobe Slab");
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeSingleSlab, 1, 1), "Mud Brick Slab");
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeDoubleSlab, 1, 1), "Mud Brick Slab");
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
	        LanguageRegistry.addName(new ItemStack(DynamicEarth.dirtSlab, 1, 0), "Dirt Slab");
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
		if (DynamicEarth.includeClayGolems) {
			LanguageRegistry.instance().addStringLocalization("entity.DynamicEarth.clayGolem.name", "en_US", "Adobe Golem");
		}
		if (DynamicEarth.includeBombs) {
			LanguageRegistry.instance().addStringLocalization("entity.DynamicEarth.bomb.name", "en_US", "Earthenware Hand-bomb");
		}
	}
	
	@Override
	public void registerRenderInformation() {
 		RenderingRegistry.registerEntityRenderingHandler(EntityMudball.class, new RenderSnowball(DynamicEarth.mudBlob, 0));
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlock.class, new RenderFallingBlock());
		if (DynamicEarth.includeAdobe) {
			if (DynamicEarth.includeBombs) {
				RenderingRegistry.registerEntityRenderingHandler(EntityBomb.class, new RenderSnowball(DynamicEarth.bombLit, 0));
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
			FluidRegistry.getFluid(FluidHandler.MILK).setIcons(DynamicEarth.liquidMilk.getIconFromDamage(0));
			FluidRegistry.getFluid(FluidHandler.SOUP).setIcons(DynamicEarth.liquidSoup.getIconFromDamage(0));
		}
	}
}
