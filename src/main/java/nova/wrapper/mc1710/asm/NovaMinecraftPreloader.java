package nova.wrapper.mc1710.asm;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.asm.transformers.TerminalTransformer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;
import nova.wrapper.mc1710.util.NovaResourcePack;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NovaMinecraftPreloader extends DummyModContainer {
	private static final ModMetadata md;

	static {
		md = new ModMetadata();
		md.modId = "novapreloader";
		md.name = "NOVA Preloader";
	}

	public NovaMinecraftPreloader() {
		super(md);
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void fixThings(FMLConstructionEvent event) {
		// Fix Java 8
		LaunchClassLoader loader = (LaunchClassLoader) this.getClass().getClassLoader();
		try {
			Field transformerField = LaunchClassLoader.class.getDeclaredField("transformers");
			transformerField.setAccessible(true);
			List<IClassTransformer> transformerList = (List<IClassTransformer>) transformerField.get(loader);
			Iterator<IClassTransformer> transformerIterator = transformerList.iterator();
			while (transformerIterator.hasNext()) {
				IClassTransformer transformer = transformerIterator.next();
				if (transformer instanceof TerminalTransformer) {
					System.out.println("Java 6 Transformer removed");
					transformerIterator.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		NovaMinecraftLoader.load(event);
	}

}
