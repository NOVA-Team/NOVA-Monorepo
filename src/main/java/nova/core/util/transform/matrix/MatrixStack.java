package nova.core.util.transform.matrix;

import nova.core.util.collection.Tuple2;
import nova.core.util.transform.vector.Transformer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Stack;

public class MatrixStack implements Transformer {

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
	public MatrixStack loadMatrix(Matrix4x4 matrix) {
		current = matrix;
		return this;
	}

	/**
	 * Exposes current transformation matrix.
	 *
	 * @return current transformation matrix.
	 */
	public Matrix4x4 getMatrix() {
		return current;
	}

	/**
	 * Transforms current matrix with give matrix.
	 *
	 * @param matrix to transform current matrix.
	 */
	public MatrixStack transform(Matrix4x4 matrix) {
		current = current.rightMultiply(matrix);
		return this;
	}

	/**
	 * Translates current transformation matrix.
	 *
	 * @param x translation.
	 * @param y translation.
	 * @param z translation.
	 */
	public MatrixStack translate(double x, double y, double z) {
		current = current.rightMultiply(MatrixHelper.translationMatrix(x, y, z));
		return this;
	}

	/**
	 * Translates current transformation matrix.
	 *
	 * @param translateVector vector of translation.
	 */
	public MatrixStack translate(Vector3D translateVector) {
		translate(translateVector.getX(), translateVector.getY(), translateVector.getZ());
		return this;
	}

	public MatrixStack rotate(Quaternion quaternion) {
		Tuple2<Vector3D, Double> axisAnglePair = quaternion.toAngleAxis();
		return rotate(axisAnglePair._1, axisAnglePair._2);
	}

	/**
	 * Rotates transformation matrix around rotateVector axis by angle radians.
	 *
	 * @param rotateVector Vector serving as rotation axis.
	 * @param angle in radians.
	 */
	public MatrixStack rotate(Vector3D rotateVector, double angle) {
		current = current.rightMultiply(MatrixHelper.rotationMatrix(rotateVector, angle));
		return this;
	}

	/**
	 * Scales current transformation matrix.
	 *
	 * @param x scale.
	 * @param y scale.
	 * @param z scale.
	 */
	public MatrixStack scale(double x, double y, double z) {
		current = current.rightMultiply(MatrixHelper.scaleMatrix(x, y, z));
		return this;
	}

	/**
	 * Scales current transformation matrix.
	 *
	 * @param scaleVector scale vector.
	 */
	public MatrixStack scale(Vector3D scaleVector) {
		scale(scaleVector.getX(), scaleVector.getY(), scaleVector.getZ());
		return this;
	}

	/**
	 * Pushes matrix onto the stack. Use it to save current state of MatrixStack in case of branching transformations.
	 */
	public MatrixStack pushMatrix() {
		stack.add(current);
		return this;
	}

	/**
	 * Pops matrix from stack. Use it to restore saved transformation.
	 */
	public MatrixStack popMatrix() {
		current = stack.pop();
		return this;
	}

	/**
	 * Called to transform a vector.
	 *
	 * @param vec - The vector being transformed
	 * @return The transformed vector by current matrix.
	 */
	@Override
	public Vector3D transform(Vector3D vec) {
		return current.transform(vec);
	}
}
