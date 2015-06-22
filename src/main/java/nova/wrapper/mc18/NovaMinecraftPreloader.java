package nova.wrapper.mc18;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.relauncher.Side;
import nova.core.loader.NovaMod;
import nova.wrapper.mc18.render.NovaFolderResourcePack;
import nova.wrapper.mc18.render.NovaResourcePack;
import nova.wrapper.mc18.util.ReflectionUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NovaMinecraftPreloader extends DummyModContainer {
	public static final String version = "0.0.1";
	private static final ModMetadata md;
	public static Set<Class<?>> modClasses;

	static {
		md = new ModMetadata();
		md.modId = "novapreloader";
		md.name = "NOVA Preloader";
		md.version = version;
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
	public void load(FMLConstructionEvent event) {
		// Scan mod classes
		ASMDataTable asmData = event.getASMHarvestedData();

		modClasses = asmData
			.getAll(NovaMod.class.getName())
			.stream()
			.map(d -> d.getClassName())
			.map(c -> {
				try {
					return Class.forName(c);
				} catch (ClassNotFoundException e) {
					throw new ExceptionInInitializerError(e);
				}
			})
			.collect(Collectors.toSet());

		//Inject fake mod containers into FML
		List<ModContainer> fmlMods = ReflectionUtil.getPrivateObject(Loader.instance(), "mods");
		List<ModContainer> newMods = new ArrayList<>();
		newMods.addAll(fmlMods);
		modClasses.forEach(mod -> {
			ModMetadata fakeMeta = new ModMetadata();
			NovaMod annotation = mod.getAnnotation(NovaMod.class);
			if (!annotation.isPlugin()) {
				fakeMeta.modId = annotation.id();
				fakeMeta.name = annotation.name();
				fakeMeta.version = annotation.version();
				fakeMeta.description = annotation.description();
				newMods.add(new DummyNovaMod(fakeMeta));
			}
		});
		ReflectionUtil.setPrivateObject(Loader.instance(), newMods, "mods");

		// Register resource packs
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			registerResourcePacks();
		}
	}

	public void registerResourcePacks() {
		Map<NovaMod, Class<?>> classesMap = modClasses
			.stream()
			.filter(clazz -> clazz.getAnnotation(NovaMod.class) != null)
			.collect(Collectors.toMap((clazz) -> clazz.getAnnotation(NovaMod.class), Function.identity()));

		try {
			// The same list exists in the Minecraft class, but that can be SRG or not.
			// Reflecting FML is just less work for us. (Minecraft.field_110449_ao)
			Field resourcePackField = FMLClientHandler.class.getDeclaredField("resourcePackList");
			resourcePackField.setAccessible(true);
			List<IResourcePack> packs = (List<IResourcePack>) resourcePackField.get(FMLClientHandler.instance());

			Set<String> addedPacks = new HashSet<>();

			classesMap.keySet().forEach(novaMod -> {
				Class<?> c = classesMap.get(novaMod);

				//Add jar resource pack
				String fn = c.getProtectionDomain().getCodeSource().getLocation().getPath();

				if (fn.contains("!")) {
					fn = fn.substring(0, fn.indexOf('!')).replaceFirst("file:", "");
					if (fn.startsWith("/")) {
						fn = fn.substring(1);
					}
					if (!addedPacks.contains(fn)) {
						addedPacks.add(fn);
						packs.add(new NovaResourcePack(new File(fn), novaMod.id(), novaMod.domains()));
						System.out.println("Registered NOVA jar resource pack: " + fn);
					}
				} else {
					//Add folder resource pack location. The folderLocation is the root of the project, including the packages of classes, and an assets folder inside.
					String folderLocation = c.getProtectionDomain().getCodeSource().getLocation().getPath();
					String classPath = c.getCanonicalName().replaceAll("\\.", "/");
					folderLocation = folderLocation.replaceFirst("file:", "").replace(classPath, "").replace("/.class", "").replaceAll("%20", " ");
					File folderFile = new File(folderLocation);
					if (!new File(folderFile, "assets").isDirectory()) {
						//Try IDEA workaround.
						folderFile = new File(folderLocation.replaceAll("build[\\\\/]classes", "build/resources"));
						folderFile = new File(folderFile, "assets").isDirectory() ? folderFile : new File(folderLocation);
					}

					addedPacks.add(folderLocation);
					packs.add(new NovaFolderResourcePack(folderFile, novaMod.id(), novaMod.domains()));
					System.out.println("Registered NOVA folder resource pack: " + folderFile.getAbsolutePath());
				}
			});
			resourcePackField.set(FMLClientHandler.instance(), packs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * A fake NovaMod to inject into FML.
	 */
	private static class DummyNovaMod extends DummyModContainer {
		public DummyNovaMod(ModMetadata meta) {
			super(meta);
		}
	}
}
