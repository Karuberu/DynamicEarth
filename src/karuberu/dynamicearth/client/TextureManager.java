package karuberu.dynamicearth.client;

import net.minecraft.util.ResourceLocation;

public class TextureManager {
	public static final String
		dirBase = "dynamicearth:",
		dirBlocks = dirBase + "textures/blocks",
		dirItems = dirBase + "textures/items",
		dirEntities = dirBase + "textures/entities";
	public static final ResourceLocation
		clayGolemTexture = new ResourceLocation(dirEntities + "/clayGolem.png");
	public static enum BlockTexture {
		MUD("mud"),
		MUDWET("mudwet"),
		MUDGRASSSIDE("mudgrass"),
		MUDGRASSSIDEWET("mudwetgrass"),
		MUDMYCELIUMSIDE("mudmycelium"),
		MUDMYCELIUMSIDEWET("mudwetmycelium"),
		MUDSNOWSIDE("mudsnow"),
		MUDSNOWSIDEWET("mudwetsnow"),
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
		FERTILESOIL("soilfertile"),
		FERTILEGRASSSIDE("soilfertilegrass"),
		FERTILEMYCELIUMSIDE("soilfertilemycelium"),
		FERTILESNOWSIDE("soilfertilesnow"),
		SANDYSOIL("soilsandy"),
		SANDYGRASSSIDE("soilsandygrass"),
		SANDYMYCELIUMSIDE("soilsandymycelium"),
		SANDYSNOWSIDE("soilsandysnow"),
		NETHERGRASSTOP("nethergrasstop"),
		NETHERGRASSSIDE("nethergrassside"),
		MILK("milk"),
		SOUP("soup");
		
		private final String name;
		BlockTexture(String name) {
			this.name = name;
		}
		public ResourceLocation getLocation() {
			return new ResourceLocation (dirBlocks, name + ".png");
		}
		public String getIconPath() {
			return dirBase + name;
		}
	}
	public static enum ItemIcon {
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
		DIRTCLOD("dirtclod");
		
		private final String name;
		ItemIcon(String name) {
			this.name = name;
		}
		public ResourceLocation getLocation() {
			return new ResourceLocation (dirItems + "/" + name + ".png");
		}
		public String getIconPath() {
			return dirBase + name;
		}
	}
}
