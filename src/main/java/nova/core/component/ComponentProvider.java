package nova.core.component;

import nova.core.component.exception.ComponentException;
import nova.core.event.Event;
import nova.core.event.EventBus;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A component provider provides the components associated with the object.
 * <p>
 * {@code ComponentProvider} is implemented in blocks or entities.
 * @author Calclavia
 */
public abstract class ComponentProvider {

	public final EventBus<Event> events = new EventBus<>();
	public EventBus<ComponentAdded> onComponentAdded = new EventBus<>();
	public EventBus<ComponentRemoved> onComponentRemoved = new EventBus<>();
	private Map<Class<? extends Component>, Component> componentMap = new HashMap<>();

	/**
	 * Adds a component to the provider.
	 * @param component The component to add.
	 * @return the component.
	 * @throws ComponentException when the component already exists on the provider.
	 */
	public final <C extends Component> C add(C component) {
		if (has(component.getClass())) {
			throw new ComponentException("Attempt to add two components of the type %s to " + this, component);
		}

		componentMap.put(component.getClass(), component);
		onComponentAdded.publish(new ComponentAdded(component));
		return component;
	}

	/**
	 * Adds a component to the provider if it is not present.
	 * @param component The component to add.
	 * @return the component.
	 */
	@SuppressWarnings("unchecked")
	public final <C extends Component> C getOrAdd(C component) {
		if (has(component.getClass())) {
			return get((Class<C>) component.getClass());
		}

		componentMap.put(component.getClass(), component);
		onComponentAdded.publish(new ComponentAdded(component));
		return component;
	}

	/**
	 * Removes a component from the provider.
	 * @param component The component to remove
	 * @return the component removed.
	 */
	public final <C extends Component> C remove(C component) {
		componentMap.remove(component.getClass());
		onComponentRemoved.publish(new ComponentRemoved(component));
		return component;
	}

	/**
	 * @param componentType the component type to check.
	 * @return true if the component exists on the provider.
	 */
	public final boolean has(Class<?> componentType) {
		return componentMap.keySet().stream()
			.anyMatch(componentType::isAssignableFrom);
	}

	/**
	 * Removes the component from the provider.
	 * @param componentType the component type.
	 * @return the component removed.
	 * @throws ComponentException when the component dies not exist.
	 */
	@SuppressWarnings("unchecked")
	public final <C extends Component> C remove(Class<C> componentType) {
		if (!has(componentType)) {
			throw new ComponentException("Attempt to remove component that does not exist: %s", componentType);
		}

		C component = (C) componentMap.remove(componentType);
		onComponentRemoved.publish(new ComponentRemoved(component));
		return component;
	}

	/**
	 * Gets an optional of the component with the specified type.
	 * @param componentType the type to get.
	 * @return the optional of the component found or {@code Optional.empty()}
	 * if the component was not found.
	 */
	@SuppressWarnings("unchecked")
	public final <C> Optional<C> getOp(Class<C> componentType) {
		if (componentType.isAssignableFrom(Component.class)) {
			Component component = componentMap.get(componentType.asSubclass(Component.class));
			if (component != null) {
				return Optional.of((C) component);
			}
		}
		Set<C> collect = components().stream()
			.filter(c -> componentType.isAssignableFrom(c.getClass()))
			.map(componentType::cast)
			.collect(Collectors.toSet());

		if (collect.size() > 1) {
			throw new ComponentException("Ambiguous component search. For component/interface %s there are multiple components found: %s", componentType, collect);
		}

		return collect.stream().findFirst();
	}

	/**
	 * Gets the component with the specified type.
	 * @param componentType the type to get.
	 * @return the component.
	 * @throws ComponentException if the component doesn't exist.
	 */
	public final <C> C get(Class<C> componentType) {
		return getOp(componentType).orElseThrow(() -> new ComponentException("Attempt to get component that does not exist: %s", componentType));
	}

	/**
	 * Gets a set of components that this ComponentProvider provides.
	 * @return A set of components.
	 */
	public final Collection<Component> components() {
		return new HashSet<>(componentMap.values());
	}

	public static class ComponentAdded {
		public final Component component;

		public ComponentAdded(Component component) {
			this.component = component;
		}
	}

	public static class ComponentRemoved {
		public final Component component;

		public ComponentRemoved(Component component) {
			this.component = component;
		}
	}
}
