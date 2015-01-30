package nova.core.block;

import nova.core.util.Registry;

import java.util.Optional;
import nova.core.util.transform.Vector3i;
import nova.internal.dummy.BlockAccessDummy;

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

	public void registerBlock(PositionedConstructor constructor) {
		registry.register(new PositionedBlockFactory(constructor));
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
	
	@FunctionalInterface
	public static interface PositionedConstructor {
		public Block construct(BlockAccess blockAccess, Vector3i position);
	}
	
	private class PositionedBlockFactory implements BlockFactory {
		private PositionedConstructor constructor;
		private Block dummyBlock;
		
		public PositionedBlockFactory(PositionedConstructor constructor) {
			this.constructor = constructor;
			dummyBlock = constructor.construct(BlockAccessDummy.INSTANCE, Vector3i.ZERO);
		}
		
		@Override
		public Block getDummyBlock()
		{
			return dummyBlock;
		}

		@Override
		public Block makeBlock(BlockAccess blockAccess, Vector3i position)
		{
			return constructor.construct(blockAccess, position);
		}

		@Override
		public String getID()
		{
			return dummyBlock.getID();
		}
	}
}
