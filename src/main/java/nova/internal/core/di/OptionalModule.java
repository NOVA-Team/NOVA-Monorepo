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
 */package nova.internal.core.di;

import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bind.BinderModule;

import java.util.Optional;

public class OptionalModule extends BinderModule {

	public OptionalModule() {
		super(NovaScopes.MULTIPLE_INSTANCES);
	}

	@Override
	protected void declare() {
		starbind(Optional.class).to(new OptionalSupplier());
	}

	public static class OptionalSupplier implements Supplier<Optional<?>> {

		@Override
		public Optional<?> supply(Dependency<? super Optional<?>> dependency,
		                          Injector injector) {
			try {
				return Optional.of(injector.resolve(Dependency.dependency(dependency.getType().getParameters()[0])));
			} catch (Throwable t) {
				return Optional.empty();
			}
		}

	}

}
