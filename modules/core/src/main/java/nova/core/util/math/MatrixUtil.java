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
 */package nova.core.util.math;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Locale;

/**
 * @author Kubuxu
 */
public class MatrixUtil {

    public static RealMatrix augment(RealMatrix matrix, int rows, int columns) {
        if (matrix.getRowDimension() > rows)
            throw new DimensionMismatchException(of("rows: {0} !< {1}"), matrix.getRowDimension(), rows);
        if (matrix.getColumnDimension() > columns)
            throw new DimensionMismatchException(of("columns: {0} !< {1}"), matrix.getColumnDimension(), columns);

        RealMatrix augmented = MatrixUtils.createRealMatrix(rows, columns);
        augmented.setSubMatrix(matrix.getData(), 0, 0);
        return augmented;
    }

    public static RealMatrix augmentWithIdentity(RealMatrix matrix, int dimensions) {

        RealMatrix augmented = augment(matrix, dimensions, dimensions);
        for (int i = MathUtil.max(matrix.getRowDimension(), matrix.getColumnDimension()) + 1; i <= dimensions; i++) {
            augmented.setEntry(i - 1, i - 1, 1);
        }

        return augmented;
    }


    // Little cheat.
    private static Localizable of(String string) {
        return new Localizable() {
			private static final long serialVersionUID = 1L;

            @Override
            public String getSourceString() {
                return string;
            }

            @Override
            public String getLocalizedString(Locale locale) {
                return string;
            }
        };
    }
}
