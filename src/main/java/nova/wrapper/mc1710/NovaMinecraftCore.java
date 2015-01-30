package nova.wrapper.mc1710;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import java.util.Map;

@MCVersion(value = "1.7.10")
public class NovaMinecraftCore implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		return null;
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		System.out.println("Hi from coremod!");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}