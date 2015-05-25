package nova.core.block.component;

import nova.core.block.Block;
import nova.core.component.misc.Collider;
import nova.core.entity.Entity;
import nova.core.util.transform.shape.Cuboid;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Handles block collision and bounds.
 * This component is required for most rendering operations.
 * @author Calclavia
 */
public class BlockCollider extends Collider {

	public final Block block;

	/**
	 * Called to check if the block is a cube.
	 * Returns true if this block is a cube.
	 */
	public Supplier<Boolean> isCube = () -> getBoundingBox().isCube();

	/**
	 * Called to check if the block is an opaque cube.
	 * Returns true if this block is a cube that is opaque.
	 */
	public Supplier<Boolean> isOpaqueCube = isCube;

	public BlockCollider(Block block) {
		this.block = block;
	}

	/**
	 * Called to get the bounding box of this block.
	 * @return The bounding box of this block.
	 */
	public Cuboid getBoundingBox() {
		return collisionBoxes.stream().findFirst().get();
	}

	/**
	 * Called to check for collisions.
	 * @param intersect Cuboid that could be colliding.
	 * @param entity The entity that could be colliding.
	 * @return Cuboids that represent colliding areas.
	 */
	public Set<Cuboid> getCollidingBoxes(Cuboid intersect, Optional<Entity> entity) {
		Set<Cuboid> bounds = new HashSet<>();
		Cuboid defaultBound = getBoundingBox();

		if (defaultBound.add(block.position()).intersects(intersect)) {
			bounds.add(getBoundingBox());
		}

		return bounds;
	}

	public BlockCollider setCube(boolean is) {
		isCube = () -> is;
		return this;
	}

	public BlockCollider setOpaqueCube(boolean is) {
		isOpaqueCube = () -> is;
		return this;
	}
}
