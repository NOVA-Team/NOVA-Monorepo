package nova.core.util.component;

import java.util.Optional;
import java.util.Set;

/**
 * A component provider is implemented in blocks or entities.
 * A component provider provides the components associated with the object.
 * @author Calclavia
 */
public interface ComponentProvider {

	/**
	 * Adds a component to the provider
	 * @param component The component to add
	 */
	default void add(Component component) {
		components().add(component);
	}

	/**
	 * Removes a component from the provider
	 * @param component The component to remove
	 */
	default void remove(Component component) {
		components().remove(component);
	}

	default <C extends Component> Optional<C> getComponent(Class<C> componentType) {
		return components()
			.stream()
			.filter(component -> componentType.isAssignableFrom(component.getClass()))
			.map(component -> componentType.cast(component))
			.findFirst();
	}

	/**
	 * Gets a set of components that this ComponentProvider provides.
	 * @return A set of components.
	 */
	Set<Component> components();
}
