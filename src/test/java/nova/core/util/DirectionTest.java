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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author Calclavia
 */
public class DirectionTest {

	@Test
	public void testOrdinal() {
		for (int i = 0; i < 6; i++) {
			Direction actual = Direction.fromOrdinal(i);
			assertThat(actual.ordinal()).isEqualTo(i);
		}
	}

	@Test
	public void testVector() {
		Vector3D v1 = new Vector3D(1, 2, 1).normalize();
		assertThat(Direction.fromVector(v1)).isEqualTo(Direction.UP);

		Vector3D v2 = new Vector3D(2, -3, 1).normalize();
		assertThat(Direction.fromVector(v2)).isEqualTo(Direction.DOWN);

		Vector3D v3 = new Vector3D(4, -3, -11).normalize();
		assertThat(Direction.fromVector(v3)).isEqualTo(Direction.NORTH);

		Vector3D v4 = new Vector3D(4, -3, 6).normalize();
		assertThat(Direction.fromVector(v4)).isEqualTo(Direction.SOUTH);

		Vector3D v5 = new Vector3D(4, -3, 1).normalize();
		assertThat(Direction.fromVector(v5)).isEqualTo(Direction.EAST);

		Vector3D v6 = new Vector3D(-4, -3, 1).normalize();
		assertThat(Direction.fromVector(v6)).isEqualTo(Direction.WEST);
	}

	@Test
	public void testOpposite() {
		assertThat(Direction.DOWN.opposite()).isEqualTo(Direction.UP);
		assertThat(Direction.EAST.opposite().opposite()).isEqualTo(Direction.EAST);
		assertThat(Direction.UNKNOWN.opposite()).isEqualTo(Direction.UNKNOWN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void ordinalTooSmall() {
		Direction.fromOrdinal(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void ordinalTooLarge() {
		Direction.fromOrdinal(7);
	}

}
