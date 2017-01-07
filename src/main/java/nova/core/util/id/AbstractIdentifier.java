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

	protected static final <T extends Identifier> boolean equalsImpl(Identifier _this, Object other, Class<T> superclass, Function<T,Object> getter) {
		if (_this == other) return true;
		if (_this == null || other == null) return false;
		if (!_this.getClass().isAssignableFrom(superclass) || !other.getClass().isAssignableFrom(superclass)) return false;
		return Objects.equals(getter.apply((T)_this), getter.apply((T)other));
	}
}
