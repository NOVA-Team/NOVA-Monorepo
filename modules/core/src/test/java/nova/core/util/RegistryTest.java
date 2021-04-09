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

package nova.core.util;

import nova.core.util.registry.Registry;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

public class RegistryTest {

	@Test
	public void testRegistry() throws Exception {

		Registry<Identifiable> registry = new Registry<>();

		Identifiable id1 = new MockIdentifiable("ID1");
		Identifiable id2 = new MockIdentifiable("ID2");

		registry.register(id1);
		registry.register(id2);

		assertThat(registry.contains("ID1")).isTrue();
		assertThat(registry.contains("ID2")).isTrue();

		assertThat(registry.get("ID1").get().getID()).isEqualTo("ID1");
		assertThat(registry.get("ID2").get().getID()).isEqualTo("ID2");

		assertThat(registry.get("ID1").get()).isEqualTo(id1);
		assertThat(registry.get("ID2").get()).isEqualTo(id2);

		assertThat(registry).containsOnly(id1, id2);

		assertThat(registry.get("None").isPresent()).isFalse();

	}
}
