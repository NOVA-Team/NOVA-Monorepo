package nova.core.util;

import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.Iterator;

public class Registry<T extends Named> implements Iterable<T> {
	private final ArrayList<T> objects = new ArrayList<T>();
	private final HashBiMap<String, T> names = HashBiMap.create();

	public void register(T object) {
		objects.add(object);
		names.put(object.getName(), object);
	}

	public boolean contains(String name) {
		return names.containsKey(name);
	}

	public T get(String name) {
		return names.get(name);
	}

	public String getName(T object) {
		return names.inverse().get(object);
	}

	@Override
	public Iterator<T> iterator() {
		return objects.iterator();
	}
}
