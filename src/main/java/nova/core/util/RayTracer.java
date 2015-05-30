package nova.core.util;

import nova.core.block.Block;
import nova.core.component.misc.Collider;
import nova.core.entity.Entity;
import nova.core.entity.component.Living;
import nova.core.util.collection.Tuple2;
import nova.core.util.math.MathUtil;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3d;
import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Ray tracing for cuboids.
 * Based on ChickenBone's Chicken Code Library ray tracer
 * @author Calclavia, ChickenBones
 */
public class RayTracer {

	private final Vector3d start;
	private final Vector3d end;

	private Vector3d vec = new Vector3d();
	private Vector3d vec2 = new Vector3d();

	private Vector3d hitVec = new Vector3d();
	private double leastDist;
	private int hitSide;

	/**
	 * Creates a ray tracer with a line segment that will be traced.
	 * @param start Start of the line segment
	 * @param end End of the line segment
	 */
	public RayTracer(Vector3d start, Vector3d end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Does an entity look ray trace to see which block the entity is looking at.
	 * @param entity The entity
	 * @return The block the entity is looking at.
	 */
	public static List<RayTraceBlockResult> rayTraceBlock(Entity entity, double maxDistance) {
		return rayTraceBlock(
			entity.world(),
			entity.position().add(entity.has(Living.class) ? entity.get(Living.class).faceDisplacement.get() : Vector3d.zero),
			entity.rotation().toZVector(),
			maxDistance
		);
	}

	public static List<RayTraceBlockResult> rayTraceBlock(World world, Vector3d position, Vector3d look, double maxDistance) {
		//TODO: Very inefficient! Consider smaller sample space
		Cuboid checkRegion = Cuboid.zero.expand(maxDistance).add(position);
		Set<Vector3i> checkPositions = new HashSet<>();
		checkRegion.forEach(checkPositions::add);

		//All relevant blocks
		Set<Block> blocks = checkPositions.stream()
			.map(world::getBlock)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.filter(block -> block.has(Collider.class))
			.collect(Collectors.toSet());

		return new RayTracer(position, position.add(look.multiply(maxDistance))).rayTraceBlocks(blocks);
	}

	/**
	 * Traces a side of a cuboid
	 */
	private void traceSide(int side, Vector3d start, Vector3d end, Cuboid cuboid) {
		vec = start;
		Vector3d hit = null;
		switch (side) {
			case 0:
				hit = vec.XZintercept(end, cuboid.min.y);
				break;
			case 1:
				hit = vec.XZintercept(end, cuboid.max.y);
				break;
			case 2:
				hit = vec.XYintercept(end, cuboid.min.z);
				break;
			case 3:
				hit = vec.XYintercept(end, cuboid.max.z);
				break;
			case 4:
				hit = vec.YZintercept(end, cuboid.min.x);
				break;
			case 5:
				hit = vec.YZintercept(end, cuboid.max.x);
				break;
		}
		if (hit == null) {
			return;
		}

		switch (side) {
			case 0:
			case 1:
				if (!MathUtil.isBetween(cuboid.min.x, hit.x, cuboid.max.x) || !MathUtil.isBetween(cuboid.min.z, hit.z, cuboid.max.z)) {
					return;
				}
				break;
			case 2:
			case 3:
				if (!MathUtil.isBetween(cuboid.min.x, hit.x, cuboid.max.x) || !MathUtil.isBetween(cuboid.min.y, hit.y, cuboid.max.y)) {
					return;
				}
				break;
			case 4:
			case 5:
				if (!MathUtil.isBetween(cuboid.min.y, hit.y, cuboid.max.y) || !MathUtil.isBetween(cuboid.min.z, hit.z, cuboid.max.z)) {
					return;
				}
				break;
		}

		vec2 = hit;
		double dist = vec2.subtract(start).magnitudeSquared();

		if (dist < leastDist) {
			hitSide = side;
			leastDist = dist;
			hitVec = vec;
		}
	}

	/**
	 * @return True if the cuboid intersects the ray.
	 */
	private boolean doRayTrace(Cuboid cuboid) {
		leastDist = Double.MAX_VALUE;
		hitSide = -1;

		for (int i = 0; i < 6; i++)
			traceSide(i, start, end, cuboid);

		return hitSide >= 0;
	}

	public Optional<RayTraceResult> rayTrace(Cuboid cuboid) {
		return Optional.ofNullable(doRayTrace(cuboid) ? new RayTraceResult(hitVec, leastDist, Direction.fromOrdinal(hitSide), cuboid) : null);
	}

	/**
	 * Ray traces a set of cuboids
	 * @param cuboids Set of cuboids
	 * @return A list of cuboids that intersect with the line segment in the order from closest to furthest.
	 */
	public List<RayTraceResult> rayTrace(Set<Cuboid> cuboids) {
		return cuboids.stream()
			.map(this::rayTrace)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.sorted()
			.collect(Collectors.toList());
	}

	/**
	 * Ray traces a set of blocks
	 * @param blocks Set of blocks
	 * @return A list of cuboids that intersect with the line segment in the order from closest to furthest.
	 */
	public List<RayTraceBlockResult> rayTraceBlocks(Set<Block> blocks) {
		return blocks.stream()
			.filter(block -> block.has(Collider.class))
			.map(block -> new Tuple2<>(
					block,
					block.get(Collider.class)
						.occlusionBoxes
						.apply(Optional.empty())
						.stream()
						.map(cuboid -> cuboid.add(block.position()))
						.collect(Collectors.toSet())
				)
			)
			.flatMap(tuple ->
					rayTrace(tuple._2)
						.stream()
						.map(r -> new RayTraceBlockResult(r.hit, r.distance, r.side, r.hitCuboid, tuple._1))
			)
			.sorted()
			.collect(Collectors.toList());
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
}
