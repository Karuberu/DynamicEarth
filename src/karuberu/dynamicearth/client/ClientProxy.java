package karuberu.dynamicearth.client;

import karuberu.core.util.Helper;
import karuberu.core.util.client.LanguageHelper;
import karuberu.core.util.client.render.RenderLayeredBlock;
import karuberu.dynamicearth.CommonProxy;
import karuberu.dynamicearth.DynamicEarth;
import karuberu.dynamicearth.blocks.BlockAdobeSlab;
import karuberu.dynamicearth.blocks.BlockDirtSlab;
import karuberu.dynamicearth.blocks.BlockGrassSlab;
import karuberu.dynamicearth.blocks.BlockPeat;
import karuberu.dynamicearth.client.render.RenderAdobeGolem;
import karuberu.dynamicearth.client.render.RenderFallingBlock;
import karuberu.dynamicearth.client.render.RenderMudball;
import karuberu.dynamicearth.client.render.RenderPeatMoss;
import karuberu.dynamicearth.entity.EntityAdobeGolem;
import karuberu.dynamicearth.entity.EntityBomb;
import karuberu.dynamicearth.entity.EntityFallingBlock;
import karuberu.dynamicearth.entity.EntityMudball;
import karuberu.dynamicearth.fluids.FluidHandler;
import karuberu.dynamicearth.items.ItemMudBlob;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerNames() {
		LanguageHelper.registerName(DynamicEarth.farmland, "Tilled Earth");
		LanguageHelper.registerName(DynamicEarth.dirtClod, "Dirt Clod");
		if (DynamicEarth.includeMud) {
			LanguageHelper.registerName(DynamicEarth.mud, DynamicEarth.mud.NORMAL, "Mud");
			LanguageHelper.registerName(DynamicEarth.mud, DynamicEarth.mud.GRASS, "Muddy Grass");
			LanguageHelper.registerName(DynamicEarth.mud, DynamicEarth.mud.MYCELIUM, "Muddy Mycelium");
			LanguageHelper.registerName(DynamicEarth.mud, DynamicEarth.mud.WET, "Wet Mud");
			LanguageHelper.registerName(DynamicEarth.mud, DynamicEarth.mud.WET_GRASS, "Sodden Grass");
			LanguageHelper.registerName(DynamicEarth.mud, DynamicEarth.mud.WET_MYCELIUM, "Sodden Mycelium");
			LanguageHelper.registerName(DynamicEarth.mudBlob, ItemMudBlob.NORMAL, "Mud Blob");
			LanguageHelper.registerHintText(new ItemStack(DynamicEarth.mud),
				"Dirt that has absorbed a great",
				"deal of water. Because of its",
				"high water content, it is not",
				"very stable and may mudslide."
			);
		}
		if (DynamicEarth.includeMudBrick) {
			LanguageHelper.registerName(DynamicEarth.mudBrick, "Mud Brick");
			LanguageHelper.registerName(DynamicEarth.adobeSingleSlab, BlockAdobeSlab.MUDBRICK, "Mud Brick Slab");
			LanguageHelper.registerName(DynamicEarth.adobeDoubleSlab, BlockAdobeSlab.MUDBRICK, "Mud Brick Slab");
			LanguageHelper.registerName(DynamicEarth.blockMudBrick, "Mud Brick");
			LanguageHelper.registerName(DynamicEarth.mudBrickStairs, "Mud Brick Stairs");
			LanguageHelper.registerName(DynamicEarth.mudBrickWall, "Mud Brick Wall");
			LanguageHelper.registerHintText(DynamicEarth.mudBrick,
				"A brick made from mud dried",
				"in a furnace. It can be used as",
				"a low-cost building material."
			);
		}
		if (DynamicEarth.includeAdobe) {
			LanguageHelper.registerName(DynamicEarth.adobeWet, "Moist Adobe");
			LanguageHelper.registerName(DynamicEarth.adobe, "Adobe");
			LanguageHelper.registerName(DynamicEarth.adobeSingleSlab, BlockAdobeSlab.ADOBE, "Adobe Slab");
			LanguageHelper.registerName(DynamicEarth.adobeDoubleSlab, BlockAdobeSlab.ADOBE, "Adobe Slab");
			LanguageHelper.registerName(DynamicEarth.adobeStairs, "Adobe Stairs");
			LanguageHelper.registerName(DynamicEarth.adobeBlob, "Moist Adobe Blob");
			LanguageHelper.registerName(DynamicEarth.adobeDust, "Adobe Dust");
			LanguageHelper.registerName(DynamicEarth.vase, "Vase");	
			LanguageHelper.registerName(DynamicEarth.vaseRaw, "Unfired Vase");	
			LanguageHelper.registerName(DynamicEarth.earthbowlRaw, "Unfired Bowl");
			LanguageHelper.registerName(DynamicEarth.earthbowl, "Earthenware Bowl");
			LanguageHelper.registerName(DynamicEarth.earthbowlSoup, "Mushroom Stew");
			LanguageHelper.registerName(DynamicEarth.liquidMilk, "Milk");
			LanguageHelper.registerName(DynamicEarth.liquidSoup, "Mushroom Stew");
			LanguageHelper.registerHintText(DynamicEarth.adobeBlob,
				"A handful of moist adobe. It",
				"is used to make earthenware",
				"products and can be formed",
				"into blocks for building."
			);
			LanguageHelper.registerHintText(DynamicEarth.adobeDust,
				"Dust from an adobe structure.",
				"It can be rehydrated if dipped",
				"in water."
			);
			LanguageHelper.registerHintText(DynamicEarth.vaseRaw,
				"A vase made from adobe. It",
				"must be fired in a furnace",
				"before it can be used."
			);
			LanguageHelper.registerHintText(DynamicEarth.earthbowlRaw,
				"A bowl made from adobe. It",
				"must be fired in a furnace",
				"before it can be used."
			);
			for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
				ItemStack itemStack = DynamicEarth.vase.getFilledVase(fluid);
				if (itemStack != null) {
					String fluidName = LanguageRegistry.instance().getStringLocalization(fluid.getUnlocalizedName());
					if (fluidName == null || fluidName.isEmpty()) {
						fluidName = Helper.UCFirst(fluid.getName());
					}
					LanguageHelper.registerName(
						itemStack,
						"Vase of " + fluidName
					);
				}
			}
			if (DynamicEarth.includeBombs) {
				LanguageHelper.registerName(DynamicEarth.bomb, "Earthenware Hand-bomb");
				LanguageHelper.registerName(DynamicEarth.bombLit, "Earthenware Hand-bomb");
			}
		}
		if (DynamicEarth.includePermafrost) {
			LanguageHelper.registerName(DynamicEarth.permafrost, "Permafrost");
			LanguageHelper.registerHintText(DynamicEarth.permafrost,
				"Dirt that has frozen hard",
				"as rock. A pickaxe or sturdy",
				"shovel will be needed to",
				"quickly mine it."
			);
		}
		if (DynamicEarth.includeDirtSlabs) {
			LanguageHelper.registerName(DynamicEarth.dirtSlab, BlockDirtSlab.DIRT, "Dirt Slab");
			LanguageHelper.registerName(DynamicEarth.grassSlab, BlockGrassSlab.GRASS, "Grass Slab");
			LanguageHelper.registerName(DynamicEarth.grassSlab, BlockGrassSlab.MYCELIUM, "Mycelium Slab");
		}
		if (DynamicEarth.includePeat) {
			LanguageHelper.registerName(DynamicEarth.peatMoss, "Peat Moss");
			LanguageHelper.registerName(DynamicEarth.peat, BlockPeat.WET, "Peat");
			LanguageHelper.registerName(DynamicEarth.peat, BlockPeat.DRY, "Dried Peat");
			LanguageHelper.registerName(DynamicEarth.peatClump, "Wet Peat Clump");
			LanguageHelper.registerName(DynamicEarth.peatBrick, "Peat Brick");
			LanguageHelper.registerName(DynamicEarth.peatMossSpecimen, "Peat Moss Specimen");
			LanguageHelper.registerHintText(DynamicEarth.peatMossSpecimen,
				"A small sample of peat moss,",
				"found naturally in swamps.",
				"Plant it in farmland and it",
				"will produce peat from dirt."
			);
			LanguageHelper.registerHintText(DynamicEarth.peat,
				"A type of fuel produced by the",
				"lifecycle of peat moss. It must",
				"be dried before it can be",
				"burned."
			);
			LanguageHelper.registerHintText(DynamicEarth.peatClump, LanguageHelper.getHintText(DynamicEarth.peat));
			LanguageHelper.registerHintText(DynamicEarth.peatBrick,
				"Dried peat that can be used as",
				"fuel. As a fuel source, it is",
				"only slightly worse than coal."
			);
		}
		if (DynamicEarth.includeFertileSoil) {
			LanguageHelper.registerName(DynamicEarth.fertileSoil, DynamicEarth.fertileSoil.DIRT, "Rich Soil");
			LanguageHelper.registerName(DynamicEarth.fertileSoil, DynamicEarth.fertileSoil.GRASS, "Fertile Grass");
			LanguageHelper.registerName(DynamicEarth.fertileSoil, DynamicEarth.fertileSoil.MYCELIUM, "Fertile Mycelium");
			LanguageHelper.registerHintText(DynamicEarth.fertileSoil,
				"This dirt has everything needed",
				"to make a plant happy. Plants",
				"are to sure to grow faster",
				"when planted in this soil."
			);
			if (DynamicEarth.includeMud) {
				LanguageHelper.registerName(DynamicEarth.fertileMud, DynamicEarth.fertileMud.NORMAL, "Rich Mud");
				LanguageHelper.registerName(DynamicEarth.fertileMud, DynamicEarth.fertileMud.GRASS, "Muddy Fertile Grass");
				LanguageHelper.registerName(DynamicEarth.fertileMud, DynamicEarth.fertileMud.MYCELIUM, "Muddy Fertile Mycelium");
				LanguageHelper.registerName(DynamicEarth.fertileMud, DynamicEarth.fertileMud.WET, "Rich Wet Mud");
				LanguageHelper.registerName(DynamicEarth.fertileMud, DynamicEarth.fertileMud.WET_GRASS, "Sodden Fertile Grass");
				LanguageHelper.registerName(DynamicEarth.fertileMud, DynamicEarth.fertileMud.WET_MYCELIUM, "Sodden Fertile Mycelium");
				LanguageHelper.registerName(DynamicEarth.mudBlob, ItemMudBlob.FERTILE, "Rich Mud Blob");
				LanguageHelper.registerHintText(DynamicEarth.fertileMud,
					"This mud has everything needed",
					"to make a plant happy. Plants",
					"are to sure to grow faster",
					"when planted in this soil."
				);
			}
		}
		if (DynamicEarth.includeSandySoil) {
			LanguageHelper.registerName(DynamicEarth.sandySoil, DynamicEarth.sandySoil.DIRT, "Sandy Soil");
			LanguageHelper.registerName(DynamicEarth.sandySoil, DynamicEarth.sandySoil.GRASS, "Dry Grass");
			LanguageHelper.registerName(DynamicEarth.sandySoil, DynamicEarth.sandySoil.MYCELIUM, "Dry Mycelium");
			LanguageHelper.registerHintText(DynamicEarth.sandySoil,
				"This dirt has a very high sand",
				"content, making it unlikely to",
				"turn into mud."
			);
		}
		if (DynamicEarth.includeGlowingSoil) {
			LanguageHelper.registerName(DynamicEarth.glowingSoil, DynamicEarth.glowingSoil.DIRT, "Glowsoil");
			LanguageHelper.registerName(DynamicEarth.glowingSoil, DynamicEarth.glowingSoil.GRASS, "Glowing Grass");
			LanguageHelper.registerName(DynamicEarth.glowingSoil, DynamicEarth.glowingSoil.MYCELIUM, "Glowing Mycelium");
			LanguageHelper.registerHintText(DynamicEarth.glowingSoil,
				"Dirt infused with glowstone.",
				"Emits light that is slightly",
				"dimmer than a torch."
			);
			if (DynamicEarth.includeMud) {
				LanguageHelper.registerName(DynamicEarth.glowingMud, DynamicEarth.glowingMud.NORMAL, "Glowmud");
				LanguageHelper.registerName(DynamicEarth.glowingMud, DynamicEarth.glowingMud.GRASS, "Muddy Glowing Grass");
				LanguageHelper.registerName(DynamicEarth.glowingMud, DynamicEarth.glowingMud.MYCELIUM, "Muddy Glowing Mycelium");
				LanguageHelper.registerName(DynamicEarth.glowingMud, DynamicEarth.glowingMud.WET, "Wet Glowmud");
				LanguageHelper.registerName(DynamicEarth.glowingMud, DynamicEarth.glowingMud.WET_GRASS, "Sodden Glowing Grass");
				LanguageHelper.registerName(DynamicEarth.glowingMud, DynamicEarth.glowingMud.WET_MYCELIUM, "Sodden Glowing Mycelium");
				LanguageHelper.registerName(DynamicEarth.mudBlob, ItemMudBlob.GLOWING, "Glowing Mud Blob");
				LanguageHelper.registerHintText(DynamicEarth.glowingMud,
					"Mud infused with glowstone.",
					"Emits light that is slightly",
					"dimmer than a torch."
				);
			}
		}
		if (DynamicEarth.includeBurningSoil) {
			LanguageHelper.registerName(DynamicEarth.burningSoil, DynamicEarth.burningSoil.DIRT, "Burning Soil");
			LanguageHelper.registerName(DynamicEarth.burningSoil, DynamicEarth.burningSoil.GRASS, "Nether Grass");
			LanguageHelper.registerHintText(new ItemStack(DynamicEarth.burningSoil, 1, DynamicEarth.burningSoil.DIRT),
				"Dirt infused with heat. While unable",
				"to grow regular vegetation, it may be",
				"the perfect soil for new strains of",
				"Nether plant life."
			);
			LanguageHelper.registerHintText(new ItemStack(DynamicEarth.burningSoil, 1, DynamicEarth.burningSoil.GRASS),
				"A new strain of grass created by",
				"hybridizing overworld vegetation",
				"with Nether vegetation. It seems",
				"to thrive in hot conditions."
			);
		}
	}

	@Override
	public void registerLocalizations() {
		if (DynamicEarth.includeAdobe) {
			LanguageHelper.registerName(FluidHandler.soup, "Mushroom Stew");
			LanguageHelper.registerName(FluidHandler.milk, "Mushroom Stew");
		}
		if (DynamicEarth.includeAdobeGolems) {
			LanguageHelper.registerLocalization("entity.DynamicEarth.clayGolem.name", "Adobe Golem");
		}
		if (DynamicEarth.includeBombs) {
			LanguageHelper.registerLocalization("entity.DynamicEarth.bomb.name", "Earthenware Hand-bomb");
		}
	}

	@Override
	public void registerRenderInformation() {
		RenderLayeredBlock.renderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlock.class, new RenderFallingBlock());
		RenderingRegistry.registerBlockHandler(new RenderLayeredBlock());
		if (DynamicEarth.includeMud) {
			RenderingRegistry.registerEntityRenderingHandler(EntityMudball.class, new RenderMudball(DynamicEarth.mudBlob, 0));
		}
		if (DynamicEarth.includeAdobe) {
			if (DynamicEarth.includeAdobeGolems) {
				RenderingRegistry.registerEntityRenderingHandler(EntityAdobeGolem.class, new RenderAdobeGolem());
			}
			if (DynamicEarth.includeBombs) {
				RenderingRegistry.registerEntityRenderingHandler(EntityBomb.class, new RenderMudball(DynamicEarth.bombLit, 0));
			}
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
	
	@Override
	public Minecraft getMinecraftClient() {
		return FMLClientHandler.instance().getClient();
	}
}
