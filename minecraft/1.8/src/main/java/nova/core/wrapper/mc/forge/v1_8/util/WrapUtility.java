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

package nova.core.wrapper.mc.forge.v1_8.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import nova.core.entity.Entity;
import nova.core.entity.component.Player;
import nova.core.wrapper.mc.forge.v1_8.wrapper.entity.EntityConverter;
import nova.core.wrapper.mc.forge.v1_8.wrapper.entity.backward.BWEntity;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Wrap utility methods.
 * @author Calclavia
 */
public class WrapUtility {

	private WrapUtility() {}

	public static Optional<Player> getNovaPlayer(@Nullable EntityPlayer player) {
		return Optional.ofNullable(player).map(EntityConverter.instance()::toNova).flatMap(e -> e.components.getOp(Player.class));
	}

	@Nonnull
	public static String getItemID(@Nonnull Item item, int meta) {
		if (item.getHasSubtypes()) {
			return String.format("%s:%d", Item.itemRegistry.getNameForObject(item), meta);
		} else {
			return String.valueOf(Item.itemRegistry.getNameForObject(item));
		}
	}

	@Nullable
	public static EntityPlayer getMCPlayer(Optional<Player> player) {
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
