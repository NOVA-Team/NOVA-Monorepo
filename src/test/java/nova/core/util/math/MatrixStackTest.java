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

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

import java.util.EmptyStackException;

import static nova.testutils.NovaAssertions.assertThat;

public class MatrixStackTest {
	MatrixStack ms;

	@Before
	public void setUp() {
		ms = new MatrixStack();
	}

	@Test(expected = EmptyStackException.class)
	public void testThrowsOnEmpty() {
		ms.popMatrix();
	}

	@Test
	public void testStack() {
		RealMatrix one = TransformUtil.translationMatrix(1, 0, 0);
		RealMatrix two = TransformUtil.translationMatrix(0, 1, 0);
		RealMatrix three = TransformUtil.translationMatrix(0, 0, 1);
		ms.loadMatrix(one);
		ms.pushMatrix();
		ms.loadMatrix(two);
		ms.pushMatrix();
		ms.loadIdentity();
		ms.pushMatrix();
		ms.loadMatrix(three);
		ms.pushMatrix();
		ms.loadIdentity();

		assertThat(ms.getMatrix()).isEqualTo(MatrixUtils.createRealIdentityMatrix(4));
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(three);
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(MatrixUtils.createRealIdentityMatrix(4));
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(two);
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(one);
	}

	@Test
	public void testTransforms() {
		ms.translate(Vector3DUtil.ONE);
		ms.scale(Vector3DUtil.ONE.scalarMultiply(2));
		ms.pushMatrix();
		ms.rotate(Vector3D.PLUS_J, Math.PI / 2);
		assertThat(ms.apply(Vector3D.PLUS_K)).isAlmostEqualTo(new Vector3D(-1, 1, 1));

		ms.popMatrix();
		ms.transform(MatrixUtils.createRealMatrix(new Rotation(Vector3D.PLUS_J, Math.PI / 2).getMatrix()));
		assertThat(ms.apply(Vector3D.PLUS_K)).isAlmostEqualTo(new Vector3D(-1, 1, 1));

		assertThat(ms.apply(Vector3DUtil.ONE)).isAlmostEqualTo(ms.apply(Vector3DUtil.ONE));

	}

	@Test
	public void testCloneCtor() {
		ms.translate(Vector3DUtil.ONE);
		ms.scale(Vector3DUtil.ONE.scalarMultiply(2));
		ms.pushMatrix();
		ms.rotate(Vector3D.PLUS_J, Math.PI / 2);
		MatrixStack ms2 = new MatrixStack(ms);
		assertThat(ms.apply(Vector3D.PLUS_K)).isEqualTo(ms2.apply(Vector3D.PLUS_K));
		ms.popMatrix();
		ms2.popMatrix();
		assertThat(ms.apply(Vector3D.PLUS_K)).isEqualTo(ms2.apply(Vector3D.PLUS_K));
	}

	@Test
	public void testMatrixLoadingCtor() {
		ms.translate(Vector3DUtil.ONE);
		MatrixStack ms2 = new MatrixStack(ms.getMatrix());
		assertThat(ms.apply(Vector3D.PLUS_K)).isEqualTo(ms2.apply(Vector3D.PLUS_K));
	}
}
