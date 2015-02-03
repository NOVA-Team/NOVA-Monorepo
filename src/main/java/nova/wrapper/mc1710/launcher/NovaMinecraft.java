package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.launchwrapper.Launch;
import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.game.Game;
import nova.internal.NovaLauncher;
import nova.wrapper.mc1710.NovaMinecraftPreloader;
import nova.wrapper.mc1710.forward.block.BlockWrapperRegistry;
import nova.wrapper.mc1710.item.ItemWrapperRegistry;
import nova.wrapper.mc1710.network.netty.ChannelHandler;
import nova.wrapper.mc1710.network.netty.MinecraftNetworkManager;
import nova.wrapper.mc1710.network.netty.PacketHandler;
import nova.wrapper.mc1710.recipes.MinecraftRecipeRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The main Nova Minecraft Wrapper loader, using Minecraft Forge.
 * @author Calclavia
 */
@Mod(modid = NovaMinecraft.id, name = NovaMinecraft.name)
public class NovaMinecraft {

	public static final String id = "nova";
	public static final String name = "NOVA";

	@SidedProxy(clientSide = "nova.wrapper.mc1710.launcher.ClientProxy", serverSide = "nova.wrapper.mc1710.launcher.CommonProxy")
	public static CommonProxy proxy;
	public static MinecraftNetworkManager networkManager;
	private static NovaLauncher launcher;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent evt) {

		/**
		 * Search through all classes with @NovaMod
		 */
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();

		Set<Class<?>> modClasses = NovaMinecraftPreloader.modClasses;

		proxy.registerResourcePacks(modClasses);
		launcher = new NovaLauncher(diep, modClasses);

		Game.instance = Optional.of(diep.init());

        BlockWrapperRegistry.instance.registerBlocks();
        ItemWrapperRegistry.instance.registerItems();
        MinecraftRecipeRegistry.instance.registerRecipes();

		launcher.preInit();

		launcher.getLoadedMods().forEach(novaMod -> {
			Map<String, String> novaMap = new HashMap<>();
			Launch.blackboard.put("nova:" + novaMod.id(), novaMap);
		});

		/**
		 * Initiate packet system
		 */
		networkManager = new MinecraftNetworkManager(id, NetworkRegistry.INSTANCE.newChannel(id, new ChannelHandler(), new PacketHandler()));
		
		proxy.preInit();
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

}
