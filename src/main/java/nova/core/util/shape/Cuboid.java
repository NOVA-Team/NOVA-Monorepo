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

package nova.core.util.shape;

import nova.core.util.Direction;
import nova.core.util.math.Transformer;
import nova.core.util.math.Vector3DUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Consumer;

/**
 * A cuboid is a shape that represents a cube.
 * @author Calclavia
 */
public class Cuboid extends Shape<Cuboid, Cuboid> {
	public static final Cuboid ZERO = new Cuboid(Vector3D.ZERO, Vector3D.ZERO);
	public static final Cuboid ONE = new Cuboid(Vector3D.ZERO, Vector3DUtil.ONE);
	public final Vector3D min;
	public final Vector3D max;

	/**
	 * New cuboid defined by the specified vectors as bounds
	 *
	 * @param min min vector
	 * @param max max vextor
	 */
	public Cuboid(Vector3D min, Vector3D max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * New Cuboid with the specified coordinates as bounds
	 *
	 * @param minX min x coord
	 * @param minY min y coord
	 * @param minZ min z coord
	 * @param maxX max x coord
	 * @param maxY max y coord
	 * @param maxZ max z coord
	 */
	public Cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		this(new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ));
	}

	@Override
	public Cuboid add(Cuboid other) {
		return new Cuboid(min.add(other.min), max.add(other.max));
	}

	/**
	 * Adds a vector to the cuboid
	 *
	 * @param other The vector to add
	 * @return The new cuboid
	 */
	public Cuboid add(Vector3D other) {
		return new Cuboid(min.add(other), max.add(other));
	}

	@Override
	public Cuboid add(double other) {
		return new Cuboid(min.add(Vector3DUtil.ONE.scalarMultiply(other)), max.add(Vector3DUtil.ONE.scalarMultiply(other)));
	}

	public Cuboid $plus(Vector3D other) {
		return add(other);
	}

	public Cuboid subtract(Vector3D other) {
		return new Cuboid(min.subtract(other), max.subtract(other));
	}

	public Cuboid $minus(Vector3D other) {
		return subtract(other);
	}

	@Override
	public Cuboid multiply(double other) {
		return new Cuboid(min.scalarMultiply(other), max.scalarMultiply(other));
	}

	@Override
	public Cuboid reciprocal() {
		return new Cuboid(Vector3DUtil.reciprocal(min), Vector3DUtil.reciprocal(max));
	}

	/**
	 * Expands the cuboid by a certain vector.
	 *
	 * @param other Given vector
	 * @return New cuboid
	 */
	public Cuboid expand(Vector3D other) {
		return new Cuboid(min.subtract(other), max.add(other));
	}

	/**
	 * Expands the cuboid by a certain amount.
	 *
	 * @param other The amount
	 * @return New cuboid
	 */
	public Cuboid expand(double other) {
		return new Cuboid(min.subtract(Vector3DUtil.ONE.scalarMultiply(other)), max.add(Vector3DUtil.ONE.scalarMultiply(other)));
	}

	/**
	 * Returns if this cuboid is a cube.
	 *
	 * @return If this cuboid is a cube.
	 */
	public boolean isCube() {
		return size().getX() == size().getY() && size().getY() == size().getZ();
	}

	/**
	 * How large the cuboid is
	 *
	 * @return The size of the cuboid
	 */
	public Vector3D size() {
		return max.subtract(min);
	}

	/**
	 * The center of the cuboid
	 *
	 * @return Vector representing the cuboid
	 */
	public Vector3D center() {
		return Vector3DUtil.midpoint(max, min);
	}

	/**
	 * The volume of the cuboid
	 *
	 * @return The volume of the cuboid
	 */
	public double volume() {
		return size().getX() * size().getY() * size().getZ();
	}

	/**
	 * The surface area of the cuboid
	 *
	 * @return The surface area of the cuvoid
	 */
	public double surfaceArea() {
		return (2 * size().getX() * size().getZ()) + (2 * size().getX() * size().getY()) + (2 * size().getZ() * size().getY());
	}

	/**
	 * Checks if another cuboid is within this cuboid
	 *
	 * @param other Cuboid to check
	 * @return Result of the check
	 */
	public boolean intersects(Cuboid other) {
		return (other.max.getX() >= min.getX() && other.min.getX() < max.getX()) ?
			((other.max.getY() >= min.getY() && other.min.getY() < max.getY()) ? other.max.getZ() >= min.getZ() && other.min.getZ() < max.getZ() : false) : false;
	}

	/**
	 * Checks if a vector is within this cuboid.
	 *
	 * @param other Vector to check
	 * @return Result of the check
	 */
	public boolean intersects(Vector3D other) {
		return other.getX() >= this.min.getX() && other.getX() < this.max.getX() ?
			(other.getY() >= this.min.getY() && other.getY() < this.max.getY() ? other.getZ() >= this.min.getZ() && other.getZ() < this.max.getZ() : false) : false;
	}

	/**
	 * Applies the given transformer to the cuboid
	 *
	 * @param transform The transformer to apply
	 * @return The transformed cuboid
	 */
	public Cuboid transform(Transformer transform) {
		Vector3D transMin = transform.apply(min);
		Vector3D transMax = transform.apply(max);
		return new Cuboid(Vector3DUtil.min(transMin, transMax), Vector3DUtil.max(transMax, transMin));
	}

	/**
	 * Applies the given rotation to the Cuboid
	 *
	 * @param transform The rotation to apply
	 * @return The rotated cuboid
	 */
	public Cuboid transform(Rotation transform) {
		Vector3D transMin = transform.applyTo(min);
		Vector3D transMax = transform.applyTo(max);
		return new Cuboid(Vector3DUtil.min(transMin, transMax), Vector3DUtil.max(transMax, transMin));
	}

	public void forEach(Consumer<Vector3D> consumer) {
		forEach(consumer::accept, 1);
	}

	public void forEach(Consumer<Vector3D> consumer, double step) {
		for (double x = min.getX(); x < max.getX(); x += step)
			for (double y = min.getY(); y < max.getY(); y += step)
				for (double z = min.getZ(); z < max.getZ(); z += step)
					consumer.accept(new Vector3D(x, y, z));
	}

	public Direction sideOf(Vector3D position) {
		return Direction.fromVector(position.subtract(center()).normalize());
	}

	@Override
	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Cuboid[" + new BigDecimal(min.getX(), cont) + ", " + new BigDecimal(min.getY(), cont) + ", " + new BigDecimal(min.getZ(), cont) + "] -> [" + new BigDecimal(max.getX(), cont) + ", " + new BigDecimal(max.getY(), cont) + ", " + new BigDecimal(max.getZ(), cont) + "]";
	}
}
