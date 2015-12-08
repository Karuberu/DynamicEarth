package karuberu.core.asm;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;

public class KaruberuCoreModContainer extends DummyModContainer {
	
	public KaruberuCoreModContainer() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "karuberu-core";
		meta.name = "Karuberu Core";
		meta.version = "0.3.0";
		meta.authorList = Arrays.asList("Karuberu");
		meta.url = "http://www.minecraftforum.net/topic/1533239-";
		meta.description = "Core code for Karuberu mods";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
}
