package nova.core.block;

import nova.core.util.Factory;
import nova.core.util.Identifiable;
import nova.internal.dummy.Wrapper;

import java.util.function.Function;

/**
 * @author Stan Hebben
 */
public class BlockFactory extends Factory<Block> implements Identifiable {

	public BlockFactory(Function<Object[], Block> constructor) {
		super(constructor);
	}

	/**
	 * Creates a new instance of this block with blockAccess and position parameters.
	 * @param wrapper The block wrapper
	 * @return A new block instance with these parameters.
	 */
	public Block makeBlock(Wrapper wrapper, Object... args) {
		Block newBlock = constructor.apply(args);
		newBlock.add(wrapper);
		return newBlock;
	}
}
