package nova.core.block;

import nova.core.item.ItemBlock;
import nova.core.util.Factory;
import nova.core.util.Identifiable;
import nova.internal.core.Game;

import java.util.function.Supplier;

/**
 * The factory type for blocks.
 * @author Calclavia
 */
public class BlockFactory extends Factory<Block> implements Identifiable {

	public BlockFactory(Supplier<Block> constructor) {
		this(constructor, true);
	}

	public BlockFactory(Supplier<Block> constructor, boolean generateItemBlock) {
		super(constructor);

		if (generateItemBlock) {
			process(block -> {
				Game.items().register(() -> new ItemBlock(this));
				return block;
			});
		}
	}
}
