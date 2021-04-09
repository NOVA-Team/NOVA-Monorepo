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

package nova.internal.core.di;

import nova.core.loader.Mod;
import nova.internal.core.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injection;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * @author Kubuxu
 */
public class LoggerModule extends BinderModule {

	private static boolean defaultNameChangeable = true;
	private static Optional<String> defaultName = Optional.empty();

	/**
	 * Changes the default logger name (the name of the logger injected into Game),
	 * must be called before doing any dependency injection.
	 * <p>
	 * Is really only useful for engine implementations using NOVA.
	 *
	 * @param name The new name.
	 */
	public static void setDefaultName(String name) {
		if (defaultNameChangeable) {
			defaultName = Optional.of(name);
		}
	}

	/**
	 * Gets the default logger name (the name of the logger injected into Game)
	 *
	 * @return The default logger name (or {@code "NOVA"} if no default logger name was set)
	 */
	public static String getDefaultName() {
		return defaultName.orElse("NOVA");
	}

	public LoggerModule() {
		super(Scoped.DEPENDENCY);
		defaultNameChangeable = false;
	}

	@Override
	protected void declare() {
		bind(Logger.class).toSupplier(LoggerSupplier.class);
	}

	public static class LoggerSupplier implements Supplier<Logger>{

		private static boolean isForGame(Dependency<? super Logger> dependency) {
			boolean isForGame = false;
			boolean isForMod = false;
			for (Injection target : dependency) {
				Class<?> clazz = target.getTarget().getInstance().getType().getRawType();
				if (Game.class.isAssignableFrom(clazz)) {
					isForGame = true;
				}
				if (clazz.isAnnotationPresent(Mod.class)) {
					isForMod = true;
				}
			}
			return isForGame && !isForMod;
		}

		@Override
		public Logger supply(Dependency<? super Logger> dependency, Injector injector) {
			if (dependency.isUntargeted() || isForGame(dependency)) {
				return LoggerFactory.getLogger(getDefaultName());
			} else {
				return StreamSupport.stream(dependency.spliterator(), false)
					.map(target -> target.getTarget().getInstance().getType().getRawType())
					.filter(clazz -> clazz.isAnnotationPresent(Mod.class))
					.findFirst()
					.map(clazz -> clazz.getAnnotation(Mod.class))
					.map(mod -> LoggerFactory.getLogger(String.format("%s (%s)", mod.id(), mod.name())))
					.orElseGet(() -> LoggerFactory.getLogger(dependency.iterator().next().getTarget().getInstance().getType().getRawType()));
			}
		}
	}
}
