package nova.core.block;

import nova.core.event.BlockEvent;
import nova.core.util.Manager;
import nova.core.util.Registry;
import nova.internal.core.Game;

import java.util.function.Supplier;

public class BlockManager extends Manager<Block, BlockFactory> {

	private BlockManager(Registry<BlockFactory> registry) {
		super(registry);
	}

	/**
	 * Gets the block registered that represents air.
	 * @return The air block factory
	 */
	public BlockFactory getAirBlock() {
		return get("air").get();
	}

	/**
	 * Register a new block with custom constructor arguments.
	 * @param constructor Block instance {@link Supplier}
	 * @return Dummy block
	 */
	@Override
	public BlockFactory register(Supplier<Block> constructor) {
		return register(new BlockFactory(constructor));
	}

	/**
	 * Register a new block with custom constructor arguments.
	 * @param factory {@link BlockFactory} of registered block
	 * @return Dummy block
	 */
	@Override
	public BlockFactory register(BlockFactory factory) {
		BlockEvent.Register event = new BlockEvent.Register(factory);
		Game.events().publish(event);
		registry.register(event.blockFactory);
		return event.blockFactory;
	}

}
