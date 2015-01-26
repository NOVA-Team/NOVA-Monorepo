package nova.core.util;

import com.google.common.collect.HashBiMap;

import java.util.Iterator;
import java.util.Objects;

/**
 * A registry of one type of identifiable object.
 *
 * @param <T> The object type.
 */
public class Registry<T extends Identifiable> implements Iterable<T> {
	private final HashBiMap<String, T> objects = HashBiMap.create();

	/**
	 * Registers an identifiable object in the registry.
	 *
	 * @param object the object to register.
	 */
	public void register(T object) {
		objects.put(object.getID(), object);
	}

	/**
	 * Tests whether a given ID is in the Registry.
	 *
	 * @param ID the id to find.
	 * @return true if the registry contains the object with the given ID.
	 */
	public boolean contains(String ID) {
		return objects.containsKey(ID);
	}

	/**
	 * Gets the object with the given id from the registry.
	 *
	 * @param ID the id to find.
	 * @return the object found.
	 * @throws java.lang.NullPointerException if the ID doesn't exist in the registry.
	 */
	public T get(String ID) {
		return Objects.requireNonNull(objects.get(ID), ID + " is not registered");
	}

	/**
	 * Gets the name of a given object.
	 *
	 * @param object the object to find.
	 * @return the name of the object.
	 * @throws java.lang.NullPointerException if the object doesn't exist in the registry.
	 */
	public String getName(T object) {
		return Objects.requireNonNull(objects.inverse().get(object), object.getID() + " is not registered");
	}

	/**
	 * @return an iterator on the values of the registry.
	 */
	@Override
	public Iterator<T> iterator() {
		return objects.values().iterator();
	}
}
