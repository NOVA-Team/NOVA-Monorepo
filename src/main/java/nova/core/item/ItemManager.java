package nova.core.item;

import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.event.bus.CancelableEvent;
import nova.core.item.event.ItemIDNotFoundEvent;
import nova.core.util.Manager;
import nova.core.util.Registry;
import nova.internal.core.Game;

import java.util.Optional;
import java.util.function.Supplier;

public class ItemManager extends Manager<Item, ItemFactory> {

	private final Supplier<BlockManager> blockManager;

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
	public ItemFactory register(Supplier<Item> constructor) {
		return register(new ItemFactory(constructor));
	}

	@Override
	public ItemFactory register(ItemFactory factory) {
		registry.register(factory);
		Game.events().publish(new ItemRegistrationEvent(factory));
		return factory;
	}

	public ItemFactory getItemFromBlock(BlockFactory block) {
		return registry.get(block.getID()).get();
	}

	public Optional<BlockFactory> getBlockFromItem(Item item) {
		return blockManager.get().get(item.getID());
	}

	@Override
	public Optional<ItemFactory> get(String name) {
		if (!registry.contains(name)) {
			ItemIDNotFoundEvent event = new ItemIDNotFoundEvent(name);
			Game.events().publish(event);

			if (event.getRemappedFactory() != null) {
				registry.register(event.getRemappedFactory());
			}
		}

		return registry.get(name);
	}

	//TODO: Move to item event
	public class ItemRegistrationEvent extends CancelableEvent {
		public final ItemFactory itemFactory;

		public ItemRegistrationEvent(ItemFactory itemFactory) {
			this.itemFactory = itemFactory;
		}
	}
}
