package nova.core.network.handler;

import nova.core.network.Packet;

/**
 * A packet type handles a certain type of PacketHandlers.
 *
 * @author Calclavia
 */
public interface PacketType<HANDLER> {

	void read(Packet packet);

	void write(HANDLER handler, Packet packet);

	boolean isHandlerFor(Object handler);
}
