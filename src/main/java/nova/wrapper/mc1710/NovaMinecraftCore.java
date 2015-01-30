package nova.wrapper.mc1710;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.asm.transformers.TerminalTransformer;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@MCVersion(value = "1.7.10")
public class NovaMinecraftCore implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
			"nova.wrapper.mc1710.asm.NovaMinecraftTransformer"
		};
	}

	@Override
	public String getModContainerClass() {
		return "nova.wrapper.mc1710.asm.NovaMinecraftPreloader";
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