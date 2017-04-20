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

import nova.core.render.model.Face;
import nova.core.render.model.Vertex;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;

import java.util.Random;

/**
 * An extension of Apache Common's Vector3D class
 * @author Calclavia
 */
public class Vector3DUtil {

	private Vector3DUtil() {

	}

	public static final Vector3D ONE = new Vector3D(1, 1, 1);
	public static final Vector3D CENTER = new Vector3D(0.5, 0.5, 0.5);
	public static final Vector3D FORWARD = Vector3D.MINUS_K;

	/**
	 * @return Creates a random unit vector
	 */
	public static Vector3D random() {
		Random random = new Random();
		return new Vector3D(random.nextDouble(), random.nextDouble(), random.nextDouble()).scalarMultiply(2).subtract(ONE);
	}

	/**
	 * Calculates maximum of each coordinate.
	 *
	 * @param a first vector.
	 * @param b second vector.
	 * @return new vector that's each coordinate is maximum of coordinates of a and b.
	 */
	public static Vector3D max(Vector3D a, Vector3D b) {
		return new Vector3D(FastMath.max(a.getX(), b.getX()), FastMath.max(a.getY(), b.getY()), FastMath.max(a.getZ(), b.getZ()));
	}

	/**
	 * Calculates minimum of each coordinate.
	 * @param a first vector.
	 * @param b second vector.
	 * @return new vector that's each coordinate is minimum of coordinates of a and b.
	 */
	public static Vector3D min(Vector3D a, Vector3D b) {
		return new Vector3D(FastMath.min(a.getX(), b.getX()), FastMath.min(a.getY(), b.getY()), FastMath.min(a.getZ(), b.getZ()));
	}

	public static Vector3D cartesianProduct(Vector3D a, Vector3D b) {
		return new Vector3D(a.getX() * b.getX(), a.getY() * b.getY(), a.getZ() * b.getZ());
	}

	public static Vector3D xCross(Vector3D vec) {
		return new Vector3D(0, vec.getZ(), -vec.getY());
	}

	public static Vector3D zCross(Vector3D vec) {
		return new Vector3D(-vec.getY(), vec.getX(), 0);
	}

	/**
	 * Calculates middle point between two vectors.
	 * @param a first vector.
	 * @param b second vector.
	 * @return new vectors that is in the middle of a and b.
	 */
	public static Vector3D midpoint(Vector3D a, Vector3D b) {
		return a.add(b).scalarMultiply(0.5);
	}

	/**
	 * Calculates one by vectos.
	 * @param vec vector to be reciprocated.
	 * @return reciprocal of vec.
	 */
	public static Vector3D reciprocal(Vector3D vec) {
		return new Vector3D(1 / vec.getX(), 1 / vec.getY(), 1 / vec.getZ());
	}

	public static Vector3D perpendicular(Vector3D vec) {
		// Special case. Z == 0 would cause a error.
		//noinspection FloatingPointEquality
		if (vec.getZ() == 0) {
			return zCross(vec);
		}

		return xCross(vec);
	}

	public static Vector3D round(Vector3D vec) {
		return new Vector3D(FastMath.round(vec.getX()), FastMath.round(vec.getY()), FastMath.round(vec.getZ()));
	}

	public static Vector3D ceil(Vector3D vec) {
		return new Vector3D(FastMath.ceil(vec.getX()), FastMath.ceil(vec.getY()), FastMath.ceil(vec.getZ()));
	}

	public static Vector3D floor(Vector3D vec) {
		return new Vector3D(FastMath.floor(vec.getX()), FastMath.floor(vec.getY()), FastMath.floor(vec.getZ()));
	}

	public static Vector3D abs(Vector3D vec) {
		return new Vector3D(FastMath.abs(vec.getX()), FastMath.abs(vec.getY()), FastMath.abs(vec.getZ()));
	}

	public static Vector3D calculateNormal(Face face) {
		// TODO: Possibly calculate from vertex normals
		Vertex firstEntry = face.vertices.get(0);
		Vertex secondEntry = face.vertices.get(1);
		Vertex thirdEntry = face.vertices.get(2);
		Vector3D v1 = secondEntry.vec.subtract(firstEntry.vec);
		Vector3D v2 = thirdEntry.vec.subtract(firstEntry.vec);

		return v1.crossProduct(v2).normalize();
	}
}
