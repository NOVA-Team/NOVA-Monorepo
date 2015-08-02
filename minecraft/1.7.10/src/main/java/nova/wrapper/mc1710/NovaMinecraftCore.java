package nova.wrapper.mc1710;

import java.util.Map;

import nova.wrapper.mc1710.asm.transformers.Transformers;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion(value = "1.7.10")
public class NovaMinecraftCore implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		return new String[] { Transformers.class.getName() };
	}

	@Override
	public String getModContainerClass() {
		return "nova.wrapper.mc1710.NovaMinecraftPreloader";
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