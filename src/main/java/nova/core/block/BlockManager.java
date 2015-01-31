package nova.core.block;

import nova.core.game.Game;
import nova.core.item.ItemBlock;
import nova.core.util.NovaException;
import nova.core.util.Registry;

import java.util.Optional;
import java.util.function.Supplier;

public class BlockManager {

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
			return Optional.of(blockFactory.get().getDummy());
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Registers a block with no constructor arguments
	 * @param block Block to register
	 * @return New block instance
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
	 * @param constructor Block instance {@link Supplier}
	 */
	public Block registerBlock(Supplier<Block> constructor) {
		return registerBlock(new BlockFactory(constructor));
	}

	public Block registerBlock(BlockFactory factory) {
		registry.register(factory);
		Game.instance.get().itemManager.registerItem(() -> new ItemBlock(factory.getDummy()));
		return factory.getDummy();
	}

}
