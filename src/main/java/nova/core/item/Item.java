/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.item;

import nova.core.component.ComponentMap;
import nova.core.component.ComponentProvider;
import nova.core.component.misc.FactoryProvider;
import nova.core.entity.Entity;
import nova.core.event.bus.Event;
import nova.core.language.LanguageManager;
import nova.core.language.Translatable;
import nova.core.render.Color;
import nova.core.retention.Storable;
import nova.core.util.Direction;
import nova.core.util.Identifiable;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.List;
import java.util.Optional;

//TODO: This Storable implementation is flawed and not based on ID.
public class Item extends ComponentProvider<ComponentMap> implements Identifiable, Storable, Cloneable, Translatable {

	/**
	 * The amount of this item that is present.
	 */
	private int count = 1;

	/**
	 * Called to get the ItemFactory that refers to this Block class.
	 * @return The {@link nova.core.item.ItemFactory} that refers to this Block class.
	 */
	public final ItemFactory getFactory() {
		return (ItemFactory) components.get(FactoryProvider.class).factory;
	}

	@Override
	public final String getID() {
		return getFactory().getID();
	}

	@Override
	public String getUnlocalizedName() {
		return getFactory().getUnlocalizedName();
	}

	@Override
	public String getLocalizedName() {
		return LanguageManager.instance().translate(getUnlocalizedName() + ".name", this.getReplacements());
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
	 * @return {@code this} instance
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
		return getFactory().build(getFactory().save(this));
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
		return sameItemType(item) && getFactory().save(this).equals(item.getFactory().save(item)) && item.count == count;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 83 * hash + this.getID().hashCode();
		hash = 83 * hash + this.count;
		hash = 83 * hash + this.getFactory().save(this).hashCode();
		return hash;
	}

	/**
	 * Check if this Item is of type of another Item.
	 * Will compare the {@link Item#getID() item id}.
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
	//TODO: Convert to component system
	@Deprecated
	public Color colorMultiplier() {
		return Color.white;
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
