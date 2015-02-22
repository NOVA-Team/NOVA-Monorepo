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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.event.EventManager;
import nova.core.game.Game;
import nova.internal.NovaLauncher;
import nova.wrapper.mc1710.NovaMinecraftPreloader;
import nova.wrapper.mc1710.backward.gui.MCGuiFactory;
import nova.wrapper.mc1710.depmodules.GuiModule;
import nova.wrapper.mc1710.depmodules.NetworkModule;
import nova.wrapper.mc1710.depmodules.SaveModule;
import nova.wrapper.mc1710.depmodules.TickerModule;
import nova.wrapper.mc1710.forward.block.BlockWrapperRegistry;
import nova.wrapper.mc1710.item.ItemWrapperRegistry;
import nova.wrapper.mc1710.item.OreDictionaryIntegration;
import nova.wrapper.mc1710.manager.ConfigManager;
import nova.wrapper.mc1710.recipes.MinecraftRecipeRegistry;

import java.io.File;
import java.util.Set;

/**
 * The main Nova Minecraft Wrapper loader, using Minecraft Forge.
 *
 * @author Calclavia
 */
@Mod(modid = NovaMinecraft.id, name = NovaMinecraft.name, version = NovaMinecraftPreloader.version)
public class NovaMinecraft {

	public static final String id = "nova";
	public static final String name = "NOVA";

	@SidedProxy(clientSide = "nova.wrapper.mc1710.launcher.ClientProxy", serverSide = "nova.wrapper.mc1710.launcher.CommonProxy")

	public static CommonProxy proxy;
	@Mod.Instance(id)
	public static NovaMinecraft instance;
	private static NovaLauncher launcher;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent evt) {

		/**
		 * Search through all classes with @NovaMod
		 */
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();
		diep.install(GuiModule.class);
		diep.install(NetworkModule.class);
		diep.install(SaveModule.class);
		diep.install(TickerModule.class);

		Set<Class<?>> modClasses = NovaMinecraftPreloader.modClasses;

		proxy.registerResourcePacks(modClasses);
		launcher = new NovaLauncher(diep, modClasses);

		Game.instance = diep.init();

		/**
		 * Set network manager parameters
		 */
		BlockWrapperRegistry.instance.registerBlocks();
		ItemWrapperRegistry.instance.registerItems();
		OreDictionaryIntegration.instance.registerOreDictionary();
		MinecraftRecipeRegistry.instance.registerRecipes();

		launcher.preInit();

		//Initiate config system
		launcher.getLoadedModMap().forEach((mod, loader) -> {
			Configuration config = new Configuration(new File(evt.getModConfigurationDirectory(), mod.name()));
			ConfigManager.instance.sync(config, loader.getClass().getPackage().getName());
		});

		proxy.preInit();

		/**
		 * Register event handlers
		 */
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
		FMLCommonHandler.instance().bus().register(new FMLEventHandler());
		MinecraftForge.EVENT_BUS.register(Game.instance.saveManager);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new MCGuiFactory.GuiHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent evt) {
		proxy.init();
		launcher.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		proxy.postInit();
		launcher.postInit();
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		Game.instance.eventManager.serverStarting.publish(new EventManager.EmptyEvent());
	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		Game.instance.eventManager.serverStopping.publish(new EventManager.EmptyEvent());
	}

}
