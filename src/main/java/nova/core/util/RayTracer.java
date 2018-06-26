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

package nova.core.util;

import nova.core.block.Block;
import nova.core.component.ComponentMap;
import nova.core.component.ComponentProvider;
import nova.core.component.misc.Collider;
import nova.core.component.transform.WorldTransform;
import nova.core.entity.Entity;
import nova.core.entity.component.Living;
import nova.core.util.math.Vector3DUtil;
import nova.core.util.shape.Cuboid;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Ray tracing for cuboids.
 * @author Calclavia
 */
//TODO: Add ray trace masks
public class RayTracer {

	public final Ray ray;
	public double distance;
	public int parallelThreshold = 1000;

	public RayTracer(Ray ray) {
		this.ray = ray;
	}

	/**
	 * Does an entity look ray trace to see which block the entity is looking at.
	 * @param entity The entity
	 */
	public RayTracer(Entity entity) {
		this(new Ray(entity.position().add(entity.components.getOp(Living.class).map(l -> l.faceDisplacement.get()).orElse(Vector3D.ZERO)), entity.rotation().applyTo(Vector3DUtil.FORWARD)));
	}

	/**
	 * Sets the distance of the ray
	 * @param distance Distance in meters
	 * @return This
	 */
	public RayTracer setDistance(double distance) {
		this.distance = distance;
		return this;
	}

	public RayTracer setParallelThreshold(int parallelThreshold) {
		this.parallelThreshold = parallelThreshold;
		return this;
	}

	public boolean doParallel() {
		return distance > parallelThreshold;
	}

	public Stream<RayTraceResult> rayTraceAll(World world) {
		return Stream.concat(rayTraceBlocks(world), rayTraceEntities(world)).sorted();
	}

	/**
	 * Check all blocks that are in a line
	 * @param world The world to perform the ray trace in.
	 * @return The blocks ray traced in the order from closest to furthest.
	 */
	public Stream<RayTraceBlockResult> rayTraceBlocks(World world) {
		//All relevant blocks
		return rayTraceBlocks(
			IntStream.range(0, (int) distance + 1)
				.mapToObj(i -> ray.origin.add(ray.dir.scalarMultiply(i)))
				.flatMap(vec -> Arrays.stream(Direction.VALID_DIRECTIONS).map(direction -> Vector3DUtil.floor(vec.add(direction.toVector())))) //Cover a larger area to be safe
				.distinct()
				.map(world::getBlock)
				.filter(Optional::isPresent)
				.map(Optional::get)
		);
	}

	/**
	 * Ray traces a set of blocks
	 * @param blocks Set of blocks
	 * @return A list of cuboids that intersect with the line segment in the order from closest to furthest.
	 */
	public Stream<RayTraceBlockResult> rayTraceBlocks(Set<Block> blocks) {
		return rayTraceBlocks(blocks.stream());
	}

	public Stream<RayTraceBlockResult> rayTraceBlocks(Stream<Block> blockStream) {
		return
			(doParallel() ? blockStream.parallel() : blockStream)
				.filter(block -> block.components.has(Collider.class))
				.flatMap(block -> rayTraceCollider(block, (pos, cuboid) -> new RayTraceBlockResult(pos, ray.origin.distance(pos), cuboid.sideOf(pos), cuboid, block)))
				.sorted();
	}

	public Stream<RayTraceEntityResult> rayTraceEntities(World world) {
		//TODO: Consider smaller check space
		return rayTraceEntities(
			world.getEntities(Cuboid.ZERO.expand(distance).add(ray.origin))
				.stream()
				.filter(entity -> entity.components.has(Collider.class))
		);
	}

	public Stream<RayTraceEntityResult> rayTraceEntities(Stream<Entity> entityStream) {
		return
			(doParallel() ? entityStream.parallel() : entityStream)
				.filter(entity -> entity.components.has(Collider.class))
				.flatMap(entity -> rayTraceCollider(entity, (pos, cuboid) -> new RayTraceEntityResult(pos, ray.origin.distance(pos), cuboid.sideOf(pos), cuboid, entity)))
				.sorted();
	}

	@SuppressWarnings("rawtypes")
	public <R extends RayTraceResult> Stream<R> rayTraceCollider(ComponentProvider<? extends ComponentMap> colliderProvider, BiFunction<Vector3D, Cuboid, R> resultMapper) {
		return
			colliderProvider.components.get(Collider.class)
				.occlusionBoxes
				.apply(Optional.empty())
				.stream()
				.map(cuboid -> cuboid.add((Vector3D) colliderProvider.components.get(WorldTransform.class).position()))
				.map(cuboid -> rayTrace(cuboid, resultMapper))
				.filter(Optional::isPresent)
				.map(Optional::get);
	}

	/**
	 * Ray traces a set of cuboids
	 * @param stream A stream of cuboids
	 * @return A list of cuboids that intersect with the line segment in the order from closest to furthest.
	 */
	public List<RayTraceResult> rayTrace(Stream<Cuboid> stream) {
		return stream
			.map(this::rayTrace)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.sorted()
			.collect(Collectors.toList());
	}

	public Optional<RayTraceResult> rayTrace(Cuboid cuboid) {
		return rayTrace(cuboid, (pos, cuboid2) -> new RayTraceResult(pos, ray.origin.distance(pos), cuboid.sideOf(pos), cuboid));
	}

	/**
	 * Ray traces a cuboid
	 * @param <R> the result type
	 * @param cuboid The cuboid in absolute world coordinates
	 * @param resultMapper the {@link RayTraceResult} converter
	 * @return The ray trace result if the ray intersects the cuboid
	 */
	public <R extends RayTraceResult> Optional<R> rayTrace(Cuboid cuboid, BiFunction<Vector3D, Cuboid, R> resultMapper) {
		return rayTrace(cuboid, 0, distance).map(vec -> resultMapper.apply(vec, cuboid));
	}

	/**
	 * Calculates intersection with the given ray between a certain distance
	 * interval.
	 * <p>
	 * Ray-box intersection is using IEEE numerical properties to ensure the
	 * test is both robust and efficient, as described in:
	 * <br>
	 * <code>Amy Williams, Steve Barrus, R. Keith Morley, and Peter Shirley: "An
	 * Efficient and Robust Ray-Box Intersection Algorithm" Journal of graphics
	 * tools, 10(1):49-54, 2005</code>
	 * @param cuboid The cuboid to trace
	 * @param minDist The minimum distance
	 * @param maxDist The maximum distance
	 * @return intersection point on the bounding box (only the first is
	 * returned) or null if no intersection
	 */
	public Optional<Vector3D> rayTrace(Cuboid cuboid, double minDist, double maxDist) {
		Vector3D bbox;

		double tMin;
		double tMax;

		bbox = ray.signDirX ? cuboid.max : cuboid.min;
		tMin = (bbox.getX() - ray.origin.getX()) * ray.invDir.getX();
		bbox = ray.signDirX ? cuboid.min : cuboid.max;
		tMax = (bbox.getX() - ray.origin.getX()) * ray.invDir.getX();

		//Y
		bbox = ray.signDirY ? cuboid.max : cuboid.min;
		double tyMin = (bbox.getY() - ray.origin.getY()) * ray.invDir.getY();
		bbox = ray.signDirY ? cuboid.min : cuboid.max;
		double tyMax = (bbox.getY() - ray.origin.getY()) * ray.invDir.getY();

		//Check with the current tMin and tMax to see if the clipping is out of bounds
		if ((tMin > tyMax) || (tyMin > tMax)) {
			return Optional.empty();
		}

		//Reset tMin and tMax
		if (tyMin > tMin) {
			tMin = tyMin;
		}
		if (tyMax < tMax) {
			tMax = tyMax;
		}
		bbox = ray.signDirZ ? cuboid.max : cuboid.min;
		double tzMin = (bbox.getZ() - ray.origin.getZ()) * ray.invDir.getZ();
		bbox = ray.signDirZ ? cuboid.min : cuboid.max;
		double tzMax = (bbox.getZ() - ray.origin.getZ()) * ray.invDir.getZ();

		//Check with the current tMin and tMax to see if the clipping is out of bounds
		if ((tMin > tzMax) || (tzMin > tMax)) {
			return Optional.empty();
		}

		//Reset tMin and tMax
		if (tzMin > tMin) {
			tMin = tzMin;
		}
		if (tzMax < tMax) {
			tMax = tzMax;
		}

		if ((tMin < maxDist) && (tMax > minDist)) {
			return Optional.of(ray.origin.add(ray.dir.scalarMultiply(tMin)));
		}

		return Optional.empty();
	}

	public static class RayTraceResult implements Comparable<RayTraceResult> {
		public final Vector3D hit;
		public final double distance;
		public final Direction side;
		public final Cuboid hitCuboid;

		public RayTraceResult(Vector3D hit, double distance, Direction side, Cuboid hitCuboid) {
			this.hit = hit;
			this.distance = distance;
			this.side = side;
			this.hitCuboid = hitCuboid;
		}

		@Override
		public int compareTo(RayTraceResult o) {
			return distance == o.distance ? 0 : distance < o.distance ? -1 : 1;
		}

	}

	public static class RayTraceBlockResult extends RayTraceResult {
		public final Block block;

		public RayTraceBlockResult(Vector3D hit, double distance, Direction side, Cuboid hitCuboid, Block block) {
			super(hit, distance, side, hitCuboid);
			this.block = block;
		}
	}

	public static class RayTraceEntityResult extends RayTraceResult {
		public final Entity entity;

		public RayTraceEntityResult(Vector3D hit, double distance, Direction side, Cuboid hitCuboid, Entity entity) {
			super(hit, distance, side, hitCuboid);
			this.entity = entity;
		}
	}
}
