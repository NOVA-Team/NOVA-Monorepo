package nova.core.event;

import nova.core.event.bus.Event;
import nova.core.world.World;

public abstract class WorldEvent extends Event {
	public final World world;

	public WorldEvent(World world) {
		this.world = world;
	}

	/**
	 * Called when a world loads.
	 */
	public static class Load extends WorldEvent {
		public Load(World world) {
			super(world);
		}
	}

	/**
	 * Called when a world unloads.
	 */
	public static class Unload extends WorldEvent {
		public Unload(World world) {
			super(world);
		}
	}
}