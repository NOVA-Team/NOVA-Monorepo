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

package nova.core.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ExE Boss
 */
public class ArrayUtilTest {

	@Test
	public void testJoin() {
		assertThat(ArrayUtil.join("foo", new String[]{"bar", "baz"}))
			.hasSize(3)
			.containsExactly("foo", "bar", "baz");
		assertThat(ArrayUtil.join(new String[]{"foo", "bar"}, "baz"))
			.hasSize(3)
			.containsExactly("foo", "bar", "baz");
		assertThat(ArrayUtil.join(new String[]{"foo", "bar"}, new String[]{"baz", "str"}))
			.hasSize(4)
			.containsExactly("foo", "bar", "baz", "str");
	}
}
