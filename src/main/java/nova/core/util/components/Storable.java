package nova.core.util.components;

import nova.core.util.ReflectionUtils;

import java.util.Map;

public interface Storable {
	default void save(Map<String, Object> data) {
		ReflectionUtils.forEachStoredField(this, (field, key) -> {
			try {
				data.put(key, field.get(this));
			} catch (IllegalAccessException e) {
				// TODO
			}
		});
	}

	default void load(Map<String, Object> data) {
		ReflectionUtils.forEachStoredField(this, (field, key) -> {
			if (data.containsKey(key)) {
				try {
					field.set(this, data.get(key));
				} catch (IllegalAccessException e) {
					// TODO
				}
			}
		});
	}
}
