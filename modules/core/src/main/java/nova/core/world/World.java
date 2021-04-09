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
 */package nova.core.world;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.item.Item;
import nova.core.sound.Sound;
import nova.core.util.Identifiable;
import nova.core.util.shape.Cuboid;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Optional;
import java.util.Set;

/**
 * An in-game world
 * @author Calclavia
 */
public abstract class World implements Identifiable {

	/**
	 * Marks a position to render static.
	 * @param position The position to perform the static re-rendering.
	 */
	public abstract void markStaticRender(Vector3D position);

	/**
	 * Marks a specific block to indicate it changed.
	 * @param position The position being changed.
	 */
	public abstract void markChange(Vector3D position);

	/**
	 * Gets the block which occupies the given position.
	 * @param position The position to query.
	 * @return The block at the position. If the block is air, it will return the air block. If no block is present (the void), it will return an empty optional.
	 */
	public abstract Optional<Block> getBlock(Vector3D position);

	/**
	 * Sets the block occupying a given position.
	 * @param position The position of the block to set.
	 * @param blockFactory The block factory.
	 * @return {@code true} if the replace was successful.
	 */
	public abstract boolean setBlock(Vector3D position, BlockFactory blockFactory);

	/**
	 * Removes the block in the specified position.
	 * @param position the position of the block to remove.
	 * @return {@code true} if the block was removed.
	 */
	public boolean removeBlock(Vector3D position) {
		return setBlock(position, Game.blocks().getAirBlock());
	}

	/**
	 * Creates an entity
	 * @param factory The entity factory
	 * @return The added entity
	 */
	public abstract Entity addEntity(EntityFactory factory);

	/**
	 * Creates an entity that holds an item
	 * @param position The position to add the item to
	 * @param item The item
	 * @return The added entity
	 */
	public abstract Entity addEntity(Vector3D position, Item item);

	/**
	 * Creates an entity only on the client side.
	 * For example, particle effects.
	 * <p>
	 * <b>TODO:</b> Replace this with an actual particle system.
	 * </p>
	 * @param factory The entity factory
	 * @return The added entity
	 */
	public abstract Entity addClientEntity(EntityFactory factory);

	/**
	 * Creates an entity only on the client side.
	 * For example, particle effects.
	 * <p>
	 * <b>TODO:</b> Replace this with an actual particle system.
	 * </p>
	 * @param <T> The entity type
	 * @param entity The entity to add
	 * @return The added entity
	 */
	public abstract <T extends Entity> T addClientEntity(T entity);

	/**
	 * Destroys an entity, removing it from the world.
	 *
	 * @param entity The entity to remove
	 */
	public abstract void removeEntity(Entity entity);

	/**
	 * Gets an entity based on its UUID
	 * @param uniqueID The entity's unique ID
	 * @return The entity or empty if the entity does not exist.
	 */
	public abstract Optional<Entity> getEntity(String uniqueID);

	/**
	 * Gets a set of entities within a certain bound
	 * @param bound The boundary
	 * @return A set of entities.
	 */
	public abstract Set<Entity> getEntities(Cuboid bound);

	/**
	 * Plays a sound at a certain position.
	 * @param position The position in the world of the sound.
	 * @param soundFactory The sound factory that will create the sound to be played.
	 */
	public abstract void playSoundAtPosition(Vector3D position, Sound soundFactory);
}
