package nova.core.block;

import nova.core.util.Registry;

import java.util.Optional;

public class BlockManager {

	public final Registry<BlockBuilder<?>> registry;

	private BlockManager(Registry<BlockBuilder<?>> registry) {
		this.registry = registry;
	}

	public Optional<BlockBuilder<?>> getBlockBuilder(String name) {
		return registry.get(name);
	}

	public Optional<Block> getBlock(String name) {
	 	Optional<BlockBuilder<?>> blockBuilder = getBlockBuilder(name);
		if (blockBuilder.isPresent()) {
			return Optional.of(blockBuilder.get().getDummyBlock());
		} else {
			return Optional.empty();
		}
	}

	public Block registerBlock(BlockBuilder<?> builder) {
		registry.register(builder);
		return builder.getDummyBlock();
	}
}
