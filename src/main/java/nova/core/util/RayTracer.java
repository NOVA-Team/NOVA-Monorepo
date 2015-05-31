package nova.core.util;

import nova.core.block.Block;
import nova.core.component.misc.Collider;
import nova.core.entity.Entity;
import nova.core.entity.component.Living;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3d;
import nova.core.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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
		this(new Ray(entity.position().add(entity.has(Living.class) ? entity.get(Living.class).faceDisplacement.get() : Vector3d.zero), entity.rotation().toZVector()));

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

	/**
	 * Check all blocks that are in a line
	 */
	public List<RayTraceBlockResult> rayTraceBlock(World world) {
		//All relevant blocks
		return rayTraceBlocks(
			IntStream.range(0, (int) distance + 1)
				.mapToObj(i -> ray.origin.add(ray.dir.multiply(i)).toInt())
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
	public List<RayTraceBlockResult> rayTraceBlocks(Set<Block> blocks) {
		return rayTraceBlocks(blocks.stream());
	}

	public List<RayTraceBlockResult> rayTraceBlocks(Stream<Block> blockStream) {
		return
			((distance > parallelThreshold) ? blockStream.parallel() : blockStream)
				.filter(block -> block.has(Collider.class))
				.flatMap(block ->
						block.get(Collider.class)
							.occlusionBoxes
							.apply(Optional.empty())
							.stream()
							.map(cuboid -> (Cuboid) cuboid.add(block.position()))
							.map(cuboid -> rayTrace(cuboid, pos -> new RayTraceBlockResult(pos, ray.origin.distance(pos), cuboid.sideOf(pos), cuboid, block)))
							.filter(Optional::isPresent)
							.map(Optional::get)
				)
				.sorted()
				.collect(Collectors.toList());
	}

	/*
	public List<RayTraceEntityResult> rayTraceEntities(World world) {
		//TODO: Consider smaller check space
		Cuboid checkRegion = Cuboid.zero.expand(distance).add(ray.origin);
		world.getEntities(checkRegion)
			.stream()
			.filter(entity -> entity.has(Collider.class));

		return rayTraceEntities()
	}*/

	/**
	 * Ray traces a set of cuboids
	 * @param cuboids Set of cuboids
	 * @return A list of cuboids that intersect with the line segment in the order from closest to furthest.
	 */
	public List<RayTraceResult> rayTrace(Set<Cuboid> cuboids) {
		//TODO: Check parallel threshold
		Stream<Cuboid> stream = (cuboids.size() > parallelThreshold ? cuboids.parallelStream() : cuboids.stream());
		return stream
			.map(this::rayTrace)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.sorted()
			.collect(Collectors.toList());
	}

	public Optional<RayTraceResult> rayTrace(Cuboid cuboid) {
		return rayTrace(cuboid, 0, distance).map(pos -> new RayTraceResult(pos, ray.origin.distance(pos), cuboid.sideOf(pos), cuboid));
	}

	/**
	 * Ray traces a cuboid
	 * @param cuboid The cuboid in absolute world coordinates
	 * @return The ray trace result if the ray intersects the cuboid
	 */
	public <R extends RayTraceResult> Optional<R> rayTrace(Cuboid cuboid, Function<Vector3d, R> resultMapper) {
		return rayTrace(cuboid, 0, distance).map(resultMapper);
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
		double txMin = (ray.dir.x == 0) ? minDist : (bbox.x - ray.origin.x) * ray.invDir.x;
		bbox = ray.signDirX ? cuboid.min : cuboid.max;
		double txMax = (ray.dir.x == 0) ? maxDist : (bbox.x - ray.origin.x) * ray.invDir.x;

		//Y
		bbox = ray.signDirY ? cuboid.max : cuboid.min;
		double tyMin = (ray.dir.y == 0) ? minDist : (bbox.y - ray.origin.y) * ray.invDir.y;
		bbox = ray.signDirY ? cuboid.min : cuboid.max;
		double tyMax = (ray.dir.y == 0) ? maxDist : (bbox.y - ray.origin.y) * ray.invDir.y;

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
		double tzMin = (ray.dir.z == 0) ? minDist : (bbox.z - ray.origin.z) * ray.invDir.z;
		bbox = ray.signDirZ ? cuboid.min : cuboid.max;
		double tzMax = (ray.dir.z == 0) ? maxDist : (bbox.z - ray.origin.z) * ray.invDir.z;

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
