package nova.wrapper.mc1710.launcher;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.launchwrapper.Launch;
import nova.core.game.Game;
import nova.core.loader.NovaMod;
import nova.internal.NovaLauncher;
import nova.wrapper.mc1710.forward.block.BlockWrapper;
import org.apache.logging.log4j.core.helpers.Loader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The main Nova Minecraft Wrapper loader, using Minecraft Forge.
 * @author Calclavia
 */
@Mod(modid = "nova", name = "NOVA")
public class NovaMinecraft {

	private NovaLauncher launcher;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent evt) {

		/**
		 * Search through all classes with @NovaMod
		 */
		ASMDataTable asmData = evt.getAsmData();

		launcher = new NovaLauncher(
			asmData.
				getAll(NovaMod.class.getName())
				.stream()
				.map(d -> d.getClassName())
				.map(c -> {
					try {
						return Class.forName(c);
					} catch (ClassNotFoundException e) {
						throw new ExceptionInInitializerError(e);
					}
				})
				.collect(Collectors.toList())
		);

		launcher.preInit();

		launcher.getLoadedMods().forEach(novaMod -> {
			Map<String, String> novaMap = new HashMap<String, String>();

			Launch.blackboard.put("nova:" + novaMod.id(), novaMap);
		});

		registerBlocks();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent evt) {
		launcher.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		launcher.postInit();
	}

	/**
	 * Register all Nova blocks
	 */
	public void registerBlocks() {
		Game.instance.get().blockManager.registry.forEach(b -> {
			BlockWrapper newBlock = new BlockWrapper(b);
			GameRegistry.registerBlock(newBlock, b.getID());
			//TODO: Testing purposes:
			newBlock.setCreativeTab(CreativeTabs.tabBlock);
			System.out.println("NovaMinecraft: Registered '" + b.getID() + "' block.");
		});

	}
}
