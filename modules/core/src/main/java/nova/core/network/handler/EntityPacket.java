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

package nova.core.network.handler;

import nova.core.entity.Entity;
import nova.core.network.NetworkException;
import nova.core.network.Packet;
import nova.core.network.Syncable;

import java.util.Optional;

/**
 * Handles Entity packets
 *
 * Packet Structure:
 * 1. Entity UID
 * 2. Data
 *
 * @author Calclavia
 */
public class EntityPacket implements PacketHandler<Entity> {

	@Override
	public void read(Packet packet) {
		Entity playerEntity = packet.player().entity();

		String uuid = packet.readString();
		Optional<Entity> op = playerEntity.world().getEntity(uuid);

		if (op.isPresent()) {
			Entity entity = op.get();
			if (entity instanceof Syncable) {
				((Syncable) entity).read(packet);
				return;
			}
		}
		throw new NetworkException("Failed to receive packet to entity with UID" + uuid);
	}

	@Override
	public void write(Entity entity, Packet packet) {
		if (entity instanceof Syncable) {
			packet.write(entity.getUniqueID());
			((Syncable) entity).write(packet);
			return;
		}

		throw new NetworkException("Failed to send packet for entity: " + entity);
	}

	@Override
	public boolean isHandlerFor(Object handler) {
		return handler instanceof Entity;
	}
}

