package nova.core.component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A component provider is implemented in blocks or entities.
 * A component provider provides the components associated with the object.
 * @author Calclavia
 */
public abstract class ComponentProvider {

	private Map<Class<? extends Component>, Component> componentMap = new HashMap<>();

	/**
	 * Adds a component to the provider
	 * @param component The component to add
	 */
	public final <C extends Component> C add(C component) {
		componentMap.put(component.getClass(), component);
		return component;
	}

	/**
	 * Removes a component from the provider
	 * @param component The component to remove
	 */
	public final <C extends Component> C remove(C component) {
		componentMap.remove(component.getClass());
		return component;
	}

	public final boolean has(Class<? extends Component> componentType) {
		return componentMap.containsKey(componentType);
	}

	public final <C extends Component> Optional<C> remove(Class<C> componentType) {
		return Optional.ofNullable((C) componentMap.remove(componentType));
	}

	public final <C extends Component> Optional<C> get(Class<C> componentType) {
		Component component = componentMap.get(componentType);

		if (component != null) {
			return Optional.of((C) component);
		} else {
			return componentMap
				.values()
				.stream()
				.filter(c -> componentType.isAssignableFrom(c.getClass()))
				.map(componentType::cast)
				.findFirst();
		}
	}

	/**
	 * Gets a set of components that this ComponentProvider provides.
	 * @return A set of components.
	 */
	public final Collection<Component> components() {
		return componentMap.values();
	}
}
