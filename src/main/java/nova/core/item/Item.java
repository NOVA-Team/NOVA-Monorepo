package nova.core.item;

import nova.core.entity.Entity;
import nova.core.game.Game;
import nova.core.player.Player;
import nova.core.render.Color;
import nova.core.render.texture.ItemTexture;
import nova.core.util.Direction;
import nova.core.util.Identifiable;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.core.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Item implements Identifiable {

	/**
	 * The amount of this item that is present.
	 */
	private int count = 1;

	/**
	 * Called to get the ItemFactory that refers to this Block class.
	 *
	 * @return The {@link nova.core.item.ItemFactory} that refers to this Block class.
	 */
	public final ItemFactory factory() {
		return Game.instance.get().itemManager.getItemFactory(this.getID()).get();
	}

	public int getMaxCount() {
		return 64;
	}

	/**
	 * @return Size of this stack size
	 */
	public int count() {
		return count;
	}

	/**
	 * Sets new size of this ItemStack
	 *
	 * @param size New size
	 */
	public Item setCount(int size) {
		count = Math.max(Math.min(getMaxCount(), size), 1);
		return this;
	}

	/**
	 * Adds size to this ItemStack
	 *
	 * @param size Size to add
	 * @return Size added
	 */
	public int addCount(int size) {
		int original = count();
		setCount(original + size);
		return count - original;
	}

	@Override
	public Item clone() {
		return factory().makeItem(factory().saveItem(this));
	}

	/**
	 * Returns new ItemStack of the same {@link Item} with specified size
	 *
	 * @param amount Size of cloned ItemStack
	 * @return new ItemStack
	 */
	public Item withAmount(int amount) {
		Item cloned = clone();
		cloned.setCount(amount);
		return cloned;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Item)) {
			return false;
		}
		Item item = (Item) o;
		//Makes sure the stored data and stacksize are the same in items.
		return sameItemType(item) && factory().saveItem(this).equals(item.factory().saveItem(item)) && item.count == count;
	}

	/**
	 * Check if this ItemStack is of type of another ItemStack. Will compare the
	 * {@link Item#getID()}.
	 *
	 * @param item The another Item
	 * @return Result
	 */
	public boolean sameItemType(Item item) {
		return getID().equals(item.getID());
	}

	/**
	 * Gets a list of tooltips for this item.
	 *
	 * @param player {@link Player}
	 * @return The tooltip strings
	 */
	public List<String> getTooltips(Optional<Player> player) {
		return new ArrayList<>();
	}

	/**
	 * Called when the entity right clicks the item onto the block.
	 *
	 * @param entity - The entity using the item
	 * @param world - The world of the block
	 * @param position - The position of the block
	 * @param side - The side the player clicked
	 * @param hit - The position the player hit on the block
	 * @return True if the player's action cancels out events.
	 */
	public boolean onUse(Entity entity, World world, Vector3i position, Direction side, Vector3d hit) {
		return false;
	}

	/**
	 * Called when an entity right clicks the item.
	 *
	 * @param entity - The entity right clicking.
	 */
	public void onRightClick(Entity entity) {

	}

	/**
	 * Called when this item is being rendered.
	 *
	 * @param type Type
	 * @param data Data
	 */
	public void onRender(int type, Object... data) {

	}

	/**
	 * Gets the color multiplier for rendering
	 * @return The color
	 */
	public Color colorMultiplier() {
		return Color.white;
	}

	/**
	 * Gets the texture of this item. If there is no texture provided, it will not render any and default to onRender() method for custom item rendering.
	 *
	 * @return {@link ItemTexture} instance
	 */
	public Optional<ItemTexture> getTexture() {
		return Optional.empty();
	}
}
