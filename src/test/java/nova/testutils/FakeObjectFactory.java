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

import nova.core.util.registry.Factory;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author ExE Boss
 */
public class FakeObjectFactory extends Factory<FakeObjectFactory, FakeObject> {

	public FakeObjectFactory(String id, Class<? extends FakeObject> type, Function<FakeObject, FakeObject> processor, Function<Class<?>, Optional<?>> mapping) {
		super(id, type, processor, mapping);
	}

	public FakeObjectFactory(String id, Class<? extends FakeObject> type, Function<FakeObject, FakeObject> processor) {
		super(id, type, processor);
	}

	public FakeObjectFactory(String id, Class<? extends FakeObject> type) {
		super(id, type);
	}

	public FakeObjectFactory(String id, Supplier<FakeObject> constructor, Function<FakeObject, FakeObject> processor) {
		super(id, constructor, processor);
	}

	public FakeObjectFactory(String id, Supplier<FakeObject> constructor) {
		super(id, constructor);
	}

	@Override
	protected FakeObjectFactory selfConstructor(String id, Supplier<FakeObject> constructor, Function<FakeObject, FakeObject> processor) {
		return new FakeObjectFactory(id, constructor, processor);
	}
}
