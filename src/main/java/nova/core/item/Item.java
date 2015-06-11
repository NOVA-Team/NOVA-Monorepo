package nova.core.item;

import nova.core.component.ComponentProvider;
import nova.core.entity.Entity;
import nova.core.event.Event;
import nova.core.event.EventBus;
import nova.core.render.Color;
import nova.core.render.texture.ItemTexture;
import nova.core.util.Direction;
import nova.core.util.Identifiable;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.List;
import java.util.Optional;

//TODO: Convert to component system
public abstract class Item extends ComponentProvider implements Identifiable {

	public final EventBus<Event> events = new EventBus<>();

	/**
	 * The amount of this item that is present.
	 */
	private int count = 1;

	/**
	 * Called to get the ItemFactory that refers to this Block class.
	 * @return The {@link nova.core.item.ItemFactory} that refers to this Block class.
	 */
	public final ItemFactory factory() {
		return Game.items().getItem(this.getID()).get();
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
	 * @param size New size
	 */
	public Item setCount(int size) {
		count = Math.max(Math.min(getMaxCount(), size), 0);
		return this;
	}

	/**
	 * Adds size to this ItemStack
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
	 * @param item The another Item
	 * @return Result
	 */
	public boolean sameItemType(Item item) {
		return getID().equals(item.getID());
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
	 * @return {@link ItemTexture} instance
	 */
	public Optional<ItemTexture> getTexture() {
		return Optional.empty();
	}

	public static class TooltipEvent extends Event {
		public final Optional<Entity> entity;
		public final List<String> tooltips;

		/**
		 * Gets a list of tooltips for this item.
		 * @param entity {@link Entity} with the component Player attached.
		 * @param tooltips The tooltip list to add to.
		 */
		public TooltipEvent(Optional<Entity> entity, List<String> tooltips) {
			this.entity = entity;
			this.tooltips = tooltips;
		}
	}

	public static class UseEvent extends Event {
		//The entity that right clicked
		public final Entity entity;

		public final Vector3D position;

		public final Direction side;

		public final Vector3D hit;

		//Did this event cause an action? True if the player's action cancels out events.
		public boolean action = false;

		/**
		 * Called when the entity right clicks the item onto the block.
		 * @param entity - The entity using the item
		 * @param position - The position of the block
		 * @param side - The side the player clicked
		 * @param hit - The position the player hit on the block
		 */
		public UseEvent(Entity entity, Vector3D position, Direction side, Vector3D hit) {
			this.entity = entity;
			this.position = position;
			this.side = side;
			this.hit = hit;
		}
	}

	public static class RightClickEvent extends Event {
		//The entity that right clicked
		public final Entity entity;

		public RightClickEvent(Entity entity) {
			this.entity = entity;
		}
	}
}
