package nova.core.util;

import com.google.common.collect.HashBiMap;

import java.util.Iterator;
import java.util.Objects;

/**
 * @param <T>
 */
public class Registry<T extends Identifiable> implements Iterable<T> {
	private final HashBiMap<String, T> objects = HashBiMap.create();

	public void register(T object) {
		objects.put(object.getID(), object);
	}

	public boolean contains(String name) {
		return objects.containsKey(name);
	}

	public T get(String name) {
		return Objects.requireNonNull(objects.get(name), name + " is not registered");
	}

	public String getName(T object) {
		return Objects.requireNonNull(objects.inverse().get(object), object.getID() + " is not registered");
	}

	@Override
	public Iterator<T> iterator() {
		return objects.values().iterator();
	}
}
