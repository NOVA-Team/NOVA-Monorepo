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
import nova.core.util.ClassLoaderUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Used to instantiate components.
 *
 * You should ALWAYS call ComponentManager.make() in order to create new
 * components. Do not create new instances of components yourself.
 * @author Calclavia
 */
public class ComponentManager {

	private Map<Class<? extends Component>, String> classToComponent = new HashMap<>();
	private Map<Class<?>, Class<? extends Component>> passthroughComponents = new HashMap<>();

	/**
	 * Use this to register components tagged with {@link Passthrough}.
	 * @param componentClass The component class to register
	 */
	public void registerNativePassthrough(Class<? extends Component> componentClass) {
		Passthrough[] pts = componentClass.getAnnotationsByType(Passthrough.class);
		if (pts.length == 0) {
			throw new ComponentException("No passthrough defined by component %s.", componentClass, componentClass);
		}
		for (Passthrough pt : pts) {
			try {
				Class<?> intfClazz = Class.forName(pt.value());
				if (!intfClazz.isAssignableFrom(componentClass)) {
					throw new ComponentException("Invalid passthrough \"%s\" on component %s, the specified interface isn't implemented.", componentClass, intfClazz, componentClass);
				}
				// TODO This might cause mods to conflict with each other as
				// they are trying to implement the same common interface using
				// different components
				if (passthroughComponents.containsKey(intfClazz)) {
					throw new ComponentException("Duplicate component %s for interface %s.", componentClass, componentClass, intfClazz);
				}
				passthroughComponents.put(intfClazz, componentClass);
			} catch (ClassNotFoundException e) {
				throw new ClassLoaderUtil.ClassLoaderException(e);
			}
		}
	}

	/**
	 * Internal
	 *
	 * @deprecated Internal
	 * @param nativeObject The native object
	 * @return The pass-through components
	 */
	@Deprecated
	public Set<Class<? extends Component>> getPassthroughtComponents(Object nativeObject) {
		Class<?> nativeClazz = nativeObject.getClass();
		Set<Class<? extends Component>> componentClazzes = new HashSet<>();
		for (Class<?> intfClazz : nativeClazz.getInterfaces()) {
			if (passthroughComponents.containsKey(intfClazz)) {
				componentClazzes.add(passthroughComponents.get(intfClazz));
			}
		}
		return componentClazzes;
	}
}
