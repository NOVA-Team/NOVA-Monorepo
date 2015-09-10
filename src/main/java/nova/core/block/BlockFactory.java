package nova.core.block;

import nova.core.event.BlockEvent;
import nova.core.event.bus.EventListener;
import nova.core.item.ItemBlock;
import nova.core.util.Factory;
import nova.internal.core.Game;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The factory type for blocks.
 * @author Calclavia
 */
public class BlockFactory extends Factory<BlockFactory, Block> {

	private BlockFactory(Supplier<Block> constructor, Function<Block, Block> processor) {
		super(constructor, processor);
	}

	public BlockFactory(Supplier<Block> constructor) {
		this(constructor, evt -> {
			Game.items().register(() -> new ItemBlock(evt.blockFactory));
		});
	}

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by post create.
	 * @param constructor The constructor function
	 * @param postCreate Function for registering item blocks
	 */
	public BlockFactory(Supplier<Block> constructor, EventListener<BlockEvent.Register> postCreate) {
		super(constructor);
		postCreate(postCreate);
	}

	protected void postCreate(EventListener<BlockEvent.Register> postCreate) {
		Game.events().on(BlockEvent.Register.class).bind(postCreate);
	}

	@Override
	public BlockFactory selfConstructor(Supplier<Block> constructor, Function<Block, Block> processor) {
		return new BlockFactory(constructor, processor);
	}
}
