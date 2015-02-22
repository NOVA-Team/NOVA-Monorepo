package nova.core.network;

import nova.core.util.ReflectionUtils;

/**
 * @author Calclavia
 */
public interface PacketReceiver {

	/**
	 * Reads a packet.
	 *
	 * @param id - An ID to indicate the type of packet receiving. An ID of 0 indicates the default packet containing basic information.
	 * @param packet - data encoded into the packet.
	 */
	default void read(int id, Packet packet) {
		ReflectionUtils.forEachAnnotatedField(Sync.class, this, (field, annotation) -> {
			if (annotation.id() == id) {
				try {
					if (field.getType() == Boolean.class || field.getType() == Boolean.TYPE) {
						field.set(this, packet.readBoolean());
					} else if (field.getType() == Byte.class || field.getType() == Byte.TYPE) {
						field.set(this, packet.readByte());
					} else if (field.getType() == Short.class || field.getType() == Short.TYPE) {
						field.set(this, packet.readShort());
					} else if (field.getType() == Integer.class || field.getType() == Integer.TYPE) {
						field.set(this, packet.readInt());
					} else if (field.getType() == Long.class || field.getType() == Long.TYPE) {
						field.set(this, packet.readLong());
					} else if (field.getType() == Character.class || field.getType() == Character.TYPE) {
						field.set(this, packet.readChar());
					} else if (field.getType() == Float.class || field.getType() == Float.TYPE) {
						field.set(this, packet.readFloat());
					} else if (field.getType() == Double.class || field.getType() == Double.TYPE) {
						field.set(this, packet.readDouble());
					} else if (field.getType() == String.class) {
						field.set(this, packet.readString());
					} else if (Enum.class.isAssignableFrom(field.getType())) {
						field.set(this, Enum.valueOf((Class<? extends Enum>) field.getType(), packet.readString()));
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
