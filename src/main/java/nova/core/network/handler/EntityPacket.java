package nova.core.network.handler;

import nova.core.entity.Entity;
import nova.core.network.Packet;
import nova.core.network.PacketHandler;
import nova.core.util.exception.NovaException;

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
public class EntityPacket implements PacketType<Entity> {

	@Override
	public Entity read(Packet packet) {
		Entity playerEntity = packet.player().entity();

		String uuid = packet.readString();
		Optional<Entity> op = playerEntity.world().getEntity(uuid);

		if (op.isPresent()) {
			Entity entity = op.get();
			if (entity instanceof PacketHandler) {
				((PacketHandler) entity).read(packet);
				return entity;
			}
		}
		throw new NovaException("Failed to receive packet to entity with UID" + uuid);
	}

	@Override
	public void write(Entity entity, Packet packet) {
		if (entity instanceof PacketHandler) {
			packet.write(entity.getUniqueID());
			((PacketHandler) entity).write(packet);
			return;
		}

		throw new NovaException("Failed to send packet for entity: " + entity);
	}

	@Override
	public Class<Entity> handler() {
		return Entity.class;
	}
}

