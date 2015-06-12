package nova.core.component.exception;

import nova.core.component.Component;
import nova.core.util.NovaException;

/**
 * Exception thrown when an error to do with components occurs
 */
public class ComponentException extends NovaException {

	public Class<?> component;

	public ComponentException(String message, Class<?> component, Object... parameters) {
		super(message, component, parameters);
		this.component = component;
	}

	public ComponentException(String message, Component component, Object... parameters) {
		this(message, component.getClass());
	}
}
