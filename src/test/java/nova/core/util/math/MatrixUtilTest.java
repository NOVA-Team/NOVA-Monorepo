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


