package nova.core.retention;

import nova.core.util.ReflectionUtils;

/**
 * Classes with this interface declare ability to store and load itself.
 * Therefore, classes using this interface must have an empty constructor for new instantiation from load.
 */
public interface Storable {

	//TODO: Store inherited classes.

	/**
	 * Saves all the data of this object.
	 * See {@link Data} for what data is storable.
	 *
	 * The default implementation saves all fields tagged with @Storable.
	 *
	 * @param data The data object to put values in.
	 */
	default void save(Data data) {
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

	default void load(Data data) {
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
