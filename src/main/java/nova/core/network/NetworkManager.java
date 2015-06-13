package nova.core.network;

import nova.core.entity.component.Player;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.handler.BlockPacket;
import nova.core.network.handler.EntityPacket;
import nova.core.network.handler.PacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A central network manager.
 *
 * @author Calclavia
 */
public abstract class NetworkManager {

	private final List<PacketHandler<?>> handlers = new ArrayList<>();

	public NetworkManager() {
		register(new BlockPacket());
		register(new EntityPacket());
	}

	/**
	 * Register a packet type. A packet type handles
	 * a specific packet handler type.
	 *
	 * @param type An ID is assigned to the packet handler
	 */
	public int register(PacketHandler<?> type) {
		handlers.add(type);
		return handlers.size() - 1;
	}

	/**
	 * @return A new empty packet
	 */
	public abstract Packet newPacket();

	public PacketHandler<?> getPacketType(int id) {
		return handlers.get(id);
	}

	public int getPacketTypeID(PacketHandler<?> type) {
		return handlers.indexOf(type);
	}

	/**
	 * Gets the packet type that handles a PacketHandler
	 *
	 * @param handler The packet handler
	 * @return The packet type for the packet handler
	 */
	public PacketHandler<?> getPacketType(Object handler) {
		Optional<PacketHandler<?>> first = handlers
			.stream()
			.filter(type -> type.isHandlerFor(handler))
			.findFirst();

		if (first.isPresent()) {
			return first.get();
		} else {
			throw new NetworkException("Invalid packet sender: " + handler);
		}
	}

	/**
	 * Sends a packet based on a sender.
	 *
	 * @param sender The packet handler sending the packet
	 * @param packet The packet to send
	 */
	public void sendPacket(Object sender, Packet packet) {
		writePacket(sender, packet);
		sendPacket(packet);
	}

	/**
	 * Sends a new custom packet without any overhead.
	 * This method directly sends the packet. It will not write the PacketType ID, which will cause an error unless the ID is written to the packet already.
	 *
	 * @param packet The packet to send
	 */
	public abstract void sendPacket(Packet packet);

	public Packet writePacket(Object sender, Packet packet) {
		int packetTypeID = getPacketTypeID(getPacketType(sender));
		packet.writeInt(packetTypeID);
		packet.writeInt(packet.getID());
		((PacketHandler) getPacketType(sender)).write(sender, packet);
		return packet;
	}

	/**
	 * Syncs a PacketHandler between server and client.
	 *
	 * @param sender {@link Syncable}
	 */
	public final void sync(Object sender) {
		sync(0, sender);
	}

	/**
	 * Syncs a PacketHandler between server and client, withPriority a specific packet ID
	 *
	 * @param id The packet ID
	 * @param sender sender {@link Syncable}
	 */
	public void sync(int id, Object sender) {
		Packet packet = newPacket();
		packet.setID(id);
		sendPacket(sender, packet);
	}

	public abstract void sendChat(Player player, String message);

	/**
	 * Use {@link Side#get()} instead.
	 *
	 * @return active side
	 */
	public final Side getSide() {
		return isClient() ? Side.CLIENT : Side.SERVER;
	}

	/**
	 * Use {@link Side#get()}{@link Side#isClient() .isClient()} instead.
	 *
	 * @return true if the active side is {@link Side#CLIENT}
	 */
	public final boolean isClient() {
		return !isServer();
	}

	/**
	 * Use {@link Side#get()}{@link Side#isServer() .isServer()} instead.
	 *
	 * @return true if the active side is {@link Side#SERVER}
	 */
	public abstract boolean isServer();
}
