/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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

import nova.core.component.ComponentProvider.ComponentAdded;
import nova.core.component.exception.ComponentException;
import nova.internal.core.Game;
import se.jbee.inject.Dependency;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A class that contains all components.
 *
 * @author Calclavia
 */
public class ComponentMap extends HashMap<Class<? extends Component>, Component> {
	private static final long serialVersionUID = 2017_02_12L;

	public final ComponentProvider<?> provider;

	public ComponentMap(ComponentProvider<?> provider) {
		this.provider = provider;
	}

	/**
	 * Adds a new component based on its superclass or interface using dependency injection.
	 * @param <C> The component type.
	 * @param componentType The interface or abstract class associated with the new component.
	 * @return The added component.
	 */
	public final <C extends Component> C add(Class<C> componentType) {
		return add(Game.injector().resolve(Dependency.dependency(componentType)));
	}

	/**
	 * Adds a component to the provider.
	 * @param <C> The component type.
	 * @param component The component to add.
	 * @return The added component.
	 * @throws ComponentException when the component already exists on the block.
	 */
	@SuppressWarnings("unchecked")
	public final <C extends Component> C add(C component) {
		if (has(component.getClass())) {
			throw new ComponentException("Attempt to add two components of the type %s to %s", component, this);
		}

		//Place component into component map
		put(component.getClass(), component);

		//Set component's provider.
		component.setProvider(provider);

		//Publish component add event
		provider.events.publish(new ComponentAdded(component));
		return component;
	}

	/**
	 * Adds a component to the block if it is not present.
	 * @param <C> The component type.
	 * @param component The component to add.
	 * @return The component present on this interface
	 */
	@SuppressWarnings("unchecked")
	public final <C extends Component> C getOrAdd(C component) {
		return getOp((Class<C>) component.getClass())
			.orElseGet(() -> add(component));
	}

	/**
	 * Checks if a component type exists in this provider.
	 * @param componentType the component type to check.
	 * @return true if the component exists on the provider.
	 */
	public final boolean has(Class<?> componentType) {
		return keySet().stream().anyMatch(componentType::isAssignableFrom);
	}

	/**
	 * Removes a component from the block.
	 * @param <C> The component type.
	 * @param component The component to remove.
	 * @return the component removed.
	 * @throws ComponentException when the component does not exist.
	 */
	public final <C extends Component> C remove(C component) {
		//Remove component based on class
		remove(component.getClass());

		//Set provider on component to null
		component.setProvider(null);

		//Publish component event
		provider.events.publish(new ComponentProvider.ComponentRemoved(component));
		return component;
	}

	/**
	 * Removes the component from the provider.
	 * @param <C> The component type.
	 * @param componentType the component type.
	 * @return the component removed.
	 * @throws ComponentException when the component does not exist.
	 */
	@SuppressWarnings("unchecked")
	public final <C extends Component> C remove(Class<C> componentType) {
		if (!has(componentType)) {
			throw new ComponentException("Attempt to remove component that does not exist: %s", componentType);
		}

		C component = (C) super.remove(componentType);

		//Set provider on component to null
		component.setProvider(null);

		//Publish component event
		provider.events.publish(new ComponentProvider.ComponentRemoved(component));
		return component;
	}

	/**
	 * Gets an optional of the component with the specified type.
	 * @param <C> The component type.
	 * @param componentType the type to get.
	 * @return the optional of the component found or {@code Optional.empty()}.
	 * if the component was not found.
	 */
	@SuppressWarnings("unchecked")
	public final <C> Optional<C> getOp(Class<C> componentType) {
		if (componentType.isAssignableFrom(Component.class)) {
			Component component = get(componentType.asSubclass(Component.class));
			if (component != null) {
				return Optional.of((C) component);
			}
		}

		Set<C> collect = getSet(componentType);

		if (collect.size() > 1) {
			throw new ComponentException("Ambiguous component search. For component/interface %s there are multiple components found: %s", componentType, collect);
		}

		return collect.stream().findFirst();
	}

	/**
	 * Gets the component with the specified type.
	 * @param <C> The component type.
	 * @param componentType the type to get.
	 * @return the component.
	 * @throws ComponentException if the component doesn't exist.
	 */
	public final <C> C get(Class<C> componentType) {
		return getOp(componentType).orElseThrow(() -> new ComponentException("Attempt to get component that does not exist: %s", componentType));
	}

	/**
	 * Gets the set of the components with the specified type.
	 * @param <C> The component type.
	 * @param componentType the type to get.
	 * @return the set of the components.
	 */
	public final <C> Set<C> getSet(Class<C> componentType) {
		return Collections.unmodifiableSet(values().stream()
			.filter(c -> componentType.isAssignableFrom(c.getClass()))
			.map(componentType::cast)
			.collect(Collectors.toSet()));
	}
}
