package nova.core.block;

import nova.core.entity.Entity;
import nova.core.game.Game;
import nova.core.item.ItemStack;
import nova.core.render.model.Model;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;
import nova.core.util.Identifiable;
import nova.core.util.transform.Cuboid;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
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
	 * @return the BlockFactory that refers to this Block class.
	 */
	public final BlockFactory getFactory() {
		return Game.instance.get().blockManager.getBlockFactory(this.getID()).get();
	}

	/**
	 * Get the BlockAccess that refers to this block.
	 * @return the BlockAccess that refers to this block.
	 */
	public BlockAccess getBlockAccess() {
		return blockAccess;
	}

	/**
	 * Get the position of the block.
	 * @return the position of the block.
	 */
	public Vector3i getPosition() {
		return position;
	}

	/**
	 * Get the x co-ordinate of the block.
	 * @return the x co-ordinate of the block.
	 */
	public final int x() {
		return getPosition().x;
	}

	/**
	 * Get the y co-ordinate of the block.
	 * @return the y co-ordinate of the block.
	 */
	public final int y() {
		return getPosition().y;
	}

	/**
	 * Get the z co-ordinate of the block.
	 * @return the z co-ordinate of the block.
	 */
	public final int z() {
		return getPosition().z;
	}

	/**
	 * Called to get the drops of this block.
	 * @return a collection of ItemStacks that this block drops.
	 */
	public Collection<ItemStack> getDrops() {
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(Game.instance.get().itemManager.getItemFromBlock(this))); // -100 style points.
		return list;
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

		if (defaultBound.add(position).intersects(intersect)) {
			bounds.add(getBoundingBox());
		}

		return bounds;
	}

	/**
	 * Called to check if the block is a cube.
	 * @return true is this block is a cube.
	 */
	public boolean isCube() {
		return getBoundingBox().isCube();
	}

	/**
	 * Called to check if the block is an opaque cube.
	 * @return true is this block is a cube that is opaque.
	 */
	public boolean isOpaqueCube() {
		return isCube();
	}

	/**
	 * Called when a block next to this one changes (removed, placed, etc...).
	 * @param neighborPosition the position of the block that changed.
	 */
	public void onNeighborChange(Vector3i neighborPosition) {

	}

	/**
	 * Called when the block is placed.
	 * @param changer the BlockChanger that placed the block.
	 */
	public void onPlaced(BlockChanger changer) {

	}

	/**
	 * Called when the block is removed.
	 * @param changer the BlockChanger that removed the block.
	 */
	public void onRemoved(BlockChanger changer) {

	}

	/**
	 * Called when the block is left clicked.
	 * @param entity The entity that right clicked this object. Most likely a player.
	 * @param side The side it was clicked.
	 * @param hit The position it was clicked.
	 * @return true if the right click action does something.
	 */
	public boolean onLeftClick(Entity entity, int side, Vector3d hit) {
		return false;
	}

	/**
	 * Called when the block is right clicked.
	 * @param entity The entity that right clicked this object. Most likely a player.
	 * @param side The side it was clicked.
	 * @param hit The position it was clicked.
	 * @return true if the right click action does something.
	 */
	public boolean onRightClick(Entity entity, int side, Vector3d hit) {
		return false;
	}

	/**
	 * Called when an entity collides with this block.
	 * More specifically, when the entity's block bounds coincide with the block bounds.
	 * @param entity colliding entity.
	 */
	public void onEntityCollide(Entity entity) {

	}

	/**
	 * Called when this block is to be rendered.
	 * @param model a {@link nova.core.render.model.Model} to use.
	 */
	public void renderWorld(Model model) {
		model.renderBlock(this);
	}

	/**
	 * Called for a dynamic render.
	 * @param model a {@link nova.core.render.model.Model} to use.
	 */
	public void renderDynamic(Model model) {
	}

	/**
	 * Called to get the texture of this block for a certain side.
	 * @param side The side of the block that the texture is for.
	 * @return an optional of the texture.
	 */
	public Optional<Texture> getTexture(Direction side) {
		return Optional.empty();
	}

	/**
	 * Called when the item of this block is to be rendered.
	 * @param model a {@link nova.core.render.model.Model} to use.
	 */
	public void renderItem(Model model) {
		renderWorld(model);
	}
}
