package nova.core.network;

import nova.core.util.ReflectionUtils;

/**
 * @author Calclavia
 */
public interface PacketReceiver {
	/**
	 * Reads a packet
	 * @param packet - data encoded into the packet
	 */
	default void read(Packet packet) {
		ReflectionUtils.forEachAnnotatedField(Sync.class, this, (field, annotation) -> {
			try {
				//TODO: find read object type.
				Object value = field.get(this);
				//				packet.read();
			} catch (IllegalAccessException e) {
				// TODO
			}
		});
	}
}
