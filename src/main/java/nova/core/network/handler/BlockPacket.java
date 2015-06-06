package nova.core.network.handler;

import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.network.Packet;
import nova.core.network.Syncable;
import nova.core.util.exception.NovaException;
import nova.core.util.transform.vector.Vector3i;

import java.util.Optional;

/**
 * Handles block packets
 *
 * Packet Structure:
 * 1. PosX
 * 2. PosY
 * 3. PosZ
 * 5. Data
 *
 * @author Calclavia
 */
public class BlockPacket implements PacketHandler<Block> {

	@Override
	public void read(Packet packet) {
		Entity entity = packet.player().entity();
		Vector3i position = new Vector3i(packet.readInt(), packet.readInt(), packet.readInt());

		Optional<Block> opBlock = entity.world().getBlock(position);

		if (opBlock.isPresent()) {
			Block block = opBlock.get();
			if (block instanceof Syncable) {
				((Syncable) block).read(packet);
				return;
			}
		}
		throw new NovaException("Failed to receive packet to block at " + position);
	}

	@Override
	public void write(Block block, Packet packet) {
		if (block instanceof Syncable) {
			Vector3i position = block.position();
			packet.writeInt(position.x);
			packet.writeInt(position.y);
			packet.writeInt(position.z);
			((Syncable) block).write(packet);
			return;
		}

		throw new NovaException("Failed to send packet for block: " + block);
	}

	@Override
	public boolean isHandlerFor(Object block) {
		return block instanceof Block;
	}
}

