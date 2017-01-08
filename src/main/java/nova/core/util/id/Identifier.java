package nova.core.util.id;

/**
 * The interface for all identifiers. The Identifier must be immutable.
 * <p>
 * Implementations should override {@link Object#equals(Object)}, {@link Object#hashCode()} and {@link Object#toString()}.
 * </p>
 *
 * @author SoniEx2, ExE Boss
 */
public interface Identifier {

	/**
	 * Converts this Identifier into a String.
	 * The output from this method may be different than {@link Object#toString()}.
	 *
	 * @return A string representation of this Identifier.
	 */
	String asString();

	/**
	 * Converts this Identifier into a short String.
	 * The default implementation returns the same result as {@link #asString()}.
	 *
	 * @return A string representation of this Identifier.
	 */
	default String asShortString() {
		return asString();
	}
}
