package nova.core.item;

import nova.core.block.Block;
import nova.core.block.BlockManager;
import nova.core.event.EventBus;
import nova.core.event.EventListener;
import nova.core.event.EventListenerHandle;
import nova.core.item.event.ItemIDNotFoundEvent;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.Factory;
import nova.core.util.Manager;
import nova.core.util.Registry;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemManager extends Manager<Item> {

	private final Supplier<BlockManager> blockManager;

	private final EventBus<ItemIDNotFoundEvent> idNotFoundListeners = new EventBus<>();
	private final EventBus<ItemRegistrationEvent> itemRegistryListeners = new EventBus<>();

	private ItemManager(Registry<Factory<Item>> itemRegistry, Supplier<BlockManager> blockManager) {
		super(itemRegistry);
		this.blockManager = blockManager;
	}

	@Override
	public Factory<Item> register(Factory<Item> factory) {
		registry.register(factory);
		itemRegistryListeners.publish(new ItemRegistrationEvent(factory));
		return factory;
	}

	public Factory<Item> getItemFactoryFromBlock(Factory<Block> block) {
		return registry.get(block.getID()).get();
	}

	public Factory<Item> getItemFromBlock(Factory<Block> block) {
		return getItemFactoryFromBlock(block);
	}

	public Optional<Factory<Block>> getBlockFromItem(Item item) {
		return blockManager.get().getFactory(item.getID());
	}

	public Optional<Factory<Item>> getItem(String name) {
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
		public final Factory<Item> itemFactory;

		public ItemRegistrationEvent(Factory<Item> itemFactory) {
			this.itemFactory = itemFactory;
		}
	}

	public Item makeItem(Factory<Item> factory, Data data) {
		Item newItem = factory.resolve();
		if (newItem instanceof Storable) {
			((Storable) newItem).load(data);
		}
		return newItem;
	}

	public Data saveItem(Item item) {
		Data data = new Data();
		if (item instanceof Storable) {
			((Storable) item).save(data);
		}
		return data;
	}
}
