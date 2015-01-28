package nova.core.block;

import nova.core.entity.Entity;
import nova.core.game.Game;
import nova.core.item.ItemStack;
import nova.core.util.Identifiable;
import nova.core.util.transform.Cuboid;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class Block implements Identifiable {
	private final BlockAccess blockAccess;
	private final Vector3i position;

	public Block(BlockAccess blockAccess, Vector3i position) {
		this.blockAccess = blockAccess;
		this.position = position;
	}

	public Collection<ItemStack> getDroppedStacks() {
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(Game.instance.get().itemManager.getItemFromBlock(this))); // -100 style points.
		return list;
	}

	public Cuboid getBoundingBox() {
		return new Cuboid(position, position.add(1));
	}

	public Set<Cuboid> getCollidingBoxes(Cuboid intersect, Entity entity) {
		Set<Cuboid> bounds = new HashSet<>();
		Cuboid defaultBound = getBoundingBox();

		if (defaultBound.intersects(intersect)) {
			bounds.add(getBoundingBox());
		}

		return bounds;
	}

	public BlockAccess getBlockAccess() {
		return blockAccess;
	}

	public Vector3i getPosition() {
		return position;
	}

	public int getX() {
		return position.x;
	}

	public int getY() {
		return position.y;
	}

	public int getZ() {
		return position.z;
	}

	public boolean isCube() {
		return true;
	}

	public boolean isOpaqueCube() {
		return isCube();
	}

	public void onNeighborChange(Vector3i neighborPosition) {

	}

	public void onPlaced(BlockChanger changer) {

	}

	public void onRemoved(BlockChanger changer) {

	}

	/**
	 * Called when the block is left clicked.
	 * @param entity - The entity that right clicked this object. Most likely a player.
	 * @param side - The side it was clicked.
	 * @param hit - The position it was clicked.
	 * @return True if the right click action does something.
	 */
	public boolean onLeftClick(Entity entity, int side, Vector3d hit) {
		return false;
	}

	/**
	 * Called when the block is right clicked.
	 * @param entity - The entity that right clicked this object. Most likely a player.
	 * @param side - The side it was clicked.
	 * @param hit - The position it was clicked.
	 * @return True if the right click action does something.
	 */
	public boolean onRightClick(Entity entity, int side, Vector3d hit) {
		return false;
	}

	/**
	 * Called when an entity collides with this block.
	 * More specifically, when the entity's block bounds coincide with the block bounds.
	 * @param entity
	 */
	public void onEntityCollide(Entity entity) {

	}
}
