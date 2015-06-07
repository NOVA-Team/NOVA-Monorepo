package nova.core.event;

import nova.core.util.NovaException;

public class EventException extends NovaException {
	public EventException() {
		super();
	}

	public EventException(String message, Object... parameters) {
		super(message, parameters);
	}

	public EventException(String message) {
		super(message);
	}

	public EventException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventException(Throwable cause) {
		super(cause);
	}
}
