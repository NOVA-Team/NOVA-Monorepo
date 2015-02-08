package nova.core.item;

import nova.core.block.Block;
import nova.core.block.BlockManager;
import nova.core.event.EventListener;
import nova.core.event.EventListenerHandle;
import nova.core.event.EventBus;
import nova.core.item.event.ItemIDNotFoundEvent;
import nova.core.util.ReflectionUtils;
import nova.core.util.Registry;
import se.jbee.inject.util.Provider;

import java.util.Optional;
import java.util.function.Supplier;

public class ItemManager {

	public final Registry<ItemFactory> registry;
	private final Supplier<BlockManager> blockManager;

	private final EventBus<ItemIDNotFoundEvent> idNotFoundListeners = new EventBus<>();
	private final EventBus<ItemRegistrationEvent> itemRegistryListeners = new EventBus<>();

	private ItemManager(Registry<ItemFactory> itemRegistry, Supplier<BlockManager> blockManager) {
		this.registry = itemRegistry;
		this.blockManager = blockManager;
	}

	public Item registerItem(Class<? extends Item> item) {
		return registerItem(() -> ReflectionUtils.newInstance(item));
	}

	/**
	 * Register a new item with custom constructor arguments.
	 *
	 * @param constructor The lambda expression to create a new constructor.
	 * @return Dummy item
	 */
	public Item registerItem(Supplier<Item> constructor) {
		return registerItem(new ItemFactory(constructor));
	}

	public Item registerItem(ItemFactory factory) {
		registry.register(factory);

		itemRegistryListeners.publish(new ItemRegistrationEvent(factory));

		return factory.getDummy();
	}

	public Item getItemFromBlock(Block block) {
		return registry.get(block.getID()).get().getDummy();
	}

	public Optional<Block> getBlockFromItem(Item item) {
		return blockManager.get().getBlock(item.getID());
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
		if (!registry.contains(name)) {
			ItemIDNotFoundEvent event = new ItemIDNotFoundEvent(name);
			idNotFoundListeners.publish(event);

			if (event.getRemappedFactory() != null)
				registry.register(event.getRemappedFactory());
		}

		return registry.get(name);
	}

	public EventListenerHandle<ItemIDNotFoundEvent> whenIDNotFound(EventListener<ItemIDNotFoundEvent> listener) {
		return idNotFoundListeners.add(listener);
	}

	public EventListenerHandle<ItemRegistrationEvent> whenItemRegistered(EventListener<ItemRegistrationEvent> listener) {
		return itemRegistryListeners.add(listener);
	}

	public class ItemRegistrationEvent {
		public final ItemFactory itemFactory;

		public ItemRegistrationEvent(ItemFactory itemFactory) {
			this.itemFactory = itemFactory;
		}
	}
}
