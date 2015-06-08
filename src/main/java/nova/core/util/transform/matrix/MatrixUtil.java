package nova.core.util.transform.matrix;

import com.google.common.math.DoubleMath;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public final class MatrixUtil {

	private MatrixUtil() {
	}

	/**
	 * Identity matrix.
	 */
	public static RealMatrix identity(int size) {
		Array2DRowRealMatrix array2DRowRealMatrix = new Array2DRowRealMatrix(size, size);
		for (int i = 0; i < size; i++)
			array2DRowRealMatrix.setEntry(i, i, 1);
		return array2DRowRealMatrix;
	}

	/**
	 * Creates translation matrix.
	 *
	 * @param x translation.
	 * @param y translation.
	 * @param z translation.
	 * @return translation matrix.
	 */
	public static RealMatrix translationMatrix(double x, double y, double z) {
		return new Array2DRowRealMatrix(new double[][] {
			{ 1, 0, 0, 0 },
			{ 0, 1, 0, 0 },
			{ 0, 0, 1, 0 },
			{ x, y, z, 1 } });
	}

	/**
	 * Creates translation matrix.
	 *
	 * @param translationVector which components are translation parameters.
	 * @return translation matrix.
	 */
	public static RealMatrix translationMatrix(Vector3D translationVector) {
		return translationMatrix(translationVector.getX(), translationVector.getY(), translationVector.getZ());
	}

	/**
	 * Creates scale matrix.
	 *
	 * @param x scale.
	 * @param y scale.
	 * @param z scale.
	 * @return scale matrix.
	 */
	public static RealMatrix scaleMatrix(double x, double y, double z) {
		return new Array2DRowRealMatrix(new double[][] {
			{ x, 0, 0, 0 },
			{ 0, y, 0, 0 },
			{ 0, 0, z, 0 },
			{ 0, 0, 0, 1 } });
	}

	/**
	 * Creates scale matrix.
	 *
	 * @param scaleVector which components are scale parameters.
	 * @return scale matrix.
	 */
	public static RealMatrix scaleMatrix(Vector3D scaleVector) {
		return scaleMatrix(scaleVector.getX(), scaleVector.getY(), scaleVector.getZ());
	}

}
