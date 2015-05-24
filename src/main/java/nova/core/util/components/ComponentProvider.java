package nova.core.util.components;

import java.util.Set;

/**
 * A component provider is implemented in blocks or entities.
 * A component provider provides the components associated with the object.
 * @author Calclavia
 */
public interface ComponentProvider {

	/**
	 * Adds a component to the provider
	 * @param component
	 */
	void add(Component component);

	/**
	 * Removes a component from the provider
	 * @param component
	 */
	void remove(Component component);

	/**
	 * Gets a list of components that this NodeProvider provides.
	 * @return - A set of components.
	 */
	Set<Component> getComponents();
}
