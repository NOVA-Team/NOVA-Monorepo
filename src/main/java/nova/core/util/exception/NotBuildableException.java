package nova.core.util.exception;

public class NotBuildableException extends NovaException {
	private static final long serialVersionUID = 5185469563276314486L;

	public NotBuildableException() { super(); }

	public NotBuildableException(String message) { super(message); }

	public NotBuildableException(String message, Throwable cause) { super(message, cause); }

	public NotBuildableException(Throwable cause) { super(cause); }
}
