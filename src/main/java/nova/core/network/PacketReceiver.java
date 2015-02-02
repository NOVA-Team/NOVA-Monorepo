package nova.core.network;

import nova.core.util.ReflectionUtils;

/**
 * @author Calclavia
 */
public interface PacketReceiver {

	default void read(Packet packet) {
		read(packet.readInt(), packet);
	}

	/**
	 * Reads a packet
	 *
	 * @param id - an ID to indicate the type of packet receiving.
	 * @param packet - data encoded into the packet.
	 */
	default void read(int id, Packet packet) {
		ReflectionUtils.forEachAnnotatedField(Sync.class, this, (field, annotation) -> {
			if (annotation.id() == id) {
				try {
					if (field.getType().isAssignableFrom(Boolean.class)) {
						field.set(this, packet.readBoolean());
					} else if (field.getType().isAssignableFrom(Byte.class)) {
						field.set(this, packet.readByte());
					} else if (field.getType().isAssignableFrom(Short.class)) {
						field.set(this, packet.readShort());
					} else if (field.getType().isAssignableFrom(Integer.class)) {
						field.set(this, packet.readInt());
					} else if (field.getType().isAssignableFrom(Long.class)) {
						field.set(this, packet.readLong());
					} else if (field.getType().isAssignableFrom(Character.class)) {
						field.set(this, packet.readChar());
					} else if (field.getType().isAssignableFrom(Float.class)) {
						field.set(this, packet.readFloat());
					} else if (field.getType().isAssignableFrom(Double.class)) {
						field.set(this, packet.readDouble());
					} else if (field.getType().isAssignableFrom(String.class)) {
						field.set(this, packet.readString());
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
