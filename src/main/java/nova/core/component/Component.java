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
import nova.core.util.id.ClassIdentifier;
import nova.core.util.id.Identifiable;
import nova.core.util.id.Identifier;

/**
 * Base interface for all Components.
 * A Component is intended as a data holder and provides data to be processed in a ComponentProvider.
 * @author Calclavia
 */
public abstract class Component implements Identifiable {

	private ComponentProvider provider;

	public ComponentProvider getProvider() {
		if (provider == null) {
			throw new ComponentException("Component not bound to any ComponentProvider.", this);
		}

		return provider;
	}

	Component setProvider(ComponentProvider provider) {
		this.provider = provider;
		onProviderChange();
		return this;
	}

	public void onProviderChange() {

	}

	@Override
	public Identifier getID() {
		return new ClassIdentifier(getClass());
	}
}
