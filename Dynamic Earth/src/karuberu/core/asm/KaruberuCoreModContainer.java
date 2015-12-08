package karuberu.core.asm;

import java.util.Arrays;
import java.util.logging.Logger;

import karuberu.core.KaruberuLogger;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;

public class KaruberuCoreModContainer extends DummyModContainer {
	
	public KaruberuCoreModContainer() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "karuberu-core";
		meta.name = "Karuberu Core";
		meta.version = "1.0.0";
		meta.authorList = Arrays.asList("Karuberu");
		meta.url = "http://www.minecraftforum.net/topic/1533239-";
		meta.description = "Essential tools and hooks for Karuberu's mods";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
}
