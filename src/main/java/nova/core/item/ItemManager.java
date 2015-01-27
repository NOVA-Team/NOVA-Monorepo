package nova.core.item;

import nova.core.block.Block;
import nova.core.block.BlockManager;
import nova.core.util.Registry;

import java.util.Optional;

import com.google.inject.Inject;

public class ItemManager {
	
	public final Registry<Item> itemRegistry;
	private final BlockManager blockManager;
	
	@Inject
	private ItemManager(Registry<Item> itemRegistry, BlockManager blockManager) {
		this.itemRegistry = itemRegistry;
		this.blockManager = blockManager;
	}

	public  Item getItemFromBlock(Block block) {
		return itemRegistry.get(block.getID()).get();
	}

	public Optional<Block> getBlockFromItem(Item item) {
		return blockManager.getBlock(item.getID());
	}

	public Optional<Item> getItem(String name) {
		return itemRegistry.get(name);
	}
}
