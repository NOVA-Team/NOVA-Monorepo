package nova.core.network;

import nova.core.network.NetworkTarget.Side;
import nova.core.player.Player;

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

	public abstract void sendChat(Player player, String message);

	/**
	 * Use {@link Side#get()} instead.
	 * 
	 * @return active side
	 */
	@Deprecated
	public final Side getSide() {
		return isClient() ? Side.CLIENT : Side.SERVER;
	}

	/**
	 * Use {@link Side#get()}{@link Side#isClient() .isClient()} instead.
	 * 
	 * @return true if the active side is {@link Side#CLIENT}
	 */
	@Deprecated
	public final boolean isClient() {
		return !isServer();
	}

	/**
	 * Use {@link Side#get()}{@link Side#isServer() .isServer()} instead.
	 * 
	 * @return true if the active side is {@link Side#SERVER}
	 */
	@Deprecated
	public abstract boolean isServer();
}
