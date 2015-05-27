package nova.core.block;

/**
 * This interface should be implemented by any block willing to keep a
 * per-block-instance state. This means that once the block instance is
 * created, it will be kept throughout the lifetime of the world.
 *
 * To make sure your block gets saved on world reloads (etc.), implement
 * Storable.
 */
//TODO: Remove methods. Redundant.
public interface Stateful {
	/**
	 * This function will get called upon the very first initialization/construction of
	 * the Stateful instance. There is no guarantee that Stored values will be retained,
	 * and there is also no guarantee that World and Position will exist.
	 */
	default void awake() {

	}

	/**
	 * This function will get called every time a object is loaded.
	 * For blocks, world and position data will be available, including Storable data.
	 */
	default void load() {

	}

	/**
	 * This function will get called before a object is destroyed. For
	 * example: removed from the World.
	 */
	default void unload() {

	}
}
