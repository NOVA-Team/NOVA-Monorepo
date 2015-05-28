package nova.core.component;

/**
 * Implement this on Blocks and Entities which can tick.
 */
public interface Updater {
	/**
	 * Ticks the object.
	 * @param deltaTime The time since the last update, in seconds.
	 */
	default void update(double deltaTime) {
		/**
		 * Update components
		 */
		if (this instanceof ComponentProvider) {
			((ComponentProvider) this).components()
				.stream()
				.filter(component -> component instanceof Updater)
				.forEach(component -> ((Updater) component).update(deltaTime));
		}
	}
}
