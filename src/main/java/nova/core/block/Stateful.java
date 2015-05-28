package nova.core.block;

/**
 * This interface should be implemented by any block willing to keep a
 * per-block-instance state. This means that once the block instance is
 * created, it will be kept throughout the lifetime of the world.
 *
 * To make sure your block gets saved on world reloads (etc.), implement
 * Storable.
 */
public interface Stateful {
	/**
	 * Called when the stateful object loads
	 */
	class LoadEvent {

	}

	/**
	 * Called when the stateful object unloads.
	 */
	class UnloadEvent {

	}
}
