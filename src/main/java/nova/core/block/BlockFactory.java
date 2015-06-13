package nova.core.block;

import nova.core.util.Factory;
import nova.core.util.Identifiable;

import java.util.function.Function;

/**
 * @author Stan Hebben
 */
public class BlockFactory extends Factory<Block> implements Identifiable {

	public BlockFactory(Function<Object[], Block> constructor) {
		super(constructor);
	}

	/**
	 * Creates a new instance of this block withPriority blockAccess and position parameters.
	 * @return A new block instance withPriority these parameters.
	 */
	public Block makeBlock(Object... args) {
		Block newBlock = constructor.apply(args);
		return newBlock;
	}
}
