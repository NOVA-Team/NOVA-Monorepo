package nova.core.util.components;

import nova.core.util.ReflectionUtils;
import nova.core.util.exception.NovaException;

import java.util.HashMap;
import java.util.Map;

/**
 * Classes with this interface declare ability to store and load itself.
 * Therefore, classes using this interface must have an empty constructor for new instantiation from load.
 */
public interface Storable {

	/**
	 * Loads an object from its stored data, with an unknown class.
	 * The class of the object must be stored within the data.
	 *
	 * @param data The data
	 * @return The object loaded with given data.
	 */
	public static <T extends Storable> T loadObj(Map<String, Object> data) {
		try {
			Class clazz = Class.forName((String) data.get("class"));
			T storableObj = (T) clazz.newInstance();
			storableObj.load(data);
			return storableObj;
		} catch (Exception e) {
			throw new NovaException(e);
		}
	}

	/**
	 * Saves an object, serializing its data.
	 * This map can be reloaded and its class with be reconstructed.
	 *
	 * @return The data of the object with
	 */
	public static Map<String, Object> saveObj(Storable obj) {
		HashMap<String, Object> data = new HashMap<>();
		data.put("class", obj.getClass().getName());
		obj.save(data);
		return data;
	}

	/**
	 * Loads an object from its stored data, given its class.
	 *
	 * @param clazz - The class to load
	 * @param data - The data
	 * @return The object loaded with given data.
	 */
	public static <T extends Storable> T load(Class<T> clazz, Map<String, Object> data) {
		try {
			T storableObj = clazz.newInstance();
			storableObj.load(data);
			return storableObj;
		} catch (Exception e) {
			throw new NovaException(e);
		}
	}

	default void save(Map<String, Object> data) {
		ReflectionUtils.forEachStoredField(this, (field, key) -> {
			try {
				field.setAccessible(true);
				data.put(key, field.get(this));
				field.setAccessible(false);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		});
	}

	default void load(Map<String, Object> data) {
		ReflectionUtils.forEachStoredField(this, (field, key) -> {
			if (data.containsKey(key)) {
				try {
					field.setAccessible(true);
					field.set(this, data.get(key));
					field.setAccessible(false);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
