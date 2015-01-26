package nova.core.util;

import com.google.common.collect.HashBiMap;

import java.util.Iterator;

public class Registry<T extends Named> implements Iterable<T> {
	private final HashBiMap<String, T> objects = HashBiMap.create();

	public void register(T object) {
		objects.put(object.getName(), object);
	}

	public boolean contains(String name) {
		return objects.containsKey(name);
	}

	public T get(String name) {
		return objects.get(name);
	}

	public String getName(T object) {
		return objects.inverse().get(object);
	}

	@Override
	public Iterator<T> iterator() {
		return objects.values().iterator();
	}
}
