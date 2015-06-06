package nova.core.network.handler;

import nova.core.network.Packet;

/**
 * A packet handler handles a certain type of objects.
 *
 * @author Calclavia
 */
public interface PacketHandler<HANDLER> {

	void read(Packet packet);

	void write(HANDLER handler, Packet packet);

	boolean isHandlerFor(Object handler);
}
