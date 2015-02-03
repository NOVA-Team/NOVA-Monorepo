package nova.core.item;

import nova.core.block.Block;
import nova.core.block.BlockManager;
import nova.core.event.EventListener;
import nova.core.event.EventListenerHandle;
import nova.core.event.EventListenerList;
import nova.core.item.event.ItemIDNotFoundEvent;
import nova.core.util.ReflectionUtils;
import nova.core.util.Registry;

import java.util.Optional;
import java.util.function.Supplier;

import se.jbee.inject.util.Provider;

public class ItemManager {

	public final Registry<ItemFactory> registry;
	private final Provider<BlockManager> blockManager;

    private final EventListenerList<ItemIDNotFoundEvent> idNotFoundListeners = new EventListenerList<>();
    private final EventListenerList<ItemRegistrationEvent> itemRegistryListeners = new EventListenerList<>();

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
	 * @return Dummy item
	 */
	public Item registerItem(Supplier<Item> constructor) {
		ItemFactory factory = new ItemFactory(constructor);
		registry.register(factory);

        itemRegistryListeners.publish(new ItemRegistrationEvent(factory));

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
