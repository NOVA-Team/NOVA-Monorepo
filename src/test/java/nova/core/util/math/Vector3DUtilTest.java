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

package nova.core.util.math;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

import static nova.core.util.math.Vector3DUtil.max;
import static nova.core.util.math.Vector3DUtil.min;
import static nova.testutils.NovaAssertions.assertThat;

public class Vector3DUtilTest {

	@Test
	public void testRandomVector() {
		for (int i = 0; i < 20; i++) {
			Vector3D vec = Vector3DUtil.random();
			//Random vector should be of maximum length 1.
			assertThat(vec.getX()).isLessThan(1);
			assertThat(vec.getY()).isLessThan(1);
			assertThat(vec.getZ()).isLessThan(1);
		}
	}

	@Test
	public void testVectorMax() {
		assertThat(max(new Vector3D(1, 2, 3), new Vector3D(3, 2, 1))).isEqualTo(new Vector3D(3, 2, 3));
	}

	@Test
	public void testVectorMin() {
		assertThat(min(new Vector3D(1, 2, 3), new Vector3D(3, 2, 1))).isEqualTo(new Vector3D(1, 2, 1));
	}

}