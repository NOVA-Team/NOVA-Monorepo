package nova.core.world;

import nova.internal.core.Game;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class WorldManager {

	/**
	 * The set of worlds that currently exist
	 */
	public final Set<World> clientWorlds = new HashSet<>();
	public final Set<World> serverWorlds = new HashSet<>();

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
