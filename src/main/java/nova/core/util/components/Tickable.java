package nova.core.util.components;

/**
 * Implement this on Blocks and Entities which can tick.
 */
public interface Tickable {
	/**
	 * Checks if the Block or Entity is still valid. If false, the object
	 * will be removed from its appropriate ticking list.
	 * @return
	 */
	boolean isValid();

	/**
	 * Ticks the object.
	 * @param deltaTime The time since the last update, in seconds.
	 */
	void update(double deltaTime);
}
