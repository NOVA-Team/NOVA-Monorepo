package nova.core.util.transform;

import java.util.Stack;

public class MatrixStack implements Transform{

	private final Stack<Matrix4x4> stack = new Stack<>();
	private Matrix4x4 current = Matrix4x4.IDENTITY;

	/**
	 * Replaces current transformation matrix by an identity matrix.
	 */
	public void loadIdentity() {
		current = Matrix4x4.IDENTITY;
	}
	/**
	 * Replaces current transformation matrix by an identity current.
	 */
	public void loadMatrix(Matrix4x4 matrix) {
		current = matrix;
	}

	/**
	 * Exposes current transformation matrix.
	 * @return current transformation matrix.
	 */
	public Matrix4x4 getMatrix() {
		return current;
	}

	/**
	 * Transforms current matrix with give matrix.
	 * @param matrix to transform current matrix.
	 */
	public void transform(Matrix4x4 matrix) {
		current = current.rightMultiply(matrix);
	}

	/**
	 * Translates current transformation matrix.
	 * @param x translation.
	 * @param y translation.
	 * @param z translation.
	 */
	public void translate(double x, double y, double z) {
		current = current.rightMultiply(MatrixHelper.translationMatrix(x, y, z));
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
		current = current.rightMultiply(MatrixHelper.rotationMatrix(rotateVector, angle));
	}

	/**
	 * Scales current transformation matrix.
	 * @param x scale.
	 * @param y scale.
	 * @param z scale.
	 */
	public void scale(double x, double y, double z) {
		current = current.rightMultiply(MatrixHelper.scaleMatrix(x, y, z));
	}

	/**
	 * Scales current transformation matrix.
	 * @param scaleVector scale vector.
	 */
	public void scale(Vector3<?> scaleVector) {
		scale(scaleVector.xd(), scaleVector.yd(), scaleVector.zd());
	}

	/**
	 * Pushes matrix onto the stack. Use it to save current state of MatrixStack in case of branching transformations.
	 */
	public void pushMatrix() {
		stack.add(current);
	}

	/**
	 * Pops matrix from stack. Use it to restore saved transformation.
	 */
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
