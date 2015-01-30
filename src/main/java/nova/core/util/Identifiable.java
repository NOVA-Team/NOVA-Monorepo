package nova.core.util;

/**
 * A generic interface signifying that this object is identifiable
 * by an ID
 */
public interface Identifiable {
	/**
	 * Get the ID to identify this object by
	 *
	 * @return the ID
	 */
	String getID();
}
