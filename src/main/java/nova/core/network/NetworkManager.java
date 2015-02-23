package nova.core.network;

import nova.core.block.Block;
import nova.core.network.NetworkTarget.Side;
import nova.core.util.exception.NovaException;

/**
 * @author Calclavia
 */
public abstract class NetworkManager {

	/**
	 * Sends a packet.
	 *
	 * @param sender {@link PacketHandler}
	 */
	public final void sync(PacketHandler sender) {
		sync(0, sender);
	}

	public final void sync(int id, PacketHandler sender) {
		if (sender instanceof Block) {
			syncBlock(id, sender);
			return;
		}

		throw new NovaException("Packet type not supported!");
	}

	protected abstract void syncBlock(int id, PacketHandler sender);

	public final Side getSide() {
		return isClient() ? Side.CLIENT : Side.SERVER;
	}

	public final boolean isClient() {
		return !isServer();
	}

	public abstract boolean isServer();
}
