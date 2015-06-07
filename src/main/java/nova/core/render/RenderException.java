package nova.core.render;

import nova.core.util.NovaException;

public class RenderException extends NovaException {
	public RenderException() {
		super();
	}

	public RenderException(String message, Object... parameters) {
		super(message, parameters);
	}

	public RenderException(String message) {
		super(message);
	}

	public RenderException(String message, Throwable cause) {
		super(message, cause);
	}

	public RenderException(Throwable cause) {
		super(cause);
	}
}
