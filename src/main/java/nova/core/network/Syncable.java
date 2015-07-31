package nova.core.network;

import nova.core.util.ReflectionUtil;

import java.util.Arrays;

/**
 * @author Calclavia
 */
public interface Syncable {

	/**
	 * Reads a packet.
	 *
	 * @param packet - data encoded into the packet.
	 */
	default void read(Packet packet) {
		ReflectionUtil.forEachRecursiveAnnotatedField(Sync.class, getClass(), (field, annotation) -> {
			if (Arrays.stream(annotation.ids()).anyMatch(i -> i == packet.getID())) {
				try {
					field.setAccessible(true);
					Object o = field.get(this);
					if (o instanceof Syncable) {
						((Syncable) o).read(packet);
					} else {
						field.set(this, packet.read(field.getType()));
					}
					field.setAccessible(false);
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
		ReflectionUtil.forEachRecursiveAnnotatedField(Sync.class, getClass(), (field, annotation) -> {
			if (Arrays.stream(annotation.ids()).anyMatch(i -> i == packet.getID())) {
				try {
					field.setAccessible(true);
					Object value = field.get(this);
					if(value != null) {
						packet.write(value);
					} else {
						throw new NullPointerException(
								String.format("Field %s in class: %s is null. Syncing nulls is not supported. Use Optional instead.",
										field.getName(), getClass()));
					}
					field.setAccessible(false);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
