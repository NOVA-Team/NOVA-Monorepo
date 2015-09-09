package nova.core.block;

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

	public BlockFactory(Supplier<Block> constructor) {
		this(constructor, true);
	}

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by calling process() manually.
	 * @param constructor The constructor function
	 * @param generateItemBlock Flag for automatic item block generation.
	 */
	public BlockFactory(Supplier<Block> constructor, boolean generateItemBlock) {
		this(constructor, block -> {
			if (generateItemBlock) {
				Game.items().register(() -> new ItemBlock(block.factory()));
			}
			return block;
		});
	}

	public BlockFactory(Supplier<Block> constructor, Function<Block, Block> processor) {
		super(constructor, processor);
	}

	@Override
	public BlockFactory selfConstructor(Supplier<Block> constructor, Function<Block, Block> processor) {
		return new BlockFactory(constructor,processor);
	}
}
