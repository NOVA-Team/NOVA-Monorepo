package nova.core.network;

import nova.core.util.NovaException;

public class NetworkException extends NovaException {
	public NetworkException() {
		super();
	}

	public NetworkException(String message, Object... parameters) {
		super(message, parameters);
	}

	public NetworkException(String message) {
		super(message);
	}

	public NetworkException(String message, Throwable cause) {
		super(message, cause);
	}

	public NetworkException(Throwable cause) {
		super(cause);
	}
}
