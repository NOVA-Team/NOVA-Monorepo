package nova.core.util;

import java.lang.reflect.Field;
import java.util.Map;

public interface Storable
{
	default void saveToStore(Map<String, Object> data) {
		for (Field f : this.getClass().getFields()) {
			if (f.isAnnotationPresent(Stored.class)) {
				String key = f.getAnnotation(Stored.class).key();
				if (key.length() == 0) {
					key = f.getName();
				}

				try {
					data.put(key, f.get(this));
				} catch (IllegalAccessException e) {
					// TODO
				}
			}
		}
	}

	default void loadFromStore(Map<String, Object> data) {
		for (Field f : this.getClass().getFields()) {
			if (f.isAnnotationPresent(Stored.class)) {
				String key = f.getAnnotation(Stored.class).key();
				if (key.length() == 0) {
					key = f.getName();
				}

				if (data.containsKey(key)) {
					try {
						f.set(this, data.get(key));
					} catch (IllegalAccessException e) {
						// TODO
					}
				}
			}
		}
	}
}
