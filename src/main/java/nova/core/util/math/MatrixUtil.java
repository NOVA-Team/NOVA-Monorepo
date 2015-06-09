package nova.core.util.math;

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

    public static RealMatrix augmentWithIdenetity(RealMatrix matrix, int dimensions) {

        RealMatrix augmented = augment(matrix, dimensions, dimensions);
        for (int i = MathUtil.max(matrix.getRowDimension(), matrix.getColumnDimension()); i <= dimensions; i++) {
            augmented.setEntry(i - 1, i - 1, 1);
        }

        return augmented;
    }


    // Little cheat.
    private static Localizable of(String string) {
        return new Localizable() {
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
