package nova.wrapper.mc18.launcher;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import nova.core.component.ComponentProvider;
import nova.core.deps.MavenDependency;
import nova.core.event.GlobalEvents;
import nova.core.loader.Loadable;
import nova.core.loader.NativeLoader;
import nova.internal.core.Game;
import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import nova.internal.core.deps.DepDownloader;
import nova.internal.core.launch.InitializationException;
import nova.internal.core.launch.ModLoader;
import nova.internal.core.launch.NovaLauncher;
import nova.wrapper.mc18.NovaMinecraftPreloader;
import nova.wrapper.mc18.depmodules.ClientModule;
import nova.wrapper.mc18.depmodules.GameInfoModule;
import nova.wrapper.mc18.depmodules.GuiModule;
import nova.wrapper.mc18.depmodules.KeyModule;
import nova.wrapper.mc18.depmodules.LanguageModule;
import nova.wrapper.mc18.depmodules.NetworkModule;
import nova.wrapper.mc18.depmodules.RenderModule;
import nova.wrapper.mc18.depmodules.SaveModule;
import nova.wrapper.mc18.depmodules.TickerModule;
import nova.wrapper.mc18.depmodules.WrapperEventModule;
import nova.wrapper.mc18.recipes.MinecraftRecipeRegistry;
import nova.wrapper.mc18.util.WrapperEventManager;
import nova.wrapper.mc18.wrapper.block.BlockConverter;
import nova.wrapper.mc18.wrapper.block.world.WorldConverter;
import nova.wrapper.mc18.wrapper.cuboid.CuboidConverter;
import nova.wrapper.mc18.wrapper.data.DataWrapper;
import nova.wrapper.mc18.wrapper.entity.EntityConverter;
import nova.wrapper.mc18.wrapper.entity.forward.MCRigidBody;
import nova.wrapper.mc18.wrapper.gui.MCGuiFactory;
import nova.wrapper.mc18.wrapper.inventory.InventoryConverter;
import nova.wrapper.mc18.wrapper.item.ItemConverter;
import nova.wrapper.mc18.wrapper.item.OreDictionaryIntegration;
import se.jbee.inject.Dependency;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The main Nova Minecraft Wrapper loader, using Minecraft Forge.
 * @author Calclavia
 */
@Mod(modid = NovaMinecraft.id, name = NovaMinecraft.name, version = NovaMinecraftPreloader.version)
public class NovaMinecraft {

	public static final String id = "nova";
	public static final String name = "NOVA";
	public static final String mcId = "minecraft";

	@SidedProxy(clientSide = "nova.wrapper.mc18.launcher.ClientProxy", serverSide = "nova.wrapper.mc18.launcher.CommonProxy")
	public static CommonProxy proxy;
	@Mod.Instance(id)
	public static NovaMinecraft instance;
	private static NovaLauncher launcher;

	private static ModLoader<NativeLoader> nativeLoader;
	private static Set<Loadable> nativeConverters;

	public static WrapperEventManager eventManager;

	/**
	 * ORDER OF LOADING.
	 *
	 * 1. Native Loaders 2. Native Converters 3. Mods
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		try {
			/**
			 * Search through all classes with @NovaMod
			 */
			DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();
			diep.install(GuiModule.class);
			diep.install(NetworkModule.class);
			diep.install(SaveModule.class);
			diep.install(TickerModule.class);
			diep.install(LanguageModule.class);
			diep.install(KeyModule.class);
			diep.install(ClientModule.class);
			diep.install(GameInfoModule.class);
			diep.install(RenderModule.class);
			diep.install(WrapperEventModule.class);

			Set<Class<?>> modClasses = NovaMinecraftPreloader.modClasses;

			proxy.registerResourcePacks(modClasses);
			launcher = new NovaLauncher(diep, modClasses);

			Game.inject(diep);

			//Inject eventManager
			eventManager = diep.getInjector().get().resolve(Dependency.dependency(WrapperEventManager.class));

			/**
			 * Register native converters
			 */
			Game.natives().registerConverter(new DataWrapper());
			Game.natives().registerConverter(new EntityConverter());
			Game.natives().registerConverter(new BlockConverter());
			Game.natives().registerConverter(new ItemConverter());
			Game.natives().registerConverter(new WorldConverter());
			Game.natives().registerConverter(new CuboidConverter());
			Game.natives().registerConverter(new InventoryConverter());

			/**
			 * Initiate recipe and ore dictionary integration
			 */
			OreDictionaryIntegration.instance.registerOreDictionary();
			MinecraftRecipeRegistry.instance.registerRecipes();

			/**
			 * Set up components
			 */
			Game.components().register(args -> args.length > 0 ? new MCRigidBody((ComponentProvider) args[0]) : new MCRigidBody(null));

			/**
			 * Download dependencies
			 */
			launcher.generateDependencies();

			try {
				for (List<MavenDependency> dependencies : launcher.getNeededDeps().values()) {
					for (MavenDependency dep : dependencies) {
						DepDownloader.downloadDepdency(dep.getDownloadURL(), FMLInjectionData.data()[6] + "/mods/" + dep.getPath());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			launcher.load();

			/**
			 * Instantiate native loaders
			 */
			nativeLoader = new ModLoader<>(NativeLoader.class, diep,
				evt.getAsmData()
					.getAll(NativeLoader.class.getName())
					.stream()
					.map(d -> d.getClassName())
					.map(c -> {
						try {
							return Class.forName(c);
						} catch (ClassNotFoundException e) {
							throw new ExceptionInInitializerError(e);
						}
					})
					.filter(c -> mcId.equals(c.getAnnotation(NativeLoader.class).forGame()))
					.collect(Collectors.toSet())
			);

			nativeLoader.load();

			nativeLoader.preInit();
			nativeConverters = Game.natives().getNativeConverters().stream().filter(n -> n instanceof Loadable).map(n -> (Loadable) n).collect(Collectors.toSet());
			nativeConverters.stream().forEachOrdered(Loadable::preInit);
			launcher.preInit();

			// Initiate config system TODO: Storables
			//		launcher.getLoadedModMap().forEach((mod, loader) -> {
			//			Configuration config = new Configuration(new File(evt.getModConfigurationDirectory(), mod.name()));
			//			ConfigManager.instance.sync(config, loader.getClass().getPackage().getName());
			//		});

			proxy.preInit();

			/**
			 * Register event handlers
			 */
			MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
			FMLCommonHandler.instance().bus().register(new FMLEventHandler());
			MinecraftForge.EVENT_BUS.register(Game.retention());

			NetworkRegistry.INSTANCE.registerGuiHandler(this, new MCGuiFactory.GuiHandler());
		} catch (Exception e) {
			System.out.println("Error during preInit");
			e.printStackTrace();
			throw new InitializationException(e);
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent evt) {
		try {

			proxy.init();
			nativeLoader.init();
			nativeConverters.stream().forEachOrdered(Loadable::init);
			launcher.init();
		} catch (Exception e) {
			System.out.println("Error during init");
			e.printStackTrace();
			throw new InitializationException(e);
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		try {
			proxy.postInit();
			nativeLoader.postInit();
			nativeConverters.stream().forEachOrdered(Loadable::postInit);
			launcher.postInit();
		} catch (Exception e) {
			System.out.println("Error during postInit");
			e.printStackTrace();
			throw new InitializationException(e);
		}
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		Game.events().events.publish(new GlobalEvents.ServerStartingEvent());
	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		Game.events().events.publish(new GlobalEvents.ServerStoppingEvent());
	}

}
