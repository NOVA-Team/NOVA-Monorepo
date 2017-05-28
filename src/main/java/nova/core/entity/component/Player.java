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

package nova.core.entity.component;

import nova.core.component.UnsidedComponent;
import nova.core.component.inventory.InventoryPlayer;
import nova.core.entity.Entity;
import nova.core.util.UniqueIdentifiable;

/**
 * Basic representation of a Player.
 */
@UnsidedComponent
public abstract class Player extends Living implements UniqueIdentifiable {

	/**
	 * Get the entity that represents this player.
	 *
	 * @return The entity of the player
	 */
	public abstract Entity entity();

	/**
	 * Get the unique ID for this player. This ID is only
	 * unique in the context of the game in which
	 * the current NOVA instance is run.
	 * <p>
	 * No guarantees are made about cross-game ID uniqueness.
	 *
	 * @return Returns the unique ID representing the player
	 */
	@Override
	public abstract String getUniqueID();

	/**
	 * Get the player's inventory.
	 *
	 * @return Inventory of the player
	 */
	public abstract InventoryPlayer getInventory();

	/**
	 * Get the player's username.
	 * <p>
	 * The difference between this and {@link #getDisplayName()}
	 * is that the name returned by this method is undecorated,
	 * while name returned by {@link #getDisplayName()}
	 * may be decorated.
	 *
	 * @return Returns the undecorated non-identifying player name
	 */
	public abstract String getUsername();

	/**
	 * Get the player's display name.
	 * <p>
	 * The difference between this and {@link #getUsername()}
	 * is that the name returned by this method may be decorated,
	 * while {@link #getUsername()} returns the undecorated name.
	 *
	 * @return Returns the decorated non-identifying player name
	 */
	public String getDisplayName() {
		return getUsername();
	}
}
