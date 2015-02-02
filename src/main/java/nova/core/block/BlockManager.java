package nova.core.block;

import java.util.Optional;
import java.util.function.Supplier;

import nova.core.item.ItemBlock;
import nova.core.item.ItemManager;
import nova.core.util.NovaException;
import nova.core.util.Registry;

import se.jbee.inject.util.Provider;

public class BlockManager {

	public final Registry<BlockFactory> registry;
	private final Provider<ItemManager> itemManager;

	private BlockManager(Registry<BlockFactory> registry, Provider<ItemManager> itemManager) {
		this.registry = registry;
		this.itemManager = itemManager;
	}

	public Optional<BlockFactory> getBlockFactory(String name) {
		return registry.get(name);
	}

	public Optional<Block> getBlock(String name) {
		Optional<BlockFactory> blockFactory = getBlockFactory(name);
		if (blockFactory.isPresent()) {
			return Optional.of(blockFactory.get().getDummy());
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Registers a block with no constructor arguments
	 * 
	 * @param block
	 *            Block to register
	 * @return New block instance
	 */
	public Block registerBlock(Class<? extends Block> block) {
		return registerBlock(new BlockFactory(() -> {
			try {
				return block.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				throw new NovaException();
			}
		}));
	}

	/**
	 * Register a new block with custom constructor arguments.
	 * 
	 * @param constructor
	 *            Block instance {@link Supplier}
	 * @return Dummy block
	 */
	public Block registerBlock(Supplier<Block> constructor) {
		return registerBlock(new BlockFactory(constructor));
	}

	/**
	 * Register a new block with custom constructor arguments.
	 * @param factory {@link BlockFactory} of registered block
	 * @return Dummy block
	 */
	public Block registerBlock(BlockFactory factory) {
		registry.register(factory);
		itemManager.provide().registerItem(() -> new ItemBlock(factory.getDummy()));
		return factory.getDummy();
	}

}
