package nova.core.network;

import nova.core.util.ReflectionUtils;

/**
 * @author Calclavia
 */
public interface PacketHandler {

	/**
	 * Reads a packet.
	 *
	 * @param packet - data encoded into the packet.
	 */
	default void read(Packet packet) {
		ReflectionUtils.forEachAnnotatedField(Sync.class, this, (field, annotation) -> {
			if (annotation.id() == packet.getID()) {
				try {
					field.set(this, packet.read(field.getType()));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Writes a packet based on the arguments.
	 *
	 * @param packet - data encoded into the packet
	 */
	default void write(Packet packet) {
		ReflectionUtils.forEachAnnotatedField(Sync.class, this, (field, annotation) -> {
			if (annotation.id() == packet.getID()) {
				try {
					packet.write(field.get(this));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
