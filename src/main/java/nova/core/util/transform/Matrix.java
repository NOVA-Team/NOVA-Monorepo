package nova.core.util.transform;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.util.Arrays;

/**
 * A class that represents a matrix.
 */

//TODO: Add unit testing
public class Matrix extends Operator<Matrix, Matrix> implements Cloneable, Transform {
	// number of rows
	public final int rows;
	// number of columns
	public final int columns;
	// rows-by-columns array
	private final double[][] mat;

	// create rows-by-columns matrix of 0's
	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		mat = new double[rows][columns];
	}

	public Matrix(int size) {
		this(size, size);
	}

	// create matrix based on 2d array
	public Matrix(double[][] data) {
		rows = data.length;
		columns = data[0].length;
		mat = new double[rows][columns];

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				mat[i][j] = data[i][j];
	}

	// create and return a random rows-by-columns matrix with values between 0 and 1
	public static Matrix random(int M, int N) {
		Matrix A = new Matrix(M, N);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				A.mat[i][j] = Math.random();
		return A;
	}

	// create and return the columns-by-columns identity matrix
	public static Matrix identity(int size) {
		Matrix I = new Matrix(size, size);
		for (int i = 0; i < size; i++)
			I.mat[i][i] = 1;
		return I;
	}

	public double get(int i, int j) {
		return apply(i, j);
	}

	public double apply(int i, int j) {
		return mat[i][j];
	}

	public void update(int i, int j, double value) {
		mat[i][j] = value;
	}

	/**
	 * Swap rows i and j
	 */
	public Matrix swap(int i, int j) {
		double[] temp = mat[i];
		Matrix C = clone();
		C.mat[i] = mat[j];
		C.mat[j] = temp;
		return C;
	}

	/**
	 * Create and return the transpose of the invoking matrix
	 */
	public Matrix transpose() {
		Matrix A = new Matrix(columns, rows);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				A.mat[j][i] = this.mat[i][j];
		return A;
	}

	@Override
	public Matrix add(double other) {
		Matrix A = new Matrix(columns, rows);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				A.mat[i][j] = mat[i][j] + other;
		return A;
	}

	/**
	 * @return C = A + B
	 */
	@Override
	public Matrix add(Matrix B) {
		Matrix A = this;
		assert B.rows == A.rows && B.columns == A.columns;

		Matrix C = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				C.mat[i][j] = A.mat[i][j] + B.mat[i][j];
		return C;
	}

	@Override
	public Matrix multiply(double other) {
		Matrix A = new Matrix(columns, rows);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				A.mat[i][j] = mat[i][j] * other;
		return A;
	}

	/**
	 * Matrix-matrix multiplication
	 *
	 * @return C = A * B
	 */
	@Override
	public Matrix multiply(Matrix B) {
		Matrix A = this;
		assert A.columns == B.rows;

		Matrix C = new Matrix(A.rows, B.columns);
		for (int i = 0; i < C.rows; i++)
			for (int j = 0; j < C.columns; j++)
				for (int k = 0; k < A.columns; k++)
					C.mat[i][j] += (A.mat[i][k] * B.mat[k][j]);
		return C;
	}

	public boolean isRowVector() {
		return rows == 1;
	}

	public boolean isColumnVector() {
		return columns == 1;
	}

	public boolean isSquare() {
		return columns == rows;
	}

	/**
	 * Augments this matrix with another, appending B's columns on the right side of this matrix.
	 *
	 * @param B The matrix to augment
	 * @return The augmented matrix
	 */
	public Matrix augment(Matrix B) {
		assert rows == B.rows;
		Matrix C = new Matrix(rows, columns + B.columns);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++)
				C.mat[i][j] = mat[i][j];

			for (int j = columns; j < C.columns; j++)
				C.mat[i][j] = B.mat[i][j - columns];
		}
		return C;
	}

	/**
	 * Finds the inverse of the matrix using Gaussian elimination.
	 *
	 * @return The inverse of the matrix
	 */
	@Override
	public Matrix reciprocal() {
		assert isSquare();

		//Augment the matrix with the identity matrix
		//Reduce it to echelon form
		//Retrieve the agumented part of the matrix
		return augment(identity(rows))
			.rref()
			.submatrix(0, rows - 1, columns - 1, 2 * columns - 1);
	}

	/**
	 * @return A submatrix that is within this matrix.
	 * @x1 - The min x bound
	 * @x2 - The max x bound
	 * @y1 - The min y bound
	 * @y2 - The max y bound
	 */
	public Matrix submatrix(int y1, int y2, int x1, int x2) {
		assert x1 < x2 && y1 < y2 && x2 <= columns && y2 <= rows;

		Matrix C = new Matrix(x2 - x1, y2 - y1);

		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				C.mat[x - x1][y - y1] = mat[x][y];
			}
		}

		return C;
	}

	/**
	 * Performs Gaussian elimnation to transform a matrix to echelon form.
	 *
	 * @return The matrix in row reduced echelon form
	 */
	public Matrix rref() {
		double[][] rref = mat.clone();

		for (int p = 0; p < rows; ++p) {
			// Make this pivot 1
			double pv = rref[p][p];
			if (pv != 0) {
				for (int i = 0; i < columns; ++i) {
					rref[p][i] /= pv;
				}
			}

			// Make other rows zero
			for (int r = 0; r < rows; ++r) {
				if (r != p) {
					double f = rref[r][p];
					for (int i = 0; i < columns; ++i) {
						rref[r][i] -= f * rref[p][i];
					}
				}
			}
		}

		return new Matrix(rref);
	}

	/**
	 * Solves a matrix-vector equation, Ax = b.
	 *
	 * @return x = A^-1 b, assuming A is square and has full rank
	 */
	public Matrix solve(Matrix rhs) {
		assert rows == columns && rhs.rows == columns && rhs.columns == 1;

		// create copies of the mat
		Matrix A = this.clone();
		Matrix b = rhs.clone();

		// Gaussian elimination with partial pivoting
		for (int i = 0; i < columns; i++) {

			// find pivot row and swap
			int max = i;
			for (int j = i + 1; j < columns; j++)
				if (Math.abs(A.mat[j][i]) > Math.abs(A.mat[max][i])) {
					max = j;
				}
			A = A.swap(i, max);
			b = b.swap(i, max);

			// singular
			if (A.mat[i][i] == 0.0) {
				throw new RuntimeException("Matrix is singular.");
			}

			// pivot within b
			for (int j = i + 1; j < columns; j++)
				b.mat[j][0] -= b.mat[i][0] * A.mat[j][i] / A.mat[i][i];

			// pivot within A
			for (int j = i + 1; j < columns; j++) {
				double m = A.mat[j][i] / A.mat[i][i];
				for (int k = i + 1; k < columns; k++) {
					A.mat[j][k] -= A.mat[i][k] * m;
				}
				A.mat[j][i] = 0.0;
			}
		}

		// back substitution
		Matrix x = new Matrix(columns, 1);
		for (int j = columns - 1; j >= 0; j--) {
			double t = 0.0;
			for (int k = j + 1; k < columns; k++)
				t += A.mat[j][k] * x.mat[k][0];
			x.mat[j][0] = (b.mat[j][0] - t) / A.mat[j][j];
		}
		return x;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Matrix) {
			Matrix B = (Matrix) obj;
			Matrix A = this;
			if (B.rows == A.rows && B.columns == A.columns) {
				for (int i = 0; i < rows; i++)
					for (int j = 0; j < columns; j++)
						if (A.mat[i][j] != B.mat[i][j]) {
							return false;
						}
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		Hasher hasher = Hashing.goodFastHash(32).newHasher();
		for (double[] array : mat)
			for (double d : array)
				hasher.putDouble(d);

		return hasher.hash().asInt();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Matrix[" + rows + "x" + columns + "]\n");
		for (int i = 0; i < rows; i++)
			sb.append(Arrays.toString(mat[i])).append("\n");
		return sb.toString();
	}

	/**
	 * Transform vector by this matrix. This method is only valid if it is a 4x4 matrix.
	 *
	 * @param vector to be transformed.
	 * @return transformed vector.
	 */
	@Override
	public Vector3d transform(Vector3<?> vector) {
		assert isSquare() && rows == 4;

		double x, y, z, w;
		x = mat[0][0] * vector.xd() + mat[1][0] * vector.yd() + mat[2][0] * vector.zd() + mat[3][0];
		y = mat[0][1] * vector.xd() + mat[1][1] * vector.yd() + mat[2][1] * vector.zd() + mat[3][1];
		z = mat[0][2] * vector.xd() + mat[1][2] * vector.yd() + mat[2][2] * vector.zd() + mat[3][2];
		w = mat[0][3] * vector.xd() + mat[1][3] * vector.yd() + mat[2][3] * vector.zd() + mat[3][3];
		return new Vector3d(x / w, y / w, z / w);
	}

	public boolean isAlmostZero() {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				if (mat[i][j] > 0.0000001) {
					return false;
				}
		return true;
	}

	@Override
	public Matrix clone() {
		return new Matrix(mat);
	}
}
