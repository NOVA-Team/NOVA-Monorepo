package nova.core.util.id;

import java.util.Objects;
import java.util.function.Function;

/**
 * Basic implementation of Identifier.
 *
 * @author soniex2
 */
public abstract class AbstractIdentifier<T> implements Identifier {
	/**
	 * The ID.
	 */
	protected final T id;

	/**
	 * Constructs a new AbstractIdentifier.
	 *
	 * @param id The ID.
	 */
	public AbstractIdentifier(T id) {
		Objects.requireNonNull(id);
		this.id = id;
	}

	@Override
	public String asString() {
		return Objects.toString(id);
	}

	@Override
	public String toString() {
		return Objects.toString(id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public abstract boolean equals(Object other);

	protected static final <T extends Identifier> boolean equalsImpl(Identifier self, Object other, Class<T> superclass, Function<T,Object> getter) {
		if (self == other) return true;
		if (self == null || other == null) return false;
		if (!superclass.isAssignableFrom(self.getClass()) || !superclass.isAssignableFrom(other.getClass())) return false;
		return Objects.equals(getter.apply((T)self), getter.apply((T)other));
	}
}
