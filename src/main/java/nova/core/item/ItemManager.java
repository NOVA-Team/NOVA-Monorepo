package nova.core.item;

import nova.core.block.Block;
import nova.core.block.BlockManager;
import nova.core.event.EventBus;
import nova.core.event.EventListener;
import nova.core.event.EventListenerHandle;
import nova.core.game.GameStatusEventBus;
import nova.core.item.event.ItemIDNotFoundEvent;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.Factory;
import nova.core.util.Manager;
import nova.core.util.Registry;

import java.util.Optional;
import java.util.function.Supplier;

public class ItemManager extends Manager<Item> {

	private final Supplier<BlockManager> blockManager;

	private final EventBus<ItemIDNotFoundEvent> idNotFoundListeners = new EventBus<>();
	private final EventBus<ItemRegistrationEvent> itemRegistryListeners = new EventBus<>();

	private ItemManager(Registry<Factory<? extends Item>> itemRegistry, GameStatusEventBus gseb, Supplier<BlockManager> blockManager) {
		super(itemRegistry, gseb, Item.class);
		this.blockManager = blockManager;
	}


	@Override
	public Factory<? extends Item> beforeRegister(Factory<? extends Item> factory) {
		itemRegistryListeners.publish(new ItemRegistrationEvent(factory));
		return factory;
	}

	public Factory<? extends Item> getItemFactoryFromBlock(Factory<? extends Block> block) {
		return getFactory(block.getID()).get();
	}

	public Factory<? extends Item> getItemFromBlock(Factory<? extends Block> block) {
		return getItemFactoryFromBlock(block);
	}

	public Optional<Factory<? extends Block>> getBlockFromItem(Item item) {
		return blockManager.get().getFactory(item.getID());
	}

	public Optional<Factory<? extends Item>> getItem(String name) {
		if (!contains(name)) {
			ItemIDNotFoundEvent event = new ItemIDNotFoundEvent(name);
			idNotFoundListeners.publish(event);

			if (event.getRemappedFactory() != null) {
				register(event.getRemappedFactory());
			}
		}

		return getFactory(name);
	}

	public EventListenerHandle<ItemIDNotFoundEvent> whenIDNotFound(EventListener<ItemIDNotFoundEvent> listener) {
		return idNotFoundListeners.add(listener);
	}

	public EventListenerHandle<ItemRegistrationEvent> whenItemRegistered(EventListener<ItemRegistrationEvent> listener) {
		return itemRegistryListeners.add(listener);
	}

	public class ItemRegistrationEvent {
		public final Factory<? extends Item> itemFactory;

		public ItemRegistrationEvent(Factory<? extends Item> itemFactory) {
			this.itemFactory = itemFactory;
		}
	}

	public Item makeItem(Factory<? extends Item> factory, Data data) {
		Item newItem = factory.make();
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
