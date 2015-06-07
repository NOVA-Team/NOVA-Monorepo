package nova.internal.launch;

import nova.core.util.NovaException;

public class InitialisationException extends NovaException {
	public InitialisationException() {
		super();
	}

	public InitialisationException(String message, Object... parameters) {
		super(message, parameters);
	}

	public InitialisationException(String message) {
		super(message);
	}

	public InitialisationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InitialisationException(Throwable cause) {
		super(cause);
	}
}
