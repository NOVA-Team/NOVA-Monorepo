package nova.core.network;

import nova.core.util.ReflectionUtil;

import java.util.Arrays;

/**
 * @author Calclavia
 */
public interface PacketHandler {

	/**
	 * Reads a packet.
	 * @param packet - data encoded into the packet.
	 */
	default void read(Packet packet) {
		ReflectionUtil.forEachRecursiveAnnotatedField(Sync.class, getClass(), (field, annotation) -> {
			if (Arrays.asList(annotation.ids()).contains(packet.getID())) {
				try {
					field.setAccessible(true);
					field.set(this, packet.read(field.getType()));
					field.setAccessible(false);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Writes a packet based on the arguments.
	 * @param packet - data encoded into the packet
	 */
	default void write(Packet packet) {
		ReflectionUtil.forEachRecursiveAnnotatedField(Sync.class, getClass(), (field, annotation) -> {
			if (Arrays.asList(annotation.ids()).contains(packet.getID())) {
				try {
					field.setAccessible(true);
					packet.write(field.get(this));
					field.setAccessible(false);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
