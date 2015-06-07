package nova.core.deps;

import nova.core.util.NovaException;

public class DependencyException extends NovaException {
	public DependencyException() {
		super();
	}

	public DependencyException(String message, Object... parameters) {
		super(message, parameters);
	}

	public DependencyException(String message) {
		super(message);
	}

	public DependencyException(String message, Throwable cause) {
		super(message, cause);
	}

	public DependencyException(Throwable cause) {
		super(cause);
	}
}
