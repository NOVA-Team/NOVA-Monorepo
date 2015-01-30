package nova.core.block;

import nova.core.util.Identifiable;
import nova.core.util.NovaException;
import nova.core.util.transform.Vector3i;

import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * @author Stan Hebben
 */
public class BlockFactory implements Identifiable {

	public static final Field blockAccessField;
	public static final Field posField;

	static {
		try {
			blockAccessField = Block.class.getDeclaredField("blockAccess");
			blockAccessField.setAccessible(true);
			posField = Block.class.getDeclaredField("position");
			posField.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NovaException();
		}
	}

	private final Supplier<Block> constructor;
	private final Block dummyBlock;

	public BlockFactory(Supplier<Block> constructor) {
		this.constructor = constructor;
		this.dummyBlock = constructor.get();
	}

	public Block getDummyBlock() {
		return dummyBlock;
	}

	/**
	 * Creates a new instance of this block with blockAccess and position parameters.
	 * @return A new block instance with these parameters.
	 */
	public Block makeBlock(BlockAccess blockAccess, Vector3i position) {
		Block newBlock = constructor.get();

		try {
			blockAccessField.set(newBlock, blockAccess);
			posField.set(newBlock, position);
		} catch (Exception e) {
			throw new NovaException();
		}

		return newBlock;
	}

	public String getID() {
		return dummyBlock.getID();
	}
}
