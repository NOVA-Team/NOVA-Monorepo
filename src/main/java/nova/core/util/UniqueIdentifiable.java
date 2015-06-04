package nova.core.util;

/**
 * A generic interface signifying that this object is uniquely identifiable
 * by an ID
 */
public interface UniqueIdentifiable {
	/**
	 * Get the unique ID to identify this object.
	 *
	 * @return the ID
	 */
	String getUID();
}
