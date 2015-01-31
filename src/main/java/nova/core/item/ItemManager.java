package nova.core.item;

import nova.core.block.Block;
import nova.core.block.BlockManager;
import nova.core.util.ReflectionUtils;
import nova.core.util.Registry;

import java.util.Optional;
import java.util.function.Supplier;

import se.jbee.inject.util.Provider;

public class ItemManager {

	public final Registry<ItemFactory> registry;
	private final Provider<BlockManager> blockManager;

	private ItemManager(Registry<ItemFactory> itemRegistry, Provider<BlockManager> blockManager) {
		this.registry = itemRegistry;
		this.blockManager = blockManager;
	}

	public Item registerItem(Class<? extends Item> item) {
		return registerItem(() -> ReflectionUtils.newInstance(item));
	}

	/**
	 * Register a new item with custom constructor arguments.
	 * @param constructor The lambda expression to create a new constructor.
	 */
	public Item registerItem(Supplier<Item> constructor) {
		ItemFactory factory = new ItemFactory(constructor);
		registry.register(factory);
		return factory.getDummy();
	}

	public Item getItemFromBlock(Block block) {
		return registry.get(block.getID()).get().getDummy();
	}

	public Optional<Block> getBlockFromItem(Item item) {
		return blockManager.provide().getBlock(item.getID());
	}

	public Optional<Item> getItem(String name) {
		Optional<ItemFactory> factory = getItemFactory(name);
		if (factory.isPresent()) {
			return Optional.of(factory.get().getDummy());
		} else {
			return Optional.empty();
		}
	}

	public Optional<ItemFactory> getItemFactory(String name) {
		return registry.get(name);
	}
}
