package nova.core.block;

import nova.core.event.CancelableEvent;
import nova.core.event.CancelableEventBus;
import nova.core.event.EventBus;
import nova.core.game.GameStatusEventBus;
import nova.core.util.Factory;
import nova.core.item.ItemManager;
import nova.core.util.Manager;
import nova.core.util.RegistrationException;
import nova.core.util.Registry;
import nova.internal.core.Game;

import java.util.function.Supplier;

public class BlockManager extends Manager<Block> {

	public final EventBus<BlockRegisteredEvent> blockRegisteredListeners = new CancelableEventBus<>();
	private final Supplier<ItemManager> itemManager;

	private BlockManager(Registry<Factory<? extends Block>> registry, GameStatusEventBus gseb, Supplier<ItemManager> itemManager) {
		super(registry,gseb, Block.class);
		this.itemManager = itemManager;
	}

	/**
	 * Gets the block registered that represents air.
	 * @return air block
	 */
	public Block getAirBlock() {
		return Game.blocks().make("air").get();  // TODO OreRegistry
	}

	/**
	 * Gets the factory registered for block that represents air.
	 * @return air block's factory.
	 */
	public Factory<? extends Block> getAirBlockFactory() {
		return Game.blocks().getFactory("air").get();
	}

	@Override
	public Factory<? extends Block> beforeRegister(Factory<? extends Block> factory) {
		if (!factory.ID.isPresent()) {
			throw new RegistrationException(String.format("Factory passed for registration is not named. [%s]", factory));
		}
		if (getFactory(factory.getID()).isPresent()) {
			throw new RegistrationException(String.format("This ID is already taken. [%s]", factory));
		}

		BlockRegisteredEvent event = new BlockRegisteredEvent(factory);
		blockRegisteredListeners.publish(event);

		return event.blockFactory;
	}

	@CancelableEvent.Cancelable
	public static class BlockRegisteredEvent extends CancelableEvent {
		public Factory<? extends Block> blockFactory;

		public BlockRegisteredEvent(Factory<? extends Block> blockFactory) {
			this.blockFactory = blockFactory;
		}
	}
}
