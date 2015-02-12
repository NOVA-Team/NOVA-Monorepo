package nova.core.util.transform;

import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.math.DoubleMath;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;

/**
 *  4x4 Matrix for 3D Vector transforms. It is immutable.
 */
public class Matrix implements Cloneable, Transform{

	/**
	 * 4x4 array [row][column]
	 */
	private double[][] mat;

	/**
	 * Creates Matrix form 2D double array creating defensive copy of it.
	 * @param mat 4x4 array to create Matrix form.
	 */
	public Matrix(double[][] mat) {
		if (mat.length != 4)
			throw new IllegalArgumentException("Template array has to bye 4x4");
		this.mat = new double[4][];
		for (int i = 0; i < 4; i++) {
			if (mat[i].length != 4)
				throw new IllegalArgumentException("Template array has to bye 4x4");
			this.mat[i] = Arrays.copyOf(mat[i],4);
		}
	}

	/**
	 * Copy constructor
	 * @param old matrix to copy.
	 */
	public Matrix(Matrix old) {
		this(old.mat);
	}

	/**
	 * Use Matrix.IDENTITY instead.
	 */
	@Deprecated()
	public Matrix() {
		this(Matrix.IDENTITY);
	}

	/**
	 * Identity matrix.
	 */
	public static Matrix IDENTITY = new Matrix(new double[][] {
		{ 1, 0, 0, 0 },
		{ 0, 1, 0, 0 },
		{ 0, 0, 1, 0 },
		{ 0, 0, 0, 1 }});

	/**
	 *  Cross multiples matrices.
	 * @param other matrix to multiply by.
	 * @return this x other
	 */
	public Matrix multiply(Matrix other) {
		double[][] res = new double[4][4];

		//Let JIT and loop unrolling do their work.
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j <4; j++) {
				for (int k = 0; k < 4; k++) {
					res[i][j] += this.mat[i][k] * other.mat[k][j];
				}
			}
		}
		return new Matrix(res);
	}

	/**
	 *  Reverse cross multiples matrices..
	 * @param other matrix to be multiplied by.
	 * @return other x this
	 */
	public Matrix rightlyMultiply(Matrix other) {
		return other.multiply(this);
	}

	/**
	 * Transposes matrix.
	 * @return transposed matrix
	 */
	public Matrix transpose() {
		double[][] res = new double[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				res[j][i] = mat[i][j];
			}
		}
		return new Matrix(res);
	}

	/**
	 * Transform vector by this matrix.
	 * @param vector to be transformed.
	 * @return transformed vector.
	 */
	@Override
	public Vector3d transform(Vector3<?> vector) {
		double x,y,z,w;
		x = mat[0][0]*vector.xd() + mat[1][0]*vector.yd() + mat[2][0]*vector.zd() + mat[3][0];
		y = mat[0][1]*vector.xd() + mat[1][1]*vector.yd() + mat[2][1]*vector.zd() + mat[3][1];
		z = mat[0][2]*vector.xd() + mat[1][2]*vector.yd() + mat[2][2]*vector.zd() + mat[3][2];
		w = mat[0][3]*vector.xd() + mat[1][3]*vector.yd() + mat[2][3]*vector.zd() + mat[3][3];
		return new Vector3d(x / w, y / w, z / w);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		} else if (obj instanceof Matrix) {
			Matrix other = (Matrix) obj;
			boolean res = true;
			outer:
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					res = DoubleMath.fuzzyEquals(this.mat[i][j], other.mat[i][j], 0.00001);
					if (!res) {
						break outer;
					}
				}
			}
			return res;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		Hasher hasher = Hashing.goodFastHash(32).newHasher();
		for(double[] array : mat)
			for(double d : array)
				hasher.putDouble(d);

		return  hasher.hash().asInt();
	}

	@Override
	public Matrix clone() {
		return new Matrix(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName()).append('\n');
		sb.append(Arrays.toString(mat[0])).append('\n');
		sb.append(Arrays.toString(mat[1])).append('\n');
		sb.append(Arrays.toString(mat[2])).append('\n');
		sb.append(Arrays.toString(mat[3])).append('\n');

		return sb.toString();
	}
}
