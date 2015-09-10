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

package nova.core.util.transform;

import nova.core.util.math.TransformUtil;
import nova.core.util.math.Vector3DUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

import static java.lang.Math.PI;
import static nova.testutils.NovaAssertions.assertThat;

public class TransformUtilTest {
	@Test
	public void testTranslation() {
		Vector3D start = Vector3D.ZERO;
		assertThat(TransformUtil.transform(start, TransformUtil.translationMatrix(0, 0, 0))).isEqualTo(start);
		Vector3D by = new Vector3D(3, 0, -6);
		assertThat(TransformUtil.transform(start, TransformUtil.translationMatrix(by))).isEqualTo(by);
		start = Vector3DUtil.ONE;
		assertThat(TransformUtil.transform(start, TransformUtil.translationMatrix(by))).isEqualTo(start.add(by));
	}

	@Test
	public void testScale() {
		Vector3D start = Vector3DUtil.ONE;
		assertThat(TransformUtil.transform(start, TransformUtil.scaleMatrix(0, 0, 0))).isEqualTo(Vector3D.ZERO);
		Vector3D by = new Vector3D(3, 0, -6);
		assertThat(TransformUtil.transform(start, TransformUtil.scaleMatrix(by))).isEqualTo(by);
		start = start.scalarMultiply(2);
		assertThat(TransformUtil.transform(start, TransformUtil.scaleMatrix(by)))
			.isEqualTo(Vector3DUtil.cartesianProduct(start, by));
	}

	@Test
	public void testRotate() {
		Vector3D start = Vector3D.PLUS_K;
		assertThat(TransformUtil.transformDirectionless(start, MatrixUtils.createRealMatrix(new Rotation(Vector3D.PLUS_J, -PI / 2).getMatrix()))).isAlmostEqualTo(Vector3D.PLUS_I);
		assertThat(TransformUtil.transformDirectionless(start, MatrixUtils.createRealMatrix(new Rotation(Vector3D.PLUS_J.scalarMultiply(2), -PI / 2).getMatrix()))).isAlmostEqualTo(Vector3D.PLUS_I);
	}
}
