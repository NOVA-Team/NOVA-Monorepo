package asie.api.block;

import asie.api.core.Direction;

public interface Connectable {
	 public enum Type {
		 DEFAULT, FORCE, DENY;
	 }

	 Connectable.Type canConnect(Class type, Direction side);
}
