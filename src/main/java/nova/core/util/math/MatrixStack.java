package nova.core.util.math;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Stack;

public class MatrixStack implements Transformer {

	private final Stack<RealMatrix> stack;
	private RealMatrix current = MatrixUtils.createRealIdentityMatrix(4);

	public MatrixStack() {
		this.stack = new Stack<>();
	}

	public MatrixStack(MatrixStack clone) {
		//noinspection unchecked
		this.stack = (Stack<RealMatrix>) clone.stack.clone();
		this.current = clone.current.copy();
	}

	public MatrixStack(RealMatrix current) {
		this.stack = new Stack<>();
		this.current = current;
	}

	/**
	 * Replaces current transformation matrix by an identity matrix.
	 */
	public void loadIdentity() {
		current = MatrixUtils.createRealIdentityMatrix(4);
	}

	/**
	 * Replaces current transformation matrix by an identity current.
	 */
	public MatrixStack loadMatrix(RealMatrix matrix) {
		current = matrix;
		return this;
	}

	/**
	 * Exposes current transformation matrix.
	 * @return current transformation matrix.
	 */
	public RealMatrix getMatrix() {
		return current;
	}

	/**
	 * Transforms current matrix with give matrix.
	 * @param matrix to transform current matrix.
	 */
	public MatrixStack transform(RealMatrix matrix) {

		current = current.preMultiply(MatrixUtil.augmentWithIdentity(matrix, 4));
		return this;
	}

	/**
	 * Translates current transformation matrix.
	 * @param x translation.
	 * @param y translation.
	 * @param z translation.
	 */
	public MatrixStack translate(double x, double y, double z) {
		current = current.preMultiply(TransformUtil.translationMatrix(x, y, z));
		return this;
	}

	/**
	 * Translates current transformation matrix.
	 * @param translateVector vector of translation.
	 */
	public MatrixStack translate(Vector3D translateVector) {
		translate(translateVector.getX(), translateVector.getY(), translateVector.getZ());
		return this;
	}

	public MatrixStack rotate(Rotation rotation) {
		RealMatrix rotMat = MatrixUtils.createRealMatrix(4, 4);
		rotMat.setSubMatrix(rotation.getMatrix(), 0, 0);
		rotMat.setEntry(3, 3, 1);
		current = current.preMultiply(rotMat);
		return this;
	}

	/**
	 * Rotates transformation matrix around rotateVector axis by angle radians.
	 * @param rotateVector Vector serving as rotation axis.
	 * @param angle in radians.
	 */
	public MatrixStack rotate(Vector3D rotateVector, double angle) {
		return rotate(new Rotation(rotateVector, angle));
	}

	/**
	 * Scales current transformation matrix.
	 * @param x scale.
	 * @param y scale.
	 * @param z scale.
	 */
	public MatrixStack scale(double x, double y, double z) {
		current = current.preMultiply(TransformUtil.scaleMatrix(x, y, z));
		return this;
	}

	/**
	 * Scales current transformation matrix.
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
	 * @param vec - The vector being transformed
	 * @return The transformed vector by current matrix.
	 */
	@Override
	public Vector3D apply(Vector3D vec) {
		return TransformUtil.transform(vec, current);
	}
}
