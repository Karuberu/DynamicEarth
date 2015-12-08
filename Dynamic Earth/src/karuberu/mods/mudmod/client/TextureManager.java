package karuberu.mods.mudmod.client;

import karuberu.mods.mudmod.CommonProxy;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;

public class TextureManager {
	private static TextureManager
		instance;
	private TextureMap
		blocks,
		items;
	public static final String
		baseDirectory = "mudmod";
	public final static String
		clayGolemTexture = "/mods/" + baseDirectory + "/textures/entity/clayGolem.png";
	public enum Texture {
		MUD("mud"),
		MUDWET("mudwet"),
		ADOBE("adobe"),
		ADOBEWET("adobewet"),
		MUDBRICKBLOCK("mudbrick"),
		PERMAFROST("permafrost"),
		GRASSSLAB("grassslab"),
		GRASSSLABOVERLAY("grassslaboverlay"),
		MYCELIUMSLAB("myceliumslab"),
		GRASSSLABSNOWY("grassslabsnowy"),
		PEATMOSS("peatmoss"),
		PEAT("peat"),
		PEATSIDE1("peatside1"),
		PEATSIDE2("peatside2"),
		PEATSIDE3("peatside3"),
		PEATSIDE4("peatside4"),
		PEATSIDE5("peatside5"),
		PEATSIDE6("peatside6"),
		PEATSIDE7("peatside7"),
		PEATMOSSYOVERLAYTOP("peatmossyoverlaytop"),
		PEATMOSSYOVERLAYSIDE("peatmossyoverlayside"),
		PEATDRY("peatdry"),
		PEATFARMLAND("peatfarmland"),
		PEATFARMLANDWET("peatfarmlandwet"),
		MUDBLOB("mudblob"),
		MUDBRICK("mudbrick"),
		ADOBEDUST("adobedust"),
		ADOBEBLOB("adobeblob"),
		VASERAW("vaseraw"),
		VASE("vase"),
		VASEWATER("vasewater"),
		VASEMILK("vasemilk"),
		VASESEALED("vasesealed"),
		VASECONTENTS("vasecontents"),
		EARTHBOWLRAW("earthbowlraw"),
		EARTHBOWL("earthbowl"),
		EARTHBOWLSOUP("earthbowlsoup"),
		BOMBLIT("bomb"),
		BOMB("bomb"),
		PEATMOSSSPECIMEN("peatmossspecimen"),
		PEATCLUMP("peatclump"),
		PEATBRICK("peatbrick"),
		MILK("milk"),
		SOUP("soup"),
		FERTILESOIL("soilfertile"),
		FERTILEGRASSSIDE("soilfertilegrass"),
		FERTILEMYCELIUMSIDE("soilfertilemycelium"),
		FERTILESNOWSIDE("soilfertilesnow"),
		SANDYSOIL("soilsandy"),
		SANDYGRASSSIDE("soilsandygrass"),
		SANDYMYCELIUMSIDE("soilsandymycelium"),
		SANDYSNOWSIDE("soilsandysnow"),
		DIRTCLOD("dirtclod");
		
		private final String name;
		Texture(String name) {
			this.name = name;
		}
		public String getName() {
			return baseDirectory + ":" + name;
		}
	}
	
	public TextureManager(TextureMap blocks, TextureMap items) {
		this.blocks = blocks;
		this.items = items;
	}
	
	public static TextureManager instance() {
		if (instance == null) {
			instance = new TextureManager(CommonProxy.proxy.getMinecraftClient().renderEngine.textureMapBlocks, CommonProxy.proxy.getMinecraftClient().renderEngine.textureMapItems);
		}
		return instance;
	}
	
	public Icon getBlockTexture(Texture texture) {
		return this.blocks.registerIcon(texture.getName());
	}
	
	public Icon getItemTexture(Texture texture) {
		return this.items.registerIcon(texture.getName());
	}
}
