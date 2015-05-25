package nova.core.block.component;

import nova.core.util.Direction;

/**
 * Implement this interface on BlockLogics which allow insertion of
 * objects into them.
 */
//TODO: Convert to component
public interface Insertable {
	/**
	 * @param type The type of the inserted object.
	 * @param side The side the object is inserted on.
	 * @return {@code true} if this type can be injected
	 */
	boolean canInsert(Class<?> type, Direction side);

	/**
	 * @param object The inserted object.
	 * @param side The side the object is inserted on.
	 * @param simulate Whether to simulate the insertion.
	 * @return An object representing the remainder of the insertion, or null
	 * if the object has been used up completely.
	 */
	Object insert(Object object, Direction side, boolean simulate);
}

