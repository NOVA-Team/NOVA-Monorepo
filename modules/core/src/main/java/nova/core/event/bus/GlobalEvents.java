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

package nova.core.event.bus;

import nova.internal.core.launch.NovaLauncher;

import java.util.Map;
import java.util.Optional;

/**
 * Global event manager that handles general events that are not object specific.
 * @author Calclavia
 */
public class GlobalEvents extends EventBus<Event> {

	@Override
	public <E extends Event> EventBinder<E> on() {
		return forMod(super.on(), Optional.empty());
	}

	@Override
	public <E extends Event> EventBinder<E> on(Class<E> clazz) {
		return forMod(super.on(clazz), Optional.of(clazz));
	}

	private <E extends Event> EventBinder<E> forMod(EventBinder<E> binder, Optional<Class<E>> clazz) {
		NovaLauncher.instance().ifPresent(launcher ->
			launcher.getCurrentMod().ifPresent(mod -> {
				String className = clazz.map(c -> ":" + c.getName()).orElse("");
				binder.withName("nova.launcher:" + mod.id() + className);
				Map<String, String> deps = launcher.dependencyToMap(mod.dependencies());
				deps.keySet().stream().forEach(id -> binder.after("nova.launcher:" + id + className));
				binder.withPriority(mod.priority());
			}));
		return binder;
	}
}
