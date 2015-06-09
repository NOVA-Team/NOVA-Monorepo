package nova.core.util.math;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author Kubuxu
 */
public class MatrixUtilTest {

    @Test
    public void augmentWitIdentityTest() {
        assertThat(MatrixUtil.augmentWithIdentity(MatrixUtils.createRealMatrix(1, 1), 2))
                .isEqualTo(MatrixUtils.createRealMatrix(new double[][]{{0, 0}, {0, 1}}));
    }

    @Test(expected = DimensionMismatchException.class)
    public void augmentDimensionsErrorColumnTest() {
        MatrixUtil.augmentWithIdentity(MatrixUtils.createRealMatrix(2, 3), 2);
    }
    @Test(expected = DimensionMismatchException.class)
    public void augmentDimensionsErrorRowTest() {
        MatrixUtil.augmentWithIdentity(MatrixUtils.createRealMatrix(2, 3), 2);
    }

    @Test
    public void passThrough() {
        assertThat(MatrixUtil.augmentWithIdentity(MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}}),2))
                .isEqualTo(MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}}));
    }


    }

