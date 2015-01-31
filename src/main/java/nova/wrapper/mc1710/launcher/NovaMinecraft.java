package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.launchwrapper.Launch;
import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.game.Game;
import nova.core.loader.NovaMod;
import nova.internal.NovaLauncher;
import nova.wrapper.mc1710.asm.NovaMinecraftLoader;
import nova.wrapper.mc1710.asm.NovaMinecraftPreloader;
import nova.wrapper.mc1710.forward.block.BlockWrapperRegistry;
import nova.wrapper.mc1710.forward.item.ItemWrapperRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The main Nova Minecraft Wrapper loader, using Minecraft Forge.
 * @author Calclavia
 */
@Mod(modid = "nova", name = "NOVA")
public class NovaMinecraft {

	@SidedProxy(clientSide = "nova.wrapper.mc1710.launcher.ClientProxy", serverSide = "nova.wrapper.mc1710.launcher.CommonProxy")
	public static CommonProxy proxy;
	private NovaLauncher launcher;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent evt) {

		/**
		 * Search through all classes with @NovaMod
		 */
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();

		Set<Class<?>> modClasses = NovaMinecraftLoader.modClasses;

		proxy.registerResourcePacks(modClasses);
		launcher = new NovaLauncher(diep, modClasses);

		Game.instance = Optional.of(diep.init());

		launcher.preInit();

		launcher.getLoadedMods().forEach(novaMod -> {
			Map<String, String> novaMap = new HashMap<>();
			Launch.blackboard.put("nova:" + novaMod.id(), novaMap);
		});

		proxy.preInit();
		BlockWrapperRegistry.instance.registerBlocks();
		ItemWrapperRegistry.instance.registerItems();
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