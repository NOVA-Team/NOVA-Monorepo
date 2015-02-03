package nova.core.util.components;

import nova.core.util.ReflectionUtils;

import java.util.Map;

/**
 * Classes with this interface declare ability to store and load itself
 */
public interface Storable {
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
