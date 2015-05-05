package nova.core.block;

import nova.core.entity.Entity;
import nova.core.game.Game;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.render.Color;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;
import nova.core.util.Identifiable;
import nova.core.util.transform.Cuboid;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.core.world.Positioned;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class Block extends Positioned<BlockWrapper, Vector3i> implements Identifiable, BlockWrapper {

	public ItemFactory getItemFactory() {
		return Game.instance.itemManager.getItemFactoryFromBlock(this);
	}

	/**
	 * Called to get the BlockFactory that refers to this Block class.
	 * @return The {@link nova.core.block.BlockFactory} that refers to this
	 * Block class.
	 */
	public final BlockFactory factory() {
		return Game.instance.blockManager.getFactory(getID()).get();
	}

	/**
	 * Get the x co-ordinate of the block.
	 * @return The x co-ordinate of the block.
	 */
	public final int x() {
		return position().x;
	}

	/**
	 * Get the y co-ordinate of the block.
	 * @return The y co-ordinate of the block.
	 */
	public final int y() {
		return position().y;
	}

	/**
	 * Get the z co-ordinate of the block.
	 * @return The z co-ordinate of the block.
	 */
	public final int z() {
		return position().z;
	}

	/**
	 * Called to get the drops of this block.
	 * @return A collection of {@link nova.core.item.Item}s that this block
	 * drops.
	 */
	public Set<Item> getDrops() {
		return Collections.singleton(Game.instance.itemManager.getItemFromBlock(this).makeItem());
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

		if (defaultBound.add(position()).intersects(intersect)) {
			bounds.add(getBoundingBox());
		}

		return bounds;
	}

	/**
	 * Called to check if the block is a cube.
	 * @return {@code true} is this block is a cube.
	 */
	public boolean isCube() {
		return getBoundingBox().isCube();
	}

	/**
	 * Called to check if the block is an opaque cube.
	 * @return {@code true} is this block is a cube that is opaque.
	 */
	public boolean isOpaqueCube() {
		return isCube();
	}

	/**
	 * Called when a block next to this one changes (removed, placed, etc...).
	 * @param neighborPosition The position of the block that changed.
	 */
	public void onNeighborChange(Vector3i neighborPosition) {

	}

	/**
	 * Called when the block is placed.
	 * @param changer The BlockChanger that placed the block.
	 */
	public void onPlaced(BlockChanger changer) {

	}

	/**
	 * Called when the block is removed.
	 * @param changer The BlockChanger that removed the block.
	 */
	public void onRemoved(BlockChanger changer) {

	}

	/**
	 * Called when the block is left clicked.
	 * @param entity The entity that right clicked this object. Most likely a
	 * player.
	 * @param side The side it was clicked.
	 * @param hit The position it was clicked.
	 * @return {@code true} if the right click action does something.
	 */
	public boolean onLeftClick(Entity entity, int side, Vector3d hit) {
		return false;
	}

	/**
	 * Called when the block is right clicked.
	 * @param entity The entity that right clicked this object. Most likely a
	 * player.
	 * @param side The side it was clicked.
	 * @param hit The position it was clicked.
	 * @return {@code true} if the right click action does something.
	 */
	public boolean onRightClick(Entity entity, int side, Vector3d hit) {
		return false;
	}

	/**
	 * Called when an entity collides with this block. More specifically, when
	 * the entity's block bounds coincide with the block bounds.
	 * @param entity colliding entity
	 */
	public void onEntityCollide(Entity entity) {

	}

	/**
	 * Called to get the texture of this block for a certain side.
	 * @param side The side of the block that the texture is for.
	 * @return An optional of the texture.
	 */
	public Optional<Texture> getTexture(Direction side) {
		return Optional.empty();
	}

	public boolean shouldRenderSide(Direction side) {
		return true;
	}

	/**
	 * Gets the color of a specific face. This is called by the default block
	 * renderer.
	 * @param side - The side of the block.
	 * @return The color
	 */
	public Color colorMultiplier(Direction side) {
		return Color.white;
	}

	/**
	 * Gets the breaking difficulty for the block. 1 is the standard, regular
	 * block hardness of the game. {@code Double.infinity} is unbreakable.
	 * @return The breaking difficulty.
	 */
	public double getHardness() {
		return 1;
	}

	/**
	 * Gets the explosion resistance for the block. 1 is the standard, regular
	 * resistance of the game. {@code Double.infinity} is unexplodeable.
	 * @return The resistance.
	 */
	public double getResistance() {
		return 1;
	}

}
