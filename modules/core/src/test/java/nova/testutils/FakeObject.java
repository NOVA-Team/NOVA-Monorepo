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

package nova.testutils;

import nova.core.component.exception.ComponentException;
import nova.core.component.misc.FactoryProvider;
import nova.core.util.Identifiable;

/**
 * @author ExE Boss
 */
public class FakeObject implements Identifiable {

	private FakeObjectFactory factory = null;

	void initFactory(FakeObjectFactory factory) {
		if (this.factory == null) {
			this.factory = factory;
		} else {
			throw new ComponentException("Attempt to add two components of the type %s to %s", FactoryProvider.class, this);
		}
	}

	public final FakeObjectFactory getFactory() {
		if (factory != null) {
			return factory;
		} else {
			throw new ComponentException("Attempt to get component that does not exist: %s", FactoryProvider.class);
		}
	}

	@Override
	public String getID() {
		return getFactory().getID();
	}
}
