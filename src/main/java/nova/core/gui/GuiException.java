package nova.core.gui;

import nova.core.util.NovaException;

public class GuiException extends NovaException {
	public GuiException() {
		super();
	}

	public GuiException(String message, Object... parameters) {
		super(message, parameters);
	}

	public GuiException(String message) {
		super(message);
	}

	public GuiException(String message, Throwable cause) {
		super(message, cause);
	}

	public GuiException(Throwable cause) {
		super(cause);
	}
}
