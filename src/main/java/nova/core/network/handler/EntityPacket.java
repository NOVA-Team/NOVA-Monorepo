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

