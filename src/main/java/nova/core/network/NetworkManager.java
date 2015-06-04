package nova.core.network;

import nova.core.entity.component.Player;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.handler.BlockPacket;
import nova.core.network.handler.EntityPacket;
import nova.core.network.handler.PacketType;
import nova.core.util.exception.NovaException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A central network manager.
 *
 * @author Calclavia
 */
public abstract class NetworkManager {

	private final List<PacketType<?>> handlers = new ArrayList<>();

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
	public int register(PacketType<?> type) {
		handlers.add(type);
		return handlers.size() - 1;
	}

	/**
	 * @return A new empty packet
	 */
	public abstract Packet newPacket();

	public PacketType<?> getPacketType(int id) {
		return handlers.get(id);
	}

	/**
	 * Gets the packet type that handles a PacketHandler
	 *
	 * @param handler The packet handler
	 * @return The packet type for the packet handler
	 */
	public PacketType<?> getPacketType(PacketHandler handler) {
		Optional<PacketType<?>> first = handlers
			.stream()
			.filter(type -> type.handler().isAssignableFrom(handler.getClass()))
			.findFirst();

		if (first.isPresent()) {
			return first.get();
		} else {
			throw new NovaException("Invalid packet sender: " + handler);
		}
	}

	/**
	 * Sends a new custom packet.
	 *
	 * @param sender The packet handler sending the packet
	 * @param packet The packet to send
	 */
	public void sendPacket(PacketHandler sender, Packet packet) {
		packet.write(packet.getID());
		((PacketType) getPacketType(sender)).write(sender, packet);
	}

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
	 *
	 * @param id The packet ID
	 * @param sender sender {@link nova.core.network.PacketHandler}
	 */
	public void sync(int id, PacketHandler sender) {
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
