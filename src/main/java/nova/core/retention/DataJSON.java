/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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
package nova.core.retention;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.LongStream;

/**
 * @author ExE Boss
 */
public final class DataJSON {

	public static void serialize(Data data, JsonGenerator writer) {
		writeData(writer, data, null);
	}

	public static String serialize(Data data) {
		StringWriter writer = new StringWriter();
		try (JsonGenerator gen = Json.createGenerator(writer)) {
			serialize(data, gen);
		}
		return writer.toString();
	}

	public static Data deserialize(JsonParser reader) {
		Set<Data> rootDatas = new HashSet<>();
		LinkedList<Data> dataStack = new LinkedList<>();
		LinkedList<String> nameStack = new LinkedList<>();
		LinkedList<Boolean> arrayStack = new LinkedList<>();
		while (reader.hasNext()) {
			switch (reader.next()) {
				case START_ARRAY: {
					arrayStack.push(true);
					dataStack.push(new Data());
					dataStack.peek().put("isCollection", true);
					nameStack.push("0");
					break;
				} case START_OBJECT: {
					arrayStack.push(false);
					dataStack.push(new Data());
					break;
				} case KEY_NAME: {
					nameStack.push(reader.getString());
					break;
				} case VALUE_STRING: {
					String key = getKey(nameStack, arrayStack);
					dataStack.peek().put(key, reader.getString());
					break;
				} case VALUE_NUMBER: {
					String key = getKey(nameStack, arrayStack);
					BigDecimal number = reader.getBigDecimal();
					if (reader.isIntegralNumber()) {
						try {
							dataStack.peek().put(key, number.intValueExact());
						} catch (ArithmeticException ex) {
							try {
								dataStack.peek().put(key, number.longValueExact());
							} catch (ArithmeticException ex1) {
								dataStack.peek().put(key, number.toBigInteger());
							}
						}
					} else {
						double d = number.doubleValue();
						if (d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY)
							dataStack.peek().put(key, number);
						else
							dataStack.peek().put(key, d);
					}
					break;
				} case VALUE_TRUE: {
					String key = getKey(nameStack, arrayStack);
					dataStack.peek().put(key, true);
					break;
				} case VALUE_FALSE: {
					String key = getKey(nameStack, arrayStack);
					dataStack.peek().put(key, false);
					break;
				} case VALUE_NULL: { // Ignore nulls
					getKey(nameStack, arrayStack);
					break;
				} case END_OBJECT: case END_ARRAY: {
					arrayStack.pop();
					Data data = dataStack.pop();
					if (!dataStack.isEmpty())
						dataStack.peek().put(nameStack.pop(), data);
					else
						rootDatas.add(data);
					break;
				} default:
					throw new AssertionError(reader.next().name());
			}
		}
		if (!rootDatas.isEmpty()) {
			if (rootDatas.size() == 1) {
				return rootDatas.stream().findFirst().get();
			} else {
				Data root = new Data();
				root.put("isCollection", true);
				long l = 0;
				for (Data data : rootDatas)
					root.put(Long.toUnsignedString(l++), data);
				return root;
			}
		}
		return new Data();
	}

	public static Data deserialize(String string) {
		try (JsonParser reader = Json.createParser(new StringReader(string))) {
			return deserialize(reader);
		}
	}

	private static String getKey(LinkedList<String> nameStack, LinkedList<Boolean> arrayStack) {
		if (arrayStack.peek()) {
			long name = Long.parseUnsignedLong(nameStack.pop());
			nameStack.push(Long.toUnsignedString(name + 1));
			return Long.toUnsignedString(name);
		} else {
			return nameStack.pop();
		}
	}

	private static void writeData(JsonGenerator writer, Data data, String key) {
		boolean isCollection;
		if (data.containsKey("isCollection") && Boolean.TRUE.equals(data.get("isCollection"))) {
			isCollection = true;
			if (key == null)
				writer.writeStartArray();
			else
				writer.writeStartArray(key);
		} else {
			isCollection = false;
			if (key == null)
				writer.writeStartObject();
			else
				writer.writeStartObject(key);
		}

		if (!isCollection)
			data.forEach((k, v) -> {
				if (v instanceof Data) {
					writeData(writer, (Data) v, k);
				} else if (v instanceof Number) {
					if (v instanceof Byte) {
						writer.write(k, (Byte) v);
					} else if (v instanceof Short) {
						writer.write(k, (Short) v);
					} else if (v instanceof Integer) {
						writer.write(k, (Integer) v);
					} else if (v instanceof Long) {
						writer.write(k, (Long) v);
					} else if (v instanceof Float) {
						writer.write(k, (Float) v);
					} else if (v instanceof Double) {
						writer.write(k, (Double) v);
					} else if (v instanceof BigInteger) {
						writer.write(k, (BigInteger) v);
					} else if (v instanceof BigDecimal) {
						writer.write(k, (BigDecimal) v);
					}
				} else {
					if (v instanceof Boolean) {
						writer.write(k, (Boolean) v);
					} else if (v instanceof Character) {
						writer.write(k, (Character) v);
					} else if (v instanceof String) {
						writer.write(k, (String) v);
					} else if (v instanceof JsonValue) {
						writer.write(k, (JsonValue) v);
					}
				}
			});
		else
			LongStream.range(0, data.size()).mapToObj(l -> data.get(Long.toUnsignedString(l))).forEachOrdered(v -> {
				if (v instanceof Data) {
					writeData(writer, (Data) v, null);
				} else if (v instanceof Number) {
					if (v instanceof Byte) {
						writer.write((Byte) v);
					} else if (v instanceof Short) {
						writer.write((Short) v);
					} else if (v instanceof Integer) {
						writer.write((Integer) v);
					} else if (v instanceof Long) {
						writer.write((Long) v);
					} else if (v instanceof Float) {
						writer.write((Float) v);
					} else if (v instanceof Double) {
						writer.write((Double) v);
					} else if (v instanceof BigInteger) {
						writer.write((BigInteger) v);
					} else if (v instanceof BigDecimal) {
						writer.write((BigDecimal) v);
					}
				} else {
					if (v instanceof Boolean) {
						writer.write((Boolean) v);
					} else if (v instanceof Character) {
						writer.write((Character) v);
					} else if (v instanceof String) {
						writer.write((String) v);
					} else if (v instanceof JsonValue) {
						writer.write((JsonValue) v);
					}
				}
			});

		writer.writeEnd();
	}
}
