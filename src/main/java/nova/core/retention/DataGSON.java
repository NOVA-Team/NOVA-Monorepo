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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author ExE Boss
 */
public class DataGSON {

	public static void serialize(Data data, JsonWriter writer) {
		try {
			writeData(writer, data);
		} catch (IOException ex) {
			throw new DataException(ex);
		}
	}

	public static String serialize(Data data) {
		StringWriter writer = new StringWriter();
		try (JsonWriter gen = new JsonWriter(writer)) {
			serialize(data, gen);
		} catch (IOException ex) {
			throw new DataException(ex);
		}
		return writer.toString();
	}

	public static Data deserialize(JsonReader reader) {
		try {
			Set<Data> rootDatas = new HashSet<>();
			LinkedList<Data> dataStack = new LinkedList<>();
			LinkedList<String> nameStack = new LinkedList<>();
			LinkedList<Boolean> arrayStack = new LinkedList<>();
			loop: while (reader.hasNext()) {
				JsonToken type = reader.peek();
				switch (type) {
					case BEGIN_ARRAY: {
						arrayStack.push(true);
						Data data = new Data();
						data.put("isCollection", true);
						dataStack.push(data);
						nameStack.push("0");
						reader.beginArray();
						break;
					} case BEGIN_OBJECT: {
						arrayStack.push(false);
						dataStack.push(new Data());
						reader.beginObject();
						break;
					} case NAME: {
						nameStack.push(reader.nextName());
						break;
					} case STRING: {
						String key = getKey(nameStack, arrayStack);
						dataStack.peek().put(key, reader.nextString());
						break;
					} case NUMBER: {
						String key = getKey(nameStack, arrayStack);
						dataStack.peek().put(key, reader.nextDouble());
						break;
					} case BOOLEAN: {
						String key = getKey(nameStack, arrayStack);
						dataStack.peek().put(key, reader.nextBoolean());
						break;
					} case NULL: { // Ignore nulls
						getKey(nameStack, arrayStack);
						reader.skipValue();
						break;
					} case END_ARRAY: case END_OBJECT: {
						if (type == JsonToken.END_OBJECT)
							reader.endObject();
						else if (type == JsonToken.END_ARRAY)
							reader.endArray();
						arrayStack.pop();
						Data data = dataStack.pop();
						if (!dataStack.isEmpty())
							dataStack.peek().put(nameStack.pop(), data);
						else
							rootDatas.add(data);
						break;
					} case END_DOCUMENT: {
						break loop;
					} default:
						throw new AssertionError(reader.peek().name());
				}
			}
			if (!rootDatas.isEmpty()) {
				if (rootDatas.size() == 1) {
					return rootDatas.stream().findFirst().get();
				} else {
					Data root = new Data();
					root.put("isCollection", true);
					long l = 0;
					for (Data data : rootDatas) {
						root.put(Long.toUnsignedString(l++), data);
					}
					return root;
				}
			}
			return new Data();
		} catch (IOException ex) {
			throw new DataException(ex);
		}
	}

	public static Data deserialize(String string) {
		try (JsonReader reader = new JsonReader(new StringReader(string))) {
			return deserialize(reader);
		} catch (IOException ex) {
			throw new DataException(ex);
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

	private static void writeData(JsonWriter writer, Data data) throws IOException {
		final boolean isCollection;
		if (data.containsKey("isCollection") && Boolean.TRUE.equals(data.get("isCollection"))) {
			isCollection = true;
			writer.beginArray();
		} else {
			isCollection = false;
			writer.beginObject();
		}
		data.entrySet().stream().filter(entry -> isCollection ? !"isCollection".equals(entry.getKey()) : true).sorted((e1, e2) -> {
			if (isCollection)
				return Long.compareUnsigned(Long.parseUnsignedLong(e1.getKey()), Long.parseUnsignedLong(e2.getKey()));
			return e1.getKey().compareTo(e2.getKey());
		}).forEach(entry -> {
			Object v = entry.getValue();
			try {
				if (!isCollection)
					writer.name(entry.getKey());
				if (v instanceof Data) {
					writeData(writer, (Data) v);
				} else if (v instanceof Number) {
					if (v instanceof Byte) {
						writer.value((Byte) v);
					} else if (v instanceof Short) {
						writer.value((Short) v);
					} else if (v instanceof Integer) {
						writer.value((Integer) v);
					} else if (v instanceof Long) {
						writer.value((Long) v);
					} else if (v instanceof Float) {
						writer.value((Float) v);
					} else if (v instanceof Double) {
						writer.value((Double) v);
					} else if (v instanceof BigInteger) {
						writer.value((BigInteger) v);
					} else if (v instanceof BigDecimal) {
						writer.value((BigDecimal) v);
					}
				} else {
					if (v instanceof Boolean) {
						writer.value((Boolean) v);
					} else if (v instanceof Character) {
						writer.value((Character) v);
					} else if (v instanceof String) {
						writer.value((String) v);
					}
				}
			} catch (IOException ex) {
				throw asUnchecked(ex);
			}
		});
		writer.endObject();
	}

	@SuppressWarnings("unchecked")
	private static <T extends Throwable> RuntimeException asUnchecked(Throwable t) throws T {
		throw (T) t;
	}
}
