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

package nova.core.wrapper.mc.forge.v1_11.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import nova.core.entity.component.Player;

import java.util.Objects;
import java.util.Optional;

/**
 * Wrap utility methods.
 * @author Calclavia
 */
public class WrapUtility {

	public static Optional<Player> getNovaPlayer(EntityPlayer player) {
		// TODO: implement
		return Optional.empty();
	}

	public static String getItemID(Item item, int meta) {
		if (item.getHasSubtypes()) {
			return Item.REGISTRY.getNameForObject(item) + ":" + meta;
		} else {
			return (String) Objects.toString(Item.REGISTRY.getNameForObject(item));
		}
	}

	public EntityPlayer getMCPlayer(Optional<Player> player) {
		return null;
	}
}
