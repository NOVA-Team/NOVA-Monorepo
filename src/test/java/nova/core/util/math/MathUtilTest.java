
package nova.core.util.math;

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
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinError() {
        MathUtil.min();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinErrorDouble() {
        MathUtil.min(new double[]{});
    }



}

