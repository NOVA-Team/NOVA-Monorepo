package nova.core.network;

import nova.core.network.NetworkTarget.Side;

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
	 * Sends a new custom packet.
	 * @param sender The packet handler sending the packet
	 * @param packet The packet to send
	 */
	public abstract void sendPacket(PacketHandler sender, Packet packet);

	/**
	 * Syncs a PacketHandler between server and client.
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
	public abstract void sync(int id, PacketHandler sender);

	public final Side getSide() {
		return isClient() ? Side.CLIENT : Side.SERVER;
	}

	public final boolean isClient() {
		return !isServer();
	}

	public abstract boolean isServer();
}
