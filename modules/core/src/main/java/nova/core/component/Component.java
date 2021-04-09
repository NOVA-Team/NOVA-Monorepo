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
import nova.core.util.Identifiable;

/**
 * Base interface for all Components.
 * A Component is intended as a data holder and provides data to be processed in a ComponentProvider.
 * @author Calclavia
 */
public abstract class Component implements Identifiable {

	@SuppressWarnings("rawtypes")
	private ComponentProvider<? extends ComponentMap> provider;

	@SuppressWarnings("rawtypes")
	public ComponentProvider<? extends ComponentMap> getProvider() {
		if (provider == null) {
			throw new ComponentException("Component not bound to any ComponentProvider.", this);
		}

		return provider;
	}

	@SuppressWarnings("rawtypes")
	Component setProvider(ComponentProvider<? extends ComponentMap> provider) {
		if (this.provider == provider)
			return this;

		this.provider = provider;
		onProviderChange();
		return this;
	}

	public void onProviderChange() {

	}

	/**
	 * Gets the ID that represents this Component type.
	 *
	 * @return The ID that represents this Component type.
	 */
	@Override
	public String getID() {
		return getClass().getSimpleName();
	}
}
