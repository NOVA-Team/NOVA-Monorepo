package nova.core.item;

import nova.core.block.Block;
import nova.core.block.BlockUtils;
import nova.core.game.Game;
import nova.core.util.Registry;

import java.util.Optional;

public class ItemUtils {
	private ItemUtils() {
	}

	public static Item getItemFromBlock(Block block) {
		return getItemRegistry().get(block.getName());
	}

	public static Block getBlockFromItem(Item item) {
		return BlockUtils.getBlockRegistry().get(item.getName());
	}

	public static Optional<Item> getItem(String name) {
		Registry<Item> r = getItemRegistry();
		return r.contains(name) ? Optional.of(r.get(name)) : Optional.empty();
	}

	public static Registry<Item> getItemRegistry() {
		return (Registry<Item>) Game.instance.getRegistry(Item.class);
	}
}
