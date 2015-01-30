package nova.core.world;

import nova.core.util.Registry;

public class WorldManager {

	public final Registry<World> worldRegistry;

	private WorldManager(Registry<World> worldRegistry) {
		this.worldRegistry = worldRegistry;
	}
}
