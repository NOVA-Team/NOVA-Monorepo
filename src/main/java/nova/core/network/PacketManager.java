package nova.core.network;

import nova.core.block.Block;
import nova.core.util.NovaException;

import java.util.Optional;

/**
 * @author Calclavia
 */
public abstract class PacketManager {

	//TODO: This is a bit hacky. Maybe we should do DI for this.
	public static Optional<PacketManager> instance;

	protected PacketManager() {
		instance = Optional.of(this);
	}

	/**
	 * Sends a packet.
	 */
	public final void sync(PacketSender sender) {
		if (sender instanceof Block) {
			syncBlock(sender);
			return;
		}

		System.out.println("Packet sender type not supported!");
		throw new NovaException();
	}

	protected abstract void syncBlock(PacketSender sender);
}
