package nova.core.network.handler;

import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.network.Packet;
import nova.core.network.PacketHandler;
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
public class BlockPacket implements PacketType<Block> {

	@Override
	public Block read(Packet packet) {
		Entity entity = packet.player().entity();
		Vector3i position = packet.read(Vector3i.class);

		Optional<Block> opBlock = entity.world().getBlock(position);

		if (opBlock.isPresent()) {
			Block block = opBlock.get();
			if (block instanceof PacketHandler) {
				((PacketHandler) block).read(packet);
				return block;
			}
		}
		throw new NovaException("Failed to receive packet to block at " + position);
	}

	@Override
	public void write(Block block, Packet packet) {
		if (block instanceof PacketHandler) {
			Vector3i position = block.position();
			packet.write(position);
			((PacketHandler) block).write(packet);
			return;
		}

		throw new NovaException("Failed to send packet for block: " + block);
	}

	@Override
	public Class<Block> handler() {
		return Block.class;
	}
}

