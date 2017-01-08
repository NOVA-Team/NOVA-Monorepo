package nova.core.util.id;

import java.util.Objects;

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
		this.id = Objects.requireNonNull(id);
	}

	@Override
	public String asString() {
		return id.toString();
	}

	@Override
	public String toString() {
		return id.toString();
	}

	@Override
	public final int hashCode() {
		return id.hashCode();
	}

	@Override
	public final boolean equals(Object o) {
		return this == o || (o != null && getClass() == o.getClass() && id.equals(((AbstractIdentifier) o).id));
	}
}
