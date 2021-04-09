/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */package nova.core.util.math;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Stack;

public class MatrixStack implements Transformer {

	private final Stack<RealMatrix> stack;

	private RealMatrix current = MatrixUtils.createRealIdentityMatrix(4);

	/**
	 * Creates new MatrixStack. Constains no transfomation base matrix.
	 */
	public MatrixStack() {
		this.stack = new Stack<>();
	}

	/**
	 * Clone construcotr of MatrixStack
	 * @param clone instance to be cloned
	 */
	@SuppressWarnings("unchecked")
	public MatrixStack(MatrixStack clone) {
		this.stack = (Stack<RealMatrix>) clone.stack.clone();
		this.current = clone.current.copy();
	}

	/**
	 * Creates new MatrixStack with starting matrix.
	 * @param current Transforation matrix to start from.
	 */
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
	 *
	 * @param matrix The new matrix to use.
	 * @return this for chaining.
	 */
	public MatrixStack loadMatrix(RealMatrix matrix) {
		current = matrix;
		return this;
	}

	/**
	 * Exposes current transformation matrix.
	 *
	 * @return current transformation matrix.
	 */
	public RealMatrix getMatrix() {
		return current;
	}

	/**
	 * Transforms current matrix with give matrix.
	 *
	 * @param matrix to transform current matrix.
	 * @return The transformed matrix
	 */
	public MatrixStack transform(RealMatrix matrix) {

		current = current.preMultiply(MatrixUtil.augmentWithIdentity(matrix, 4));
		return this;
	}

	/**
	 * Translates current transformation matrix.
	 *
	 * @param x translation.
	 * @param y translation.
	 * @param z translation.
	 * @return The tranlated matrix
	 */
	public MatrixStack translate(double x, double y, double z) {
		current = current.preMultiply(TransformUtil.translationMatrix(x, y, z));
		return this;
	}

	/**
	 * Translates current transformation matrix.
	 *
	 * @param translateVector vector of translation.
	 * @return The tranformed matrix
	 */
	public MatrixStack translate(Vector3D translateVector) {
		translate(translateVector.getX(), translateVector.getY(), translateVector.getZ());
		return this;
	}

	/**
	 * Rotates the current matrix
	 *
	 * @param rotation The rotation to aply
	 * @return The rorated matrix
	 */
	public MatrixStack rotate(Rotation rotation) {
		RealMatrix rotMat = MatrixUtils.createRealMatrix(4, 4);
		rotMat.setSubMatrix(rotation.getMatrix(), 0, 0);
		rotMat.setEntry(3, 3, 1);
		current = current.preMultiply(rotMat);
		return this;
	}

	/**
	 * Rotates transformation matrix around rotateVector axis by angle radians.
	 *
	 * @param rotateVector Vector serving as rotation axis.
	 * @param angle in radians.
	 * @return The rotated matrix
	 */
	public MatrixStack rotate(Vector3D rotateVector, double angle) {
		return rotate(new Rotation(rotateVector, angle));
	}

	/**
	 * Scales current transformation matrix.
	 *
	 * @param x scale.
	 * @param y scale.
	 * @param z scale.
	 * @return this for chaining.
	 */
	public MatrixStack scale(double x, double y, double z) {
		current = current.preMultiply(TransformUtil.scaleMatrix(x, y, z));
		return this;
	}

	/**
	 * Scales current transformation matrix.
	 *
	 * @param scaleVector scale vector.
	 * @return The current matrix
	 */
	public MatrixStack scale(Vector3D scaleVector) {
		scale(scaleVector.getX(), scaleVector.getY(), scaleVector.getZ());
		return this;
	}

	/**
	 * Pushes matrix onto the stack. Use it to save current state of MatrixStack in case of branching transformations.
	 * @return this for chaining.
	 */
	public MatrixStack pushMatrix() {
		stack.add(current);
		return this;
	}

	/**
	 * Pops matrix from stack. Use it to restore saved transformation.
	 * @return this for chaining.
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
