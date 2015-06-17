package nova.core.util;

/**
 * General exception that can be thrown by Nova internals
 */
public abstract class NovaException extends RuntimeException {
	private static final long serialVersionUID = -2692979724920608046L;

	/**
	 *
	 */
	public NovaException() {
		super();
	}

	/**
	 * Formatted with {@link String#format(String, Object...)}
	 *
	 * @param message The error message
	 * @param parameters additional parameters
	 */
	public NovaException(String message, Object... parameters) {
		this(String.format(message, parameters));
	}

	/**
	 * General exception that can be thrown by Nova internals
	 *
	 * @param message Error message
	 */
	public NovaException(String message) {
		super(message);
	}

	/**
	 * General exception that can be thrown by Nova internals
	 *
	 * @param message Error message
	 * @param cause Cause of the exception
	 */
	public NovaException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * General exception that can be thrown by Nova internals
	 *
	 * @param cause Cause of the exception
	 */
	public NovaException(Throwable cause) {
		super(cause);
	}
}
