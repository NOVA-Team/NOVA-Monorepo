package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.game.Game;
import nova.internal.NovaLauncher;
import nova.internal.tick.UpdateTicker;
import nova.wrapper.mc1710.NovaMinecraftPreloader;
import nova.wrapper.mc1710.depmodules.GuiModule;
import nova.wrapper.mc1710.forward.block.BlockWrapperRegistry;
import nova.wrapper.mc1710.item.ItemWrapperRegistry;
import nova.wrapper.mc1710.item.OreDictionaryIntegration;
import nova.wrapper.mc1710.manager.ConfigManager;
import nova.wrapper.mc1710.manager.MinecraftSaveManager;
import nova.wrapper.mc1710.network.netty.ChannelHandler;
import nova.wrapper.mc1710.network.netty.MinecraftNetworkManager;
import nova.wrapper.mc1710.network.netty.PacketHandler;
import nova.wrapper.mc1710.recipes.MinecraftRecipeRegistry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The main Nova Minecraft Wrapper loader, using Minecraft Forge.
 *
 * @author Calclavia
 */
@Mod(modid = NovaMinecraft.id, name = NovaMinecraft.name)
public class NovaMinecraft {

	public static final String id = "nova";
	public static final String name = "NOVA";

	@SidedProxy(clientSide = "nova.wrapper.mc1710.launcher.ClientProxy", serverSide = "nova.wrapper.mc1710.launcher.CommonProxy")
	public static CommonProxy proxy;
	public static MinecraftNetworkManager networkManager;
	public static MinecraftSaveManager saveManager;
	private static NovaLauncher launcher;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent evt) {

		/**
		 * Search through all classes with @NovaMod
		 */
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();
		diep.install(GuiModule.class);

		Set<Class<?>> modClasses = NovaMinecraftPreloader.modClasses;

		proxy.registerResourcePacks(modClasses);
		launcher = new NovaLauncher(diep, modClasses);

		Game.instance = Optional.of(diep.init());

		BlockWrapperRegistry.instance.registerBlocks();
		ItemWrapperRegistry.instance.registerItems();
		OreDictionaryIntegration.instance.registerOreDictionary();
		MinecraftRecipeRegistry.instance.registerRecipes();

		launcher.preInit();

		launcher.getLoadedMods().forEach(novaMod -> {
			Map<String, String> novaMap = new HashMap<>();
			Launch.blackboard.put("nova:" + novaMod.id(), novaMap);
		});

		/**
		 * Initiate different systems
		 */
		//Initiate network manager
		networkManager = new MinecraftNetworkManager(id, NetworkRegistry.INSTANCE.newChannel(id, new ChannelHandler(), new PacketHandler()));
		//Initiate save manager
		saveManager = new MinecraftSaveManager();
		//Initiate config manager
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
		MinecraftForge.EVENT_BUS.register(saveManager);
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

		/**
		 * Initiated threaded ticker
		 */
		UpdateTicker.ThreadTicker.instance = new UpdateTicker.ThreadTicker(20);
		UpdateTicker.ThreadTicker.instance.start();
	}

}
