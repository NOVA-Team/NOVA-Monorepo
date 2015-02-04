package nova.core.block;

import nova.core.entity.Entity;
import nova.core.game.Game;
import nova.core.item.ItemStack;
import nova.core.render.model.Model;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;
import nova.core.util.Identifiable;
import nova.core.util.exception.NovaException;
import nova.core.util.transform.Cuboid;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.core.world.World;
import nova.internal.dummy.BlockAccessDummy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class Block implements Identifiable {

	private final BlockAccess blockAccess = BlockAccessDummy.INSTANCE;
	private final Vector3i position = Vector3i.ZERO;

	/**
	 * Called to get the BlockFactory that refers to this Block class.
	 *
	 * @return The {@link nova.core.block.BlockFactory} that refers to this Block class.
	 */
	public final BlockFactory getFactory() {
		return Game.instance.get().blockManager.getBlockFactory(this.getID()).get();
	}

	/**
	 * Get the BlockAccess that refers to this block.
	 *
	 * @return The {@link nova.core.block.BlockAccess} that refers to this block.
	 */
	public BlockAccess getBlockAccess() {
		return blockAccess;
	}

	/**
	 * Get the world that the block is in.
	 *
	 * @throws NovaException "Attempt to cast blockAccess to world invalidly!"
	 * @return {@link nova.core.world.World} that the block is in.
	 */
	public World getWorld() {
		if (blockAccess instanceof World) {
			return (World) blockAccess;
		}

		throw new NovaException("Attempt to cast blockAccess to world invalidly!");
	}

	/**
	 * Get the position of the block.
	 *
	 * @return The position of the block.
	 */
	public Vector3i getPosition() {
		return position;
	}

	/**
	 * Get the x co-ordinate of the block.
	 *
	 * @return The x co-ordinate of the block.
	 */
	public final int x() {
		return getPosition().x;
	}

	/**
	 * Get the y co-ordinate of the block.
	 *
	 * @return The y co-ordinate of the block.
	 */
	public final int y() {
		return getPosition().y;
	}

	/**
	 * Get the z co-ordinate of the block.
	 *
	 * @return The z co-ordinate of the block.
	 */
	public final int z() {
		return getPosition().z;
	}

	/**
	 * Called to get the drops of this block.
	 *
	 * @return A collection of {@link nova.core.item.ItemStack}s that this block drops.
	 */
	public Collection<ItemStack> getDrops() {
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(Game.instance.get().itemManager.getItemFromBlock(this))); // -100 style points.
		return list;
	}

	/**
	 * Called to get the bounding box of this block.
	 *
	 * @return The bounding box of this block.
	 */
	public Cuboid getBoundingBox() {
		return new Cuboid(new Vector3i(0, 0, 0), new Vector3i(1, 1, 1));
	}

	/**
	 * Called to check for collisions.
	 *
	 * @param intersect Cuboid that could be colliding.
	 * @param entity The entity that could be colliding.
	 * @return Cuboids that represent colliding areas.
	 */
	public Set<Cuboid> getCollidingBoxes(Cuboid intersect, Optional<Entity> entity) {
		Set<Cuboid> bounds = new HashSet<>();
		Cuboid defaultBound = getBoundingBox();

		if (defaultBound.add(position).intersects(intersect)) {
			bounds.add(getBoundingBox());
		}

		return bounds;
	}

	/**
	 * Called to check if the block is a cube.
	 *
	 * @return {@code true} is this block is a cube.
	 */
	public boolean isCube() {
		return getBoundingBox().isCube();
	}

	/**
	 * Called to check if the block is an opaque cube.
	 *
	 * @return {@code true}  is this block is a cube that is opaque.
	 */
	public boolean isOpaqueCube() {
		return isCube();
	}

	/**
	 * Called when a block next to this one changes (removed, placed, etc...).
	 *
	 * @param neighborPosition The position of the block that changed.
	 */
	public void onNeighborChange(Vector3i neighborPosition) {

	}

	/**
	 * Called when the block is placed.
	 *
	 * @param changer The BlockChanger that placed the block.
	 */
	public void onPlaced(BlockChanger changer) {

	}

	/**
	 * Called when the block is removed.
	 *
	 * @param changer The BlockChanger that removed the block.
	 */
	public void onRemoved(BlockChanger changer) {

	}

	/**
	 * Called when the block is left clicked.
	 *
	 * @param entity The entity that right clicked this object. Most likely a player.
	 * @param side The side it was clicked.
	 * @param hit The position it was clicked.
	 * @return {@code true} if the right click action does something.
	 */
	public boolean onLeftClick(Entity entity, int side, Vector3d hit) {
		return false;
	}

	/**
	 * Called when the block is right clicked.
	 *
	 * @param entity The entity that right clicked this object. Most likely a player.
	 * @param side The side it was clicked.
	 * @param hit The position it was clicked.
	 * @return {@code true}  if the right click action does something.
	 */
	public boolean onRightClick(Entity entity, int side, Vector3d hit) {
		return false;
	}

	/**
	 * Called when an entity collides with this block.
	 * More specifically, when the entity's block bounds coincide with the block bounds.
	 *
	 * @param entity colliding entity
	 */
	public void onEntityCollide(Entity entity) {

	}

	/**
	 * Called when this block is to be rendered.
	 *
	 * @param model A {@link nova.core.render.model.Model} to use.
	 */
	public void renderStatic(Model model) {
		model.renderBlock(this);
	}

	/**
	 * Called for a dynamic render.
	 *
	 * @param model A {@link nova.core.render.model.Model} to use
	 */
	public void renderDynamic(Model model) {
	}

	/**
	 * Called to get the texture of this block for a certain side.
	 *
	 * @param side The side of the block that the texture is for.
	 * @return An optional of the texture.
	 */
	public Optional<Texture> getTexture(Direction side) {
		return Optional.empty();
	}

	/**
	 * Called when the item of this block is to be rendered.
	 *
	 * @param model A {@link nova.core.render.model.Model} to use.
	 */
	public void renderItem(Model model) {
		renderStatic(model);
	}
}
