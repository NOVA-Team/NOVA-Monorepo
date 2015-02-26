package nova.core.network;

import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.item.Item;
import nova.core.network.NetworkTarget.Side;
import nova.core.util.exception.NovaException;

/**
 * A central network manager. 
 * @author Calclavia
 */
public abstract class NetworkManager {

	/**
	 * @return A new empty packet
	 */
	public abstract Packet newPacket();

	/**
	 * Syncs a PacketHandler between server and client.
	 *
	 * @param sender {@link PacketHandler}
	 */
	public final void sync(PacketHandler sender) {
		sync(0, sender);
	}

	/**
	 * Syncs a PacketHandler between server and client, with a specific packet ID
	 * @param id The packet ID
	 * @param sender sender {@link nova.core.network.PacketHandler}
	 */
	public final void sync(int id, PacketHandler sender) {
		if (sender instanceof Block) {
			syncBlock(id, sender);
			return;
		} else if (sender instanceof Item) {
			syncBlock(id, sender);
			return;
		} else if (sender instanceof Entity) {
			syncBlock(id, sender);
			return;
		}

		//TODO: Add custom packet object syncing.

		throw new NovaException("Packet type not supported!");
	}

	protected abstract void syncBlock(int id, PacketHandler sender);

	protected abstract void syncItem(int id, PacketHandler sender);

	protected abstract void syncEntity(int id, PacketHandler sender);

	public final Side getSide() {
		return isClient() ? Side.CLIENT : Side.SERVER;
	}

	public final boolean isClient() {
		return !isServer();
	}

	public abstract boolean isServer();
}
