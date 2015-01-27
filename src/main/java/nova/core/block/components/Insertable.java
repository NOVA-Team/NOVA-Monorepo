package nova.core.block.components;

import nova.core.util.Direction;

/**
 * Implement this interface on BlockLogics which allow insertion of
 * objects into them.
 */
public interface Insertable {
	/**
	 * @param type The type of the inserted object.
	 * @param side The side the object is inserted on.
	 * @return true if this type can be injected
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

