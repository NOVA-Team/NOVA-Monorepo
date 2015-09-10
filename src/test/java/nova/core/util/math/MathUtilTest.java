
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
import org.assertj.core.data.Offset;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;


/**
 * @author Kubuxu
 */

public class MathUtilTest {
    @Test
    public void testMax() {
        assertThat(MathUtil.max(1,2)).isEqualTo(2);
        assertThat(MathUtil.max(2,1)).isEqualTo(2);

        assertThat(MathUtil.max(0,1,2)).isEqualTo(2);
        assertThat(MathUtil.max(0,2,1)).isEqualTo(2);
        assertThat(MathUtil.max(2,0,1)).isEqualTo(2);

        assertThat(MathUtil.max(0,1,2,4)).isEqualTo(4);
        assertThat(MathUtil.max(0,4,2,1)).isEqualTo(4);
        assertThat(MathUtil.max(4,2,0,1)).isEqualTo(4);

        assertThat(MathUtil.max(1d,2d)).isEqualTo(2d);
        assertThat(MathUtil.max(2d,1d)).isEqualTo(2d);

        assertThat(MathUtil.max(0d,1d,2d)).isEqualTo(2d);
        assertThat(MathUtil.max(0d,2d,1d)).isEqualTo(2d);
        assertThat(MathUtil.max(2d,0d,1d)).isEqualTo(2d);

        assertThat(MathUtil.max(0d,1d,2d,4d)).isEqualTo(4d);
        assertThat(MathUtil.max(0d,4d,2d,1d)).isEqualTo(4d);
        assertThat(MathUtil.max(4d,2d,0d,1d)).isEqualTo(4d);

	    assertThat(MathUtil.max(1d,2d)).isEqualTo(2d);
	    assertThat(MathUtil.max(2d,1d)).isEqualTo(2d);

	    assertThat(MathUtil.max(0f,1f,2f)).isEqualTo(2f);
	    assertThat(MathUtil.max(0f,2f,1f)).isEqualTo(2f);
	    assertThat(MathUtil.max(2f,0f,1f)).isEqualTo(2f);

	    assertThat(MathUtil.max(0f,1f,2f,4f)).isEqualTo(4f);
	    assertThat(MathUtil.max(0f,4f,2f,1f)).isEqualTo(4f);
	    assertThat(MathUtil.max(4f,2f,0f,1f)).isEqualTo(4f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxError() {
        MathUtil.max();

    }
    @Test(expected = IllegalArgumentException.class)
    public void testMaxErrorDouble() {
        MathUtil.max(new double[]{});
    }


    @Test
    public void testMin() {
        assertThat(MathUtil.min(1, 2)).isEqualTo(1);
        assertThat(MathUtil.min(2, 1)).isEqualTo(1);

        assertThat(MathUtil.min(0,1,2)).isEqualTo(0);
        assertThat(MathUtil.min(0, 2, 1)).isEqualTo(0);
        assertThat(MathUtil.min(2, 0, 1)).isEqualTo(0);

        assertThat(MathUtil.min(0, 1, 2, 4)).isEqualTo(0);
        assertThat(MathUtil.min(0, 4, 2, 1)).isEqualTo(0);
        assertThat(MathUtil.min(4, 2, 0, 1)).isEqualTo(0);

        assertThat(MathUtil.min(1d, 2d)).isEqualTo(1d);
        assertThat(MathUtil.min(2d, 1d)).isEqualTo(1d);

        assertThat(MathUtil.min(0d,1d,2d)).isEqualTo(0d);
        assertThat(MathUtil.min(0d, 2d, 1d)).isEqualTo(0d);
        assertThat(MathUtil.min(2d, 0d, 1d)).isEqualTo(0d);

        assertThat(MathUtil.min(0d, 1d, 2d, 4d)).isEqualTo(0d);
        assertThat(MathUtil.min(0d, 4d, 2d, 1d)).isEqualTo(0d);
        assertThat(MathUtil.min(4d, 2d, 0d, 1d)).isEqualTo(0d);
	    
	    assertThat(MathUtil.min(1f, 2f)).isEqualTo(1f);
	    assertThat(MathUtil.min(2f, 1f)).isEqualTo(1f);

	    assertThat(MathUtil.min(0f,1f,2f)).isEqualTo(0f);
	    assertThat(MathUtil.min(0f, 2f, 1f)).isEqualTo(0f);
	    assertThat(MathUtil.min(2f, 0f, 1f)).isEqualTo(0f);

	    assertThat(MathUtil.min(0f, 1f, 2f, 4f)).isEqualTo(0f);
	    assertThat(MathUtil.min(0f, 4f, 2f, 1f)).isEqualTo(0f);
	    assertThat(MathUtil.min(4f, 2f, 0f, 1f)).isEqualTo(0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinError() {
        MathUtil.min();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinErrorDouble() {
        MathUtil.min(new double[]{});
    }

    @Test
    public void testClamp() {
	    assertThat(MathUtil.clamp(6, 2, 3)).isEqualTo(3);
	    assertThat(MathUtil.clamp(2, 2, 3)).isEqualTo(2);
	    assertThat(MathUtil.clamp(3, 2, 3)).isEqualTo(3);
	    assertThat(MathUtil.clamp(1, 2, 3)).isEqualTo(2);

	    assertThat(MathUtil.clamp(6D, 2, 3)).isEqualTo(3);
	    assertThat(MathUtil.clamp(2D, 2, 3)).isEqualTo(2);
	    assertThat(MathUtil.clamp(3D, 2, 3)).isEqualTo(3);
	    assertThat(MathUtil.clamp(1D, 2, 3)).isEqualTo(2);
    }

	@Test
	public void testLerp() {
		Offset<Double> offsetD = Offset.offset(1e-11);
		Offset<Float> offsetF = Offset.offset(1e-7F);
		assertThat(MathUtil.lerp(1D, 2D, 0)).isCloseTo(1, offsetD);
		assertThat(MathUtil.lerp(1D, 2D, .5)).isCloseTo(1.5, offsetD);
		assertThat(MathUtil.lerp(1D, 2D, 1)).isCloseTo(2, offsetD);

		assertThat(MathUtil.lerp(1F, 2F, 0F)).isCloseTo(1F, offsetF);
		assertThat(MathUtil.lerp(1F, 2F, .5F)).isCloseTo(1.5F, offsetF);
		assertThat(MathUtil.lerp(1F, 2F, 1F)).isCloseTo(2F, offsetF);

		assertThat(MathUtil.lerp(Vector3D.ZERO, Vector3DUtil.ONE, 0)).isAlmostEqualTo(Vector3D.ZERO);
		assertThat(MathUtil.lerp(Vector3D.ZERO, Vector3DUtil.ONE, .5F)).isAlmostEqualTo(Vector3DUtil.ONE.scalarMultiply(0.5));
		assertThat(MathUtil.lerp(Vector3D.ZERO, Vector3DUtil.ONE, 1)).isAlmostEqualTo(Vector3DUtil.ONE);
	}

}

