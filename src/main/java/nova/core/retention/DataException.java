package nova.core.retention;

import nova.core.util.NovaException;

public class DataException extends NovaException {
	public DataException() {
		super();
	}

	public DataException(String message, Object... parameters) {
		super(message, parameters);
	}

	public DataException(String message) {
		super(message);
	}

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataException(Throwable cause) {
		super(cause);
	}
}
