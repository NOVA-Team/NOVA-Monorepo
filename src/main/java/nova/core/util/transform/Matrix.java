package nova.core.util.transform;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.util.Arrays;

/**
 * A class that represents a matrix.
 */

//TODO: Add unit testing
public class Matrix extends Operator<Matrix, Matrix> implements Cloneable {
	// number of rows
	private final int m;
	// number of columns
	private final int n;
	// m-by-n array
	private final double[][] mat;

	// create m-by-n matrix of 0's
	public Matrix(int M, int N) {
		this.m = M;
		this.n = N;
		mat = new double[M][N];
	}

	public Matrix(int M) {
		this(M, M);
	}

	// create matrix based on 2d array
	public Matrix(double[][] data) {
		m = data.length;
		n = data[0].length;
		this.mat = new double[m][n];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				this.mat[i][j] = data[i][j];
	}

	// create and return a random m-by-n matrix with values between 0 and 1
	public static Matrix random(int M, int N) {
		Matrix A = new Matrix(M, N);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				A.mat[i][j] = Math.random();
		return A;
	}

	// create and return the n-by-n identity matrix
	public static Matrix identity(int size) {
		Matrix I = new Matrix(size, size);
		for (int i = 0; i < size; i++)
			I.mat[i][i] = 1;
		return I;
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
	private void swap(int i, int j) {
		double[] temp = mat[i];
		mat[i] = mat[j];
		mat[j] = temp;
	}

	/**
	 * Create and return the transpose of the invoking matrix
	 */
	public Matrix transpose() {
		Matrix A = new Matrix(n, m);
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				A.mat[j][i] = this.mat[i][j];
		return A;
	}

	@Override
	public Matrix add(double other) {
		Matrix A = new Matrix(n, m);
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				A.mat[i][j] = mat[i][j] + other;
		return A;
	}

	/**
	 * @return C = A + B
	 */
	@Override
	public Matrix add(Matrix B) {
		Matrix A = this;
		assert B.m == A.m && B.n == A.n;

		Matrix C = new Matrix(m, n);
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				C.mat[i][j] = A.mat[i][j] + B.mat[i][j];
		return C;
	}

	@Override
	public Matrix multiply(double other) {
		Matrix A = new Matrix(n, m);
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				A.mat[i][j] = mat[i][j] * other;
		return A;
	}

	/**
	 * Matrix-matrix multiplication
	 * @return C = A * B
	 */
	@Override
	public Matrix multiply(Matrix B) {
		Matrix A = this;
		assert A.n == B.m;

		Matrix C = new Matrix(A.m, B.n);
		for (int i = 0; i < C.m; i++)
			for (int j = 0; j < C.n; j++)
				for (int k = 0; k < A.n; k++)
					C.mat[i][j] += (A.mat[i][k] * B.mat[k][j]);
		return C;
	}

	/**
	 * Finds the inverse of the matrix
	 * @return The inverse of the matrix
	 */
	@Override
	public Matrix reciprocal() {
		//TODO: Implement matrix inverse
		return null;
	}

	public boolean isRowVector() {
		return m == 1;
	}

	public boolean isColumnVector() {
		return n == 1;
	}

	/**
	 * Solves a matrix-vector equation, Ax = b.
	 * @return x = A^-1 b, assuming A is square and has full rank
	 */
	public Matrix solve(Matrix rhs) {
		assert m == n && rhs.m == n && rhs.n == 1;

		// create copies of the mat
		Matrix A = this.clone();
		Matrix b = rhs.clone();

		// Gaussian elimination with partial pivoting
		for (int i = 0; i < n; i++) {

			// find pivot row and swap
			int max = i;
			for (int j = i + 1; j < n; j++)
				if (Math.abs(A.mat[j][i]) > Math.abs(A.mat[max][i])) {
					max = j;
				}
			A.swap(i, max);
			b.swap(i, max);

			// singular
			if (A.mat[i][i] == 0.0) {
				throw new RuntimeException("Matrix is singular.");
			}

			// pivot within b
			for (int j = i + 1; j < n; j++)
				b.mat[j][0] -= b.mat[i][0] * A.mat[j][i] / A.mat[i][i];

			// pivot within A
			for (int j = i + 1; j < n; j++) {
				double m = A.mat[j][i] / A.mat[i][i];
				for (int k = i + 1; k < n; k++) {
					A.mat[j][k] -= A.mat[i][k] * m;
				}
				A.mat[j][i] = 0.0;
			}
		}

		// back substitution
		Matrix x = new Matrix(n, 1);
		for (int j = n - 1; j >= 0; j--) {
			double t = 0.0;
			for (int k = j + 1; k < n; k++)
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
			assert B.m == A.m && B.n == A.n;

			for (int i = 0; i < m; i++)
				for (int j = 0; j < n; j++)
					if (A.mat[i][j] != B.mat[i][j]) {
						return false;
					}
			return true;
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
		sb.append("Matrix[" + m + "," + n + "]\n");
		for (int i = 0; i < m; i++)
			sb.append(Arrays.toString(mat[i])).append("\n");
		return sb.toString();
	}

	@Override
	public Matrix clone() {
		return new Matrix(mat);
	}
}
