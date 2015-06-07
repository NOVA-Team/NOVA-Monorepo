package nova.internal.launch;

import nova.core.util.NovaException;

public class InitializationException extends NovaException {
	public InitializationException() {
		super();
	}

	public InitializationException(String message, Object... parameters) {
		super(message, parameters);
	}

	public InitializationException(String message) {
		super(message);
	}

	public InitializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InitializationException(Throwable cause) {
		super(cause);
	}
}
