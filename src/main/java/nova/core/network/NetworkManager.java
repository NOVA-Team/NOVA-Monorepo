package nova.core.network;

import nova.core.block.Block;
import nova.core.util.exception.NovaException;

import java.util.Optional;

/**
 * @author Calclavia
 */
public abstract class NetworkManager {

	//TODO: This is a bit hacky. Maybe we should do DI for this.
	public static Optional<NetworkManager> instance;

	protected NetworkManager() {
		instance = Optional.of(this);
	}

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

	public final boolean isClient() {
		return !isServer();
	}

	public abstract boolean isServer();
}
