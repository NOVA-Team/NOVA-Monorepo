package nova.core.block;

import nova.core.core.Direction;

/**
 * Implement this interface on BlockLogics which allow extraction of
 * objects into them.
 */
public interface Extractable {
	/**
	 * @param type The type of the extracted object.
	 * @param side The side the object is extracted on.
	 * @return true if this type can be injected
	 */
	boolean canExtract(Class type, Direction side);

	/**
	 * @param object The extracted object.
	 * @param side The side the object is extracted on.
	 * @param simulate Whether to simulate the extraction.
	 * @return An object representing the remainder of the extraction, or null
	 * if the object has been used up completely.
	 */
	Object extract(Object object, Direction side, boolean simulate);
}

