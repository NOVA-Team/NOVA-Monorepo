package nova.core.component;

import nova.core.util.exception.NovaException;

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
		if (has(component.getClass())) {
			throw new NovaException("Attempt to add two of the same component types: " + component.getClass() + " for block: " + this);
		}

		componentMap.put(component.getClass(), component);
		return component;
	}

	/**
	 * Adds a component to the provider if it is not present
	 * @param component The component to add
	 */
	public final <C extends Component> C getOrAdd(C component) {
		if (has(component.getClass())) {
			return (C) get(component.getClass());
		}

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
		return componentMap
			.keySet()
			.stream()
			.anyMatch(componentType::isAssignableFrom);
	}

	public final <C extends Component> C remove(Class<C> componentType) {
		if (!has(componentType)) {
			throw new NovaException("Attempt to remove component that does not exist: " + componentType);
		}
		return (C) componentMap.remove(componentType);
	}

	public final <C extends Component> Optional<C> getOp(Class<C> componentType) {
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

	public final <C extends Component> C get(Class<C> componentType) {
		return getOp(componentType).orElseThrow(() -> new NovaException("Attempt to get component that does not exist: " + componentType));
	}

	/**
	 * Gets a set of components that this ComponentProvider provides.
	 * @return A set of components.
	 */
	public final Collection<Component> components() {
		return componentMap.values();
	}
}
