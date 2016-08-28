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

import com.google.common.collect.Sets;
import nova.internal.core.di.DICoreModule;
import nova.internal.core.di.LoggerModule;
import nova.internal.core.di.OptionalModule;
import se.jbee.inject.bootstrap.BootstrapperBundle;
import se.jbee.inject.bootstrap.Bundle;

import java.util.Collections;
import java.util.Set;

public class CoreBundle extends BootstrapperBundle {
	private static Set<Class<? extends Bundle>> coreModules = Sets.newHashSet();

	static {
		/**
		 * Managers
		 */
		add(BlockModule.class);
		add(ItemModule.class);
		add(FluidModule.class);
		add(EventModule.class);
		add(WorldModule.class);
		add(EntityModule.class);
		add(RenderModule.class);
		add(DictionaryModule.class);
		add(SoundModule.class);

		add(RecipesModule.class);
		add(CraftingModule.class);
		add(NativeModule.class);
		add(ComponentModule.class);

		/**
		 * General
		 */
		add(UtilModule.class);

		/**
		 * DI Internal
		 */
		add(OptionalModule.class);
		add(DICoreModule.class);
		add(LoggerModule.class);
	}

	public static Set<Class<? extends Bundle>> getAllCoreModules() {
		return Collections.unmodifiableSet(coreModules);
	}

	private static void add(Class<? extends Bundle> module) {
		coreModules.add(module.asSubclass(Bundle.class));
	}

	@Override
	protected void bootstrap() {
		coreModules.stream().forEach(this::install);
	}

}
