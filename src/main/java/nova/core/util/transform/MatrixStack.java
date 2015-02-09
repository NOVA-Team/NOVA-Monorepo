package nova.core.util.transform;

import java.util.Stack;

public class MatrixStack implements Transform{

	private final Stack<Matrix> stack = new Stack<Matrix>();
	private Matrix current = Matrix.IDENTITY;

	/**
	 * Replaces current transformation matrix by an identity matrix.
	 */
	public void loadIdentity() {
		current = Matrix.IDENTITY;
	}
	/**
	 * Replaces current transformation matrix by an identity current.
	 */
	public void loadMatrix(Matrix matrix) {
		current = matrix;
	}

	/**
	 * Exposes current transformation matrix.
	 * @return current transformation matrix.
	 */
	public Matrix getMatrix() {
		return current;
	}

	/**
	 * Transforms current matrix with give matrix.
	 * @param matrix to transform current matrix.
	 */
	public void transform(Matrix matrix) {
		current = current.multiply(matrix);
	}

	/**
	 * Translates current transformation matrix.
	 * @param x translation.
	 * @param y translation.
	 * @param z translation.
	 */
	public void translate(double x, double y, double z) {
		current = current.multiply(MatrixHelper.translationMatrix(x, y, z));
	}

	/**
	 * Translates current transformation matrix.
	 * @param translateVector vector of translation.
	 */
	public void translate(Vector3<?> translateVector){
		translate(translateVector.xd(), translateVector.yd(), translateVector.zd());
	}

	/**
	 * Rotates transformation matrix around rotateVector axis by angle radians.
	 * @param rotateVector Vector serving as rotation axis.
	 * @param angle in radians.
	 */
	public void rotate(Vector3<?> rotateVector, double angle) {
		current = current.multiply(MatrixHelper.rotationMatrix(rotateVector, angle));
	}

	public void scale(double x, double y, double z) {
		current = current.multiply(MatrixHelper.scaleMatrix(x, y, z));
	}

	public void scale(Vector3<?> scaleVector) {
		scale(scaleVector.xd(), scaleVector.yd(), scaleVector.zd());
	}

	public void pushMatrix() {
		stack.add(current);
	}
	public void popMatrix() {
		current = stack.pop();
	}

	/**
	 * Called to transform a vector.
	 *
	 * @param vec - The vector being transformed
	 * @return The transformed vector by current matrix.
	 */
	@Override
	public Vector3d transform(Vector3<?> vec) {
		return current.transform(vec);
	}
}
