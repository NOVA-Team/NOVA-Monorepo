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

import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bootstrap.SuppliedBy;

import static se.jbee.inject.util.ToString.describe;

/**
 * A dummy class containing the {@link SupplierSupplier} to match
 * the rest of the NOVA Dependency Injection setup.
 *
 * @author ExE Boss
 */
public final class SupplierModule {

	public static final class SupplierSupplier implements Supplier<java.util.function.Supplier<?>> {
		@Override
		public java.util.function.Supplier<?> supply(Dependency<? super java.util.function.Supplier<?>> dependency, Injector injector) {
			Dependency<?> providedType = dependency.onTypeParameter();
			if (!dependency.getName().isDefault()) {
				providedType = providedType.named(dependency.getName());
			}
			final Dependency<?> finalProvidedType = providedType;
			return () -> SuppliedBy.lazyProvider(finalProvidedType.uninject().ignoredExpiry(), injector).provide();
		}

		@Override
		public String toString() {
			return describe("supplies", java.util.function.Supplier.class);
		}
	}
}
