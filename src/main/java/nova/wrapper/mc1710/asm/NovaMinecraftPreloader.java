package nova.wrapper.mc1710.asm;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.asm.transformers.TerminalTransformer;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

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
	public void fixTransformers(FMLConstructionEvent event) {
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
	}

}
