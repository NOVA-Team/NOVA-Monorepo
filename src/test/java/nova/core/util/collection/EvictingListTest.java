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

package nova.core.util.collection;

import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

public class EvictingListTest {

	EvictingList<String> list;

	@Before
	public void setUp() {
		list = new EvictingList<>(5);
	}

	@Test
	public void testCapacity() {
		assertThat(list.limit()).isEqualTo(5);
	}

	@Test
	public void testMaximumSize() {
		fill();
		assertThat(list.size()).isEqualTo(list.limit());
	}

	@Test
	public void testOldestAndNewest() {
		fill();
		assertThat(list.getOldest()).isEqualTo("5");
		assertThat(list.getLastest()).isEqualTo("9");
	}

	private void fill() {
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("8");
		list.add("9");
	}
}