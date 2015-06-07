package nova.core.util;

/**
 * General exception that can be thrown by Nova internals
 */
public abstract class NovaException extends RuntimeException {
	private static final long serialVersionUID = -2692979724920608046L;

	public NovaException() {
		super();
	}

	/**
	 * Formatted with {@link String#format(String, Object...)}
	 *
	 * @param message
	 * @param parameters
	 */
	public NovaException(String message, Object... parameters) {
		this(String.format(message, parameters));
	}

	public NovaException(String message) {
		super(message);
	}

	public NovaException(String message, Throwable cause) {
		super(message, cause);
	}

	public NovaException(Throwable cause) {
		super(cause);
	}
}
