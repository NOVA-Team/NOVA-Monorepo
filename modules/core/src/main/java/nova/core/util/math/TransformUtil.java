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
 */

package nova.core.util.math;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public final class TransformUtil {

	private TransformUtil() {
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

	/**
	 * Transform vector by this matrix.
	 *
	 * @param vector to be transformed.
	 * @param m The 4x4 matrix to transform the vector by
	 * @return transformed vector.
	 */
	public static Vector3D transform(Vector3D vector, RealMatrix m) {
		double x, y, z, w;
		x = m.getEntry(0, 0) * vector.getX() + m.getEntry(1, 0) * vector.getY() + m.getEntry(2, 0) * vector.getZ() + m.getEntry(3, 0);
		y = m.getEntry(0, 1) * vector.getX() + m.getEntry(1, 1) * vector.getY() + m.getEntry(2, 1) * vector.getZ() + m.getEntry(3, 1);
		z = m.getEntry(0, 2) * vector.getX() + m.getEntry(1, 2) * vector.getY() + m.getEntry(2, 2) * vector.getZ() + m.getEntry(3, 2);
		w = m.getEntry(0, 3) * vector.getX() + m.getEntry(1, 3) * vector.getY() + m.getEntry(2, 3) * vector.getZ() + m.getEntry(3, 3);
		return new Vector3D(x / w, y / w, z / w);
	}

	/**
	 * Transform vector by this matrix.
	 *
	 * @param vector to be transformed.
	 * @param m The 4x4 matrix to transform the vector by
	 * @return transformed vector.
	 */
	public static Vector3D transformDirectionless(Vector3D vector, RealMatrix m) {
		double x, y, z;
		x = m.getEntry(0, 0) * vector.getX() + m.getEntry(1, 0) * vector.getY() + m.getEntry(2, 0) * vector.getZ();
		y = m.getEntry(0, 1) * vector.getX() + m.getEntry(1, 1) * vector.getY() + m.getEntry(2, 1) * vector.getZ();
		z = m.getEntry(0, 2) * vector.getX() + m.getEntry(1, 2) * vector.getY() + m.getEntry(2, 2) * vector.getZ();
		return new Vector3D(x, y, z);
	}
}
