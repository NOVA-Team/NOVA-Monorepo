package nova.core.util.id;

import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Store;

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
		return id.toString();
	}

	@Override
	public String toString() {
		return id.toString();
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return this == o || (o != null && getClass() == o.getClass() && id.equals(((AbstractIdentifier) o).id));
	}
}
