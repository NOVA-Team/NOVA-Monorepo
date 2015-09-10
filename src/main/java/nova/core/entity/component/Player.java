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

import nova.core.component.inventory.InventoryPlayer;
import nova.core.entity.Entity;

/**
 * Basic representation of Player
 */
public abstract class Player extends Living {
	/**
	 * @return Returns player name that can be used to identify this player
	 */
	public abstract String getUsername();

	/**
	 * @return The entity of the player
	 */
	public abstract Entity entity();

	/**
	 * @return Returns the ID representing the player.
	 * For many games, the username is the ID.
	 */
	public String getPlayerID() {
		return getUsername();
	}

	/**
	 * @return Inventory of the player
	 */
	public abstract InventoryPlayer getInventory();

	/**
	 * @return Returns non-identifying player name
	 */
	public String getDisplayName() {
		return getUsername();
	}
}
