package nova.core.block.component;

import nova.core.util.Direction;

/**
 * Implement this interface on BlockLogics which allow extraction of
 * objects into them.
 */
//TODO: Convert to component
public interface Extractable {
	/**
	 * Called to check if a block can have something extracted from it.
	 *
	 * @param type The type of the extracted object.
	 * @param side The side the object is extracted on.
	 * @return {@code true} if this type can be extracted.
	 */
	boolean canExtract(Class<?> type, Direction side);

	/**
	 * Called when something is extracted from a block.
	 *
	 * @param object The extracted object.
	 * @param side The side the object is extracted on.
	 * @param simulate Whether to simulate the extraction.
	 * @return An object representing the remainder of the extraction, or null
	 * if the object has been used up completely.
	 */
	Object extract(Object object, Direction side, boolean simulate);
}

