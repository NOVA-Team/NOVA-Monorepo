package nova.core.wrapper.mc17;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import nova.core.wrapper.mc17.asm.transformers.Transformers;

import java.util.Map;

@MCVersion(value = "1.7.10")
public class NovaMinecraftCore implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		return new String[] { Transformers.class.getName() };
	}

	@Override
	public String getModContainerClass() {
		return "nova.core.wrapper.mc17.NovaMinecraftPreloader";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}