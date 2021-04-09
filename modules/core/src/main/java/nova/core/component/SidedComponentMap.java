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

import nova.core.component.exception.ComponentException;
import nova.core.util.Direction;
import nova.internal.core.Game;
import se.jbee.inject.Dependency;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class that contains all sided components.
 *
 * This was implemented horribly poorly, so I intend to re‑implement
 * it in a better way, which will also break backwards compatibility
 * for the most part.
 *
 * @author ExE Boss
 */
public class SidedComponentMap extends ComponentMap {
	private static final long serialVersionUID = 2017_02_12L;

	public final ComponentMap up = new ComponentMap(provider);
	public final ComponentMap down = new ComponentMap(provider);

	public final ComponentMap north = new ComponentMap(provider);
	public final ComponentMap south = new ComponentMap(provider);

	public final ComponentMap west = new ComponentMap(provider);
	public final ComponentMap east = new ComponentMap(provider);

	public SidedComponentMap(ComponentProvider<?> provider) {
		super(provider);
	}

	public final Map<Direction, ComponentMap> getComponentsForDirections() {
		return Arrays.stream(Direction.values()).collect(Collectors.toMap(Function.identity(), this::getComponents));
	}

	public final ComponentMap getComponents(Direction direction) {
		switch (direction) {
			case UP:
				return up;
			case DOWN:
				return down;

			case NORTH:
				return north;
			case SOUTH:
				return south;

			case WEST:
				return west;
			case EAST:
				return east;

			default:
				return this;
		}
	}

	/**
	 * Adds a new component based on its superclass or interface using dependency injection.
	 * @param direction The direction to add the component to.
	 * @param componentType The interface or abstract class associated with the new component.
	 * @param <C> The node type.
	 * @return A new node of N type.
	 */
	public final <C extends Component> C add(Class<C> componentType, Direction direction) {
		return getComponents(direction).add(ensureValid(componentType, direction));
	}

	/**
	 * Adds a new component based on its superclass or interface using dependency injection.
	 * @param directions The directions to add the component to.
	 * @param componentType The interface or abstract class associated with the new component.
	 * @param <C> The node type.
	 * @return A new node of N type.
	 */
	public final <C extends Component> C add(Class<C> componentType, Direction... directions) {
		return this.add(Game.injector().resolve(Dependency.dependency(ensureValid(componentType, directions))), directions);
	}

	/**
	 * Adds a component to the provider.
	 * @param <C> The component type.
	 * @param direction The direction to add the component to.
	 * @param component The component to add.
	 * @return the component.
	 * @throws ComponentException when the component already exists on the block.
	 */
	public final <C extends Component> C add(C component, Direction direction) {
		return getComponents(direction).add(ensureValid(component, direction));
	}

	/**
	 * Adds a component to the provider.
	 * @param <C> The component type.
	 * @param component The component to add.
	 * @param directions The directions to add the component to.
	 * @return the component.
	 * @throws ComponentException when the component already exists on the block.
	 */
	public final <C extends Component> C add(C component, Direction... directions) {
		for (Direction direction : directions)
			return getComponents(direction).add(ensureValid(component, direction));
		return component;
	}

	/**
	 * Adds a component to the block if it is not present.
	 * @param <C> The component type.
	 * @param component The component to add.
	 * @param direction The direction to get or add the component to.
	 * @return the component.
	 */
	public final <C extends Component> C getOrAdd(C component, Direction direction) {
		return getComponents(direction).getOrAdd(component);
	}

	/**
	 * Checks if a component type exists in this provider.
	 * @param componentType the component type to check.
	 * @param direction The direction to check.
	 * @return true if the component exists on the provider.
	 */
	public final boolean has(Class<?> componentType, Direction direction) {
		return getComponents(direction).has(componentType) || this.has(componentType);
	}

	/**
	 * Checks if a component type can be removed from this provider.
	 * @param componentType the component type to check.
	 * @param direction The direction to check.
	 * @return true if the component exists on the provider.
	 */
	public final boolean canRemove(Class<?> componentType, Direction direction) {
		return getComponents(direction).has(componentType);
	}

	/**
	 * Removes the component from the block.
	 * @param <C> The component type.
	 * @param component the component type.
	 * @param direction The direction to remove the component from.
	 * @return the component removed.
	 * @throws ComponentException when the component does not exist.
	 */
	public final <C extends Component> C remove(C component, Direction direction) {
		return getComponents(direction).remove(component);
	}

	/**
	 * Removes the component from the provider.
	 * @param <C> The component type.
	 * @param componentType the component type.
	 * @param direction The direction to remove the component from.
	 * @return the component removed.
	 * @throws ComponentException when the component does not exist.
	 */
	@SuppressWarnings("unchecked")
	public final <C extends Component> C remove(Class<C> componentType, Direction direction) {
		return getComponents(direction).remove(componentType);
	}

	/**
	 * Gets an optional of the component with the specified type.
	 * @param <C> The component type.
	 * @param componentType the type to get.
	 * @param direction The direction to get the component from.
	 * @return the optional of the component found or {@code Optional.empty()}.
	 * if the component was not found.
	 */
	@SuppressWarnings("unchecked")
	public final <C> Optional<C> getOp(Class<C> componentType, Direction direction) {
		Optional<C> c = getComponents(direction).getOp(componentType);
		if (!c.isPresent())
			c = this.getOp(componentType);
		return c;
	}

	/**
	 * Gets the component with the specified type.
	 * @param <C> The component type.
	 * @param componentType the type to get.
	 * @param direction The direction to get the component from.
	 * @return the component.
	 * @throws ComponentException if the component doesn't exist.
	 */
	public final <C> C get(Class<C> componentType, Direction direction) {
		return getComponents(direction).getOp(componentType).orElseGet(() -> this.get(componentType));
	}

	/**
	 * Gets the set of the components with the specified type.
	 * @param <C> The component type.
	 * @param componentType the type to get.
	 * @param direction The direction to get the component from.
	 * @return the set of the components.
	 */
	public final <C> Set<C> getSet(Class<C> componentType, Direction direction) {
		Set<C> c = new HashSet<>(getComponents(direction).getSet(componentType));
		c.addAll(this.getSet(componentType));
		return c;
	}

	private <C extends Component> C ensureValid(C component, Direction... directions) {
		for (Direction direction : directions)
			ensureValid(component, direction);
		return component;
	}

	private <C extends Component> C ensureValid(C component, Direction direction) {
		ensureValid(component.getClass(), direction);
		return component;
	}

	private <C extends Component> Class<C> ensureValid(Class<C> componentType, Direction... directions) {
		for (Direction direction : directions)
			ensureValid(componentType, direction);
		return componentType;
	}

	private <C extends Component> Class<C> ensureValid(Class<C> componentType, Direction direction) {
		if (getComponents(direction) == this)
			return componentType;

		if (componentType.isAnnotationPresent(UnsidedComponent.class))
			throw new IllegalArgumentException("Unsided components can't be added to a specific side");

		return componentType;
	}
}
