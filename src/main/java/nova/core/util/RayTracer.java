package nova.core.util;

import nova.core.block.Block;
import nova.core.component.ComponentProvider;
import nova.core.component.misc.Collider;
import nova.core.component.transform.WorldTransform;
import nova.core.entity.Entity;
import nova.core.entity.component.Living;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3;
import nova.core.util.transform.vector.Vector3d;
import nova.core.world.World;

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
		this(new Ray(entity.position().add(entity.has(Living.class) ? entity.get(Living.class).faceDisplacement.get() : Vector3d.zero), entity.rotation().toForwardVector()));
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
	 * @return The blocks ray traced in the order from closest to furthest.
	 */
	public Stream<RayTraceBlockResult> rayTraceBlocks(World world) {
		//All relevant blocks
		return rayTraceBlocks(
			IntStream.range(0, (int) distance + 1)
				.mapToObj(i -> ray.origin.add(ray.dir.multiply(i)).toInt())
				.flatMap(vec -> Arrays.stream(Direction.DIRECTIONS).map(direction -> vec.add(direction.toVector()))) //Cover a larger area to be safe
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
				.filter(block -> block.has(Collider.class))
				.flatMap(block -> rayTraceCollider(block, (pos, cuboid) -> new RayTraceBlockResult(pos, ray.origin.distance(pos), cuboid.sideOf(pos), cuboid, block)))
				.sorted();
	}

	public Stream<RayTraceEntityResult> rayTraceEntities(World world) {
		//TODO: Consider smaller check space
		return rayTraceEntities(
			world.getEntities(Cuboid.zero.expand(distance).add(ray.origin))
				.stream()
				.filter(entity -> entity.has(Collider.class))
		);
	}

	public Stream<RayTraceEntityResult> rayTraceEntities(Stream<Entity> entityStream) {
		return
			(doParallel() ? entityStream.parallel() : entityStream)
				.filter(entity -> entity.has(Collider.class))
				.flatMap(entity -> rayTraceCollider(entity, (pos, cuboid) -> new RayTraceEntityResult(pos, ray.origin.distance(pos), cuboid.sideOf(pos), cuboid, entity)))
				.sorted();
	}

	public <R extends RayTraceResult> Stream<R> rayTraceCollider(ComponentProvider colliderProvider, BiFunction<Vector3d, Cuboid, R> resultMapper) {
		return
			colliderProvider.get(Collider.class)
				.occlusionBoxes
				.apply(Optional.empty())
				.stream()
				.map(cuboid -> cuboid.add((Vector3) colliderProvider.get(WorldTransform.class).position()))
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
	 * @param cuboid The cuboid in absolute world coordinates
	 * @return The ray trace result if the ray intersects the cuboid
	 */
	public <R extends RayTraceResult> Optional<R> rayTrace(Cuboid cuboid, BiFunction<Vector3d, Cuboid, R> resultMapper) {
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
	public Optional<Vector3d> rayTrace(Cuboid cuboid, double minDist, double maxDist) {
		//X
		Vector3d bbox = ray.signDirX ? cuboid.max : cuboid.min;
		double txMin = (Math.abs(ray.dir.x) < 0.0000001) ? minDist : (bbox.x - ray.origin.x) * ray.invDir.x;
		bbox = ray.signDirX ? cuboid.min : cuboid.max;
		double txMax = (Math.abs(ray.dir.x) < 0.0000001) ? maxDist : (bbox.x - ray.origin.x) * ray.invDir.x;

		//Y
		bbox = ray.signDirY ? cuboid.max : cuboid.min;
		double tyMin = (Math.abs(ray.dir.y) < 0.0000001) ? minDist : (bbox.y - ray.origin.y) * ray.invDir.y;
		bbox = ray.signDirY ? cuboid.min : cuboid.max;
		double tyMax = (Math.abs(ray.dir.y) < 0.0000001) ? maxDist : (bbox.y - ray.origin.y) * ray.invDir.y;

		if ((txMin > tyMax) || (tyMin > txMax)) {
			return Optional.empty();
		}
		if (tyMin > txMin) {
			txMin = tyMin;
		}
		if (tyMax < txMax) {
			txMax = tyMax;
		}

		//Z
		bbox = ray.signDirZ ? cuboid.max : cuboid.min;
		double tzMin = (Math.abs(ray.dir.z) < 0.0000001) ? minDist : (bbox.z - ray.origin.z) * ray.invDir.z;
		bbox = ray.signDirZ ? cuboid.min : cuboid.max;
		double tzMax = (Math.abs(ray.dir.z) < 0.0000001) ? maxDist : (bbox.z - ray.origin.z) * ray.invDir.z;

		if ((txMin > tzMax) || (tzMin > txMax)) {
			return Optional.empty();
		}
		if (tzMin > txMin) {
			txMin = tzMin;
		}
		if (tzMax < txMax) {
			txMax = tzMax;
		}
		if ((txMin < maxDist) && (txMax > minDist)) {
			return Optional.of(ray.origin.add(ray.dir.multiply(txMin)));
		}
		return Optional.empty();
	}

	public static class Ray {
		public final Vector3d origin;
		public final Vector3d dir;
		public final Vector3d invDir;
		public final boolean signDirX;
		public final boolean signDirY;
		public final boolean signDirZ;

		/**
		 * @param origin The ray's beginning
		 * @param dir The ray's direction (unit vector)
		 */
		public Ray(Vector3d origin, Vector3d dir) {
			this.origin = origin;
			this.dir = dir;
			this.invDir = dir.reciprocal();
			this.signDirX = invDir.x < 0;
			this.signDirY = invDir.y < 0;
			this.signDirZ = invDir.z < 0;
		}

		public static Ray fromInterval(Vector3d start, Vector3d end) {
			return new Ray(start, end.subtract(start).normalize());
		}
	}

	public static class RayTraceResult implements Comparable<RayTraceResult> {
		public final Vector3d hit;
		public final double distance;
		public final Direction side;
		public final Cuboid hitCuboid;

		public RayTraceResult(Vector3d hit, double distance, Direction side, Cuboid hitCuboid) {
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

		public RayTraceBlockResult(Vector3d hit, double distance, Direction side, Cuboid hitCuboid, Block block) {
			super(hit, distance, side, hitCuboid);
			this.block = block;
		}
	}

	public static class RayTraceEntityResult extends RayTraceResult {
		public final Entity entity;

		public RayTraceEntityResult(Vector3d hit, double distance, Direction side, Cuboid hitCuboid, Entity entity) {
			super(hit, distance, side, hitCuboid);
			this.entity = entity;
		}
	}
}
