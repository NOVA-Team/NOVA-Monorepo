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

import nova.core.event.bus.Event;
import nova.core.event.bus.EventBus;
import nova.core.util.Identifiable;
import nova.internal.core.util.InjectionUtil;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

/**
 * A component block provides the components associated with the object.
 * <p>
 * {@code ComponentProvider} is implemented in blocks or entities.
 * @author Calclavia
 */
@SuppressWarnings("rawtypes")
public abstract class ComponentProvider<CM extends ComponentMap> {

	public final EventBus<Event> events = new EventBus<>();

	public final CM components;

	@SuppressWarnings("unchecked")
	public ComponentProvider() {
		this(ComponentMap.class);
	}

	@SuppressWarnings("unchecked")
	public <C extends ComponentMap> ComponentProvider(Class<C> componentsClass) {
		this.components = (CM) InjectionUtil.newInstance(componentsClass,
			clazz -> clazz.isAssignableFrom(getClass()) ? Optional.of(this) : Optional.empty());
	}

	/**
	 * Gets a set of components that this ComponentProvider provides.
	 * @return A set of components.
	 */
	@SuppressWarnings("unchecked")
	public final Collection<Component> components() {
		return new HashSet<>(components.values());
	}

	@Override
	public String toString() {
		return (this instanceof Identifiable) ? ((Identifiable) this).getID() : super.toString();
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
