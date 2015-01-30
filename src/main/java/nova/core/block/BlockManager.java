package nova.core.block;

import nova.core.util.NovaException;
import nova.core.util.Registry;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Supplier;

public class BlockManager {

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

	public final Registry<BlockFactory> registry;

	private BlockManager(Registry<BlockFactory> registry) {
		this.registry = registry;
	}

	public Optional<BlockFactory> getBlockFactory(String name) {
		return registry.get(name);
	}

	public Optional<Block> getBlock(String name) {
		Optional<BlockFactory> blockFactory = getBlockFactory(name);
		if (blockFactory.isPresent()) {
			return Optional.of(blockFactory.get().getDummyBlock());
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Registers a new block that will not receive blockAccess or position values.
	 */
	public Block registerBlock(Block block) {
		return registerBlock(new BlockFactory(() -> block));
	}

	/**
	 * Registers a block with no constructor arguments
	 */
	public Block registerBlock(Class<? extends Block> block) {
		return registerBlock(new BlockFactory(
			() -> {
				try {
					return block.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
					throw new NovaException();
				}
			}
		));
	}

	/**
	 * Register a new block with custom constructor arguments.
	 */
	public void registerBlock(Supplier<Block> constructor) {
		registry.register(new BlockFactory(constructor));
	}

	public Block registerBlock(BlockFactory factory) {
		registry.register(factory);
		return factory.getDummyBlock();
	}

}
