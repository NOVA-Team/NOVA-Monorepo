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

	/**
	 * Compares the ID of the Identifialbes
	 *
	 * @param other Identifiable to compare to
	 * @return If the Identifiables are the same type
	 */

	default boolean sameType(Identifiable other) {
		return getID().equals(other.getID());
	}
}
