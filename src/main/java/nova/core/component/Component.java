package nova.core.component;

import nova.core.util.Identifiable;

/**
 * Base interface for all Components.
 * A Component is intended as a data holder and provides data to be processed in a ComponentProvider.
 * @author Calclavia
 */
public abstract class Component implements Identifiable {

	@Override
	public String getID() {
		return getClass().getSimpleName();
	}
}
