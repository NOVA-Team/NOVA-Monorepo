package nova.wrapper.mc18;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import nova.wrapper.mc18.asm.transformers.Transformers;

import java.util.Map;

@MCVersion(value = "1.7.10")
public class NovaMinecraftCore implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		return new String[] { Transformers.class.getName() };
	}

	@Override
	public String getModContainerClass() {
		return "nova.wrapper.mc18.NovaMinecraftPreloader";
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