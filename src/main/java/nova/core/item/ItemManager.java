package nova.core.item;

import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.event.EventBus;
import nova.core.event.EventListener;
import nova.core.event.EventListenerHandle;
import nova.core.item.event.ItemIDNotFoundEvent;
import nova.core.util.Manager;
import nova.core.util.Registry;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemManager extends Manager<Item, ItemFactory> {

	private final Supplier<BlockManager> blockManager;

	private final EventBus<ItemIDNotFoundEvent> idNotFoundListeners = new EventBus<>();
	private final EventBus<ItemRegistrationEvent> itemRegistryListeners = new EventBus<>();

	private ItemManager(Registry<ItemFactory> itemRegistry, Supplier<BlockManager> blockManager) {
		super(itemRegistry);
		this.blockManager = blockManager;
	}

	/**
	 * Register a new item with custom constructor arguments.
	 * @param constructor The lambda expression to create a new constructor.
	 * @return Dummy item
	 */
	@Override
	public ItemFactory register(Function<Object[], Item> constructor) {
		return register(new ItemFactory(constructor));
	}

	@Override
	public ItemFactory register(ItemFactory factory) {
		registry.register(factory);
		itemRegistryListeners.publish(new ItemRegistrationEvent(factory));
		return factory;
	}

	public ItemFactory getItemFactoryFromBlock(BlockFactory block) {
		return registry.get(block.getID()).get();
	}

	public ItemFactory getItemFromBlock(BlockFactory block) {
		return getItemFactoryFromBlock(block);
	}

	public Optional<BlockFactory> getBlockFromItem(Item item) {
		return blockManager.get().getFactory(item.getID());
	}

	public Optional<ItemFactory> getItem(String name) {
		if (!registry.contains(name)) {
			ItemIDNotFoundEvent event = new ItemIDNotFoundEvent(name);
			idNotFoundListeners.publish(event);

			if (event.getRemappedFactory() != null) {
				registry.register(event.getRemappedFactory());
			}
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
