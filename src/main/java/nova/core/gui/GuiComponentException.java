package nova.core.gui;

public class GuiComponentException extends GuiException {
	public GuiComponentException() {
		super();
	}

	public GuiComponentException(String message, Object... parameters) {
		super(message, parameters);
	}

	public GuiComponentException(String message) {
		super(message);
	}

	public GuiComponentException(String message, Throwable cause) {
		super(message, cause);
	}

	public GuiComponentException(Throwable cause) {
		super(cause);
	}
}
