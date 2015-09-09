package nova.core.component;

import nova.core.component.exception.ComponentException;
import nova.core.util.Identifiable;

/**
 * Base interface for all Components.
 * A Component is intended as a data holder and provides data to be processed in a ComponentProvider.
 * @author Calclavia
 */
public abstract class Component implements Identifiable {

	private ComponentProvider provider;

	public ComponentProvider getProvider() {
		if (provider == null) {
			throw new ComponentException("Component not bound to any ComponentProvider.", this);
		}

		return provider;
	}

	Component setProvider(ComponentProvider provider) {
		this.provider = provider;
		return this;
	}

	@Override
	public String getID() {
		return getClass().getSimpleName();
	}
}
