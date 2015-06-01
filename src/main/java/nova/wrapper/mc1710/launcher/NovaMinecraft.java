package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraftforge.common.MinecraftForge;
import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.component.ComponentProvider;
import nova.core.deps.DepDownloader;
import nova.core.deps.MavenDependency;
import nova.core.event.Event;
import nova.core.event.GlobalEvents;
import nova.core.game.Game;
import nova.core.loader.Loadable;
import nova.core.loader.NativeLoader;
import nova.core.util.exception.NovaException;
import nova.internal.launch.ModLoader;
import nova.internal.launch.NovaLauncher;
import nova.wrapper.mc1710.NovaMinecraftPreloader;
import nova.wrapper.mc1710.backward.gui.MCGuiFactory;
import nova.wrapper.mc1710.depmodules.ClientModule;
import nova.wrapper.mc1710.depmodules.GuiModule;
import nova.wrapper.mc1710.depmodules.KeyModule;
import nova.wrapper.mc1710.depmodules.LanguageModule;
import nova.wrapper.mc1710.depmodules.NetworkModule;
import nova.wrapper.mc1710.depmodules.RenderModule;
import nova.wrapper.mc1710.depmodules.SaveModule;
import nova.wrapper.mc1710.depmodules.TickerModule;
import nova.wrapper.mc1710.recipes.MinecraftRecipeRegistry;
import nova.wrapper.mc1710.wrapper.block.BlockConverter;
import nova.wrapper.mc1710.wrapper.block.world.WorldConverter;
import nova.wrapper.mc1710.wrapper.cuboid.CuboidConverter;
import nova.wrapper.mc1710.wrapper.data.DataWrapper;
import nova.wrapper.mc1710.wrapper.entity.EntityConverter;
import nova.wrapper.mc1710.wrapper.entity.forward.MCEntityTransform;
import nova.wrapper.mc1710.wrapper.entity.forward.MCRigidBody;
import nova.wrapper.mc1710.wrapper.item.ItemConverter;
import nova.wrapper.mc1710.wrapper.item.OreDictionaryIntegration;

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

	@SidedProxy(clientSide = "nova.wrapper.mc1710.launcher.ClientProxy", serverSide = "nova.wrapper.mc1710.launcher.CommonProxy")
	public static CommonProxy proxy;
	@Mod.Instance(id)
	public static NovaMinecraft instance;
	private static NovaLauncher launcher;

	private static ModLoader<NativeLoader> nativeLoader;
	private static Set<Loadable> nativeConverters;

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
			diep.install(RenderModule.class);

			Set<Class<?>> modClasses = NovaMinecraftPreloader.modClasses;

			proxy.registerResourcePacks(modClasses);
			launcher = new NovaLauncher(diep, modClasses);

			Game.inject(diep.init());

			/**
			 * Register native converters
			 */
			Game.nativeManager().registerConverter(new DataWrapper());
			Game.nativeManager().registerConverter(new EntityConverter());
			Game.nativeManager().registerConverter(new BlockConverter());
			Game.nativeManager().registerConverter(new ItemConverter());
			Game.nativeManager().registerConverter(new WorldConverter());
			Game.nativeManager().registerConverter(new CuboidConverter());

			/**
			 * Initiate recipe and ore dictionary integration
			 */
			OreDictionaryIntegration.instance.registerOreDictionary();
			MinecraftRecipeRegistry.instance.registerRecipes();

			/**
			 * Set up components
			 */
			Game.componentManager().register(args -> args.length > 0 ? new MCRigidBody((ComponentProvider) args[0]) : new MCRigidBody(null));
			Game.componentManager().register(args -> args.length > 0 ? new MCEntityTransform((ComponentProvider) args[0]) : new MCEntityTransform(null));

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
			nativeConverters = Game.nativeManager().getNativeConverters().stream().filter(n -> n instanceof Loadable).map(n -> (Loadable) n).collect(Collectors.toSet());
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
			MinecraftForge.EVENT_BUS.register(Game.saveManager());

			NetworkRegistry.INSTANCE.registerGuiHandler(this, new MCGuiFactory.GuiHandler());
		} catch (Exception e) {
			System.out.println("Error during preInit");
			e.printStackTrace();
			throw new NovaException(e);
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
			throw new NovaException(e);
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
			throw new NovaException(e);
		}
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		Game.eventManager().serverStarting.publish(new Event());
	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		Game.eventManager().serverStopping.publish(new Event());
	}

}
