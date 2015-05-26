package nova.core.block;

import nova.core.event.CancelableEvent;
import nova.core.event.CancelableEventBus;
import nova.core.event.EventBus;
import nova.core.event.EventListener;
import nova.core.event.EventListenerHandle;
import nova.core.game.Game;
import nova.core.item.ItemBlock;
import nova.core.item.ItemManager;
import nova.core.util.Manager;
import nova.core.util.Registry;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockManager extends Manager<Block, BlockFactory> {

	private final Supplier<ItemManager> itemManager;
	private final EventBus<BlockRegisteredEvent> blockRegisteredListeners = new CancelableEventBus<>();

	private BlockManager(Registry<BlockFactory> registry, Supplier<ItemManager> itemManager) {
		super(registry);
		this.itemManager = itemManager;
	}

	/**
	 * Gets the block registered that represents air.
	 * @return
	 */
	public Block getAirBlock() {
		return Game.instance.blockManager.get("air").get();
	}

	public BlockFactory getAirBlockFactory() {
		return Game.instance.blockManager.getFactory("air").get();
	}

	/**
	 * Register a new block with custom constructor arguments.
	 * @param constructor Block instance {@link Supplier}
	 * @return Dummy block
	 */
	@Override
	public BlockFactory register(Function<Object[], Block> constructor) {
		return register(new BlockFactory(constructor));
	}

	/**
	 * Register a new block with custom constructor arguments.
	 * @param factory {@link BlockFactory} of registered block
	 * @return Dummy block
	 */
	@Override
	public BlockFactory register(BlockFactory factory) {
		registry.register(factory);
		blockRegisteredListeners.publish(new BlockRegisteredEvent(factory));
		itemManager.get().register((args) -> new ItemBlock(factory));
		return factory;
	}

	public EventListenerHandle<BlockRegisteredEvent> whenBlockRegistered(EventListener<BlockRegisteredEvent> listener) {
		return blockRegisteredListeners.add(listener);
	}

	@CancelableEvent.Cancelable
	public static class BlockRegisteredEvent extends CancelableEvent {
		public final BlockFactory blockFactory;

		public BlockRegisteredEvent(BlockFactory blockFactory) {
			this.blockFactory = blockFactory;
		}
	}
}
