package nova.core.component.inventory;

import nova.core.util.NovaException;

public class InventoryException extends NovaException {
	public InventoryException() {
		super();
	}

	public InventoryException(String message, Object... parameters) {
		super(message, parameters);
	}

	public InventoryException(String message) {
		super(message);
	}

	public InventoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public InventoryException(Throwable cause) {
		super(cause);
	}
}
