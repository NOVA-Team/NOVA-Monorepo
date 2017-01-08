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

import nova.core.util.id.Identifier;
import nova.core.util.id.IdentifierRegistry;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.*;
import java.util.function.Consumer;

/**
 * The data class is capable of storing named data.
 *
 * Data types supported:
 * - Boolean
 * - Byte
 * - Short
 * - Integer
 * - Long
 * - Character
 * - Float
 * - Double
 * - String
 *
 * - Enumerator
 * - Storable (Converted into Data)
 * - Data
 * @author Calclavia
 */
//TODO: Add collection and array support
public class Data extends HashMap<String, Object> {

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
		//Special data types that all convert into Data.
		Identifier.class,
		Enum.class,
		Storable.class,
		Data.class,
		Collection.class,
		Vector3D.class,
		Vector2D.class,
		Class.class,
		UUID.class };

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
	 * @param obj The object to store.
	 * @return The data of the object with
	 */
	public static Data serialize(Storable obj) {
		Data data = new Data(obj.getClass());
		obj.save(data);
		data.putIfAbsent("class", obj.getClass().getName());
		obj.save(data);
		return data;
	}
	
	/**
	 * Saves an object, serializing its data.
	 * This map can be reloaded and its class with be reconstructed.
	 * @param obj The object to store.
	 * @return The data of the object with
	 */
	public static Data serialize(Object obj) {
		try {
			if (obj instanceof Enum) {
				Data enumData = new Data(obj.getClass());
				enumData.put("value", ((Enum) obj).name());
				return enumData;
			} else if (obj instanceof Vector3D) {
				Data vectorData = new Data(Vector3D.class);
				vectorData.put("x", ((Vector3D) obj).getX());
				vectorData.put("y", ((Vector3D) obj).getY());
				vectorData.put("z", ((Vector3D) obj).getZ());
				return vectorData;
			} else if (obj instanceof Vector2D) {
				Data vectorData = new Data(Vector2D.class);
				vectorData.put("x", ((Vector2D) obj).getX());
				vectorData.put("y", ((Vector2D) obj).getY());
				return vectorData;
			} else if (obj instanceof UUID) {
				Data uuidData = new Data(UUID.class);
				uuidData.put("uuid", obj.toString());
				return uuidData;
			} else if (obj instanceof Class) {
				Data classData = new Data(Class.class);
				classData.put("name", ((Class) obj).getName());
				return classData;
			} else if (obj instanceof Identifier) {
				Data identifierData = new Data(Identifier.class);
				IdentifierRegistry.instance().save(identifierData, (Identifier) obj);
				return identifierData;
			} else if (obj instanceof Storable) {
				return serialize((Storable) obj);
			} else {
				return (Data) obj;
			}
		} catch (Exception e) {
			throw new DataException(e);
		}
	}

	/**
	 * Loads an object from its stored data, with an unknown class.
	 * The class of the object must be stored within the data.
	 * @param data The data
	 * @return The object loaded with given data.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> T unserialize(Data data) {
		try {
			Class clazz = Class.forName((String) data.get("class"));
			if (clazz.isEnum()) {
				return (T) Enum.valueOf(clazz, data.get("value"));
			} else if (clazz == Vector3D.class) {
				return (T) new Vector3D(data.get("x"), data.get("y"), data.get("z"));
			} else if (clazz == Vector2D.class) {
				return (T) new Vector2D(data.get("x"), (double) data.get("y"));
			} else if (clazz == UUID.class) {
				return (T) UUID.fromString(data.get("uuid"));
			} else if (clazz == Class.class) {
				return (T) Class.forName(data.get("name"));
			} else if (clazz == Identifier.class) {
				return (T) IdentifierRegistry.instance().load(data);
			} else {
				return (T) unserialize(clazz, data);
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
			T storableObj = clazz.newInstance();
			storableObj.load(data);
			return storableObj;
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
			value = uuidData;
		} else if (value instanceof Class) {
			Data classData = new Data(Class.class);
			classData.put("name", ((Class) value).getName());
			value = classData;
		} else if (value instanceof Identifier) {
			Data identifierData = new Data(Identifier.class);
			IdentifierRegistry.instance().save(identifierData, (Identifier) value);
			value = identifierData;
		} else if (value instanceof Storable) {
			value = serialize((Storable) value);
		}

		return super.put(key, value);
	}

	/**
	 * A pre-cast version of get.
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

	@SuppressWarnings("unchecked")
	public <T extends Identifier> T getIdentifier(String key) {
		Data storableData = get(key);
		try {
			return (T) IdentifierRegistry.instance().load(storableData);
		} catch (Exception e) {
			throw new DataException(e);
		}
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
