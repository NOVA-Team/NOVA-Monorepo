/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.component;

import nova.core.component.exception.ComponentException;
import nova.core.event.bus.Event;
import nova.core.event.bus.EventBus;
import nova.internal.core.Game;
import se.jbee.inject.Dependency;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A component block provides the components associated with the object.
 * <p>
 * {@code ComponentProvider} is implemented in blocks or entities.
 * @author Calclavia
 */
public abstract class ComponentProvider {

	public final EventBus<Event> events = new EventBus<>();

	private Map<Class<? extends Component>, Component> componentMap = new HashMap<>();

	/**
	 * Adds a new component based on its superclass or interface using dependency injection.
	 * @param theInterface The interface or abstract class associated with the new component
	 * @param <C> The node type
	 * @return A new node of N type.
	 */
	public final <C extends Component> C add(Class<C> theInterface) {
		return add(Game.injector().resolve(Dependency.dependency(theInterface)));
	}

	/**
	 * Adds a component to the block.
	 * @param component The component to add.
	 * @return the component.
	 * @throws ComponentException when the component already exists on the block.
	 */
	public final <C extends Component> C add(C component) {
		if (has(component.getClass())) {
			throw new ComponentException("Attempt to add two components of the type %s to " + this, component);
		}

		//Place component into component map
		componentMap.put(component.getClass(), component);

		//Set component's provider.
		component.setProvider(this);

		//Publish component add event
		events.publish(new ComponentAdded(component));
		return component;
	}

	/**
	 * Adds a component to the block if it is not present.
	 * @param component The component to add.
	 * @return the component.
	 */
	@SuppressWarnings("unchecked")
	public final <C extends Component> C getOrAdd(C component) {
		if (has(component.getClass())) {
			return get((Class<C>) component.getClass());
		}

		return add(component);
	}

	/**
	 * Removes a component from the block.
	 * @param component The component to remove
	 * @return the component removed.
	 */
	public final <C extends Component> C remove(C component) {
		//Remove component based on class
		componentMap.remove(component.getClass());

		//Set provider on component to null
		component.setProvider(null);

		//Publish component event
		events.publish(new ComponentRemoved(component));
		return component;
	}

	/**
	 * Checks if a component type exists in this provider.
	 * @param componentType the component type to check.
	 * @return true if the component exists on the block.
	 */
	public final boolean has(Class<?> componentType) {
		return componentMap.keySet().stream()
			.anyMatch(componentType::isAssignableFrom);
	}

	/**
	 * Removes the component from the block.
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

		//Set provider on component to null
		component.setProvider(null);

		//Publish component event
		events.publish(new ComponentRemoved(component));
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

	public static class ComponentAdded extends Event {
		public final Component component;

		public ComponentAdded(Component component) {
			this.component = component;
		}
	}

	public static class ComponentRemoved extends Event {
		public final Component component;

		public ComponentRemoved(Component component) {
			this.component = component;
		}
	}
}
