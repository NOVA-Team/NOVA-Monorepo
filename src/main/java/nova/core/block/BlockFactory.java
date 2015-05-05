package nova.core.block;

import nova.core.util.Factory;
import nova.core.util.Identifiable;
import nova.core.util.exception.NovaException;
import nova.core.world.Positioned;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * @author Stan Hebben
 */
public class BlockFactory extends Factory<Block> implements Identifiable {

	public static final Field wrapperField;

	static {
		try {
			wrapperField = Positioned.class.getDeclaredField("wrapper");
			wrapperField.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NovaException();
		}
	}

	public BlockFactory(Function<Object[], Block> constructor) {
		super(constructor);
	}

	/**
	 * Creates a new instance of this block with blockAccess and position parameters.
	 *
	 * @param wrapper The block wrapper
	 * @return A new block instance with these parameters.
	 */
	public Block makeBlock(BlockWrapper wrapper, Object... args) {
		Block newBlock = constructor.apply(args);

		try {
			wrapperField.set(newBlock, wrapper);
		} catch (Exception e) {
			throw new NovaException();
		}

		return newBlock;
	}
}
