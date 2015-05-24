package nova.core.world.component;

import nova.core.util.Identifiable;

/**
 * Base interface for all Components. A Component is intended as a data holder and provides data to be processed in a ComponentProvider.
 * @author Calclavia
 */
public interface Component extends Identifiable {

	/**
	 * @return The provider holding this component.
	 */
	ComponentProvider provider();
}
