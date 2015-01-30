package nova.core.block;

import nova.core.util.Registry;

import java.util.Optional;
import nova.core.util.transform.Vector3i;

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
			return Optional.of(blockFactory.get().getDummyBlock());
		} else {
			return Optional.empty();
		}
	}
	
	public void registerBlock(Block block) {
		registry.register(new SimpleBlockFactory(block));
	}

	public Block registerBlock(BlockFactory factory) {
		registry.register(factory);
		return factory.getDummyBlock();
	}
	
	private class SimpleBlockFactory implements BlockFactory {
		
		private final Block block;
		
		private SimpleBlockFactory(Block block) {
			this.block = block;
		}
		
		@Override
		public Block getDummyBlock() {
			return block;
		}

		@Override
		public Block makeBlock(BlockAccess blockAccess, Vector3i position) {
			return block;
		}

		@Override
		public String getID() {
			return block.getID();
		}
	}
}
