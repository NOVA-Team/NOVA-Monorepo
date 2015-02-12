package nova.core.util.transform;

import com.google.common.math.DoubleMath;

public class MatrixHelper {

	private MatrixHelper() {}

	/**
	 * Creates translation matrix.
	 * @param x translation.
	 * @param y translation.
	 * @param z translation.
	 * @return translation matrix.
	 */
	public static Matrix4x4 translationMatrix(double x, double y, double z) {
		return new Matrix4x4(new double[][] {
			{ 1, 0, 0, 0 },
			{ 0, 1, 0, 0 },
			{ 0, 0, 1, 0 },
			{ x, y, z, 1 } });
	}

	/**
	 * Creates translation matrix.
	 * @param translationVector which components are translation parameters.
	 * @return translation matrix.
	 */
	public static Matrix4x4 translationMatrix(Vector3<?> translationVector) {
		return translationMatrix(translationVector.xd(), translationVector.yd(), translationVector.zd());
	}

	/**
	 * Creates scale matrix.
	 * @param x scale.
	 * @param y scale.
	 * @param z scale.
	 * @return scale matrix.
	 */
	public static Matrix4x4 scaleMatrix(double x, double y, double z) {
		return new Matrix4x4(new double[][] {
			{ x, 0, 0, 0 },
			{ 0, y, 0, 0 },
			{ 0, 0, z, 0 },
			{ 0, 0, 0, 1 } });
	}

	/**
	 * Creates scale matrix.
	 * @param scaleVector which components are scale parameters.
	 * @return scale matrix.
	 */
	public static Matrix4x4 scaleMatrix(Vector3<?>  scaleVector) {
		return scaleMatrix(scaleVector.xd(), scaleVector.yd(), scaleVector.zd());
	}

	/**
	 * Creates rotation matrix.
	 * @param rotationVector Rotation will occur direction of this vector. Passing normalized vector reduces required computations.
	 * @param angle in radians.
	 * @return Rotation matrix.
	 */
	public static Matrix4x4 rotationMatrix(Vector3<?> rotationVector, double angle) {
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		if (!DoubleMath.fuzzyEquals(rotationVector.magnitudeSquared(), 1,0.000001)) {
			rotationVector = rotationVector.normalize();
		}
		double x = rotationVector.xd();
		double y = rotationVector.yd();
		double z = rotationVector.zd();

		return new Matrix4x4(new double[][] {
			{ x * x * (1 - c) + c, x * y * (1 - c) - z * s, x * z * (1 - c) + y * s, 0 },
			{ y * x * (1 - c) + z * s, y * y * (1 - c) + c, y * z * (1 - c) - x * s, 0 },
			{ x * z * (1 - c) - y * s, y * z * (1 - c) + x * s, z * z * (1 - c) + c, 0 },
			{ 0, 0, 0, 1 } });
	}

}
