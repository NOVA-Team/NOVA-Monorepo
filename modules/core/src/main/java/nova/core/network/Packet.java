/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.network;

import nova.core.entity.component.Player;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * A packet of data that is writable or readable.
 * @author Calclavia
 */
public interface Packet {

	/**
	 * @return Gets the ID of this packet, used to determine what this packet is about.
	 */
	int getID();

	/**
	 * Sets the ID of this packet, allowing it to be sent accordingly.
	 * @param id The packet ID
	 * @return The packet itself.
	 */
	Packet setID(int id);

	/**
	 * The player sending the packet
	 * @return The player sending the packet
	 */
	Player player();

	/**
	 * Writes an arbitrary object, automatically finding the relevant class.
	 * @param data Object to write
	 * @return This packet
	 */
	default Packet write(Object data) {
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
		} else if (data instanceof Enum) {
			writeEnum((Enum) data);
		} else if (data instanceof Optional) {
			writeOptional((Optional) data);
		} else if (data instanceof Data) {
			writeData((Data) data);
		} else if (data instanceof Syncable) {
			((Syncable) data).write(this);
		} else if (data instanceof Storable) {
			writeStorable((Storable) data);
		} else if (data instanceof Collection) {
			writeCollection((Collection) data);
		} else if (data instanceof Collection) {
			writeCollection((Collection) data);
		} else if (data instanceof Vector3D) {
			writeDouble(((Vector3D) data).getX());
			writeDouble(((Vector3D) data).getY());
			writeDouble(((Vector3D) data).getZ());
		} else if (data instanceof Vector2D) {
			writeDouble(((Vector2D) data).getX());
			writeDouble(((Vector2D) data).getY());
		} else {
			throw new IllegalArgumentException("Packet attempt to write an invalid object: " + data);
		}

		return this;
	}

	default Packet $less$less$less(Object data) {
		return write(data);
	}

	/**
	 * Sets the specified boolean at the current {@code writerIndex}
	 * and increases the {@code writerIndex} by {@code 1} in this buffer.
	 * @param value Data to write
	 * @return This packet
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 1}
	 */
	Packet writeBoolean(boolean value);

	/**
	 * Sets the specified byte at the current {@code writerIndex}
	 * and increases the {@code writerIndex} by {@code 1} in this buffer.
	 * The 24 high-order bits of the specified value are ignored.
	 * @param value Data to write
	 * @return This packet
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 1}
	 */
	Packet writeByte(int value);

	/**
	 * Sets the specified 16-bit short integer at the current
	 * {@code writerIndex} and increases the {@code writerIndex} by {@code 2}
	 * in this buffer.  The 16 high-order bits of the specified value are ignored.
	 * @param value Data to write
	 * @return This packet
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 2}
	 */
	Packet writeShort(int value);

	/**
	 * Sets the specified 32-bit integer at the current {@code writerIndex}
	 * and increases the {@code writerIndex} by {@code 4} in this buffer.
	 * @param value Data to write
	 * @return This packet
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 4}
	 */
	Packet writeInt(int value);

	/**
	 * Sets the specified 64-bit long integer at the current
	 * {@code writerIndex} and increases the {@code writerIndex} by {@code 8}
	 * in this buffer.
	 * @param value Data to write
	 * @return This packet
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 8}
	 */
	Packet writeLong(long value);

	/**
	 * Sets the specified 2-byte UTF-16 character at the current
	 * {@code writerIndex} and increases the {@code writerIndex} by {@code 2}
	 * in this buffer.  The 16 high-order bits of the specified value are ignored.
	 * @param value Data to write
	 * @return This packet
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 2}
	 */
	Packet writeChar(int value);

	/**
	 * Sets the specified 32-bit floating point number at the current
	 * {@code writerIndex} and increases the {@code writerIndex} by {@code 4}
	 * in this buffer.
	 * @param value Data to write
	 * @return This packet
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 4}
	 */
	Packet writeFloat(float value);

	/**
	 * Sets the specified 64-bit floating point number at the current
	 * {@code writerIndex} and increases the {@code writerIndex} by {@code 8}
	 * in this buffer.
	 * @param value Data to write
	 * @return This packet
	 * @throws IndexOutOfBoundsException if {@code this.writableBytes} is less than {@code 8}
	 */
	Packet writeDouble(double value);

	Packet writeString(String value);

	//TODO: Packet handler is bad at reading/writing enums for unknown reasons
	default Packet writeEnum(Enum<?> data) {
		writeString(data.getClass().getName());
		writeString(data.name());
		return this;
	}

	default int getType(Class<?> compare) {
		return IntStream
			.range(0, Data.dataTypes.length)
			.filter(i -> Data.dataTypes[i].isAssignableFrom(compare))
			.findFirst()
			.getAsInt();
	}

	default Packet writeData(Data data) {
		//Write the data size
		writeInt(data.size());
		//Write the data class
		writeString(data.className);

		data.forEach((k, v) -> {
				int typeID = getType(v.getClass());
				//Write key
				writeString(k);
				//Write data type
				writeShort(typeID);
				//Write value
				write(v);
			}
		);

		return this;
	}

	default Packet writeStorable(Storable storable) {
		writeData(Data.serialize(storable));
		return this;
	}

	default Packet writeCollection(Collection<?> col) {
		writeInt(col.size());
		col.forEach(obj -> {
			writeShort(getType(obj.getClass()));
			write(obj);
		});
		return this;
	}

	default Packet writeOptional(Optional<?> optional) {
		if (optional.isPresent()) {
			writeShort(getType(optional.get().getClass()));
			write(optional.get());
		} else {
			writeShort(-1);
		}
		return this;
	}

	Packet writeBytes(byte[] array);

	byte[] readBytes(int length);

	/**
	 * Gets a boolean at the current {@code readerIndex} and increases
	 * the {@code readerIndex} by {@code 1} in this buffer.
	 * @return Data read from this packet
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 1}
	 */
	boolean readBoolean();

	/**
	 * Gets a byte at the current {@code readerIndex} and increases
	 * the {@code readerIndex} by {@code 1} in this buffer.
	 * @return Data read from this packet
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 1}
	 */
	byte readByte();

	/**
	 * Gets an unsigned byte at the current {@code readerIndex} and increases
	 * the {@code readerIndex} by {@code 1} in this buffer.
	 * @return Data read from this packet
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 1}
	 */
	short readUnsignedByte();

	/**
	 * Gets a 16-bit short integer at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 2} in this buffer.
	 * @return Data read from this packet
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 2}
	 */
	short readShort();

	/**
	 * Gets a 32-bit integer at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 4} in this buffer.
	 * @return Data read from this packet
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
	 */
	int readInt();

	/**
	 * Gets an unsigned 32-bit integer at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 4} in this buffer.
	 * @return Data read from this packet
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
	 */
	long readUnsignedInt();

	/**
	 * Gets a 64-bit integer at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 8} in this buffer.
	 * @return Data read from this packet
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 8}
	 */
	long readLong();

	/**
	 * Gets a 2-byte UTF-16 character at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 2} in this buffer.
	 * @return Data read from this packet
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 2}
	 */
	char readChar();

	/**
	 * Gets a 32-bit floating point number at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 4} in this buffer.
	 * @return Data read from this packet
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
	 */
	float readFloat();

	/**
	 * Gets a 64-bit floating point number at the current {@code readerIndex}
	 * and increases the {@code readerIndex} by {@code 8} in this buffer.
	 * @return Data read from this packet
	 * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 8}
	 */
	double readDouble();

	String readString();

	default <E extends Enum<E>> E readEnum() {
		try {
			String enumClassName = readString();
			@SuppressWarnings("unchecked")
			Class<E> className = (Class<E>) Class.forName(enumClassName);
			return readEnum(className);
		} catch (Exception e) {
			throw new NetworkException("Failed to read enum.", e);
		}
	}

	default <E extends Enum<E>> E readEnum(Class<E> type) {
		return Enum.valueOf(type, readString());
	}

	default Syncable readPacketHandler(Syncable handler) {
		handler.read(this);
		return handler;
	}

	/**
	 * Reads a {@link Data} type.
	 * @return The data type
	 */
	default Data readData() {
		Data readData = new Data();
		int size = readInt();
		readData.className = readString();
		IntStream
			.range(0, size)
			.forEach(i -> {
				String key = readString();
				short type = readShort();
				Object value = read(Data.dataTypes[type]);
				readData.put(key, value);
			});
		return readData;
	}

	default Object readStorable() {
		Data data = readData();
		return Data.unserialize(data);
	}

	default <T> List<T> readList() {
		ArrayList<T> arrayList = new ArrayList<>();
		int size = readInt();

		IntStream
			.range(0, size)
			.forEach(i -> {
				short type = readShort();
				@SuppressWarnings("unchecked")
				T value = (T) read(Data.dataTypes[type]);
				arrayList.add(value);
			});

		return arrayList;
	}

	default <T> Set<T> readSet() {
		Set<T> set = new HashSet<>();
		int size = readInt();

		IntStream
			.range(0, size)
			.forEach(i -> {
				short type = readShort();
				@SuppressWarnings("unchecked")
				T value = (T) read(Data.dataTypes[type]);
				set.add(value);
			});

		return set;
	}

	@SuppressWarnings("unchecked")
	default <T> Optional<T> readOptional() {
		short type = readShort();
		if (type != -1) {
			return (Optional<T>) Optional.of(read(Data.dataTypes[type]));
		} else {
			return Optional.empty();
		}
	}

	default Vector2D readVector2D() {
		return new Vector2D(readDouble(), readDouble());
	}

	default Vector3D readVector3D() {
		return new Vector3D(readDouble(), readDouble(), readDouble());
	}

	@SuppressWarnings("unchecked")
	default <T> T read(Class<T> clazz) {
		if (clazz == Boolean.class || clazz == boolean.class) {
			return (T) Boolean.valueOf(readBoolean());
		} else if (clazz == Byte.class || clazz == byte.class) {
			return (T) Byte.valueOf(readByte());
		} else if (clazz == Short.class || clazz == short.class) {
			return (T) Short.valueOf(readShort());
		} else if (clazz == Integer.class || clazz == int.class) {
			return (T) Integer.valueOf(readInt());
		} else if (clazz == Long.class || clazz == long.class) {
			return (T) Long.valueOf(readLong());
		} else if (clazz == Character.class || clazz == char.class) {
			return (T) Character.valueOf(readChar());
		} else if (clazz == Float.class || clazz == float.class) {
			return (T) Float.valueOf(readFloat());
		} else if (clazz == Double.class || clazz == double.class) {
			return (T) Double.valueOf(readDouble());
		} else if (clazz == String.class) {
			return (T) readString();
		} else if (clazz == Optional.class) {
			return (T) readOptional();
		}
		//Special data types that all convert into Data.
		else if (Syncable.class.isAssignableFrom(clazz)) {
			throw new NetworkException("Attempt to read PacketHandler object by its class");
		} else if (Enum.class.isAssignableFrom(clazz)) {
			return (T) readEnum();
		} else if (Data.class.isAssignableFrom(clazz)) {
			return (T) readData();
		} else if (Vector3D.class.isAssignableFrom(clazz)) {
			return (T) readVector3D();
		} else if (Vector2D.class.isAssignableFrom(clazz)) {
			return (T) readVector2D();
		} else if (List.class.isAssignableFrom(clazz)) {
			return (T) readList();
		} else if (Set.class.isAssignableFrom(clazz)) {
			return (T) readSet();
		} else {
			return (T) readStorable();
		}
	}
}
