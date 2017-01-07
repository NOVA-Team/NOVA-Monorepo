package nova.core.util.id;

/**
 * The interface for all identifiers.
 * <p>
 * Implementations should override {@link Object#equals(Object)}, {@link Object#hashCode()} and {@link Object#toString()}.
 * </p>
 *
 * @author soniex2
 */
public interface Identifier {

	/**
	 * Converts this Identifier into a String.
	 * The output from this method may be different than {@link Object#toString()}.
	 *
	 * @return A string representation of this Identifier.
	 */
	String asString();
}
