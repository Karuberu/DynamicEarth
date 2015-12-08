package karuberu.dynamicearth.client;

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
import karuberu.dynamicearth.items.ItemBlockBurningSoil;
import karuberu.dynamicearth.items.ItemBlockFertileMud;
import karuberu.dynamicearth.items.ItemBlockFertileSoil;
import karuberu.dynamicearth.items.ItemBlockGlowingMud;
import karuberu.dynamicearth.items.ItemBlockGlowingSoil;
import karuberu.dynamicearth.items.ItemBlockMud;
import karuberu.dynamicearth.items.ItemBlockPeat;
import karuberu.dynamicearth.items.ItemBlockPermafrost;
import karuberu.dynamicearth.items.ItemBlockSandySoil;
import karuberu.dynamicearth.items.ItemMudBlob;
import karuberu.dynamicearth.items.ItemPeatMossSpecimen;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerNames() {
		LanguageRegistry.addName(DynamicEarth.farmland, "Tilled Earth");
		LanguageRegistry.addName(DynamicEarth.dirtClod, "Dirt Clod");
		if (DynamicEarth.includeMud) {
			LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, DynamicEarth.mud.NORMAL), "Mud");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, DynamicEarth.mud.GRASS), "Muddy Grass");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, DynamicEarth.mud.MYCELIUM), "Muddy Mycelium");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, DynamicEarth.mud.WET), "Wet Mud");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, DynamicEarth.mud.WET_GRASS), "Sodden Grass");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.mud, 1, DynamicEarth.mud.WET_MYCELIUM), "Sodden Mycelium");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.mudBlob, 1, ItemMudBlob.NORMAL), "Mud Blob");
			ItemBlockMud.hintText = new String[] {
				"Dirt that has absorbed a great",
				"deal of water. Because of its",
				"high water content, it is not",
				"very stable and may mudslide."
			};
		}
		if (DynamicEarth.includeMudBrick) {
			LanguageRegistry.addName(DynamicEarth.mudBrick, "Mud Brick");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeSingleSlab, 1, BlockAdobeSlab.MUDBRICK), "Mud Brick Slab");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeDoubleSlab, 1, BlockAdobeSlab.MUDBRICK), "Mud Brick Slab");
			LanguageRegistry.addName(DynamicEarth.blockMudBrick, "Mud Brick");
			LanguageRegistry.addName(DynamicEarth.mudBrickStairs, "Mud Brick Stairs");
			LanguageRegistry.addName(DynamicEarth.mudBrickWall, "Mud Brick Wall");
			DynamicEarth.mudBrick.setHintText(
				"A brick made from mud dried",
				"in a furnace. It can be used as",
				"a low-cost building material."
			);
		}
		if (DynamicEarth.includeAdobe) {
			LanguageRegistry.addName(DynamicEarth.adobeWet, "Moist Adobe");
			LanguageRegistry.addName(DynamicEarth.adobe, "Adobe");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeSingleSlab, 1, BlockAdobeSlab.ADOBE), "Adobe Slab");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.adobeDoubleSlab, 1, BlockAdobeSlab.ADOBE), "Adobe Slab");
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
			DynamicEarth.adobeBlob.setHintText(
				"A handful of moist adobe. It",
				"is used to make earthenware",
				"products and can be formed",
				"into blocks for building."
			);
			DynamicEarth.adobeDust.setHintText(
				"Dust from an adobe structure.",
				"It can be rehydrated if dipped",
				"in water."
			);
			DynamicEarth.vaseRaw.setHintText(
				"A vase made from adobe. It",
				"must be fired in a furnace",
				"before it can be used."
			);
			DynamicEarth.earthbowlRaw.setHintText(
				"A bowl made from adobe. It",
				"must be fired in a furnace",
				"before it can be used."
			);
			if (DynamicEarth.includeBombs) {
				LanguageRegistry.addName(DynamicEarth.bomb, "Earthenware Hand-bomb");
				LanguageRegistry.addName(DynamicEarth.bombLit, "Earthenware Hand-bomb");
			}
		}
		if (DynamicEarth.includePermafrost) {
			LanguageRegistry.addName(DynamicEarth.permafrost, "Permafrost");
			ItemBlockPermafrost.hintText = new String[] {
				"Dirt that has frozen hard",
				"as rock. A pickaxe or sturdy",
				"shovel will be needed to",
				"quickly mine it."
			};
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
			ItemPeatMossSpecimen.hintText = new String[] {
				"A small sample of peat moss,",
				"found naturally in swamps.",
				"Plant it in farmland and it",
				"will produce peat from dirt."
			};
			ItemBlockPeat.hintText = new String[] {
				"A type of fuel produced by the",
				"lifecycle of peat moss. It must",
				"be dried before it can be",
				"burned."
			};
			DynamicEarth.peatClump.setHintText(ItemBlockPeat.hintText);
			DynamicEarth.peatBrick.setHintText(
				"Dried peat that can be used as",
				"fuel. As a fuel source, it is",
				"only slightly worse than coal."
			);
		}
		if (DynamicEarth.includeFertileSoil) {
			LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileSoil, 1, DynamicEarth.fertileSoil.DIRT), "Rich Soil");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileSoil, 1, DynamicEarth.fertileSoil.GRASS), "Fertile Grass");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileSoil, 1, DynamicEarth.fertileSoil.MYCELIUM), "Fertile Mycelium");
			ItemBlockFertileSoil.hintText = new String[] {
				"This dirt has everything needed",
				"to make a plant happy. Plants",
				"are to sure to grow faster",
				"when planted in this soil."
			};
			if (DynamicEarth.includeMud) {
				LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileMud, 1, DynamicEarth.fertileMud.NORMAL), "Rich Mud");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileMud, 1, DynamicEarth.fertileMud.GRASS), "Muddy Fertile Grass");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileMud, 1, DynamicEarth.fertileMud.MYCELIUM), "Muddy Fertile Mycelium");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileMud, 1, DynamicEarth.fertileMud.WET), "Rich Wet Mud");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileMud, 1, DynamicEarth.fertileMud.WET_GRASS), "Sodden Fertile Grass");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.fertileMud, 1, DynamicEarth.fertileMud.WET_MYCELIUM), "Sodden Fertile Mycelium");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.mudBlob, 1, ItemMudBlob.FERTILE), "Rich Mud Blob");
				ItemBlockFertileMud.hintText = new String[] {
					"This mud has everything needed",
					"to make a plant happy. Plants",
					"are to sure to grow faster",
					"when planted in this soil."
				};
			}
		}
		if (DynamicEarth.includeSandySoil) {
			LanguageRegistry.addName(new ItemStack(DynamicEarth.sandySoil, 1, DynamicEarth.sandySoil.DIRT), "Sandy Soil");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.sandySoil, 1, DynamicEarth.sandySoil.GRASS), "Dry Grass");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.sandySoil, 1, DynamicEarth.sandySoil.MYCELIUM), "Dry Mycelium");
			ItemBlockSandySoil.hintText = new String[] {
				"This dirt has a very high sand",
				"content, making it unlikely to",
				"turn into mud."
			};
		}
		if (DynamicEarth.includeGlowingSoil) {
			LanguageRegistry.addName(new ItemStack(DynamicEarth.glowingSoil, 1, DynamicEarth.glowingSoil.DIRT), "Glowsoil");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.glowingSoil, 1, DynamicEarth.glowingSoil.GRASS), "Glowing Grass");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.glowingSoil, 1, DynamicEarth.glowingSoil.MYCELIUM), "Glowing Mycelium");
			ItemBlockGlowingSoil.hintText = new String[] {
				"Dirt infused with glowstone.",
				"Emits light that is slightly",
				"dimmer than a torch."
			};
			if (DynamicEarth.includeMud) {
				LanguageRegistry.addName(new ItemStack(DynamicEarth.glowingMud, 1, DynamicEarth.glowingMud.NORMAL), "Glowmud");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.glowingMud, 1, DynamicEarth.glowingMud.GRASS), "Muddy Glowing Grass");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.glowingMud, 1, DynamicEarth.glowingMud.MYCELIUM), "Muddy Glowing Mycelium");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.glowingMud, 1, DynamicEarth.glowingMud.WET), "Wet Glowmud");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.glowingMud, 1, DynamicEarth.glowingMud.WET_GRASS), "Sodden Glowing Grass");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.glowingMud, 1, DynamicEarth.glowingMud.WET_MYCELIUM), "Sodden Glowing Mycelium");
				LanguageRegistry.addName(new ItemStack(DynamicEarth.mudBlob, 1, ItemMudBlob.GLOWING), "Glowing Mud Blob");
				ItemBlockGlowingMud.hintText = new String[] {
					"Mud infused with glowstone.",
					"Emits light that is slightly",
					"dimmer than a torch."
				};
			}
		}
		if (DynamicEarth.includeBurningSoil) {
			LanguageRegistry.addName(new ItemStack(DynamicEarth.burningSoil, 1, DynamicEarth.burningSoil.DIRT), "Burning Soil");
			LanguageRegistry.addName(new ItemStack(DynamicEarth.burningSoil, 1, DynamicEarth.burningSoil.GRASS), "Nether Grass");
			ItemBlockBurningSoil.dirtHintText = new String[] {
				"Dirt infused with heat. While unable",
				"to grow regular vegetation, it may be",
				"the perfect soil for new strains of",
				"Nether plant life."
			};
			ItemBlockBurningSoil.grassHintText = new String[] {
				"A new strain of grass created by",
				"hybridizing overworld vegetation",
				"with Nether vegetation. It seems",
				"to thrive in hot conditions."
			};
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
