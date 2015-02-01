package nova.core.network;

import nova.core.util.ReflectionUtils;

/**
 * @author Calclavia
 */
public interface PacketSender {
	/**
	 * Writes a packet based on the arguments.
	 * @param packet - data encoded into the packet
	 */
	default void write(Packet packet) {
		ReflectionUtils.forEachField(Sync.class, this, (field, annotation) -> {
			try {
				packet.write(field.get(this));
			} catch (IllegalAccessException e) {
				// TODO
			}
		});
	}

}
