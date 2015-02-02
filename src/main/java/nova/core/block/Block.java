package nova.core.block;

import nova.core.entity.Entity;
import nova.core.game.Game;
import nova.core.item.ItemStack;
import nova.core.render.Artist;
import nova.core.render.Texture;
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

	public final BlockFactory getFactory() {
		return Game.instance.get().blockManager.getBlockFactory(this.getID()).get();
	}

	public BlockAccess getBlockAccess() {
		return blockAccess;
	}

	public Vector3i getPosition() {
		return position;
	}

	public final int x() {
		return getPosition().x;
	}

	public final int y() {
		return getPosition().y;
	}

	public final int z() {
		return getPosition().z;
	}

	public Collection<ItemStack> getDrops() {
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(Game.instance.get().itemManager.getItemFromBlock(this))); // -100 style points.
		return list;
	}

	public Cuboid getBoundingBox() {
		return new Cuboid(new Vector3i(0, 0, 0), new Vector3i(1, 1, 1));
	}

	public Set<Cuboid> getCollidingBoxes(Cuboid intersect, Optional<Entity> entity) {
		Set<Cuboid> bounds = new HashSet<>();
		Cuboid defaultBound = getBoundingBox();

		if (defaultBound.add(position).intersects(intersect)) {
			bounds.add(getBoundingBox());
		}

		return bounds;
	}

	public boolean isCube() {
		return getBoundingBox().isCube();
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
	 * @param entity Colliding entity
	 */
	public void onEntityCollide(Entity entity) {

	}

	/**
	 * Called when this block is to be rendered.
	 * @param artist The artist who is rendering this block.
	 */
	public void renderWorld(Artist artist) {
		artist.renderBlock(this);
	}

	/**
	 * Called for a dynamic render.
	 * @param artist an {@link Artist} to use
	 */
	public void renderDynamic(Artist artist) {
	}

	public Optional<Texture> getTexture(Direction side) {
		return Optional.empty();
	}

	/**
	 * Called when the item of this block is to be rendered.
	 * @param artist The artist who is rendering this block.
	 */
	public void renderItem(Artist artist) {
		renderWorld(artist);
	}
}
