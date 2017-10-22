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

package nova.core.util.registry;

import nova.testutils.FakeObject;
import nova.testutils.FakeObjectFactory;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
public class FactoryTest {

	private FakeObjectFactory factory;

	@Before
	public void setUp() {
		this.factory = new FakeObjectFactory("id", FactoryTestObject::new);
	}

	@Test
	public void testType() {
		assertThat(this.factory.getType()).isEqualTo(FactoryTestObject.class);
	}

	public static class FactoryTestObject extends FakeObject {

	}
}
