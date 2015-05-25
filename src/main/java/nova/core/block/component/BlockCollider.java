package nova.core.block.component;

import nova.core.block.Block;
import nova.core.component.Component;
import nova.core.entity.Entity;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3i;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Handles block collision and bounds.
 * This component is required for most rendering operations.
 * @author Calclavia
 */
public class BlockCollider extends Component {

	public final Block block;

	/**
	 * Called when an entity collides with this block. More specifically, when
	 * the entity's block bounds coincide with the block bounds.
	 * @param entity colliding entity
	 */
	public Consumer<Entity> onEntityCollide = (entity) -> {
	};

	/**
	 * Called to check if the block is a cube.
	 * @return {@code true} is this block is a cube.
	 */
	public Supplier<Boolean> isCube = () -> getBoundingBox().isCube();

	/**
	 * Called to check if the block is an opaque cube.
	 * @return {@code true} is this block is a cube that is opaque.
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
		return new Cuboid(new Vector3i(0, 0, 0), new Vector3i(1, 1, 1));
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

		if (defaultBound.add(block.transform.position()).intersects(intersect)) {
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

	public BlockCollider setEntityCollide(Consumer<Entity> onEntityCollide) {
		this.onEntityCollide = onEntityCollide;
		return this;
	}
}
