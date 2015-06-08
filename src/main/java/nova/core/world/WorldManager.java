package nova.core.world;

import nova.core.util.Factory;
import nova.core.util.Manager;
import nova.core.util.Registry;
import nova.internal.core.Game;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class WorldManager extends Manager<World, Factory<World>> {

	/**
	 * The set of worlds that currently exist
	 */
	public final Set<World> clientWorlds = new HashSet<>();
	public final Set<World> serverWorlds = new HashSet<>();

	public WorldManager(Registry<Factory<World>> registry) {
		super(registry);
	}

	@Override
	public Factory<World> register(Function<Object[], World> constructor) {
		return register(new Factory<>(constructor));
	}

	public Set<World> sidedWorlds() {
		if (Game.network().isClient()) {
			return clientWorlds;
		}
		return serverWorlds;
	}

	public Optional<World> findWorld(String id) {
		return sidedWorlds()
			.stream()
			.filter(world -> id.equals(world.getID()))
			.findFirst();
	}
}
