
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
import org.junit.BeforeClass;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author Kubuxu
 */

public class MathUtilTest {
	@BeforeClass
	public static void hiddenConstructor() {
		try {
			// Needed for 100% code coverage because MathUtil has a private constructor as it is a utility class.
			java.lang.reflect.Constructor<MathUtil> c = MathUtil.class.getDeclaredConstructor();
			c.setAccessible(true);
			c.newInstance();
			c.setAccessible(false);
		} catch (Exception e) {}
	}

	@Override
	public String toString() {
		return super.toString(); //To change body of generated methods, choose Tools | Templates.
	}

    @Test
    public void testMax() {
        assertThat(MathUtil.max(1, 2)).isEqualTo(2);
        assertThat(MathUtil.max(2, 1)).isEqualTo(2);

        assertThat(MathUtil.max(0, 1, 2)).isEqualTo(2);
        assertThat(MathUtil.max(0, 2, 1)).isEqualTo(2);
        assertThat(MathUtil.max(2, 0, 1)).isEqualTo(2);

        assertThat(MathUtil.max(0, 1, 2, 4)).isEqualTo(4);
        assertThat(MathUtil.max(0, 4, 2, 1)).isEqualTo(4);
        assertThat(MathUtil.max(4, 2, 0, 1)).isEqualTo(4);

        assertThat(MathUtil.max(1l, 2l)).isEqualTo(2l);
        assertThat(MathUtil.max(2l, 1l)).isEqualTo(2l);

        assertThat(MathUtil.max(0l, 1l, 2l)).isEqualTo(2l);
        assertThat(MathUtil.max(0l, 2l, 1l)).isEqualTo(2l);
        assertThat(MathUtil.max(2l, 0l, 1l)).isEqualTo(2l);

        assertThat(MathUtil.max(0l, 1l, 2l, 4l)).isEqualTo(4l);
        assertThat(MathUtil.max(0l, 4l, 2l, 1l)).isEqualTo(4l);
        assertThat(MathUtil.max(4l, 2l, 0l, 1l)).isEqualTo(4l);

        assertThat(MathUtil.max(1d, 2d)).isEqualTo(2d);
        assertThat(MathUtil.max(2d, 1d)).isEqualTo(2d);

        assertThat(MathUtil.max(0d, 1d, 2d)).isEqualTo(2d);
        assertThat(MathUtil.max(0d, 2d, 1d)).isEqualTo(2d);
        assertThat(MathUtil.max(2d, 0d, 1d)).isEqualTo(2d);

        assertThat(MathUtil.max(0d, 1d, 2d, 4d)).isEqualTo(4d);
        assertThat(MathUtil.max(0d, 4d, 2d, 1d)).isEqualTo(4d);
        assertThat(MathUtil.max(4d, 2d, 0d, 1d)).isEqualTo(4d);

	    assertThat(MathUtil.max(1d, 2d)).isEqualTo(2d);
	    assertThat(MathUtil.max(2d, 1d)).isEqualTo(2d);

	    assertThat(MathUtil.max(0f, 1f, 2f)).isEqualTo(2f);
	    assertThat(MathUtil.max(0f, 2f, 1f)).isEqualTo(2f);
	    assertThat(MathUtil.max(2f, 0f, 1f)).isEqualTo(2f);

	    assertThat(MathUtil.max(0f, 1f, 2f, 4f)).isEqualTo(4f);
	    assertThat(MathUtil.max(0f, 4f, 2f, 1f)).isEqualTo(4f);
	    assertThat(MathUtil.max(4f, 2f, 0f, 1f)).isEqualTo(4f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxErrorInt() {
        MathUtil.max(new int[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxErrorLong() {
        MathUtil.max(new long[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxErrorDouble() {
        MathUtil.max(new double[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxErrorFloat() {
        MathUtil.max(new float[]{});
    }

    @Test
    public void testMin() {
        assertThat(MathUtil.min(1, 2)).isEqualTo(1);
        assertThat(MathUtil.min(2, 1)).isEqualTo(1);

        assertThat(MathUtil.min(0, 1, 2)).isEqualTo(0);
        assertThat(MathUtil.min(0, 2, 1)).isEqualTo(0);
        assertThat(MathUtil.min(2, 0, 1)).isEqualTo(0);

        assertThat(MathUtil.min(0, 1, 2, 4)).isEqualTo(0);
        assertThat(MathUtil.min(0, 4, 2, 1)).isEqualTo(0);
        assertThat(MathUtil.min(4, 2, 0, 1)).isEqualTo(0);

        assertThat(MathUtil.min(1l, 2l)).isEqualTo(1l);
        assertThat(MathUtil.min(2l, 1l)).isEqualTo(1l);

        assertThat(MathUtil.min(0l, 1l, 2l)).isEqualTo(0l);
        assertThat(MathUtil.min(0l, 2l, 1l)).isEqualTo(0l);
        assertThat(MathUtil.min(2l, 0l, 1l)).isEqualTo(0l);

        assertThat(MathUtil.min(0l, 1l, 2l, 4l)).isEqualTo(0l);
        assertThat(MathUtil.min(0l, 4l, 2l, 1l)).isEqualTo(0l);
        assertThat(MathUtil.min(4l, 2l, 0l, 1l)).isEqualTo(0l);

        assertThat(MathUtil.min(1d, 2d)).isEqualTo(1d);
        assertThat(MathUtil.min(2d, 1d)).isEqualTo(1d);

        assertThat(MathUtil.min(0d, 1d, 2d)).isEqualTo(0d);
        assertThat(MathUtil.min(0d, 2d, 1d)).isEqualTo(0d);
        assertThat(MathUtil.min(2d, 0d, 1d)).isEqualTo(0d);

        assertThat(MathUtil.min(0d, 1d, 2d, 4d)).isEqualTo(0d);
        assertThat(MathUtil.min(0d, 4d, 2d, 1d)).isEqualTo(0d);
        assertThat(MathUtil.min(4d, 2d, 0d, 1d)).isEqualTo(0d);

	    assertThat(MathUtil.min(1f, 2f)).isEqualTo(1f);
	    assertThat(MathUtil.min(2f, 1f)).isEqualTo(1f);

	    assertThat(MathUtil.min(0f, 1f, 2f)).isEqualTo(0f);
	    assertThat(MathUtil.min(0f, 2f, 1f)).isEqualTo(0f);
	    assertThat(MathUtil.min(2f, 0f, 1f)).isEqualTo(0f);

	    assertThat(MathUtil.min(0f, 1f, 2f, 4f)).isEqualTo(0f);
	    assertThat(MathUtil.min(0f, 4f, 2f, 1f)).isEqualTo(0f);
	    assertThat(MathUtil.min(4f, 2f, 0f, 1f)).isEqualTo(0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinErrorInt() {
        MathUtil.min(new int[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinErrorLong() {
        MathUtil.min(new long[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinErrorDouble() {
        MathUtil.min(new double[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinErrorFloat() {
        MathUtil.min(new float[]{});
    }

    @Test
    public void testClamp() {
	    assertThat(MathUtil.clamp(6, 2, 3)).isEqualTo(3);
	    assertThat(MathUtil.clamp(2, 2, 3)).isEqualTo(2);
	    assertThat(MathUtil.clamp(3, 2, 3)).isEqualTo(3);
	    assertThat(MathUtil.clamp(1, 2, 3)).isEqualTo(2);

	    assertThat(MathUtil.clamp(6l, 2l, 3l)).isEqualTo(3l);
	    assertThat(MathUtil.clamp(2l, 2l, 3l)).isEqualTo(2l);
	    assertThat(MathUtil.clamp(3l, 2l, 3l)).isEqualTo(3l);
	    assertThat(MathUtil.clamp(1l, 2l, 3l)).isEqualTo(2l);

	    assertThat(MathUtil.clamp(6d, 2d, 3d)).isEqualTo(3);
	    assertThat(MathUtil.clamp(2d, 2d, 3d)).isEqualTo(2);
	    assertThat(MathUtil.clamp(3d, 2d, 3d)).isEqualTo(3);
	    assertThat(MathUtil.clamp(1d, 2d, 3d)).isEqualTo(2);

	    assertThat(MathUtil.clamp(6f, 2f, 3f)).isEqualTo(3);
	    assertThat(MathUtil.clamp(2f, 2f, 3f)).isEqualTo(2);
	    assertThat(MathUtil.clamp(3f, 2f, 3f)).isEqualTo(3);
	    assertThat(MathUtil.clamp(1f, 2f, 3f)).isEqualTo(2);
    }

    @Test
    public void testAbsClamp() {
	    assertThat(MathUtil.absClamp( 2, 1)).isEqualTo( 1);
	    assertThat(MathUtil.absClamp( 1, 1)).isEqualTo( 1);
	    assertThat(MathUtil.absClamp( 0, 1)).isEqualTo( 0);
	    assertThat(MathUtil.absClamp(-1, 1)).isEqualTo(-1);
	    assertThat(MathUtil.absClamp(-2, 1)).isEqualTo(-1);

	    assertThat(MathUtil.absClamp( 2l, 1l)).isEqualTo( 1);
	    assertThat(MathUtil.absClamp( 1l, 1l)).isEqualTo( 1);
	    assertThat(MathUtil.absClamp( 0l, 1l)).isEqualTo( 0);
	    assertThat(MathUtil.absClamp(-1l, 1l)).isEqualTo(-1);
	    assertThat(MathUtil.absClamp(-2l, 1l)).isEqualTo(-1);

	    assertThat(MathUtil.absClamp( 2d, 1d)).isEqualTo( 1);
	    assertThat(MathUtil.absClamp( 1d, 1d)).isEqualTo( 1);
	    assertThat(MathUtil.absClamp( 0d, 1d)).isEqualTo( 0);
	    assertThat(MathUtil.absClamp(-1d, 1d)).isEqualTo(-1);
	    assertThat(MathUtil.absClamp(-2d, 1d)).isEqualTo(-1);

	    assertThat(MathUtil.absClamp( 2f, 1f)).isEqualTo( 1);
	    assertThat(MathUtil.absClamp( 1f, 1f)).isEqualTo( 1);
	    assertThat(MathUtil.absClamp( 0f, 1f)).isEqualTo( 0);
	    assertThat(MathUtil.absClamp(-1f, 1f)).isEqualTo(-1);
	    assertThat(MathUtil.absClamp(-2f, 1f)).isEqualTo(-1);
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

	@Test
	public void testLog() {
		assertThat(MathUtil.log(1, 2)).isEqualTo(0);
		assertThat(MathUtil.log(1l, 2l)).isEqualTo(0);
		assertThat(MathUtil.log(1d, 2d)).isEqualTo(0);
		assertThat(MathUtil.log(1f, 2f)).isEqualTo(0);
	}

	@Test
	public void testRoundDecimals() {
		assertThat(MathUtil.roundDecimals(10, 0)).isEqualTo(10);
		assertThat(MathUtil.roundDecimals(10.5, 0)).isEqualTo(11);
		assertThat(MathUtil.roundDecimals(11, 0)).isEqualTo(11);
	}

	@Test
	public void testBetween() {
		assertThat(MathUtil.isBetween(1, 0, 3)).isFalse();
		assertThat(MathUtil.isBetween(1, 2, 3)).isTrue();
		assertThat(MathUtil.isBetween(1, 4, 3)).isFalse();
	}

	@Test
	public void testToString() {
		assertThat(MathUtil.toString(0, false)).isEqualTo("0.0");
		assertThat(MathUtil.toString(0.5, false)).isEqualTo("0.5");
		assertThat(MathUtil.toString(1, false)).isEqualTo("1.0");
		assertThat(MathUtil.toString(0, true)).isEqualTo("0");
		assertThat(MathUtil.toString(0.5, true)).isEqualTo("0.5");
		assertThat(MathUtil.toString(1, true)).isEqualTo("1");
	}
}

