package nova.core.config;

import nova.core.util.NovaException;

public class ConfigException extends NovaException {
	public ConfigException() {
		super();
	}

	public ConfigException(String message, Object... parameters) {
		super(message, parameters);
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}
}
