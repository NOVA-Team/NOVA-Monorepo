package nova.wrapper.mc1710.forward.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import nova.core.block.Block;
import nova.core.game.Game;
import nova.wrapper.mc1710.forward.item.ItemBlockWrapper;
import nova.wrapper.mc1710.launcher.NovaMinecraft;

import java.util.HashMap;

/**
 * @author Calclavia
 */
public class BlockWrapperUtil {
	/**
	 * A map of all blocks registered
	 */
	public static final HashMap<Block, net.minecraft.block.Block> blockWrapperMap = new HashMap<>();

	/**
	 * Register all Nova blocks
	 */
	public static void registerBlocks() {
		Game.instance.get().blockManager.registry.forEach(b -> {
			BlockWrapper blockWrapper = new BlockWrapper(b);
			blockWrapperMap.put(blockWrapper.block, blockWrapper);
			NovaMinecraft.proxy.registerBlock(blockWrapper);
			GameRegistry.registerBlock(blockWrapper, ItemBlockWrapper.class, b.getID());

			//TODO: Testing purposes:
			blockWrapper.setCreativeTab(CreativeTabs.tabBlock);
			System.out.println("NovaMinecraft: Registered '" + b.getID() + "' block.");
		});

	}
}
