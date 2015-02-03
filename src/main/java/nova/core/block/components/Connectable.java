package nova.core.block.components;

import nova.core.util.Direction;

public interface Connectable {

	/**
	 * Called to check if a block can connect to another block next to it.
	 *
	 * @param type The type of the connectable object
	 * @param side The side the side the object is connected on
	 * @return true if this type can be connected
	 */
	Connectable.Type canConnect(Class<?> type, Direction side);

	public enum Type {
		DEFAULT, FORCE, DENY
	}
}
