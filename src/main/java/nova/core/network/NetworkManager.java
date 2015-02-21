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
	 * @param sender {@link PacketSender}
	 */
	public final void sync(PacketSender sender) {
		sync(0, sender);
	}

	public final void sync(int id, PacketSender sender) {
		if (sender instanceof Block) {
			syncBlock(id, sender);
			return;
		}

		System.out.println("Packet sender type not supported!");
		throw new NovaException();
	}

	protected abstract void syncBlock(int id, PacketSender sender);

	public final Side getSide() {
		return isClient() ? Side.CLIENT : Side.SERVER;
	}

	public final boolean isClient() {
		return !isServer();
	}

	public abstract boolean isServer();
}
