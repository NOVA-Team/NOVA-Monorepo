package nova.core.block;

import nova.core.util.Registry;

import java.util.Optional;

import com.google.inject.Inject;

public class BlockManager {
	
	public final Registry<Block> registry;
	
	@Inject
	private BlockManager(Registry<Block> registry) {
		this.registry = registry;
	}

	public Optional<Block> getBlock(String name) {
		return registry.get(name);
	}
}
