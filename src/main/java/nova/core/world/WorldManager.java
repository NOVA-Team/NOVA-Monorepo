package nova.core.world;

import com.google.inject.Inject;

import nova.core.util.Registry;

public class WorldManager {
	
	public final Registry<World> worldRegistry;
	
	@Inject
	private WorldManager(Registry<World> worldRegistry){
		this.worldRegistry = worldRegistry;
	}
}
