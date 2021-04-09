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

import nova.core.util.MockIdentifiable;
import nova.core.util.registry.Registry;
import org.junit.Before;
import org.junit.Test;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Parameter;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.bootstrap.Bootstrap;
import se.jbee.inject.bootstrap.BootstrapperBundle;
import se.jbee.inject.util.Scoped;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static nova.testutils.NovaAssertions.assertThat;

public class OptionalDITest {
	Injector injector;

	@Before
	public void prepare() {
		injector = Bootstrap.injector(OptionalBundle.class);
	}

	@Test
	public void should_inject_Optional() {

		TestManager m = injector.resolve(Dependency
			.dependency(TestManager.class));

		assertThat(m.map).isPresent();
		assertThat(m.set).isEmpty();
		assertThat(m.map2).isNotSameAs(m.map);
		assertThat(m.map2.get()).isNotSameAs(m.map.get());
	}

	public static class TestManager {
		Optional<Map<Integer, Integer>> map;

		Optional<Registry<MockIdentifiable>> set;
		Optional<Map<Integer, Integer>> map2;

		public TestManager(Optional<Map<Integer, Integer>> map,
		                   Optional<Registry<MockIdentifiable>> set, Optional<Map<Integer, Integer>> map2) {
			this.map = map;
			this.set = set;
			this.map2 = map2;
		}
	}

	public static class MapModule extends BinderModule {
		public MapModule() {
			super(Scoped.INJECTION);
		}

		@Override
		protected void declare() {

			bind(TestManager.class).toConstructor();
			try {
				starbind(Map.class).to(
					HashMap.class.getConstructor(new Class<?>[] {}), (Parameter[]) null);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}

	}

	public static class OptionalBundle extends BootstrapperBundle {

		@Override
		protected void bootstrap() {
			install(MapModule.class);
			install(OptionalModule.class);
		}
	}

}
