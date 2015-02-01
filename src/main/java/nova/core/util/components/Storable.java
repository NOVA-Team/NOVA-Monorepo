package nova.core.util.components;

import nova.core.util.ReflectionUtils;

import java.util.Map;

public interface Storable {
	default void save(Map<String, Object> data) {
		ReflectionUtils.forEachStoredField(this, (field, annotation) -> {
			try {
				String key = annotation.key();
				if (key.length() == 0) {
					key = field.getName();
				}

				data.put(key, field.get(this));
			} catch (IllegalAccessException e) {
				// TODO
			}
		});
	}

	default void load(Map<String, Object> data) {
		ReflectionUtils.forEachStoredField(this, (field, annotation) -> {
			String key = annotation.key();
			if (key.length() == 0) {
				key = field.getName();
			}

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
