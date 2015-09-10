package nova.core.world;

import nova.core.util.Factory;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Factory for creating worlds.
 * @author Calclavia
 */
public class WorldFactory extends Factory<WorldFactory, World> {

	public WorldFactory(Supplier<World> constructor, Function<World, World> processor) {
		super(constructor, processor);
	}

	public WorldFactory(Supplier<World> constructor) {
		super(constructor);
	}

	@Override
	public WorldFactory selfConstructor(Supplier<World> constructor, Function<World, World> processor) {
		return new WorldFactory(constructor, processor);
	}
}
