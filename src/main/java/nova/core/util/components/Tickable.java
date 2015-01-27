package nova.core.util.components;

/**
 * Implement this on Blocks and Entities which can tick.
 */
public interface Tickable {
	/**
	 * Ticks the object.
	 * @param deltaTime The time since the last update, in seconds.
	 */
	void update(double deltaTime);
}
