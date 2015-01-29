package nova.core.block.components;

import nova.core.block.components.PositionDependent;

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
	 * a block instance. It will NOT be called after loading, for example via
	 * the Storable interface.
	 */
	void initialize();

	/**
	 * This function will get called every time a block is created or reloaded,
	 * including Storable loads.
	 */
	void validate();

	/**
	 * This function will get called before a block instance is destroyed, for
	 * example removed from the World.
	 */
	void invalidate();

	/**
	 * This function should return whether a block instance has ben invalidated
	 * or not.
	 * @return
	 */
	boolean isValid();
}
