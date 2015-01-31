package nova.wrapper.mc1710.forward.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import nova.core.game.Game;
import nova.core.item.Item;
import nova.wrapper.mc1710.launcher.NovaMinecraft;

import java.util.HashMap;

/**
 * @author Calclavia
 */
public class ItemWrapperRegistry {

	public static final ItemWrapperRegistry instance = new ItemWrapperRegistry();

	/**
	 * A map of all blocks registered
	 */
	private final HashMap<Item, net.minecraft.item.Item> wrapperMap = new HashMap<>();

	public net.minecraft.item.Item get(Item item) {
		return wrapperMap.get(item);
	}

	/**
	 * Register all Nova blocks
	 */
	public void registerItems() {
		Game.instance.get().itemManager.registry.forEach(itemFactory -> {
			ItemWrapper itemWrapper = new ItemWrapper(itemFactory);
			wrapperMap.put(itemFactory.getDummy(), itemWrapper);
			NovaMinecraft.proxy.registerItem(itemWrapper);
			GameRegistry.registerItem(itemWrapper, itemFactory.getID());

			//TODO: Testing purposes:
			itemWrapper.setCreativeTab(CreativeTabs.tabBlock);
			System.out.println("[NOVA]: Registered '" + itemFactory.getID() + "' item.");
		});
	}
}
