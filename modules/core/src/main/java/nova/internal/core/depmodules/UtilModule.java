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

package nova.internal.core.depmodules;

import nova.core.util.Dictionary;
import nova.core.util.registry.Registry;
import nova.internal.core.Game;
import nova.internal.core.di.NovaScopes;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class UtilModule extends BinderModule {

	UtilModule() {
		super(NovaScopes.MULTIPLE_INSTANCES);
	}

	@Override
	protected void declare() {
		bind(Registry.class).toConstructor();
		bind(Dictionary.class).toConstructor();

		per(Scoped.APPLICATION).bind(Game.class).toConstructor();
	}

}
