package nova.core.network;

import nova.core.util.ReflectionUtils;

/**
 * @author Calclavia
 */
public interface PacketSender {

	default void write(Packet packet) {
		write(packet.readInt(), packet);
	}

	/**
	 * Writes a packet based on the arguments.
	 *
	 * @param id - The ID of the packet.
	 * @param packet - data encoded into the packet
	 */
	default void write(int id, Packet packet) {
		ReflectionUtils.forEachAnnotatedField(Sync.class, this, (field, annotation) -> {
			if (annotation.id() == id) {
				try {
					packet.write(field.get(this));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
