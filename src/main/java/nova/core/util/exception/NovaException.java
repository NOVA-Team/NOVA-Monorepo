package nova.core.util.exception;

/**
 * General exception that can be thrown by Nova internals
 */
public class NovaException extends RuntimeException {
	private static final long serialVersionUID = -2692979724920608046L;
	
	public NovaException() { super(); }
	public NovaException(String message) { super(message); }
	public NovaException(String message, Throwable cause) { super(message, cause); }
	public NovaException(Throwable cause) { super(cause); }
}
