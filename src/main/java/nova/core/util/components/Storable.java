package nova.core.util.components;

import nova.core.util.Stored;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BiConsumer;

public interface Storable {
	default void saveToStore(Map<String, Object> data) {
		forEachStoredField((field, key) -> {
			try {
				data.put(key, field.get(this));
			} catch (IllegalAccessException e) {
				// TODO
			}
		});
	}

	default void loadFromStore(Map<String, Object> data) {
		forEachStoredField((field, key) -> {
			if (data.containsKey(key)) {
				try {
					field.set(this, data.get(key));
				} catch (IllegalAccessException e) {
					// TODO
				}
			}
		});
	}

	default void forEachStoredField(BiConsumer<Field, String> action) {
		for (Field f : this.getClass().getFields()) {
			if (f.isAnnotationPresent(Stored.class)) {
				String key = f.getAnnotation(Stored.class).key();
				if (key.length() == 0) {
					key = f.getName();
				}

				action.accept(f, key);
			}
		}
	}
}
