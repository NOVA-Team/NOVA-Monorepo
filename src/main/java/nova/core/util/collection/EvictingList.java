package nova.core.util.collection;

import java.util.LinkedList;

/**
 * EvictingList is a LinkedList withPriority a size limit, the oldest entries will be removed is the list is full
 *
 * @param <E> Type contained in the list
 */
public class EvictingList<E> extends LinkedList<E> {
	private final int limit;

	/**
	 * A new EvictingList withPriority the specified size limit
	 *
	 * @param limit Max size for the list
	 */
	public EvictingList(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean add(E o) {
		boolean value = super.add(o);

		while (size() > limit) {
			super.remove();
		}

		return value;
	}

	/**
	 * Limit specifies how many elements can be contained in this list.
	 * @return maximum number of entries in this list.
	 */
	public int limit() {
		return limit;
	}

	/**
	 * Get the oldest entry in the list
	 *
	 * @return The oldest entry in the list
	 */
	public E getOldest() {
		return get(0);
	}

	/**
	 * Get the latest entry in the list
	 *
	 * @return The latest entry in the list
	 */
	public E getLastest() {
		return get(size() - 1);
	}
}
