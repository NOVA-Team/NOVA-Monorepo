package nova.core.network;

/**
 * A packet of data that is writable or readable.
 * @author Calclavia
 */
public interface Packet {

	/**
	 * Writes an arbitrary object, automatically finding the relevant class.
	 */
	default Packet write(Object data) {
		//TODO: Add collection and array support
		if (data instanceof Boolean) {
			writeBoolean((boolean) data);
		} else if (data instanceof Byte) {
			writeByte((byte) data);
		} else if (data instanceof Short) {
			writeShort((short) data);
		} else if (data instanceof Integer) {
			writeInt((int) data);
		} else if (data instanceof Long) {
			writeLong((long) data);
		} else if (data instanceof Character) {
			writeChar((Character) data);
		} else if (data instanceof Float) {
			writeFloat((float) data);
		} else if (data instanceof Double) {
			writeDouble((double) data);
		} else if (data instanceof String) {
			writeString((String) data);
		} else {
			throw new IllegalArgumentException("Packet attempt to write an invalid object: " + data + "]");
		}

		return this;
	}

	/**
	 * Sets the specified boolean at the current {@code writerIndex}
	 * and increases the {@code writerIndex} by {@code 1} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 1}
	 */
	Packet writeBoolean(boolean value);

	/**
	 * Sets the specified byte at the current {@code writerIndex}
	 * and increases the {@code writerIndex} by {@code 1} in this buffer.
	 * The 24 high-order bits of the specified value are ignored.
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 1}
	 */
	Packet writeByte(int value);

	/**
	 * Sets the specified 16-bit short integer at the current
	 * {@code writerIndex} and increases the {@code writerIndex} by {@code 2}
	 * in this buffer.  The 16 high-order bits of the specified value are ignored.
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 2}
	 */
	Packet writeShort(int value);

	/**
	 * Sets the specified 32-bit integer at the current {@code writerIndex}
	 * and increases the {@code writerIndex} by {@code 4} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 4}
	 */
	Packet writeInt(int value);

	/**
	 * Sets the specified 64-bit long integer at the current
	 * {@code writerIndex} and increases the {@code writerIndex} by {@code 8}
	 * in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 8}
	 */
	Packet writeLong(long value);

	/**
	 * Sets the specified 2-byte UTF-16 character at the current
	 * {@code writerIndex} and increases the {@code writerIndex} by {@code 2}
	 * in this buffer.  The 16 high-order bits of the specified value are ignored.
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 2}
	 */
	Packet writeChar(int value);

	/**
	 * Sets the specified 32-bit floating point number at the current
	 * {@code writerIndex} and increases the {@code writerIndex} by {@code 4}
	 * in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 4}
	 */
	Packet writeFloat(float value);

	/**
	 * Sets the specified 64-bit floating point number at the current
	 * {@code writerIndex} and increases the {@code writerIndex} by {@code 8}
	 * in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 8}
	 */
	Packet writeDouble(double value);

	Packet writeString(String value);

	/**
	 * Gets a boolean at the current {@code readerIndex} and increases
	 * the {@code readerIndex} by {@code 1} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 1}
	 */
	boolean readBoolean();

	/**
	 * Gets a byte at the current {@code readerIndex} and increases
	 * the {@code readerIndex} by {@code 1} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 1}
	 */
	byte readByte();

	/**
	 * Gets an unsigned byte at the current {@code readerIndex} and increases
	 * the {@code readerIndex} by {@code 1} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 1}
	 */
	short readUnsignedByte();

	/**
	 * Gets a 16-bit short integer at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 2} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 2}
	 */
	short readShort();

	/**
	 * Gets a 32-bit integer at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 4} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
	 */
	int readInt();

	/**
	 * Gets an unsigned 32-bit integer at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 4} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
	 */
	long readUnsignedInt();

	/**
	 * Gets a 64-bit integer at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 8} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 8}
	 */
	long readLong();

	/**
	 * Gets a 2-byte UTF-16 character at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 2} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 2}
	 */
	char readChar();

	/**
	 * Gets a 32-bit floating point number at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 4} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
	 */
	float readFloat();

	/**
	 * Gets a 64-bit floating point number at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 8} in this buffer.
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 8}
	 */
	double readDouble();

	String readString();
}
