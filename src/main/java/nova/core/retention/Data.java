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

package nova.core.retention;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

/**
 * The data class is capable of storing named data.
 * <p>
 * Data types supported:<br>
 * - {@link Boolean}<br>
 * - {@link Byte}<br>
 * - {@link Short}<br>
 * - {@link Integer}<br>
 * - {@link Long}<br>
 * - {@link Character}<br>
 * - {@link Float}<br>
 * - {@link Double}<br>
 * - {@link String}<br>
 * <br>
 * - {@link BigInteger}<br>
 * - {@link BigDecimal}<br>
 * - {@link Enum Enumerator}<br>
 * - {@link Storable} (Converted into Data)<br>
 * - {@link Data}<br>
 * - {@link Collection} (Converted into Data)<br>
 * - {@link Vector3D}<br>
 * - {@link Vector2D}<br>
 * - {@link Class}<br>
 * - {@link UUID}<br>
 * @author Calclavia
 */
//TODO: Add collection and array support
public class Data extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	public static Class<?>[] dataTypes = {
		Boolean.class,
		Byte.class,
		Short.class,
		Integer.class,
		Long.class,
		Character.class,
		Float.class,
		Double.class,
		String.class,
		BigInteger.class,
		BigDecimal.class,
		//Special data types that all convert into Data.
		Enum.class,
		Storable.class,
		Data.class,
		Collection.class,
		Vector3D.class,
		Vector2D.class,
		Class.class,
		UUID.class
	};

	/**
	 * The pattern denoting the illegal suffix for keys.
	 * It is reserved for NOVA wrapper uses.
	 */
	public static final Pattern ILLEGAL_SUFFIX = Pattern.compile("::nova\\.\\w*$", Pattern.CASE_INSENSITIVE);

	public String className;

	public Data() {
	}

	public Data(Class<?> clazz) {
		className = clazz.getName();
		super.put("class", className);
	}

	/**
	 * Saves an object, serializing its data.
	 * This map can be reloaded and its class with be reconstructed.
	 *
	 * @param storable - The object to store.
	 * @return The data of the object.
	 */
	public static Data serialize(Storable storable) {
		Data data = new Data(storable.getClass());
		storable.save(data);
		return data;
	}

	/**
	 * Saves an object, serializing its data.
	 * This map can be reloaded and its class with be reconstructed.
	 *
	 * @param value - The object to store.
	 * @return The data of the object.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Data serialize(Object value) {
		try {
			if (value instanceof Enum) {
				Data enumData = new Data(value.getClass());
				enumData.put("value", ((Enum) value).name());
				return enumData;
			} else if (value instanceof Vector3D) {
				Data vectorData = new Data(Vector3D.class);
				vectorData.put("x", ((Vector3D) value).getX());
				vectorData.put("y", ((Vector3D) value).getY());
				vectorData.put("z", ((Vector3D) value).getZ());
				return vectorData;
			} else if (value instanceof Vector2D) {
				Data vectorData = new Data(Vector2D.class);
				vectorData.put("x", ((Vector2D) value).getX());
				vectorData.put("y", ((Vector2D) value).getY());
				return vectorData;
			} else if (value instanceof BigInteger) {
				Data bigNumData = new Data(value.getClass());
				bigNumData.put("value", ((BigInteger) value).toString());
				return bigNumData;
			} else if (value instanceof BigDecimal) {
				Data bigNumData = new Data(value.getClass());
				bigNumData.put("value", ((BigDecimal) value).toString());
				return bigNumData;
			} else if (value instanceof UUID) {
				Data uuidData = new Data(UUID.class);
				uuidData.put("uuid", value.toString());
				return uuidData;
			} else if (value instanceof Class) {
				Data classData = new Data(Class.class);
				classData.put("name", ((Class) value).getName());
				return classData;
			} else if (value instanceof Storable) {
				return serialize((Storable) value);
			} else {
				return (Data) value;
			}
		} catch (Exception e) {
			throw new DataException(e);
		}
	}

	/**
	 * Loads an object from its stored data, with an unknown class.
	 * The class of the object must be stored within the data.
	 *
	 * @param data - The data
	 * @param <T> - The object type
	 * @return The object loaded with given data.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> T unserialize(Data data) {
		try {
			Class<T> clazz = (Class<T>) Class.forName((String) data.get("class"));
			if (clazz.isEnum()) {
				return (T) Enum.valueOf((Class<? extends Enum>)clazz, data.get("value"));
			} else if (clazz == Vector3D.class) {
				return (T) new Vector3D(data.get("x"), data.get("y"), data.get("z"));
			} else if (clazz == Vector2D.class) {
				return (T) new Vector2D(data.get("x"), (double) data.get("y"));
			} else if (clazz == UUID.class) {
				return (T) UUID.fromString(data.get("uuid"));
			} else if (clazz == Collection.class) {
				ArrayList<T> ret = new ArrayList<>(data.size());
				LongStream.range(0, data.size() - 1).forEachOrdered(i -> ret.add(data.get(Long.toUnsignedString(i))));
				return (T) ret;
			} else if (clazz == Class.class) {
				return (T) Class.forName(data.get("name"));
			} else if (Storable.class.isAssignableFrom(clazz)) {
				return (T) unserialize((Class<? extends Storable>) clazz, data);
			} else {
				throw new IllegalArgumentException(data.className);
			}
		} catch (Exception e) {
			throw new DataException(e);
		}
	}

	/**
	 * Loads an object from its stored data, given its class.
	 * @param clazz - The class to load
	 * @param data - The data
	 * @return The object loaded with given data.
	 */
	public static <T extends Storable> T unserialize(Class<T> clazz, Data data) {
		try {
			T storable = clazz.newInstance();
			storable.load(data);
			return storable;
		} catch (Exception e) {
			throw new DataException(e);
		}
	}

	@Override
	public void putAll(Map<? extends String, ?> m) {
		//TODO: More efficient way to do this?
		m.forEach(this::put);
	}

	public void putAll(Data m) {
		super.putAll(m);
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Object put(String key, Object value) {
		assert key != null && value != null;
		assert !key.equals("class");
		final Object check = value;
		assert Arrays.stream(dataTypes).anyMatch(clazz -> clazz.isAssignableFrom(check.getClass()));

		if (value instanceof Enum) {
			Data enumData = new Data(value.getClass());
			enumData.put("value", ((Enum) value).name());
			value = enumData;
		} else if (value instanceof Vector3D) {
			Data vectorData = new Data(Vector3D.class);
			vectorData.put("x", ((Vector3D) value).getX());
			vectorData.put("y", ((Vector3D) value).getY());
			vectorData.put("z", ((Vector3D) value).getZ());
			value = vectorData;
		} else if (value instanceof Vector2D) {
			Data vectorData = new Data(Vector2D.class);
			vectorData.put("x", ((Vector2D) value).getX());
			vectorData.put("y", ((Vector2D) value).getY());
			value = vectorData;
		} else if (value instanceof UUID) {
			Data uuidData = new Data(UUID.class);
			uuidData.put("uuid", value.toString());
			return uuidData;
		} else if (value instanceof Class) {
			Data classData = new Data(Class.class);
			classData.put("name", ((Class) value).getName());
			return classData;
		} else if (value instanceof Collection) {
			Data collectionData = new Data(value.getClass());
			collectionData.put("isCollection", true);
			long l = 0;
			for (Object obj : (Collection<?>)value)
				collectionData.put(Long.toString(l++), obj);
			value = collectionData;
		} else if (value instanceof Storable) {
			value = serialize((Storable) value);
		}

		return super.put(key, value);
	}

	/**
	 * A pre-cast version of get.
	 *
	 * @param key - The key
	 * @param <T> - The type
	 * @return The value
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) super.get(key);
	}

	public <T extends Enum<T>> T getEnum(String key) {
		Data enumData = get(key);
		try {
			@SuppressWarnings("unchecked")
			Class<T> enumClass = (Class<T>) Class.forName(enumData.className);
			return Enum.valueOf(enumClass, enumData.get("value"));
		} catch (Exception e) {
			throw new DataException(e);
		}
	}

	public Vector3D getVector3D(String key) {
		Data data = get(key);
		return new Vector3D(data.get("x"), data.get("y"), data.get("z"));
	}

	public Vector2D getVector2D(String key) {
		Data data = get(key);
		return new Vector2D(data.get("x"), (double) data.get("y"));
	}

	public <T extends Storable> T getStorable(String key) {
		Data storableData = get(key);
		try {
			@SuppressWarnings("unchecked")
			Class<T> storableClass = (Class<T>) Class.forName(storableData.className);
			T obj = storableClass.newInstance();
			obj.load(storableData);
			return obj;
		} catch (Exception e) {
			throw new DataException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getCollection(String key) {
		Data data = this.get(key);
		ArrayList<T> ret = new ArrayList<>(data.size());
		LongStream.range(0, data.size() - 1).forEachOrdered(i -> ret.add(data.get(Long.toUnsignedString(i))));
		return ret;
	}

	public <T> Class<T> getClass(String key) {
		Data classData = get(key);
		try {
			@SuppressWarnings("unchecked")
			Class<T> classClass = (Class<T>) Class.forName(classData.className);
			return classClass;
		} catch (Exception e) {
			throw new DataException(e);
		}
	}

	public UUID getUUID(String key) {
		Data data = get(key);
		return UUID.fromString(data.get("uuid"));
	}
}
