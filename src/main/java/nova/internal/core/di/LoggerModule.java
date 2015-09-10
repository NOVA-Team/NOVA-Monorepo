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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

/**
 * @author Kubuxu
 */
public class LoggerModule extends BinderModule {

	public LoggerModule() {
		super(Scoped.DEPENDENCY_TYPE);
	}


	@Override
	protected void declare() {
		bind(Logger.class).toSupplier(LoggerSupplier.class);
	}

	public static class LoggerSupplier implements Supplier<Logger>{

		@Override
		public Logger supply(Dependency<? super Logger> dependency, Injector injector) {
			if (dependency.isUntargeted()) {
				return LoggerFactory.getLogger("General");
			} else {
				return LoggerFactory.getLogger(dependency.target().getType().getRawType());
			}
		}
	}
}
