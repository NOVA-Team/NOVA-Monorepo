package nova.core.util;

/**
 * An exception thrown when registration in a manager, dictionary or registry fails.
 */
public class RegistrationException extends NovaException {
	public RegistrationException() {
		super();
	}

	public RegistrationException(String message, Object... parameters) {
		super(message, parameters);
	}

	public RegistrationException(String message) {
		super(message);
	}

	public RegistrationException(String message, Throwable cause) {
		super(message, cause);
	}

	public RegistrationException(Throwable cause) {
		super(cause);
	}
}
