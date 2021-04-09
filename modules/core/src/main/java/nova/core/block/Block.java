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

package nova.core.block;

import nova.core.block.component.BlockProperty;
import nova.core.component.SidedComponentProvider;
import nova.core.component.misc.FactoryProvider;
import nova.core.component.transform.BlockTransform;
import nova.core.entity.Entity;
import nova.core.event.bus.CancelableEvent;
import nova.core.event.bus.Event;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.language.LanguageManager;
import nova.core.language.Translatable;
import nova.core.util.Direction;
import nova.core.util.Identifiable;
import nova.core.world.World;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * The class that represents the instance of a block in the world.
 *
 * @author Calclavia
 */
public class Block extends SidedComponentProvider implements Identifiable, Translatable {

	/**
	 * Called to get the {@link ItemFactory} that refers to this Block instance.
	 *
	 * @return The {@link ItemFactory} that refers to this Block instance.
	 */
	public ItemFactory getItemFactory() {
		return Game.items().getItemFromBlock(getFactory());
	}

	/**
	 * Called to get the {@link BlockFactory} that refers to this Block class.
	 *
	 * @return The {@link BlockFactory} that refers to this Block class.
	 */
	public final BlockFactory getFactory() {
		return (BlockFactory) components.get(FactoryProvider.class).factory;
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
		return LanguageManager.instance().translate(this.getUnlocalizedName() + ".name", this.getReplacements());
	}

	/**
	 * Gets the block transformation data.
	 * ({@link #world() world} and {@link #position() position})
	 *
	 * @return The block transformation data
	 */
	public final BlockTransform transform() {
		return components.get(BlockTransform.class);
	}

	/**
	 * Gets the world that this block resides in.
	 *
	 * @return The world that this block resides in
	 */
	public final World world() {
		return transform().world();
	}

	/**
	 * Gets the position of this block in the world.
	 *
	 * @return The position of this block
	 */
	public final Vector3D position() {
		return transform().position();
	}

	/**
	 * Get the {@code X} coordinate of this block.
	 *
	 * @return The {@code X} coordinate of this block
	 */
	public final long x() {
		return (long) FastMath.floor(position().getX());
	}

	/**
	 * Get the {@code Y} coordinate of this block.
	 *
	 * @return The {@code Y} coordinate of this block
	 */
	public final long y() {
		return (long) FastMath.floor(position().getY());
	}

	/**
	 * Get the {@code Z} coordinate of this block.
	 *
	 * @return The {@code Z} coordinate of this block
	 */
	public final long z() {
		return (long) FastMath.floor(position().getZ());
	}

	/**
	 * Gets the breaking difficulty for the block. 1 is the standard, regular
	 * block hardness of the game. {@code Double.infinity} is unbreakable.
	 * @return The breaking difficulty.
	 */
	public double getHardness() {
		return components.getOp(BlockProperty.Hardness.class).orElseGet(BlockProperty.Hardness::new).getHardness();
	}

	/**
	 * Gets the explosion resistance for the block. 1 is the standard, regular
	 * resistance of the game. {@code Double.infinity} is unexplodeable.
	 * @return The resistance.
	 */
	public double getResistance() {
		return components.getOp(BlockProperty.Resistance.class).orElseGet(BlockProperty.Resistance::new).getResistance();
	}

	/**
	 * Checks if this block can be replaced.
	 * @return True if this block can be replaced.
	 */
	public boolean canReplace() {
		return components.getOp(BlockProperty.Replaceable.class).filter(BlockProperty.Replaceable::isReplaceable).isPresent();
	}

	/**
	 * Called when an ItemBlock tries to place a block in this position whether to displace the place position or not.
	 * If the ItemBlock does not displace the position, it will replace this block.
	 * TODO move out into BlockProperty
	 * @return True if by right clicking on this block, the placement of the new block should be displaced.
	 */
	public boolean shouldDisplacePlacement() {
		return true;
	}

	// Block Events

	/**
	 * The event that is called when a block next to this one changes (removed, placed, etc...).
	 */
	public static class NeighborChangeEvent extends CancelableEvent {
		public final Optional<Vector3D> neighborPosition;

		/**
		 * Called when a block next to this one changes (removed, placed, etc...).
		 * @param neighborPosition The position of the block that changed.
		 */
		public NeighborChangeEvent(Optional<Vector3D> neighborPosition) {
			this.neighborPosition = neighborPosition;
		}
	}

	/**
	 * The event that is called when the block is placed.
	 */
	public static class PlaceEvent extends Event {

		/**
		 * The entity that placed the block
		 */
		public final Entity placer;

		/**
		 * Side placed on
		 */
		public final Direction side;

		/**
		 * Where the player clicked on the block for placement.
		 */
		public final Vector3D hit;

		/**
		 * The item used to place this block.
		 */
		public final Item item;

		/**
		 * Called when the block is placed.
		 *
		 * @param placer The entity that placed the block
		 * @param side The side placed on
		 * @param hit Where the entity clicked on the block for placement
		 * @param item The item used to place this block
		 */
		public PlaceEvent(Entity placer, Direction side, Vector3D hit, Item item) {
			this.placer = placer;
			this.side = side;
			this.hit = hit;
			this.item = item;
		}
	}

	/**
	 * The event that is called when the block is about to be removed.
	 */
	public static class RemoveEvent extends Event {
		/**
		 * The entity that is removing the block
		 */
		public final Optional<Entity> entity;

		/**
		 * {@code true} if the block can be removed
		 */
		public boolean result = true;

		/**
		 * Called when the block is about to be removed.
		 *
		 * @param entity The entity that is removing the block
		 */
		public RemoveEvent(Optional<Entity> entity) {
			this.entity = entity;
		}
	}

	/**
	 * The event that is called when the block is right clicked.
	 */
	public static class RightClickEvent extends Event {
		/**
		 * The entity that clicked this object. Most likely a
		 * player.
		 */
		public final Entity entity;

		/**
		 * The side it was clicked.
		 */
		public final Direction side;

		/**
		 * The position it was clicked.
		 */
		public final Vector3D position;

		/**
		 * {@code true} if the right click action does something.
		 */
		public boolean result = false;

		/**
		 * Called when the block is right clicked.
		 *
		 * @param entity The entity that clicked this object
		 * @param side The side it was clicked
		 * @param position The position it was clicked
		 */
		public RightClickEvent(Entity entity, Direction side, Vector3D position) {
			this.entity = entity;
			this.side = side;
			this.position = position;
		}
	}

	/**
	 * The event that is called when the block is left clicked.
	 */
	public static class LeftClickEvent extends Event {
		/**
		 * The entity that clicked this object. Most likely a
		 * player.
		 */
		public final Entity entity;

		/**
		 * The side it was clicked.
		 */
		public final Direction side;

		/**
		 * The position it was clicked.
		 */
		public final Vector3D position;

		/**
		 * {@code true} if the right click action does something.
		 */
		public boolean result = false;

		/**
		 * Called when the block is left clicked.
		 *
		 * @param entity The entity that clicked this object
		 * @param side The side it was clicked
		 * @param position The position it was clicked
		 */
		public LeftClickEvent(Entity entity, Direction side, Vector3D position) {
			this.entity = entity;
			this.side = side;
			this.position = position;
		}
	}

	/**
	 * The event that is called when the block is broken and it is time to drop it as an item.
	 */
	public static class DropEvent extends Event {

		/**
		 * The set of items to drop.
		 */
		public Set<Item> drops;

		/**
		 * Called when the block is broken and it is time to drop it as an item.
		 *
		 * @param block The block that was broken.
		 */
		public DropEvent(Block block) {
			this.drops = Collections.singleton(block.getItemFactory().build());
		}
	}
}
