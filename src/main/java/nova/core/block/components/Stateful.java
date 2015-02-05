package nova.core.block.components;

/**
 * This interface should be implemented by any block willing to keep a
 * per-block-instance state. This means that once the block instance is
 * created, it will be kept throughout the lifetime of the world.
 *
 * To make sure your block gets saved on world reloads (etc.), implement
 * Storable.
 */
public interface Stateful extends PositionDependent {
	/**
	 * This function will get called upon the very first initialization of
	 * a block instance. There is no guarantee that Stored values will be retained,
	 * and there is also no guarantee that World and Position will exist.
	 */
	default void awake() {

	}

	/**
	 * This function will get called every time a block is loaded. World and position data will be available, including Storable data.
	 */
	default void load() {

	}

	/**
	 * This function will get called before a block instance is destroyed. For
	 * example: removed from the World.
	 */
	default void unload() {

	}
}
