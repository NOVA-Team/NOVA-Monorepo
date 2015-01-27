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
		return getItemRegistry().get(block.getID()).get();
	}

	public static Optional<Block> getBlockFromItem(Item item) {
		return BlockUtils.getBlockRegistry().get(item.getID());
	}

	public static Optional<Item> getItem(String name) {
		return getItemRegistry().get(name);
	}

	public static Registry<Item> getItemRegistry() {
		return Game.instance.getRegistry(Item.class);
	}
}
