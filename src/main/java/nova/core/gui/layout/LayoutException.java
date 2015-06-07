package nova.core.gui.layout;

import nova.core.util.NovaException;

public class LayoutException extends NovaException {
	public LayoutException() {
		super();
	}

	public LayoutException(String message, Object... parameters) {
		super(message, parameters);
	}

	public LayoutException(String message) {
		super(message);
	}

	public LayoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public LayoutException(Throwable cause) {
		super(cause);
	}
}
