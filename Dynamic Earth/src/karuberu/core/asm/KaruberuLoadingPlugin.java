package karuberu.core.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions({"karuberu.core.asm"})
public class KaruberuLoadingPlugin implements IFMLLoadingPlugin {
	
	@Override
	public String[] getLibraryRequestClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "karuberu.core.asm.ClassTransformer" };
	}

	@Override
	public String getModContainerClass() {
		return "karuberu.core.asm.KaruberuCoreModContainer";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

}
