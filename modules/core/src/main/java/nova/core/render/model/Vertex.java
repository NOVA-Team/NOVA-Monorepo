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

package nova.core.render.model;

import nova.core.render.Color;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * A Vertex contains a position and UV data.
 * @author Calclavia, Kubuxu, inspired by ChickenBones
 */
public class Vertex implements Cloneable {
	public Vector3D vec;
	public Vector2D uv;

	/**
	 * The normal (or direction) this vertex is facing.
	 * Normals must be unit vectors.
	 */
	public Optional<Vector3D> normal;

	/**
	 * A RGB color value from 0 to 1.
	 */
	public Color color;

	/**
	 * Constructor for vertex
	 * @param vertex coordinates in 3D space.
	 * @param uv coordinates on the texture.
	 * @param normal the vertex normal.
	 */
	public Vertex(Vector3D vertex, Vector2D uv, Vector3D normal) {
		this(vertex, uv);
		this.normal = Optional.of(normal);
	}

	/**
	 * Constructor for vertex
	 * @param vertex coordinates in 3D space.
	 * @param uv coordinates on the texture.
	 */
	public Vertex(Vector3D vertex, Vector2D uv) {
		this.vec = vertex;
		this.uv = uv;
		this.color = Color.white;
		this.normal = Optional.empty();
	}

	/**
	 * Creates new instance of vertex using separate doubles.
	 * @param x coordinate in space.
	 * @param y coordinate in space.
	 * @param z coordinate in space.
	 * @param u coordinate on texture.
	 * @param v coordinate on texture.
	 */
	public Vertex(double x, double y, double z, double u, double v) {
		this(new Vector3D(x, y, z), new Vector2D(u, v));
	}

	@Override
	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Vertex5[" + new BigDecimal(vec.getX(), cont) + ", " + new BigDecimal(vec.getY(), cont) + ", " + new BigDecimal(vec.getZ(), cont) + "]" +
			"[" + new BigDecimal(uv.getX(), cont) + ", " + new BigDecimal(uv.getY()) + "]";
	}

	@Override
	protected Vertex clone() {
		Vertex vertex = new Vertex(vec, uv);
		vertex.normal = normal;
		vertex.color = color;
		return vertex;
	}
}