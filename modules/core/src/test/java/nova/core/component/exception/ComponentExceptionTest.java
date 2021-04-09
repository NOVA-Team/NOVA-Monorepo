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

package nova.core.component.exception;

import nova.core.component.Category;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ExE Boss
 */
public class ComponentExceptionTest {

	@Test
	public void testComponentException() {
		ComponentException ex = new ComponentException("Object %s is %s", Category.class, Object.class);
		assertThat(ex.getMessage()).isEqualTo("Object " + Category.class + " is " + Object.class);
		assertThat(ex.component).isEqualTo(Category.class);

		ex = new ComponentException("Component %s is %s", new Category("Test"), Object.class);
		assertThat(ex.getMessage()).isEqualTo("Component " + Category.class + " is " + Object.class);
		assertThat(ex.component).isEqualTo(Category.class);
	}
}
