package nova.core.core;

import com.google.common.collect.HashBiMap;

import java.util.ArrayList;

public class Registry<T extends Named> {
	private final ArrayList<T> ids = new ArrayList<T>();
	private final HashBiMap<String, T> names = HashBiMap.create();

	public void register(T object) {
		ids.add(object);
		names.put(object.getName(), object);
	}

	public T get(String name) {
		return names.get(name);
	}

	public String getName(T object) {
		return names.inverse().get(object);
	}
}
