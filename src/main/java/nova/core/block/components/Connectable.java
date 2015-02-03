package nova.core.block.components;

import nova.core.util.Direction;

public interface Connectable {

	/**
	 * Called to check if a block can connect to another block next to it.
	 *
	 * @param type The type of the connectable object
	 * @param side The side the side the object is connected on
	 * @return {@link nova.core.block.components.Connectable.Type} of connection
	 */
	Connectable.Type canConnect(Class<?> type, Direction side);

	// TODO: What do these actually do / mean?
	public enum Type {
		DEFAULT, FORCE, DENY
	}
}
