package nova.core.world;

import nova.core.event.WorldEvent;
import nova.core.event.bus.GlobalEvents;
import nova.core.util.Manager;
import nova.core.util.Registry;
import nova.internal.core.Game;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class WorldManager extends Manager<World, WorldFactory> {

	/**
	 * The set of worlds that currently exist
	 */
	public final Set<World> clientWorlds = new HashSet<>();
	public final Set<World> serverWorlds = new HashSet<>();

	public WorldManager(Registry<WorldFactory> registry, GlobalEvents events) {
		super(registry);

		//Bind events
		events.on(WorldEvent.Load.class).bind(evt -> sidedWorlds().add(evt.world));
		events.on(WorldEvent.Unload.class).bind(evt -> sidedWorlds().remove(evt.world));
	}

	@Override
	public WorldFactory register(Supplier<World> constructor) {
		return register(new WorldFactory(constructor));
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
