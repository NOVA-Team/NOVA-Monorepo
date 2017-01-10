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

package nova.core.wrapper.mc.forge.v18.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import nova.core.entity.Entity;
import nova.core.entity.component.Player;
import nova.core.wrapper.mc.forge.v18.wrapper.entity.backward.BWEntity;
import nova.internal.core.Game;

import java.util.Optional;

/**
 * Wrap utility methods.
 * @author Calclavia
 */
public class WrapUtility {

	public static Optional<Player> getNovaPlayer(EntityPlayer player) {
		return ((Entity)Game.natives().toNova(player)).components.getOp(Player.class);
	}

	public static String getItemID(Item item, int meta) {
		if (item.getHasSubtypes()) {
			return Item.itemRegistry.getNameForObject(item) + ":" + meta;
		} else {
			return (String) Item.itemRegistry.getNameForObject(item);
		}
	}

	public EntityPlayer getMCPlayer(Optional<Player> player) {
		if (!player.isPresent())
			return null;

		Entity entity = player.get().entity();
		if (entity instanceof BWEntity) {
			if (((BWEntity)entity).entity instanceof EntityPlayer)
				return (EntityPlayer)((BWEntity)entity).entity;
		}

		// TODO: Implement FWEntityPlayer
		return null;
	}
}
