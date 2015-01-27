package nova.core.util.components;

import nova.core.util.ReflectionUtils;
import nova.core.util.Stored;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BiConsumer;

public interface Storable {
	default void saveToStore(Map<String, Object> data) {
		ReflectionUtils.forEachStoredField(this, (field, key) -> {
			try {
				data.put(key, field.get(this));
			} catch (IllegalAccessException e) {
				// TODO
			}
		});
	}

	default void loadFromStore(Map<String, Object> data) {
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
